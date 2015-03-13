package ro.teamnet.bootstrap.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * A Role. This entity is used as a {@link GrantedAuthority} and represents an authority.
 */
@Entity
@Table(name = "T_ROLE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Role extends AbstractAuditingEntity implements Serializable, GrantedAuthority {


    @Id
    @Column(name = "ID_ROLE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @Column(name = "VERSION")
    private Long version;

    @NotNull
    @Column(name = "CODE", length = 100, unique = true)
    private String code;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "SORT")
    private Integer order;

    @NotNull
    @Column(name = "VALID_FROM")
    @Temporal(TemporalType.DATE)
    private Date validFrom;

    @NotNull
    @Column(name = "VALID_TO")
    @Temporal(TemporalType.DATE)
    private Date validTo;

    @NotNull @Column(name = "IS_ACTIVE")
    private Boolean active;

    @Column(name = "LOCAL")
    private Short local;

    @ManyToMany (fetch=FetchType.EAGER)
    @JoinTable(
        name = "T_ROLE_MODULE_RIGHTS",
        joinColumns = {@JoinColumn(name = "fk_role", referencedColumnName = "id_role")},
        inverseJoinColumns = {@JoinColumn(name = "fk_module_right", referencedColumnName = "id_module_right")})
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
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
        this.moduleRights.clear();
        this.moduleRights.addAll(moduleRights);
    }

    /* (non-Javadoc)
                  * @see org.springframework.security.core.GrantedAuthority#getAuthority()
                  */
    @Override
    @Transient
    public String getAuthority() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Role role = (Role) o;

        if (version != null ? !version.equals(role.version) : role.version != null) return false;
        if (code != null ? !code.equals(role.code) : role.code != null) return false;
        if (description != null ? !description.equals(role.description) : role.description != null) return false;
        if (order != null ? !order.equals(role.order) : role.order != null) return false;
        if (validFrom != null ? !validFrom.equals(role.validFrom) : role.validFrom != null) return false;
        if (validTo != null ? !validTo.equals(role.validTo) : role.validTo != null) return false;
        if (active != null ? !active.equals(role.active) : role.active != null) return false;
        if (local != null ? !local.equals(role.local) : role.local != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (order != null ? order.hashCode() : 0);
        result = 31 * result + (validFrom != null ? validFrom.hashCode() : 0);
        result = 31 * result + (validTo != null ? validTo.hashCode() : 0);
        result = 31 * result + (active != null ? active.hashCode() : 0);
        result = 31 * result + (local != null ? local.hashCode() : 0);
        return result;
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
