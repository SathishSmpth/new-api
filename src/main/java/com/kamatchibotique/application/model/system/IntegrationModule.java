package com.kamatchibotique.application.model.system;

import com.kamatchibotique.application.model.common.Auditable;
import jakarta.persistence.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "MODULE_CONFIGURATION", indexes = {
        @Index(name = "MODULE_CONFIGURATION_MODULE", columnList = "MODULE")})
public class IntegrationModule extends Auditable<String> {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "MODULE_CONF_ID")
    @TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "MOD_CONF_SEQ_NEXT_VAL")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private Long id;

    @Column(name = "MODULE")
    private String module;

    @Column(name = "CODE", nullable = false)
    private String code;

    @Column(name = "REGIONS")
    private String regions;

    @Column(name = "CONFIGURATION", length = 4000)
    private String configuration;

    @Column(name = "DETAILS")
    private String configDetails;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "IMAGE")
    private String image;

    @Column(name = "CUSTOM_IND")
    private boolean customModule = false;

    @Transient
    private Set<String> regionsSet = new HashSet<String>();

    @Transient
    private String binaryImage = null;

    @Transient
    private Map<String, ModuleConfig> moduleConfigs = new HashMap<String, ModuleConfig>();

    @Transient
    private Map<String, String> details = new HashMap<String, String>();

    @Transient
    private String configurable = null;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getRegions() {
        return regions;
    }

    public void setRegions(String regions) {
        this.regions = regions;
    }

    public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    public void setRegionsSet(Set<String> regionsSet) {
        this.regionsSet = regionsSet;
    }

    public Set<String> getRegionsSet() {
        return regionsSet;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setModuleConfigs(Map<String, ModuleConfig> moduleConfigs) {
        this.moduleConfigs = moduleConfigs;
    }

    public Map<String, ModuleConfig> getModuleConfigs() {
        return moduleConfigs;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setCustomModule(boolean customModule) {
        this.customModule = customModule;
    }

    public boolean isCustomModule() {
        return customModule;
    }

    public String getConfigDetails() {
        return configDetails;
    }

    public void setConfigDetails(String configDetails) {
        this.configDetails = configDetails;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getBinaryImage() {
        return binaryImage;
    }

    public void setBinaryImage(String binaryImage) {
        this.binaryImage = binaryImage;
    }

    public String getConfigurable() {
        return configurable;
    }

    public void setConfigurable(String configurable) {
        this.configurable = configurable;
    }

    public Map<String, String> getDetails() {
        return details;
    }

    public void setDetails(Map<String, String> details) {
        this.details = details;
    }
}
