package ro.teamnet.bootstrap.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.security.core.GrantedAuthority;
import ro.teamnet.bootstrap.domain.util.ModuleRightTypeEnum;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A ModuleRight. This entity is used as a {@link GrantedAuthority} and represents a permission.
 */
@Entity
@Table(name = "T_MODULE_RIGHT")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ModuleRight implements Serializable, GrantedAuthority {

    @Id
    @Column(name = "ID_MODULE_RIGHT")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "VERSION")
    private String version;

    @Column( name = "MODULE_RIGHT" )
    private Short right;

    @ManyToOne
    @JoinColumn(name = "FK_MODULE")
    private Module module;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Short getRight() {
        return right;
    }

    public void setRight(Short right) {
        this.right = right;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    @JsonInclude
    public String getModuleRightCode(){
        return ModuleRightTypeEnum.getCodeByValue(getRight());
    }

    /* (non-Javadoc)
           * @see org.springframework.security.core.GrantedAuthority#getAuthority()
       */
    @Override
    @Transient
    public String getAuthority() { return ModuleRightTypeEnum.getCodeByValue(getRight()) + '_' + module.getCode(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ModuleRight moduleRight = (ModuleRight) o;

        if (version != null ? !version.equals(moduleRight.version) : moduleRight.version != null) return false;
        if (right != null ? !right.equals(moduleRight.right) : moduleRight.right != null) return false;
        if (module != null ? !module.equals(moduleRight.module) : moduleRight.module != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (right != null ? right.hashCode() : 0);
        result = 31 * result + (module != null ? module.hashCode() : 0);
        return result;
    }

    @Override
    public String toString(){
        return "ModuleRight{" +
                "id='" + id + '\'' +
                ", version='" + version + '\'' +
                ", right='" + right + '\'' +
                ", module='" + module + '\'' +
                "}";
    }

}
