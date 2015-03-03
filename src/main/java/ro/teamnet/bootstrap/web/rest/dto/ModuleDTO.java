package ro.teamnet.bootstrap.web.rest.dto;


import ro.teamnet.bootstrap.domain.Module;

public class ModuleDTO {

    private Long id;
    private Long version;
    private String code;
    private String description;
    private Short type;
    private Module parentModule;


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
