package ro.teamnet.bootstrap.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import ro.teamnet.bootstrap.domain.util.ModuleTypeEnum;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;


@Entity
@Table(name = "T_MODULE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Module implements Serializable{

    @Id
    @Column(name = "ID_MODULE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @Column(name = "VERSION")
    private Long version;

    @NotNull
    @Column(name = "CODE", length = 100)
    private String code;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "TYPE")
    private Short type;

    @OneToMany(mappedBy = "module", cascade = {CascadeType.PERSIST,CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH})
    private Collection<ModuleRight> moduleRights = new ArrayList<>();

    @OneToMany(mappedBy = "parentModule")
    private Collection<Module> childModules = new ArrayList<>();

    @ManyToOne()
    @JoinColumn(name = "FK_PARENT_MODULE", nullable = true)
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

    public Collection<ModuleRight> getModuleRights() {
        return moduleRights;
    }

    public void setModuleRights(Collection<ModuleRight> moduleRights) {
        this.moduleRights = moduleRights;
    }

    public Collection<Module> getChildModules() {
        return childModules;
    }

    public void setChildModules(Collection<Module> childModules) {
        this.childModules = childModules;
    }

    public Module getParentModule() {
        return parentModule;
    }

    public void setParentModule(Module parentModule) {
        this.parentModule = parentModule;
    }

    @JsonInclude
    public String getModuleType(){
        return ModuleTypeEnum.getCodeByValue(getType());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Module module = (Module) o;

        if (!code.equals(module.code)) return false;
        if (!type.equals(module.type)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = code.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }
}
