package com.kamatchibotique.application.service.impl.system;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kamatchibotique.application.constants.Constants;
import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.model.system.IntegrationModule;
import com.kamatchibotique.application.model.system.ModuleConfig;
import com.kamatchibotique.application.repository.system.ModuleConfigurationRepository;
import com.kamatchibotique.application.service.impl.common.CommonServiceImpl;
import com.kamatchibotique.application.service.impl.reference.loader.IntegrationModulesLoader;
import com.kamatchibotique.application.service.services.system.ModuleConfigurationService;
import com.kamatchibotique.application.utils.CacheUtils;
import modules.commons.ModuleStarter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Service("moduleConfigurationService")
public class ModuleConfigurationServiceImpl extends CommonServiceImpl<Long, IntegrationModule>
		implements ModuleConfigurationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ModuleConfigurationServiceImpl.class);

	@Autowired
	private IntegrationModulesLoader integrationModulesLoader;

	private ModuleConfigurationRepository moduleConfigurationRepository;

	@Autowired
	private CacheUtils cache;

	@Autowired(required = false)
	private List<ModuleStarter> payments = null; // all bound payment module starters if any

	@Autowired
	public ModuleConfigurationServiceImpl(ModuleConfigurationRepository moduleConfigurationRepository) {
		super(moduleConfigurationRepository);
		this.moduleConfigurationRepository = moduleConfigurationRepository;
	}

	@Override
	public IntegrationModule getByCode(String moduleCode) {
		return moduleConfigurationRepository.findByCode(moduleCode);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<IntegrationModule> getIntegrationModules(String module) {

		List<IntegrationModule> modules = null;
		try {

			/**
			 * Modules are loaded using
			 */
			modules = (List<IntegrationModule>) cache.getFromCache("INTEGRATION_M" + module); // PAYMENT_MODULES
																								// SHIPPING_MODULES
			if (modules == null) {
				modules = moduleConfigurationRepository.findByModule(module);
				// set json objects
				for (IntegrationModule mod : modules) {

					String regions = mod.getRegions();
					if (regions != null) {
						Object objRegions = JSONValue.parse(regions);
						JSONArray arrayRegions = (JSONArray) objRegions;
						for (Object arrayRegion : arrayRegions) {
							mod.getRegionsSet().add((String) arrayRegion);
						}
					}

					String details = mod.getConfigDetails();
					if (details != null) {

						Map<String, String> objDetails = (Map<String, String>) JSONValue.parse(details);
						mod.setDetails(objDetails);

					}

					String configs = mod.getConfiguration();
					if (configs != null) {

						Object objConfigs = JSONValue.parse(configs);
						JSONArray arrayConfigs = (JSONArray) objConfigs;

						Map<String, ModuleConfig> moduleConfigs = new HashMap<String, ModuleConfig>();

						for (Object arrayConfig : arrayConfigs) {

							Map values = (Map) arrayConfig;
							String env = (String) values.get("env");
							ModuleConfig config = new ModuleConfig();
							config.setScheme((String) values.get("scheme"));
							config.setHost((String) values.get("host"));
							config.setPort((String) values.get("port"));
							config.setUri((String) values.get("uri"));
							config.setEnv((String) values.get("env"));
							if (values.get("config1") != null) {
								config.setConfig1((String) values.get("config1"));
							}
							if (values.get("config2") != null) {
								config.setConfig1((String) values.get("config2"));
							}

							moduleConfigs.put(env, config);

						}

						mod.setModuleConfigs(moduleConfigs);

					}

				}

				if (this.payments != null) {
					for (ModuleStarter mod : this.payments) {
						IntegrationModule m = new IntegrationModule();
						m.setCode(mod.getUniqueCode());
						m.setModule(Constants.PAYMENT_MODULES);
						
						
						if(CollectionUtils.isNotEmpty(mod.getSupportedCountry())) {
							m.setRegions(mod.getSupportedCountry().toString());
							m.setRegionsSet(new HashSet<String>(mod.getSupportedCountry()));
						}
						
						if(!StringUtils.isBlank(mod.getLogo())) {
							m.setBinaryImage(mod.getLogo());//base 64
						}
						
						
						if(StringUtils.isNotBlank(mod.getConfigurable())) {
							m.setConfigurable(mod.getConfigurable());
						}

						modules.add(m);
					}
				}

				cache.putInCache(modules, "INTEGRATION_M" + module);
			}

		} catch (Exception e) {
			LOGGER.error("getIntegrationModules()", e);
		}
		return modules;

	}


	@Override
	public void createOrUpdateModule(String json) throws ServiceException {

		ObjectMapper mapper = new ObjectMapper();

		try {
			@SuppressWarnings("rawtypes")
			Map object = mapper.readValue(json, Map.class);
			IntegrationModule module = integrationModulesLoader.loadModule(object);
			if (module != null) {
				IntegrationModule m = this.getByCode(module.getCode());
				if (m != null) {
					this.delete(m);
				}
				this.create(module);
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}

	}

}
