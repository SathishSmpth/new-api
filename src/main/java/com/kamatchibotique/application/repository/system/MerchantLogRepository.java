package com.kamatchibotique.application.repository.system;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.system.MerchantLog;

public interface MerchantLogRepository extends JpaRepository<MerchantLog, Long> {

	List<MerchantLog> findByStore(MerchantStore store);
}
