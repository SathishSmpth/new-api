package com.kamatchibotique.application.service.services.reference.zone;

import java.util.List;
import java.util.Map;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.service.services.common.CommonService;
import com.kamatchibotique.application.model.reference.country.Country;
import com.kamatchibotique.application.model.reference.language.Language;
import com.kamatchibotique.application.model.reference.zone.Zone;
import com.kamatchibotique.application.model.reference.zone.ZoneDescription;

public interface ZoneService extends CommonService<Long, Zone> {
	
	Zone getByCode(String code);

	void addDescription(Zone zone, ZoneDescription description) throws ServiceException;

	List<Zone> getZones(Country country, Language language)
			throws ServiceException;

	Map<String, Zone> getZones(Language language) throws ServiceException;

	List<Zone> getZones(String countryCode, Language language) throws ServiceException;


}
