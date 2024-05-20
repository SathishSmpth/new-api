package com.kamatchibotique.application.repository.system;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kamatchibotique.application.model.system.SystemConfiguration;

public interface SystemConfigurationRepository extends JpaRepository<SystemConfiguration, Long> {


	SystemConfiguration findByKey(String key);

}
