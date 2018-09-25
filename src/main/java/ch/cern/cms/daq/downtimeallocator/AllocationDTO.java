package ch.cern.cms.daq.downtimeallocator;

import ch.cern.cms.daq.downtimeallocator.model.Downtime;
import lombok.Data;

import java.util.Map;

@Data
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

    @Override
    public String toString() {
        return "AllocationDTO{" +
                "mergeMap=" + mergeMap +
                '}';
    }

}
