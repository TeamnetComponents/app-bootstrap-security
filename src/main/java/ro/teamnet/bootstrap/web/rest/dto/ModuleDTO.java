package ro.teamnet.bootstrap.web.rest.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import ro.teamnet.bootstrap.domain.Module;
import ro.teamnet.bootstrap.domain.util.ModuleTypeEnum;

import java.util.Set;

public class ModuleDTO {

    private Long id;
    private Long version;
    private String code;
    private String description;
    private Short type;
    private Module parentModule;
    private Set<ModuleRightDTO> moduleRights;

    public ModuleDTO() {
    }

    public ModuleDTO(Long id, Long version, String code, String description, Short type, Module parentModule, Set<ModuleRightDTO> moduleRights) {
        this.id = id;
        this.version = version;
        this.code = code;
        this.description = description;
        this.type = type;
        this.parentModule = parentModule;
        this.moduleRights = moduleRights;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    public Module getParentModule() {
        return parentModule;
    }

    public void setParentModule(Module parentModule) {
        this.parentModule = parentModule;
    }

    public Set<ModuleRightDTO> getModuleRights() {
        return moduleRights;
    }

    public void setModuleRights(Set<ModuleRightDTO> moduleRights) {
        this.moduleRights = moduleRights;
    }

    @JsonInclude
    public String getModuleType(){
        return ModuleTypeEnum.getCodeByValue(getType());
    }

    @Override
    public String toString() {
        return "Module{" +
                "id='" + id + '\'' +
                ", version='" + version + '\'' +
                ", code='" + code + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", parentModule='" + parentModule + '\'' +
                "}";
    }
}
