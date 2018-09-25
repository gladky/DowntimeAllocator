package ch.cern.cms.daq.downtimeallocator.model;

import ch.cern.cms.daq.downtimeallocator.DowntimeRespository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AllocationService {

    private static final Logger logger = LoggerFactory.getLogger(AllocationService.class);

    @Autowired
    DowntimeRespository downtimeRespository;

    @Autowired
    AllocationResolver allocationResolver;

    /**
     * Update given downtime record with new allocation values (given by map of allocation-type-name to integer value)
     * @param downtime downtime in context
     * @param newAllocations Allocation-type-name to value map
     */
    public void updateAllocations(Downtime downtime, Map<String,Integer> newAllocations){

        Map<AllocationType, Integer> allocations = transformAllocations(newAllocations);
        updateAllocations(downtime.getAllocation(), allocations);


        allocationResolver.resolveAllocation(downtime.getAllocation());

        downtimeRespository.save(downtime);
    }

    /**
     * Recursive method that will allocate new values for a give allocation node.
     * @param allocation allocation to be updated
     * @param allocations map of new allocations
     */
    private void updateAllocations(Allocation allocation, Map<AllocationType, Integer> allocations){

        logger.info("Allocating " + allocation.getAllocationType() + " with keys: " + allocations.keySet());

        Integer value = allocations.get(allocation.getAllocationType());

        if(value != null){
            allocation.setValue(value);
        }else{
            logger.warn("Could not get a new allocation value for " + allocation.getAllocationType() + ", current value remains " + allocation.getValue());
        }
        allocation.getChildren().stream().forEach(a->updateAllocations(a, allocations));

    }

    /**
     * Transform allocations. From String->Integer to AllocationType->Integer
     * @param allocations allocation map to transform
     * @return transformed allocation map
     */
    private Map<AllocationType, Integer> transformAllocations(Map<String,Integer> allocations){

        Map<AllocationType, Integer> result = new HashMap<>();

        for(Map.Entry<String, Integer> entry: allocations.entrySet()){

            AllocationType mapped = AllocationType.valueOf(entry.getKey());

            if(mapped != null){
                result.put(mapped, entry.getValue());
            }
        }

        return result;

    }
}
