package ch.cern.cms.daq.downtimeallocator;

import ch.cern.cms.daq.downtimeallocator.model.Downtime;

import java.util.Map;
import java.util.TreeMap;

public class AllocationDTO {

    private String title;

    private Long id;

    private Map<String, Integer> mergeMap;

    private Map<String, Integer> unassigned;

    private Map<String, Boolean> automatic;

    private String link;

    private Downtime downtime;

    private String previous;

    private String deleteLink;

    private String next;

    private String subsystem;

    private Long shifterId;


    private Boolean shortFirstAction;


    private Boolean unidentified;

    private Boolean stableBeams;

    private Boolean automaticAvailable;

    private Boolean automaticUsed;

    private Boolean rcmsRecovery;

    private Boolean smoothDatatakingBefore;

    private Boolean ignore;

    private String status;



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

    public Map<String, Integer> getMergeMap() {
        return mergeMap;
    }

    public void setMergeMap(Map<String, Integer> mergeMap) {

        this.mergeMap = mergeMap;
    }

    @Override
    public String toString() {
        return "AllocationDTO{" +
                "mergeMap=" + mergeMap +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Map<String, Integer> getUnassigned() {
        return unassigned;
    }

    public void setUnassigned(Map<String, Integer> unassigned) {
        this.unassigned = unassigned;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Downtime getDowntime() {
        return downtime;
    }

    public void setDowntime(Downtime downtime) {
        this.downtime = downtime;
    }

    public Map<String, Boolean> getAutomatic() {
        return automatic;
    }

    public void setAutomatic(Map<String, Boolean> automatic) {
        this.automatic = automatic;
    }

    public String getSubsystem() {
        return subsystem;
    }

    public void setSubsystem(String subsystem) {
        this.subsystem = subsystem;
    }

    public Long getShifterId() {
        return shifterId;
    }

    public void setShifterId(Long shifterId) {
        this.shifterId = shifterId;
    }


    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public String getDeleteLink() {
        return deleteLink;
    }

    public void setDeleteLink(String deleteLink) {
        this.deleteLink = deleteLink;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
