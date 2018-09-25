package ch.cern.cms.daq.downtimeallocator.model;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.Date;

/**
 * Downtime entry that will be a starting point of the analysis and allocation process. It is based on results from
 * DAQExpert.
 */
@Entity
public class Downtime {

    private static Logger logger = LoggerFactory.getLogger(Downtime.class);


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    /**
     * An optional title that would make the downtimes easier to identify: e.g Fed problem #384
     */
    private String title;


    private Long shifterId;

    private String subsystem;

    private Boolean shortFirstAction;

    private Boolean unidentified;

    private Boolean stableBeams;

    private Boolean automaticAvailable;

    private Boolean automaticUsed;

    private Boolean rcmsRecovery;

    private Boolean smoothDatatakingBefore;

    private Boolean ignore;

    private String status;

    /**
     * Time allocation. Holds root entry. Under that root are the children.
     */
    @OneToOne(targetEntity = ch.cern.cms.daq.downtimeallocator.model.Allocation.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Allocation allocation;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_date")
    private Date start;

    public void setAllocation(Allocation allocation) {
        this.allocation = allocation;
    }

    public Allocation getAllocation() {
        return allocation;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStart(Date start) {
        this.start = start;
    }


    public String getTitle() {
        return title;
    }

    public Date getStart() {
        return start;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Downtime{" +

                "id=" + id +
                ", allocation=" + allocation +
                ", start=" + start +
                ", duration=" + allocation.getValue() +
                '}';
    }

    public Long getShifterId() {
        return shifterId;
    }

    public void setShifterId(Long shifterId) {
        this.shifterId = shifterId;
    }

    public String getSubsystem() {
        return subsystem;
    }

    public void setSubsystem(String subsystem) {
        this.subsystem = subsystem;
    }

    public Boolean getShortFirstAction() {
        return shortFirstAction;
    }

    public void setShortFirstAction(Boolean shortFirstAction) {
        this.shortFirstAction = shortFirstAction;
    }

    public Boolean getUnidentified() {
        return unidentified;
    }

    public void setUnidentified(Boolean unidentified) {
        this.unidentified = unidentified;
    }

    public Boolean getStableBeams() {
        return stableBeams;
    }

    public void setStableBeams(Boolean stableBeams) {
        this.stableBeams = stableBeams;
    }

    public Boolean getAutomaticAvailable() {
        return automaticAvailable;
    }

    public void setAutomaticAvailable(Boolean automaticAvailable) {
        this.automaticAvailable = automaticAvailable;
    }

    public Boolean getAutomaticUsed() {
        return automaticUsed;
    }

    public void setAutomaticUsed(Boolean automaticUsed) {
        this.automaticUsed = automaticUsed;
    }

    public Boolean getRcmsRecovery() {
        return rcmsRecovery;
    }

    public void setRcmsRecovery(Boolean rcmsRecovery) {
        this.rcmsRecovery = rcmsRecovery;
    }

    public Boolean getSmoothDatatakingBefore() {
        return smoothDatatakingBefore;
    }

    public void setSmoothDatatakingBefore(Boolean smoothDatatakingBefore) {
        this.smoothDatatakingBefore = smoothDatatakingBefore;
    }

    public Boolean getIgnore() {
        return ignore;
    }

    public void setIgnore(Boolean ignore) {
        this.ignore = ignore;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class DowntimeBuilder{
        public Downtime build(Date start, int duration){
            Downtime downtime = new Downtime();
            downtime.setStart(start);
            downtime.setStatus("new");
            downtime.setAllocation(AllocationHierarchyBuilder.buildAllocationHierarchy());

            downtime.getAllocation().setValue(duration);

            return downtime;
        }

        public Downtime build(){
            logger.info("Building default object");
            Downtime downtime = new Downtime();
            downtime.setAllocation(AllocationHierarchyBuilder.buildAllocationHierarchy());
            return downtime;
        }

    }


}
