package com.kamatchibotique.application.service.impl.shipping;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kamatchibotique.application.constants.ShippingConstants;
import com.kamatchibotique.application.enums.shipping.ShippingOptionPriceType;
import com.kamatchibotique.application.enums.shipping.ShippingPackageType;
import com.kamatchibotique.application.enums.shipping.ShippingType;
import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.integration.IntegrationExceptionService;
import com.kamatchibotique.application.integration.shipping.model.Packaging;
import com.kamatchibotique.application.integration.shipping.model.ShippingQuoteModule;
import com.kamatchibotique.application.integration.shipping.model.ShippingQuotePrePostProcessModule;
import com.kamatchibotique.application.model.catalog.product.Product;
import com.kamatchibotique.application.model.common.Delivery;
import com.kamatchibotique.application.model.common.UserContext;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.reference.country.Country;
import com.kamatchibotique.application.model.reference.language.Language;
import com.kamatchibotique.application.model.shipping.*;
import com.kamatchibotique.application.model.shoppingcart.ShoppingCartItem;
import com.kamatchibotique.application.model.system.CustomIntegrationConfiguration;
import com.kamatchibotique.application.model.system.IntegrationConfiguration;
import com.kamatchibotique.application.model.system.IntegrationModule;
import com.kamatchibotique.application.model.system.MerchantConfiguration;
import com.kamatchibotique.application.service.impl.reference.loader.ConfigurationModulesLoader;
import com.kamatchibotique.application.service.services.catalog.pricing.PricingService;
import com.kamatchibotique.application.service.services.reference.country.CountryService;
import com.kamatchibotique.application.service.services.reference.language.LanguageService;
import com.kamatchibotique.application.service.services.shipping.ShippingOriginService;
import com.kamatchibotique.application.service.services.shipping.ShippingQuoteService;
import com.kamatchibotique.application.service.services.shipping.ShippingService;
import com.kamatchibotique.application.service.services.system.MerchantConfigurationService;
import com.kamatchibotique.application.service.services.system.ModuleConfigurationService;
import com.kamatchibotique.application.utils.Encryption;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;


@Service("shippingService")
public class ShippingServiceImpl implements ShippingService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ShippingServiceImpl.class);
	
	
	private final static String SUPPORTED_COUNTRIES = "SUPPORTED_CNTR";
	private final static String SHIPPING_MODULES = "SHIPPING";
	private final static String SHIPPING_DISTANCE = "shippingDistanceModule";

	
	@Autowired
	private MerchantConfigurationService merchantConfigurationService;
	

	@Autowired
	private PricingService pricingService;
	
	@Autowired
	private ModuleConfigurationService moduleConfigurationService;
	
	@Autowired
	private Packaging packaging;
	
	@Autowired
	private CountryService countryService;
	
	@Autowired
	private LanguageService languageService;
	
	@Autowired
	private Encryption encryption;

	@Autowired
	private ShippingOriginService shippingOriginService;
	
	@Autowired
	private ShippingQuoteService shippingQuoteService;
	
	@Autowired
	@Resource(name="shippingModules")
	private Map<String,ShippingQuoteModule> shippingModules;
	
	//shipping pre-processors
	@Autowired
	@Resource(name="shippingModulePreProcessors")
	private List<ShippingQuotePrePostProcessModule> shippingModulePreProcessors;
	
	//shipping post-processors
	@Autowired
	@Resource(name="shippingModulePostProcessors")
	private List<ShippingQuotePrePostProcessModule> shippingModulePostProcessors;
	
	@Override
	public ShippingConfiguration getShippingConfiguration(MerchantStore store) throws ServiceException {

		MerchantConfiguration configuration = merchantConfigurationService.getMerchantConfiguration(ShippingConstants.SHIPPING_CONFIGURATION, store);
		
		ShippingConfiguration shippingConfiguration = null;
		
		if(configuration!=null) {
			String value = configuration.getValue();
			
			ObjectMapper mapper = new ObjectMapper();
			try {
				shippingConfiguration = mapper.readValue(value, ShippingConfiguration.class);
			} catch(Exception e) {
				throw new ServiceException("Cannot parse json string " + value);
			}
		}
		return shippingConfiguration;
		
	}
	
	@Override
	public IntegrationConfiguration getShippingConfiguration(String moduleCode, MerchantStore store) throws ServiceException {

		
		Map<String,IntegrationConfiguration> configuredModules = getShippingModulesConfigured(store);
		if(configuredModules!=null) {
			for(String key : configuredModules.keySet()) {
				if(key.equals(moduleCode)) {
					return configuredModules.get(key);	
				}
			}
		}
		
		return null;
		
	}
	
	@Override
	public CustomIntegrationConfiguration getCustomShippingConfiguration(String moduleCode, MerchantStore store) throws ServiceException {

		
		ShippingQuoteModule quoteModule = shippingModules.get(moduleCode);
		if(quoteModule==null) {
			return null;
		}
		return quoteModule.getCustomModuleConfiguration(store);
		
	}
	
	@Override
	public void saveShippingConfiguration(ShippingConfiguration shippingConfiguration, MerchantStore store) throws ServiceException {
		
		MerchantConfiguration configuration = merchantConfigurationService.getMerchantConfiguration(ShippingConstants.SHIPPING_CONFIGURATION, store);

		if(configuration==null) {
			configuration = new MerchantConfiguration();
			configuration.setMerchantStore(store);
			configuration.setKey(ShippingConstants.SHIPPING_CONFIGURATION);
		}
		
		String value = shippingConfiguration.toJSONString();
		configuration.setValue(value);
		merchantConfigurationService.saveOrUpdate(configuration);
		
	}
	
	@Override
	public void saveCustomShippingConfiguration(String moduleCode, CustomIntegrationConfiguration shippingConfiguration, MerchantStore store) throws ServiceException {
		
		
		ShippingQuoteModule quoteModule = shippingModules.get(moduleCode);
		if(quoteModule==null) {
			throw new ServiceException("Shipping module " + moduleCode + " does not exist");
		}
		
		String configurationValue = shippingConfiguration.toJSONString();
		
		
		try {

			MerchantConfiguration configuration = merchantConfigurationService.getMerchantConfiguration(moduleCode, store);
	
			if(configuration==null) {

				configuration = new MerchantConfiguration();
				configuration.setKey(moduleCode);
				configuration.setMerchantStore(store);
			}
			configuration.setValue(configurationValue);
			merchantConfigurationService.saveOrUpdate(configuration);
		
		} catch (Exception e) {
			throw new IntegrationExceptionService(e);
		}

		
		
	}
	

	@Override
	public List<IntegrationModule> getShippingMethods(MerchantStore store) throws ServiceException {
		
		List<IntegrationModule> modules =  moduleConfigurationService.getIntegrationModules(SHIPPING_MODULES);
		List<IntegrationModule> returnModules = new ArrayList<IntegrationModule>();
		
		for(IntegrationModule module : modules) {
			if(module.getRegionsSet().contains(store.getCountry().getIsoCode())
					|| module.getRegionsSet().contains("*")) {
				
				returnModules.add(module);
			}
		}
		
		return returnModules;
	}
	
	@Override
	public void saveShippingQuoteModuleConfiguration(IntegrationConfiguration configuration, MerchantStore store) throws ServiceException {
		
			//validate entries
			try {
				
				String moduleCode = configuration.getModuleCode();
				ShippingQuoteModule quoteModule = (ShippingQuoteModule)shippingModules.get(moduleCode);
				if(quoteModule==null) {
					throw new ServiceException("Shipping quote module " + moduleCode + " does not exist");
				}
				quoteModule.validateModuleConfiguration(configuration, store);
				
			} catch (IntegrationExceptionService ie) {
				throw ie;
			}
			
			try {
				Map<String,IntegrationConfiguration> modules = new HashMap<String,IntegrationConfiguration>();
				MerchantConfiguration merchantConfiguration = merchantConfigurationService.getMerchantConfiguration(SHIPPING_MODULES, store);
				if(merchantConfiguration!=null) {
					if(!StringUtils.isBlank(merchantConfiguration.getValue())) {
						
						String decrypted = encryption.decrypt(merchantConfiguration.getValue());
						modules = ConfigurationModulesLoader.loadIntegrationConfigurations(decrypted);
					}
				} else {
					merchantConfiguration = new MerchantConfiguration();
					merchantConfiguration.setMerchantStore(store);
					merchantConfiguration.setKey(SHIPPING_MODULES);
				}
				modules.put(configuration.getModuleCode(), configuration);
				
				String configs =  ConfigurationModulesLoader.toJSONString(modules);
				
				String encrypted = encryption.encrypt(configs);
				merchantConfiguration.setValue(encrypted);
				merchantConfigurationService.saveOrUpdate(merchantConfiguration);
				
			} catch (Exception e) {
				throw new ServiceException(e);
			}
	}
	
	
	@Override
	public void removeShippingQuoteModuleConfiguration(String moduleCode, MerchantStore store) throws ServiceException {
		
		

		try {
			Map<String,IntegrationConfiguration> modules = new HashMap<String,IntegrationConfiguration>();
			MerchantConfiguration merchantConfiguration = merchantConfigurationService.getMerchantConfiguration(SHIPPING_MODULES, store);
			if(merchantConfiguration!=null) {
				if(!StringUtils.isBlank(merchantConfiguration.getValue())) {
					String decrypted = encryption.decrypt(merchantConfiguration.getValue());
					modules = ConfigurationModulesLoader.loadIntegrationConfigurations(decrypted);
				}
				
				modules.remove(moduleCode);
				String configs =  ConfigurationModulesLoader.toJSONString(modules);
				String encrypted = encryption.encrypt(configs);
				merchantConfiguration.setValue(encrypted);
				merchantConfigurationService.saveOrUpdate(merchantConfiguration);
				
				
			} 
			
			MerchantConfiguration configuration = merchantConfigurationService.getMerchantConfiguration(moduleCode, store);
			
			if(configuration!=null) {//custom module

				merchantConfigurationService.delete(configuration);
			}

			
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	
	}
	
	@Override
	public void removeCustomShippingQuoteModuleConfiguration(String moduleCode, MerchantStore store) throws ServiceException {
		
		

		try {
			
			removeShippingQuoteModuleConfiguration(moduleCode,store);
			MerchantConfiguration merchantConfiguration = merchantConfigurationService.getMerchantConfiguration(moduleCode, store);
			if(merchantConfiguration!=null) {
				merchantConfigurationService.delete(merchantConfiguration);
			} 
			
			
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	
	}
	
	@Override
	public Map<String,IntegrationConfiguration> getShippingModulesConfigured(MerchantStore store) throws ServiceException {
		try {
			

			Map<String,IntegrationConfiguration> modules = new HashMap<String,IntegrationConfiguration>();
			MerchantConfiguration merchantConfiguration = merchantConfigurationService.getMerchantConfiguration(SHIPPING_MODULES, store);
			if(merchantConfiguration!=null) {
				if(!StringUtils.isBlank(merchantConfiguration.getValue())) {
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
	public ShippingSummary getShippingSummary(MerchantStore store, ShippingQuote shippingQuote, ShippingOption selectedShippingOption) throws ServiceException {
		
		ShippingSummary shippingSummary = new ShippingSummary();
		shippingSummary.setFreeShipping(shippingQuote.isFreeShipping());
		shippingSummary.setHandling(shippingQuote.getHandlingFees());
		shippingSummary.setShipping(selectedShippingOption.getOptionPrice());
		shippingSummary.setShippingModule(shippingQuote.getShippingModuleCode());
		shippingSummary.setShippingOption(selectedShippingOption.getDescription());
		
		return shippingSummary;
	}

	@Override
	public ShippingQuote getShippingQuote(Long shoppingCartId, MerchantStore store, Delivery delivery, List<ShippingProduct> products, Language language) throws ServiceException  {
		
		
		//ShippingConfiguration -> Global configuration of a given store
		//IntegrationConfiguration -> Configuration of a given module
		//IntegrationModule -> The concrete module as defined in integrationmodules.properties
		
		//delivery without postal code is accepted
		Validate.notNull(store,"MerchantStore must not be null");
		Validate.notNull(delivery,"Delivery must not be null");
		Validate.notEmpty(products,"products must not be empty");
		Validate.notNull(language,"Language must not be null");
		
		
		
		ShippingQuote shippingQuote = new ShippingQuote();
		ShippingQuoteModule shippingQuoteModule = null;
		
		try {
			
			
			if(StringUtils.isBlank(delivery.getPostalCode())) {
				shippingQuote.getWarnings().add("No postal code in delivery address");
				shippingQuote.setShippingReturnCode(ShippingQuote.NO_POSTAL_CODE);
			}
		
			//get configuration
			ShippingConfiguration shippingConfiguration = getShippingConfiguration(store);
			ShippingType shippingType = ShippingType.INTERNATIONAL;
			
			/** get shipping origin **/
			ShippingOrigin shippingOrigin = shippingOriginService.getByStore(store);
			if(shippingOrigin == null || !shippingOrigin.isActive()) {
				shippingOrigin = new ShippingOrigin();
				shippingOrigin.setAddress(store.getStoreaddress());
				shippingOrigin.setCity(store.getStorecity());
				shippingOrigin.setCountry(store.getCountry());
				shippingOrigin.setPostalCode(store.getStorepostalcode());
				shippingOrigin.setState(store.getStorestateprovince());
				shippingOrigin.setZone(store.getZone());
			}
			
			
			if(shippingConfiguration==null) {
				shippingConfiguration = new ShippingConfiguration();
			}
			
			if(shippingConfiguration.getShippingType()!=null) {
					shippingType = shippingConfiguration.getShippingType();
			}

			//look if customer country code excluded
			Country shipCountry = delivery.getCountry();
			
			//a ship to country is required
			Validate.notNull(shipCountry,"Ship to Country cannot be null");
			Validate.notNull(store.getCountry(), "Store Country canot be null");
			
			if(shippingType.name().equals(ShippingType.NATIONAL.name())){
				//customer country must match store country
				if(!shipCountry.getIsoCode().equals(store.getCountry().getIsoCode())) {
					shippingQuote.setShippingReturnCode(ShippingQuote.NO_SHIPPING_TO_SELECTED_COUNTRY + " " + shipCountry.getIsoCode());
					return shippingQuote;
				}
			} else if(shippingType.name().equals(ShippingType.INTERNATIONAL.name())){
				
				//customer shipping country code must be in accepted list
				List<String> supportedCountries = this.getSupportedCountries(store);
				if(!supportedCountries.contains(shipCountry.getIsoCode())) {
					shippingQuote.setShippingReturnCode(ShippingQuote.NO_SHIPPING_TO_SELECTED_COUNTRY + " " + shipCountry.getIsoCode());
					return shippingQuote;
				}
			}
			
			//must have a shipping module configured
			Map<String, IntegrationConfiguration> modules = this.getShippingModulesConfigured(store);
			if(modules == null){
				shippingQuote.setShippingReturnCode(ShippingQuote.NO_SHIPPING_MODULE_CONFIGURED);
				return shippingQuote;
			}

			
			/** uses this module name **/
			String moduleName = null;
			IntegrationConfiguration configuration = null;
			for(String module : modules.keySet()) {
				moduleName = module;
				configuration = modules.get(module);
				//use the first active module
				if(configuration.isActive()) {
					shippingQuoteModule = shippingModules.get(module);
					if(shippingQuoteModule instanceof ShippingQuotePrePostProcessModule) {
						shippingQuoteModule = null;
						continue;
					} else {
						break;
					}
				}
			}
			
			if(shippingQuoteModule==null){
				shippingQuote.setShippingReturnCode(ShippingQuote.NO_SHIPPING_MODULE_CONFIGURED);
				return shippingQuote;
			}
			
			/** merchant module configs **/
			List<IntegrationModule> shippingMethods = this.getShippingMethods(store);
			IntegrationModule shippingModule = null;
			for(IntegrationModule mod : shippingMethods) {
				if(mod.getCode().equals(moduleName)){
					shippingModule = mod;
					break;
				}
			}
			
			/** general module configs **/
			if(shippingModule==null) {
				shippingQuote.setShippingReturnCode(ShippingQuote.NO_SHIPPING_MODULE_CONFIGURED);
				return shippingQuote;
			}
			
			//calculate order total
			BigDecimal orderTotal = calculateOrderTotal(products,store);
			List<PackageDetails> packages = getPackagesDetails(products, store);
			
			//free shipping ?
			boolean freeShipping = false;
			if(shippingConfiguration.isFreeShippingEnabled()) {
				BigDecimal freeShippingAmount = shippingConfiguration.getOrderTotalFreeShipping();
				if(freeShippingAmount!=null) {
					if(orderTotal.doubleValue()>freeShippingAmount.doubleValue()) {
						if(shippingConfiguration.getFreeShippingType() == ShippingType.NATIONAL) {
							if(store.getCountry().getIsoCode().equals(shipCountry.getIsoCode())) {
								freeShipping = true;
								shippingQuote.setFreeShipping(true);
								shippingQuote.setFreeShippingAmount(freeShippingAmount);
								return shippingQuote;
							}
						} else {//international all
							freeShipping = true;
							shippingQuote.setFreeShipping(true);
							shippingQuote.setFreeShippingAmount(freeShippingAmount);
							return shippingQuote;
						}
	
					}
				}
			}
			

			//handling fees
			BigDecimal handlingFees = shippingConfiguration.getHandlingFees();
			if(handlingFees!=null) {
				shippingQuote.setHandlingFees(handlingFees);
			}
			
			//tax basis
			shippingQuote.setApplyTaxOnShipping(shippingConfiguration.isTaxOnShipping());
			

			Locale locale = languageService.toLocale(language, store);
			
			//invoke pre processors
			//the main pre-processor determines at runtime the shipping module
			//also available distance calculation
			if(!CollectionUtils.isEmpty(shippingModulePreProcessors)) {
				for(ShippingQuotePrePostProcessModule preProcessor : shippingModulePreProcessors) {
					//System.out.println("Using pre-processor " + preProcessor.getModuleCode());
					preProcessor.prePostProcessShippingQuotes(shippingQuote, packages, orderTotal, delivery, shippingOrigin, store, configuration, shippingModule, shippingConfiguration, shippingMethods, locale);
					//TODO switch module if required
					if(shippingQuote.getCurrentShippingModule()!=null && !shippingQuote.getCurrentShippingModule().getCode().equals(shippingModule.getCode())) {
						shippingModule = shippingQuote.getCurrentShippingModule();//determines the shipping module
						configuration = modules.get(shippingModule.getCode());
						if(configuration!=null) {
							if(configuration.isActive()) {
								moduleName = shippingModule.getCode();
								shippingQuoteModule = this.shippingModules.get(shippingModule.getCode());
								configuration = modules.get(shippingModule.getCode());
							} //TODO use default
						}
						
					}
				}
			}

			//invoke module
			List<ShippingOption> shippingOptions = null;
					
			try {
				shippingOptions = shippingQuoteModule.getShippingQuotes(shippingQuote, packages, orderTotal, delivery, shippingOrigin, store, configuration, shippingModule, shippingConfiguration, locale);
			} catch(Exception e) {
				LOGGER.error("Error while calculating shipping : " + e.getMessage(), e);
/*				merchantLogService.save(
						new MerchantLog(store,
								"Can't process " + shippingModule.getModule()
								+ " -> "
								+ e.getMessage()));
				shippingQuote.setQuoteError(e.getMessage());
				shippingQuote.setShippingReturnCode(ShippingQuote.ERROR);
				return shippingQuote;*/
			}
			
			if(shippingOptions==null && !StringUtils.isBlank(delivery.getPostalCode())) {
				
				//absolutely need to use in this case store pickup or other default shipping quote
				shippingQuote.setShippingReturnCode(ShippingQuote.NO_SHIPPING_TO_SELECTED_COUNTRY);
			}
			
			
			shippingQuote.setShippingModuleCode(moduleName);	
			
			//filter shipping options
			ShippingOptionPriceType shippingOptionPriceType = shippingConfiguration.getShippingOptionPriceType();
			ShippingOption selectedOption = null;
			
			if(shippingOptions!=null) {
				
				for(ShippingOption option : shippingOptions) {
					if(selectedOption==null) {
						selectedOption = option;
					}
					//set price text
					String priceText = pricingService.getDisplayAmount(option.getOptionPrice(), store);
					option.setOptionPriceText(priceText);
					option.setShippingModuleCode(moduleName);
				
					if(StringUtils.isBlank(option.getOptionName())) {
						
						String countryName = delivery.getCountry().getName();
						if(countryName == null) {
							Map<String,Country> deliveryCountries = countryService.getCountriesMap(language);
							Country dCountry = deliveryCountries.get(delivery.getCountry().getIsoCode());
							if(dCountry!=null) {
								countryName = dCountry.getName();
							} else {
								countryName = delivery.getCountry().getIsoCode();
							}
						}
							option.setOptionName(countryName);		
					}
				
					if(shippingOptionPriceType.name().equals(ShippingOptionPriceType.HIGHEST.name())) {

						if (option.getOptionPrice()
								.longValue() > selectedOption
								.getOptionPrice()
								.longValue()) {
							selectedOption = option;
						}
					}

				
					if(shippingOptionPriceType.name().equals(ShippingOptionPriceType.LEAST.name())) {

						if (option.getOptionPrice()
								.longValue() < selectedOption
								.getOptionPrice()
								.longValue()) {
							selectedOption = option;
						}
					}
					
				
					if(shippingOptionPriceType.name().equals(ShippingOptionPriceType.ALL.name())) {
	
						if (option.getOptionPrice()
								.longValue() < selectedOption
								.getOptionPrice()
								.longValue()) {
							selectedOption = option;
						}
					}

				}
				
				shippingQuote.setSelectedShippingOption(selectedOption);
				
				if(selectedOption!=null && !shippingOptionPriceType.name().equals(ShippingOptionPriceType.ALL.name())) {
					shippingOptions = new ArrayList<ShippingOption>();
					shippingOptions.add(selectedOption);
				}

			}
			
			/** set final delivery address **/
			shippingQuote.setDeliveryAddress(delivery);
			
			shippingQuote.setShippingOptions(shippingOptions);
			
			/** post processors **/
			//invoke pre processors
			if(!CollectionUtils.isEmpty(shippingModulePostProcessors)) {
				for(ShippingQuotePrePostProcessModule postProcessor : shippingModulePostProcessors) {
					//get module info
					
					//get module configuration
					IntegrationConfiguration integrationConfiguration = modules.get(postProcessor.getModuleCode());
					
					IntegrationModule postProcessModule = null;
					for(IntegrationModule mod : shippingMethods) {
						if(mod.getCode().equals(postProcessor.getModuleCode())){
							postProcessModule = mod;
							break;
						}
					}
					
					IntegrationModule module = postProcessModule;
					if(integrationConfiguration != null) {
						postProcessor.prePostProcessShippingQuotes(shippingQuote, packages, orderTotal, delivery, shippingOrigin, store, integrationConfiguration, module, shippingConfiguration, shippingMethods, locale);
					}
				}
			}
			String ipAddress = null;
	    	UserContext context = UserContext.getCurrentInstance();
	    	if(context != null) {
	    		ipAddress = context.getIpAddress();
	    	}
			
			if(shippingQuote!=null && CollectionUtils.isNotEmpty(shippingQuote.getShippingOptions())) {
				//save SHIPPING OPTIONS
				List<ShippingOption> finalShippingOptions = shippingQuote.getShippingOptions();
				for(ShippingOption option : finalShippingOptions) {
					
					//transform to Quote
					Quote q = new Quote();
					q.setCartId(shoppingCartId);
					q.setDelivery(delivery);
					if(!StringUtils.isBlank(ipAddress)) {
						q.setIpAddress(ipAddress);
					}
					if(!StringUtils.isBlank(option.getEstimatedNumberOfDays())) {
						try {
							q.setEstimatedNumberOfDays(Integer.valueOf(option.getEstimatedNumberOfDays()));
						} catch(Exception e) {
							LOGGER.error("Cannot cast to integer " + option.getEstimatedNumberOfDays());
						}
					}
					
					if(freeShipping) {
						q.setFreeShipping(true);
						q.setPrice(new BigDecimal(0));
						q.setModule("FREE");
						q.setOptionCode("FREE");
						q.setOptionName("FREE");
					} else {
						q.setModule(option.getShippingModuleCode());
						q.setOptionCode(option.getOptionCode());
						if(!StringUtils.isBlank(option.getOptionDeliveryDate())) {
							try {
							//q.setOptionDeliveryDate(DateUtil.formatDate(option.getOptionDeliveryDate()));
							} catch(Exception e) {
								LOGGER.error("Cannot transform to date " + option.getOptionDeliveryDate());
							}
						}
						q.setOptionName(option.getOptionName());
						q.setOptionShippingDate(new Date());
						q.setPrice(option.getOptionPrice());
						
					}
					
					if(handlingFees != null) {
						q.setHandling(handlingFees);
					}
					
					q.setQuoteDate(new Date());
					shippingQuoteService.save(q);
					option.setShippingQuoteOptionId(q.getId());
					
				}
			}
			
			
			
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new ServiceException(e);
		}
		
		return shippingQuote;
		
	}

	@Override
	public List<String> getSupportedCountries(MerchantStore store) throws ServiceException {
		
		List<String> supportedCountries = new ArrayList<String>();
		MerchantConfiguration configuration = merchantConfigurationService.getMerchantConfiguration(SUPPORTED_COUNTRIES, store);
		
		if(configuration!=null) {
			
			String countries = configuration.getValue();
			if(!StringUtils.isBlank(countries)) {

				Object objRegions=JSONValue.parse(countries); 
				JSONArray arrayRegions=(JSONArray)objRegions;
				for (Object arrayRegion : arrayRegions) {
					supportedCountries.add((String) arrayRegion);
				}
			}
			
		}
		
		return supportedCountries;
	}
	
	@Override
	public List<Country> getShipToCountryList(MerchantStore store, Language language) throws ServiceException {
		
		
		ShippingConfiguration shippingConfiguration = getShippingConfiguration(store);
		ShippingType shippingType = ShippingType.INTERNATIONAL;
		List<String> supportedCountries = new ArrayList<String>();
		if(shippingConfiguration==null) {
			shippingConfiguration = new ShippingConfiguration();
		}
		
		if(shippingConfiguration.getShippingType()!=null) {
				shippingType = shippingConfiguration.getShippingType();
		}

		
		if(shippingType.name().equals(ShippingType.NATIONAL.name())){
			
			supportedCountries.add(store.getCountry().getIsoCode());
			
		} else {

			MerchantConfiguration configuration = merchantConfigurationService.getMerchantConfiguration(SUPPORTED_COUNTRIES, store);
			
			if(configuration!=null) {
				
				String countries = configuration.getValue();
				if(!StringUtils.isBlank(countries)) {

					Object objRegions=JSONValue.parse(countries); 
					JSONArray arrayRegions=(JSONArray)objRegions;
					for (Object arrayRegion : arrayRegions) {
						supportedCountries.add((String) arrayRegion);
					}
				}
				
			}

		}
		
		return countryService.getCountries(supportedCountries, language);

	}
	

	@Override
	public void setSupportedCountries(MerchantStore store, List<String> countryCodes) throws ServiceException {
		
		
		//transform a list of string to json entry
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			String value  = mapper.writeValueAsString(countryCodes);
			
			MerchantConfiguration configuration = merchantConfigurationService.getMerchantConfiguration(SUPPORTED_COUNTRIES, store);
			
			if(configuration==null) {
				configuration = new MerchantConfiguration();
				configuration.
				setKey(SUPPORTED_COUNTRIES);
				configuration.setMerchantStore(store);
			} 
			
			configuration.setValue(value);

			merchantConfigurationService.saveOrUpdate(configuration);
			
		} catch (Exception e) {
			throw new ServiceException(e);
		}

	}
	

	private BigDecimal calculateOrderTotal(List<ShippingProduct> products, MerchantStore store) throws Exception {
		
		BigDecimal total = new BigDecimal(0);
		for(ShippingProduct shippingProduct : products) {
			BigDecimal currentPrice = shippingProduct.getFinalPrice().getFinalPrice();
			currentPrice = currentPrice.multiply(new BigDecimal(shippingProduct.getQuantity()));
			total = total.add(currentPrice);
		}
		
		
		return total;
		
		
	}

	@Override
	public List<PackageDetails> getPackagesDetails(
			List<ShippingProduct> products, MerchantStore store)
			throws ServiceException {
		
		List<PackageDetails> packages = null;
		
		ShippingConfiguration shippingConfiguration = this.getShippingConfiguration(store);
		//determine if the system has to use BOX or ITEM
		ShippingPackageType shippingPackageType = ShippingPackageType.ITEM;
		if(shippingConfiguration!=null) {
			shippingPackageType = shippingConfiguration.getShippingPackageType();
		}
		
		if(shippingPackageType.name().equals(ShippingPackageType.BOX.name())){
			packages = packaging.getBoxPackagesDetails(products, store);
		} else {
			packages = packaging.getItemPackagesDetails(products, store);
		}
		
		return packages;
		
	}

	@Override
	public boolean requiresShipping(List<ShoppingCartItem> items,
			MerchantStore store) throws ServiceException {

		boolean requiresShipping = false;
		for(ShoppingCartItem item : items) {
			Product product = item.getProduct();
			if(!product.isProductVirtual() && product.isProductShipeable()) {
				requiresShipping = true;
			}
		}

		return requiresShipping;		
	}

	@Override
	public ShippingMetaData getShippingMetaData(MerchantStore store)
			throws ServiceException {
		
		
		try {
		
		ShippingMetaData metaData = new ShippingMetaData();

		// configured country
		List<Country> countries = getShipToCountryList(store, store.getDefaultLanguage());
		metaData.setShipToCountry(countries);
		
		// configured modules
		Map<String,IntegrationConfiguration> modules = Optional.ofNullable(getShippingModulesConfigured(store))
				.orElse(Collections.emptyMap());
		metaData.setModules(new ArrayList<>(modules.keySet()));
		
		// pre processors
		List<ShippingQuotePrePostProcessModule> preProcessors = this.shippingModulePreProcessors;
		List<String> preProcessorKeys = new ArrayList<String>();
		if(preProcessors!=null) {
			for(ShippingQuotePrePostProcessModule processor : preProcessors) {
				preProcessorKeys.add(processor.getModuleCode());
				if(SHIPPING_DISTANCE.equals(processor.getModuleCode())) {
					metaData.setUseDistanceModule(true);
				}
			}
		}
		metaData.setPreProcessors(preProcessorKeys);
		
		//post processors
		List<ShippingQuotePrePostProcessModule> postProcessors = this.shippingModulePostProcessors;
		List<String> postProcessorKeys = new ArrayList<String>();
		if(postProcessors!=null) {
			for(ShippingQuotePrePostProcessModule processor : postProcessors) {
				postProcessorKeys.add(processor.getModuleCode());
			}
		}
		metaData.setPostProcessors(postProcessorKeys);
		
		
		return metaData;
		
		} catch(Exception e) {
			throw new ServiceException("Exception while getting shipping metadata ",e);
		}
	}

	@Override
	public boolean hasTaxOnShipping(MerchantStore store) throws ServiceException {
		ShippingConfiguration shippingConfiguration = getShippingConfiguration(store);
		return shippingConfiguration.isTaxOnShipping();
	}
}