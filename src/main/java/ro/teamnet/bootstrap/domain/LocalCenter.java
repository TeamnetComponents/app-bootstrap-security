package ro.teamnet.bootstrap.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "T_LOCAL_CENTER")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class LocalCenter {

    @Id
    @Column(name = "ID_LOCAL_CENTER")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "CODE")
    private String code;

    @Column(name = "NAME")
    private String name;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "TYPE")
    private String type;

    @OneToMany(mappedBy = "localCenter")
    private Collection<EagleAccount> eagleUsers = new ArrayList<>();

    @Column(name = "IS_ACTIVE")
    private Boolean active;

    @OneToMany(mappedBy = "parentLocalCenter")
    private Collection<LocalCenter> childLocalCenter = new ArrayList<>();

    @ManyToOne()
    @JoinColumn(name = "FK_LOCAL_CENTER", nullable = true)
    private LocalCenter parentLocalCenter;

    //TODO conplete entity with all the needed fields

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LocalCenter)) return false;

        LocalCenter localCenter = (LocalCenter) o;

        if (name != null ? !name.equals(localCenter.name) : localCenter.name != null) return false;
        if (code != null ? !code.equals(localCenter.code) : localCenter.code != null) return false;
        if (address != null ? !address.equals(localCenter.address) : localCenter.address != null) return false;
        if (phone != null ? !phone.equals(localCenter.phone) : localCenter.phone != null) return false;
        if (email != null ? !email.equals(localCenter.email) : localCenter.email != null) return false;
        if (active != null ? !active.equals(localCenter.active) : localCenter.active != null) return false;
        if (type != null ? !type.equals(localCenter.type) : localCenter.type != null) return false;
        if (parentLocalCenter != null ? !parentLocalCenter.equals(localCenter.parentLocalCenter) : localCenter.parentLocalCenter != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (active != null ? active.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (parentLocalCenter != null ? parentLocalCenter.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", code=" + code +
                ", name=" + name +
                ", address=" + address +
                ", phone=" + phone +
                ", type=" + type +
                ", eagleUsers=" + email +
                ", active=" + active +
                ", parentLocalCenter=" + parentLocalCenter;

    }
}
