package ro.teamnet.bootstrap.domain;


import javax.persistence.*;

@Entity
@Table(name = "T_EAGLE_USER")
public class EagleAccount extends Account {

    @ManyToOne
    @JoinColumn(name = "FK_LOCAL_CENTER")
    private LocalCenter localCenter;

    //TODO figure out what this entity is for

    public LocalCenter getLocalCenter() {
        return localCenter;
    }

    public void setLocalCenter(LocalCenter localCenter) {
        this.localCenter = localCenter;
    }
}
