package com.kamatchibotique.application.service.services.order;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.service.services.common.CommonService;
import com.kamatchibotique.application.model.customer.Customer;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.order.Order;
import com.kamatchibotique.application.model.order.OrderCriteria;
import com.kamatchibotique.application.model.order.OrderList;
import com.kamatchibotique.application.model.order.OrderSummary;
import com.kamatchibotique.application.model.order.OrderTotalSummary;
import com.kamatchibotique.application.model.order.orderstatus.OrderStatusHistory;
import com.kamatchibotique.application.model.payments.Payment;
import com.kamatchibotique.application.model.payments.Transaction;
import com.kamatchibotique.application.model.reference.language.Language;
import com.kamatchibotique.application.model.shoppingcart.ShoppingCart;
import com.kamatchibotique.application.model.shoppingcart.ShoppingCartItem;



public interface OrderService extends CommonService<Long, Order> {

    void addOrderStatusHistory(Order order, OrderStatusHistory history)
                    throws ServiceException;

    /**
     * Can be used to calculates the final prices of all items contained in checkout page
     * @param orderSummary
     * @param customer
     * @param store
     * @param language
     * @return
     * @throws ServiceException
     */
    OrderTotalSummary caculateOrderTotal(OrderSummary orderSummary,
                                         Customer customer, MerchantStore store, Language language)
                                                         throws ServiceException;

    /**
     * Can be used to calculates the final prices of all items contained in a ShoppingCart
     * @param orderSummary
     * @param store
     * @param language
     * @return
     * @throws ServiceException
     */
    OrderTotalSummary caculateOrderTotal(OrderSummary orderSummary,
                                         MerchantStore store, Language language) throws ServiceException;


    /**
     * Can be used to calculates the final prices of all items contained in checkout page
     * @param shoppingCart
     * @param customer
     * @param store
     * @param language
     * @return  @return {@link OrderTotalSummary}
     * @throws ServiceException
     */
    OrderTotalSummary calculateShoppingCartTotal(final ShoppingCart shoppingCart,final Customer customer, final MerchantStore store, final Language language) throws ServiceException;

    /**
     * Can be used to calculates the final prices of all items contained in a ShoppingCart
     * @param shoppingCart
     * @param store
     * @param language
     * @return {@link OrderTotalSummary}
     * @throws ServiceException
     */
    OrderTotalSummary calculateShoppingCartTotal(final ShoppingCart shoppingCart,final MerchantStore store, final Language language) throws ServiceException;

    ByteArrayOutputStream generateInvoice(MerchantStore store, Order order,
                                          Language language) throws ServiceException;

    Order getOrder(Long id, MerchantStore store);

    
    /**
     * For finding orders. Mainly used in the administration tool
     * @param store
     * @param criteria
     * @return
     */
    OrderList listByStore(MerchantStore store, OrderCriteria criteria);


    /**
	 * get all orders. Mainly used in the administration tool
	 * @param criteria
	 * @return
	 */
	OrderList getOrders(OrderCriteria criteria, MerchantStore store);

    void saveOrUpdate(Order order) throws ServiceException;

	Order processOrder(Order order, Customer customer,
			List<ShoppingCartItem> items, OrderTotalSummary summary,
			Payment payment, MerchantStore store) throws ServiceException;

	Order processOrder(Order order, Customer customer,
			List<ShoppingCartItem> items, OrderTotalSummary summary,
			Payment payment, Transaction transaction, MerchantStore store)
			throws ServiceException;



	
	/**
	 * Determines if an Order has download files
	 * @param order
	 * @return
	 * @throws ServiceException
	 */
	boolean hasDownloadFiles(Order order) throws ServiceException;
	
	/**
	 * List all orders that have been pre-authorized but not captured
	 * @param store
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws ServiceException
	 */
	List<Order> getCapturableOrders(MerchantStore store, Date startDate, Date endDate) throws ServiceException;

}
