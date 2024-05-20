package com.kamatchibotique.application.service.impl.payments;

import com.kamatchibotique.application.constants.Constants;
import com.kamatchibotique.application.enums.order.OrderStatus;
import com.kamatchibotique.application.enums.order.OrderTotalType;
import com.kamatchibotique.application.enums.payment.CreditCardType;
import com.kamatchibotique.application.enums.payment.PaymentType;
import com.kamatchibotique.application.enums.payment.TransactionType;
import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.integration.IntegrationExceptionService;
import com.kamatchibotique.application.integration.payment.model.PaymentModule;
import com.kamatchibotique.application.model.customer.Customer;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.order.Order;
import com.kamatchibotique.application.model.order.OrderTotal;
import com.kamatchibotique.application.model.order.orderstatus.OrderStatusHistory;
import com.kamatchibotique.application.model.payments.CreditCardPayment;
import com.kamatchibotique.application.model.payments.Payment;
import com.kamatchibotique.application.model.payments.PaymentMethod;
import com.kamatchibotique.application.model.payments.Transaction;
import com.kamatchibotique.application.model.shoppingcart.ShoppingCartItem;
import com.kamatchibotique.application.model.system.IntegrationConfiguration;
import com.kamatchibotique.application.model.system.IntegrationModule;
import com.kamatchibotique.application.model.system.MerchantConfiguration;
import com.kamatchibotique.application.service.impl.reference.loader.ConfigurationModulesLoader;
import com.kamatchibotique.application.service.services.order.OrderService;
import com.kamatchibotique.application.service.services.payments.PaymentService;
import com.kamatchibotique.application.service.services.payments.TransactionService;
import com.kamatchibotique.application.service.services.system.MerchantConfigurationService;
import com.kamatchibotique.application.service.services.system.ModuleConfigurationService;
import com.kamatchibotique.application.utils.CoreConfiguration;
import com.kamatchibotique.application.utils.Encryption;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service("paymentService")
public class PaymentServiceImpl implements PaymentService {


    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentServiceImpl.class);


    @Autowired
    private MerchantConfigurationService merchantConfigurationService;

    @Autowired
    private ModuleConfigurationService moduleConfigurationService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CoreConfiguration coreConfiguration;

    @Autowired
    @Resource(name = "paymentModules")
    private Map<String, PaymentModule> paymentModules;

    @Autowired
    private Encryption encryption;

    @Override
    public List<IntegrationModule> getPaymentMethods(MerchantStore store) throws ServiceException {

        List<IntegrationModule> modules = moduleConfigurationService.getIntegrationModules(Constants.PAYMENT_MODULES);
        List<IntegrationModule> returnModules = new ArrayList<IntegrationModule>();

        for (IntegrationModule module : modules) {
            if (module.getRegionsSet().contains(store.getCountry().getIsoCode())
                    || module.getRegionsSet().contains("*")) {

                returnModules.add(module);
            }
        }

        return returnModules;
    }

    @Override
    public List<PaymentMethod> getAcceptedPaymentMethods(MerchantStore store) throws ServiceException {

        Map<String, IntegrationConfiguration> modules = this.getPaymentModulesConfigured(store);

        List<PaymentMethod> returnModules = new ArrayList<PaymentMethod>();

        for (String module : modules.keySet()) {
            IntegrationConfiguration config = modules.get(module);
            if (config.isActive()) {

                IntegrationModule md = this.getPaymentMethodByCode(store, config.getModuleCode());
                if (md == null) {
                    continue;
                }
                PaymentMethod paymentMethod = new PaymentMethod();

                paymentMethod.setDefaultSelected(config.isDefaultSelected());
                paymentMethod.setPaymentMethodCode(config.getModuleCode());
                paymentMethod.setModule(md);
                paymentMethod.setInformations(config);

                PaymentType type = PaymentType.fromString(md.getType());

                paymentMethod.setPaymentType(type);
                returnModules.add(paymentMethod);
            }
        }

        return returnModules;


    }

    @Override
    public IntegrationModule getPaymentMethodByType(MerchantStore store, String type) throws ServiceException {
        List<IntegrationModule> modules = getPaymentMethods(store);

        for (IntegrationModule module : modules) {
            if (module.getModule().equals(type)) {

                return module;
            }
        }

        return null;
    }

    @Override
    public IntegrationModule getPaymentMethodByCode(MerchantStore store,
                                                    String code) throws ServiceException {
        List<IntegrationModule> modules = getPaymentMethods(store);

        for (IntegrationModule module : modules) {
            if (module.getCode().equals(code)) {

                return module;
            }
        }

        return null;
    }

    @Override
    public IntegrationConfiguration getPaymentConfiguration(String moduleCode, MerchantStore store) throws ServiceException {

        Objects.requireNonNull(moduleCode, "Module code must not be null");
        Objects.requireNonNull(store, "Store must not be null");

        String mod = moduleCode.toLowerCase();

        Map<String, IntegrationConfiguration> configuredModules = getPaymentModulesConfigured(store);
        if (configuredModules != null) {
            for (String key : configuredModules.keySet()) {
                if (key.equals(mod)) {
                    return configuredModules.get(key);
                }
            }
        }

        return null;

    }


    @Override
    public Map<String, IntegrationConfiguration> getPaymentModulesConfigured(MerchantStore store) throws ServiceException {

        try {

            Map<String, IntegrationConfiguration> modules = new HashMap<String, IntegrationConfiguration>();
            MerchantConfiguration merchantConfiguration = merchantConfigurationService.getMerchantConfiguration(Constants.PAYMENT_MODULES, store);
            if (merchantConfiguration != null) {

                if (!StringUtils.isBlank(merchantConfiguration.getValue())) {

                    String decrypted = encryption.decrypt(merchantConfiguration.getValue());
                    modules = ConfigurationModulesLoader.loadIntegrationConfigurations(decrypted);


                }
            }
            return modules;

        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void savePaymentModuleConfiguration(IntegrationConfiguration configuration, MerchantStore store) throws ServiceException {

        //validate entries
        try {

            String moduleCode = configuration.getModuleCode();
            PaymentModule module = paymentModules.get(moduleCode);
            if (module == null) {
                throw new ServiceException("Payment module " + moduleCode + " does not exist");
            }
            module.validateModuleConfiguration(configuration, store);

        } catch (IntegrationExceptionService ie) {
            throw ie;
        }

        try {
            Map<String, IntegrationConfiguration> modules = new HashMap<String, IntegrationConfiguration>();
            MerchantConfiguration merchantConfiguration = merchantConfigurationService.getMerchantConfiguration(Constants.PAYMENT_MODULES, store);
            if (merchantConfiguration != null) {
                if (!StringUtils.isBlank(merchantConfiguration.getValue())) {

                    String decrypted = encryption.decrypt(merchantConfiguration.getValue());

                    modules = ConfigurationModulesLoader.loadIntegrationConfigurations(decrypted);
                }
            } else {
                merchantConfiguration = new MerchantConfiguration();
                merchantConfiguration.setMerchantStore(store);
                merchantConfiguration.setKey(Constants.PAYMENT_MODULES);
            }
            modules.put(configuration.getModuleCode(), configuration);

            String configs = ConfigurationModulesLoader.toJSONString(modules);

            String encrypted = encryption.encrypt(configs);
            merchantConfiguration.setValue(encrypted);

            merchantConfigurationService.saveOrUpdate(merchantConfiguration);

        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void removePaymentModuleConfiguration(String moduleCode, MerchantStore store) throws ServiceException {


        try {
            Map<String, IntegrationConfiguration> modules = new HashMap<String, IntegrationConfiguration>();
            MerchantConfiguration merchantConfiguration = merchantConfigurationService.getMerchantConfiguration(Constants.PAYMENT_MODULES, store);
            if (merchantConfiguration != null) {

                if (!StringUtils.isBlank(merchantConfiguration.getValue())) {

                    String decrypted = encryption.decrypt(merchantConfiguration.getValue());
                    modules = ConfigurationModulesLoader.loadIntegrationConfigurations(decrypted);
                }

                modules.remove(moduleCode);
                String configs = ConfigurationModulesLoader.toJSONString(modules);

                String encrypted = encryption.encrypt(configs);
                merchantConfiguration.setValue(encrypted);

                merchantConfigurationService.saveOrUpdate(merchantConfiguration);


            }

            MerchantConfiguration configuration = merchantConfigurationService.getMerchantConfiguration(moduleCode, store);

            if (configuration != null) {//custom module

                merchantConfigurationService.delete(configuration);
            }


        } catch (Exception e) {
            throw new ServiceException(e);
        }

    }


    @Override
    public Transaction processPayment(Customer customer,
                                      MerchantStore store, Payment payment, List<ShoppingCartItem> items, Order order)
            throws ServiceException {


        Objects.requireNonNull(customer);
        Objects.requireNonNull(store);
        Objects.requireNonNull(payment);
        Objects.requireNonNull(order);
        Objects.requireNonNull(order.getTotal());

        payment.setCurrency(store.getCurrency());

        BigDecimal amount = order.getTotal();

        //must have a shipping module configured
        Map<String, IntegrationConfiguration> modules = this.getPaymentModulesConfigured(store);
        if (modules == null) {
            throw new ServiceException("No payment module configured");
        }

        IntegrationConfiguration configuration = modules.get(payment.getModuleName());

        if (configuration == null) {
            throw new ServiceException("Payment module " + payment.getModuleName() + " is not configured");
        }

        if (!configuration.isActive()) {
            throw new ServiceException("Payment module " + payment.getModuleName() + " is not active");
        }

        String sTransactionType = configuration.getIntegrationKeys().get("transaction");
        if (sTransactionType == null) {
            sTransactionType = TransactionType.AUTHORIZECAPTURE.name();
        }

        try {
            TransactionType.valueOf(sTransactionType);
        } catch (IllegalArgumentException ie) {
            LOGGER.warn("Transaction type " + sTransactionType + " does noe exist, using default AUTHORIZECAPTURE");
            sTransactionType = "AUTHORIZECAPTURE";
        }


        if (sTransactionType.equals(TransactionType.AUTHORIZE.name())) {
            payment.setTransactionType(TransactionType.AUTHORIZE);
        } else {
            payment.setTransactionType(TransactionType.AUTHORIZECAPTURE);
        }


        PaymentModule module = this.paymentModules.get(payment.getModuleName());

        if (module == null) {
            throw new ServiceException("Payment module " + payment.getModuleName() + " does not exist");
        }

        if (payment instanceof CreditCardPayment && "true".equals(coreConfiguration.getProperty("VALIDATE_CREDIT_CARD"))) {
            CreditCardPayment creditCardPayment = (CreditCardPayment) payment;
            validateCreditCard(creditCardPayment.getCreditCardNumber(), creditCardPayment.getCreditCard(), creditCardPayment.getExpirationMonth(), creditCardPayment.getExpirationYear());
        }

        IntegrationModule integrationModule = getPaymentMethodByCode(store, payment.getModuleName());
        TransactionType transactionType = TransactionType.valueOf(sTransactionType);
        if (transactionType == null) {
            transactionType = payment.getTransactionType();
            if (transactionType.equals(TransactionType.CAPTURE.name())) {
                throw new ServiceException("This method does not allow to process capture transaction. Use processCapturePayment");
            }
        }

        Transaction transaction = null;
        if (transactionType == TransactionType.AUTHORIZE) {
            transaction = module.authorize(store, customer, items, amount, payment, configuration, integrationModule);
        } else if (transactionType == TransactionType.AUTHORIZECAPTURE) {
            transaction = module.authorizeAndCapture(store, customer, items, amount, payment, configuration, integrationModule);
        } else if (transactionType == TransactionType.INIT) {
            transaction = module.initTransaction(store, customer, amount, payment, configuration, integrationModule);
        }


        if (transactionType != TransactionType.INIT) {
            transactionService.create(transaction);
        }

        if (transactionType == TransactionType.AUTHORIZECAPTURE) {
            order.setStatus(OrderStatus.ORDERED);
            if (!payment.getPaymentType().name().equals(PaymentType.MONEYORDER.name())) {
                order.setStatus(OrderStatus.PROCESSED);
            }
        }

        return transaction;


    }

    @Override
    public PaymentModule getPaymentModule(String paymentModuleCode) throws ServiceException {
        return paymentModules.get(paymentModuleCode);
    }

    @Override
    public Transaction processCapturePayment(Order order, Customer customer,
                                             MerchantStore store)
            throws ServiceException {


        Objects.requireNonNull(customer);
        Objects.requireNonNull(store);
        Objects.requireNonNull(order);


        //must have a shipping module configured
        Map<String, IntegrationConfiguration> modules = this.getPaymentModulesConfigured(store);
        if (modules == null) {
            throw new ServiceException("No payment module configured");
        }

        IntegrationConfiguration configuration = modules.get(order.getPaymentModuleCode());

        if (configuration == null) {
            throw new ServiceException("Payment module " + order.getPaymentModuleCode() + " is not configured");
        }

        if (!configuration.isActive()) {
            throw new ServiceException("Payment module " + order.getPaymentModuleCode() + " is not active");
        }


        PaymentModule module = this.paymentModules.get(order.getPaymentModuleCode());

        if (module == null) {
            throw new ServiceException("Payment module " + order.getPaymentModuleCode() + " does not exist");
        }


        IntegrationModule integrationModule = getPaymentMethodByCode(store, order.getPaymentModuleCode());

        //TransactionType transactionType = payment.getTransactionType();

        //get the previous transaction
        Transaction trx = transactionService.getCapturableTransaction(order);
        if (trx == null) {
            throw new ServiceException("No capturable transaction for order id " + order.getId());
        }
        Transaction transaction = module.capture(store, customer, order, trx, configuration, integrationModule);
        transaction.setOrder(order);


        transactionService.create(transaction);


        OrderStatusHistory orderHistory = new OrderStatusHistory();
        orderHistory.setOrder(order);
        orderHistory.setStatus(OrderStatus.PROCESSED);
        orderHistory.setDateAdded(new Date());

        orderService.addOrderStatusHistory(order, orderHistory);

        order.setStatus(OrderStatus.PROCESSED);
        orderService.saveOrUpdate(order);

        return transaction;


    }

    @Override
    public Transaction processRefund(Order order, Customer customer,
                                     MerchantStore store, BigDecimal amount)
            throws ServiceException {


        Objects.requireNonNull(customer);
        Objects.requireNonNull(store);
        Objects.requireNonNull(amount);
        Objects.requireNonNull(order);
        Objects.requireNonNull(order.getOrderTotal());


        BigDecimal orderTotal = order.getTotal();

        if (amount.doubleValue() > orderTotal.doubleValue()) {
            throw new ServiceException("Invalid amount, the refunded amount is greater than the total allowed");
        }


        String module = order.getPaymentModuleCode();
        Map<String, IntegrationConfiguration> modules = this.getPaymentModulesConfigured(store);
        if (modules == null) {
            throw new ServiceException("No payment module configured");
        }

        IntegrationConfiguration configuration = modules.get(module);

        if (configuration == null) {
            throw new ServiceException("Payment module " + module + " is not configured");
        }

        PaymentModule paymentModule = this.paymentModules.get(module);

        if (paymentModule == null) {
            throw new ServiceException("Payment module " + paymentModule + " does not exist");
        }

        boolean partial = false;
        if (amount.doubleValue() != order.getTotal().doubleValue()) {
            partial = true;
        }

        IntegrationModule integrationModule = getPaymentMethodByCode(store, module);

        //get the associated transaction
        Transaction refundable = transactionService.getRefundableTransaction(order);

        if (refundable == null) {
            throw new ServiceException("No refundable transaction for this order");
        }

        Transaction transaction = paymentModule.refund(partial, store, refundable, order, amount, configuration, integrationModule);
        transaction.setOrder(order);
        transactionService.create(transaction);

        OrderTotal refund = new OrderTotal();
        refund.setModule(Constants.OT_REFUND_MODULE_CODE);
        refund.setText(Constants.OT_REFUND_MODULE_CODE);
        refund.setTitle(Constants.OT_REFUND_MODULE_CODE);
        refund.setOrderTotalCode(Constants.OT_REFUND_MODULE_CODE);
        refund.setOrderTotalType(OrderTotalType.REFUND);
        refund.setValue(amount);
        refund.setSortOrder(100);
        refund.setOrder(order);

        order.getOrderTotal().add(refund);

        //update order total
        orderTotal = orderTotal.subtract(amount);

        //update ordertotal refund
        Set<OrderTotal> totals = order.getOrderTotal();
        for (OrderTotal total : totals) {
            if (total.getModule().equals(Constants.OT_TOTAL_MODULE_CODE)) {
                total.setValue(orderTotal);
            }
        }


        order.setTotal(orderTotal);
        order.setStatus(OrderStatus.REFUNDED);


        OrderStatusHistory orderHistory = new OrderStatusHistory();
        orderHistory.setOrder(order);
        orderHistory.setStatus(OrderStatus.REFUNDED);
        orderHistory.setDateAdded(new Date());
        order.getOrderHistory().add(orderHistory);

        orderService.saveOrUpdate(order);

        return transaction;
    }

    @Override
    public void validateCreditCard(String number, CreditCardType creditCard, String month, String date)
            throws ServiceException {

        try {
            Integer.parseInt(month);
            Integer.parseInt(date);
        } catch (NumberFormatException nfe) {
            throw new ServiceException(ServiceException.EXCEPTION_VALIDATION, "Invalid date format", "messages.error.creditcard.dateformat");
        }

        if (StringUtils.isBlank(number)) {
            throw new ServiceException(ServiceException.EXCEPTION_VALIDATION, "Invalid card number", "messages.error.creditcard.number");
        }

        Matcher m = Pattern.compile("[^\\d\\s.-]").matcher(number);

        if (m.find()) {
            throw new ServiceException(ServiceException.EXCEPTION_VALIDATION, "Invalid card number", "messages.error.creditcard.number");
        }

        Matcher matcher = Pattern.compile("[\\s.-]").matcher(number);

        number = matcher.replaceAll("");
        validateCreditCardDate(Integer.parseInt(month), Integer.parseInt(date));
        validateCreditCardNumber(number, creditCard);
    }

    private void validateCreditCardDate(int m, int y) throws ServiceException {
        java.util.Calendar cal = new java.util.GregorianCalendar();
        int monthNow = cal.get(java.util.Calendar.MONTH) + 1;
        int yearNow = cal.get(java.util.Calendar.YEAR);
        if (yearNow > y) {
            throw new ServiceException(ServiceException.EXCEPTION_VALIDATION, "Invalid date format", "messages.error.creditcard.dateformat");
        }
        // OK, change implementation
        if (yearNow == y && monthNow > m) {
            throw new ServiceException(ServiceException.EXCEPTION_VALIDATION, "Invalid date format", "messages.error.creditcard.dateformat");
        }

    }

    @Deprecated
    /**
     * Use commons validator CreditCardValidator
     * @param number
     * @param creditCard
     * @throws ServiceException
     */
    private void validateCreditCardNumber(String number, CreditCardType creditCard)
            throws ServiceException {

        //TODO implement
        if (CreditCardType.MASTERCARD.equals(creditCard.name())) {
            if (number.length() != 16
                    || Integer.parseInt(number.substring(0, 2)) < 51
                    || Integer.parseInt(number.substring(0, 2)) > 55) {
                throw new ServiceException(ServiceException.EXCEPTION_VALIDATION, "Invalid card number", "messages.error.creditcard.number");
            }
        }

        if (CreditCardType.VISA.equals(creditCard.name())) {
            if ((number.length() != 13 && number.length() != 16)
                    || Integer.parseInt(number.substring(0, 1)) != 4) {
                throw new ServiceException(ServiceException.EXCEPTION_VALIDATION, "Invalid card number", "messages.error.creditcard.number");
            }
        }

        if (CreditCardType.AMEX.equals(creditCard.name())) {
            if (number.length() != 15
                    || (Integer.parseInt(number.substring(0, 2)) != 34 && Integer
                    .parseInt(number.substring(0, 2)) != 37)) {
                throw new ServiceException(ServiceException.EXCEPTION_VALIDATION, "Invalid card number", "messages.error.creditcard.number");
            }
        }

        if (CreditCardType.DINERS.equals(creditCard.name())) {
            if (number.length() != 14
                    || ((Integer.parseInt(number.substring(0, 2)) != 36 && Integer
                    .parseInt(number.substring(0, 2)) != 38)
                    && Integer.parseInt(number.substring(0, 3)) < 300 || Integer
                    .parseInt(number.substring(0, 3)) > 305)) {
                throw new ServiceException(ServiceException.EXCEPTION_VALIDATION, "Invalid card number", "messages.error.creditcard.number");
            }
        }

        if (CreditCardType.DISCOVERY.equals(creditCard.name())) {
            if (number.length() != 16
                    || Integer.parseInt(number.substring(0, 5)) != 6011) {
                throw new ServiceException(ServiceException.EXCEPTION_VALIDATION, "Invalid card number", "messages.error.creditcard.number");
            }
        }

        luhnValidate(number);
    }

    // The Luhn algorithm is basically a CRC type
    // system for checking the validity of an entry.
    // All major credit cards use numbers that will
    // pass the Luhn check. Also, all of them are based
    // on MOD 10.
    @Deprecated
    private void luhnValidate(String numberString)
            throws ServiceException {
        char[] charArray = numberString.toCharArray();
        int[] number = new int[charArray.length];
        int total = 0;

        for (int i = 0; i < charArray.length; i++) {
            number[i] = Character.getNumericValue(charArray[i]);
        }

        for (int i = number.length - 2; i > -1; i -= 2) {
            number[i] *= 2;

            if (number[i] > 9)
                number[i] -= 9;
        }

        for (int j : number) {
            total += j;
        }

        if (total % 10 != 0) {
            throw new ServiceException(ServiceException.EXCEPTION_VALIDATION, "Invalid card number", "messages.error.creditcard.number");
        }

    }

    @Override
    public Transaction initTransaction(Order order, Customer customer, Payment payment, MerchantStore store) throws ServiceException {

        Objects.requireNonNull(store);
        Objects.requireNonNull(payment);
        Objects.requireNonNull(order);
        Objects.requireNonNull(order.getTotal());

        payment.setCurrency(store.getCurrency());

        BigDecimal amount = order.getTotal();

        //must have a shipping module configured
        Map<String, IntegrationConfiguration> modules = this.getPaymentModulesConfigured(store);
        if (modules == null) {
            throw new ServiceException("No payment module configured");
        }

        IntegrationConfiguration configuration = modules.get(payment.getModuleName());

        if (configuration == null) {
            throw new ServiceException("Payment module " + payment.getModuleName() + " is not configured");
        }

        if (!configuration.isActive()) {
            throw new ServiceException("Payment module " + payment.getModuleName() + " is not active");
        }

        PaymentModule module = this.paymentModules.get(order.getPaymentModuleCode());

        if (module == null) {
            throw new ServiceException("Payment module " + order.getPaymentModuleCode() + " does not exist");
        }

        IntegrationModule integrationModule = getPaymentMethodByCode(store, payment.getModuleName());

        return module.initTransaction(store, customer, amount, payment, configuration, integrationModule);
    }

    @Override
    public Transaction initTransaction(Customer customer, Payment payment, MerchantStore store) throws ServiceException {

        Objects.requireNonNull(store);
        Objects.requireNonNull(payment);
        Objects.requireNonNull(payment.getAmount());

        payment.setCurrency(store.getCurrency());

        BigDecimal amount = payment.getAmount();

        //must have a shipping module configured
        Map<String, IntegrationConfiguration> modules = this.getPaymentModulesConfigured(store);
        if (modules == null) {
            throw new ServiceException("No payment module configured");
        }

        IntegrationConfiguration configuration = modules.get(payment.getModuleName());

        if (configuration == null) {
            throw new ServiceException("Payment module " + payment.getModuleName() + " is not configured");
        }

        if (!configuration.isActive()) {
            throw new ServiceException("Payment module " + payment.getModuleName() + " is not active");
        }

        PaymentModule module = this.paymentModules.get(payment.getModuleName());

        if (module == null) {
            throw new ServiceException("Payment module " + payment.getModuleName() + " does not exist");
        }

        IntegrationModule integrationModule = getPaymentMethodByCode(store, payment.getModuleName());

        Transaction transaction = module.initTransaction(store, customer, amount, payment, configuration, integrationModule);

        transactionService.save(transaction);

        return transaction;
    }


}
