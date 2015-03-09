package ro.teamnet.bootstrap.domain;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 *  A EagleAccount. This class is for future case usage
 *  TODO figure out what this entity is for
 */
@Entity
@Table(name = "T_EAGLE_USER")
public class EagleAccount extends Account {

    @ManyToOne
    @JoinColumn(name = "FK_LOCAL_CENTER")
    private LocalCenter localCenter;

    @OneToMany(mappedBy = "eagleUser")
    private Collection<Employee> employee = new ArrayList<>();

    public LocalCenter getLocalCenter() {
        return localCenter;
    }

    public void setLocalCenter(LocalCenter localCenter) {
        this.localCenter = localCenter;
    }

    public Collection<Employee> getEmployee() {
        return employee;
    }

    public void setEmployee(Collection<Employee> employee) {
        this.employee = employee;
    }
}
