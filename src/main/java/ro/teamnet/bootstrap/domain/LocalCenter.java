package ro.teamnet.bootstrap.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 *  A LocalCenter. This class is for future case usage
 *  TODO conplete entity with all the needed fields
 */
@Entity
@Table(name = "T_LOCAL_CENTER")
public class LocalCenter {

    @Id
    @Column(name = "ID_LOCAL_CENTER")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
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
    private Set<EagleAccount> eagleUsers = new HashSet<>();

    @Column(name = "IS_ACTIVE")
    private Boolean active;

    @OneToMany(mappedBy = "parentLocalCenter")
    private Set<LocalCenter> childLocalCenter = new HashSet<>();

    @ManyToOne()
    @JoinColumn(name = "FK_LOCAL_CENTER", nullable = true)
    private LocalCenter parentLocalCenter;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<LocalCenter> getChildLocalCenter() {
        return childLocalCenter;
    }

    public void setChildLocalCenter(Set<LocalCenter> childLocalCenter) {
        this.childLocalCenter = childLocalCenter;
    }

    public LocalCenter getParentLocalCenter() {
        return parentLocalCenter;
    }

    public void setParentLocalCenter(LocalCenter parentLocalCenter) {
        this.parentLocalCenter = parentLocalCenter;
    }

    public Set<EagleAccount> getEagleUsers() {
        return eagleUsers;
    }

    public void setEagleUsers(Set<EagleAccount> eagleUsers) {
        this.eagleUsers = eagleUsers;
    }

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
                ", email=" + email +
                ", active=" + active +
                ", childLocalCenter=" + childLocalCenter +
                ", parentLocalCenter=" + parentLocalCenter +
                ", eagleUsers=" + eagleUsers +
                '}';

    }
}
