package ch.cern.cms.daq.downtimeallocator.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

@Entity
public class Allocation implements Comparable<Allocation> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private AllocationType allocationType;

    /** Value of time allocation. In milliseconds. */
    private int value;

    /** Whether the allocation should be automatic or manual */
    private Boolean automatic;

    /** In case of manual this will summarize all children allocations */
    private Integer unassigned;

    /** All children time allocations */
    @OneToMany(targetEntity = ch.cern.cms.daq.downtimeallocator.model.Allocation.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Allocation> children;

    /** Flag indicating if there is any allocation problem this node or in children nodes */
    private Boolean allocationProblem;

    public Allocation(){ }

    public Allocation(AllocationType allocationType){

        this.allocationType = allocationType;
        this.children =  new TreeSet<>();

    }

    public void setChildren(Set<Allocation> children) {
        this.children = children;
    }

    public Allocation withChild(Allocation child){
        this.children.add(child);
        return this;
    }

    public Allocation withAutomatic(boolean automatic){
        this.automatic = automatic;
        return this;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setAutomatic(Boolean automatic) {
        this.automatic = automatic;
    }

    public void setUnassigned(Integer unassigned) {
        this.unassigned = unassigned;
    }

    public void setAllocationProblem(Boolean allocationProblem) {
        this.allocationProblem = allocationProblem;
    }

    public AllocationType getAllocationType() {
        return allocationType;
    }

    public Boolean getAllocationProblem() {
        return allocationProblem;
    }

    public int getValue() {
        return value;
    }

    public Boolean isAutomatic() {
        return automatic;
    }

    public Integer getUnassigned() {
        return unassigned;
    }

    public Set<Allocation> getChildren() {
        return children;
    }

    @Override
    public String toString() {
        return "Allocation{" +
                "allocationType=" + allocationType +
                ", value=" + value +
                ", automatic=" + automatic +
                '}';
    }

    @Override
    public int compareTo(Allocation o) {
        return this.getAllocationType().name().compareTo(o.getAllocationType().name());
    }
}
