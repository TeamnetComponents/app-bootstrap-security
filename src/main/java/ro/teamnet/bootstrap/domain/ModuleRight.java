package ro.teamnet.bootstrap.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

//    @NotNull
    @Column(name = "VERSION")
    private Long version;

    @Column( name = "MODULE_RIGHT" )
    private Short right;

    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH})
    @JoinColumn(name = "FK_MODULE", updatable = true, insertable = true)
    private Module module;

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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ModuleRight that = (ModuleRight) o;

        if (right != null ? !right.equals(that.right) : that.right != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return right != null ? right.hashCode() : 0;
    }
}
