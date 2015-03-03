package ro.teamnet.bootstrap.web.rest.dto;


import ro.teamnet.bootstrap.domain.ModuleRight;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class RoleDTO {

    private Long id;
    private Long version;
    private String code;
    private String description;
    private Integer order;
    private Date validFrom;
    private Date validTo;
    private Boolean active;
    private Short local;
    private Collection<ModuleRight> moduleRights = new ArrayList<>();

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

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Date getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }

    public Date getValidTo() {
        return validTo;
    }

    public void setValidTo(Date validTo) {
        this.validTo = validTo;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Short getLocal() {
        return local;
    }

    public void setLocal(Short local) {
        this.local = local;
    }

    public Collection<ModuleRight> getModuleRights() {
        return moduleRights;
    }

    public void setModuleRights(Collection<ModuleRight> moduleRights) {
        this.moduleRights = moduleRights;
    }

    @Override
    public String toString(){
        return "Role{" +
                "id='+" + id + '\'' +
                ", version='" + version + '\'' +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", order='" + order + '\'' +
                ", validFrom='" + validFrom + '\'' +
                ", validTo='" + validTo + '\'' +
                ", active='" + active + '\'' +
                ", local='" + local + '\'' +
                "}";
    }
}
