package ro.teamnet.bootstrap.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import ro.teamnet.bootstrap.domain.util.ModuleTypeEnum;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;


@Entity
@Table(name = "T_MODULE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Module {

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

    @OneToMany(mappedBy = "module", cascade = CascadeType.PERSIST)
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
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Module module = (Module) o;

        if (code != null ? !code.equals(module.code) : module.code != null) return false;
        if (type != null ? !type.equals(module.type) : module.type != null) return false;
        if (description != null ? !description.equals(module.description) : module.description != null) return false;
        if (parentModule != null ? !parentModule.equals(module.parentModule) : module.parentModule != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (parentModule != null ? parentModule.hashCode() : 0);
        return result;
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
