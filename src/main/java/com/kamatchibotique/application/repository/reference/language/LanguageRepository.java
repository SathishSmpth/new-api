package com.kamatchibotique.application.repository.reference.language;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.model.reference.language.Language;

public interface LanguageRepository extends JpaRepository <Language, Integer> {
	
	Language findByCode(String code) throws ServiceException;
	


}
