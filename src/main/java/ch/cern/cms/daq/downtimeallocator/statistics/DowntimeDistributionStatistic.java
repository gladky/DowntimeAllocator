package ch.cern.cms.daq.downtimeallocator.statistics;

import ch.cern.cms.daq.downtimeallocator.DowntimeRespository;
import ch.cern.cms.daq.downtimeallocator.helpers.HierarchyPrinter;
import ch.cern.cms.daq.downtimeallocator.model.Allocation;
import ch.cern.cms.daq.downtimeallocator.model.AllocationHierarchyBuilder;
import ch.cern.cms.daq.downtimeallocator.model.AllocationResolver;
import ch.cern.cms.daq.downtimeallocator.model.Downtime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DowntimeDistributionStatistic implements Statistic {

    @Autowired
    DowntimeRespository downtimeRespository;

    @Autowired AllocationResolver allocationResolver;

    Map<String, Double> results;

    Logger logger = LoggerFactory.getLogger(DowntimeDistributionStatistic.class);


    @Override
    public void bulidStatistic(Date start, Date end) {

        results = new HashMap<>();

        List<Downtime> entries = downtimeRespository.findBetween(start, end);
        logger.info("Building downtime distribution statistic. " + entries.size() +" entries for period " + start + " - " + end);

        List<Downtime> validated = entries.stream().filter(e->"validated".equalsIgnoreCase(e.getStatus())).collect(Collectors.toList());
        logger.info("Using only validated entries: " + (entries.size() - validated.size()) + " are not validated in this period" );

        List<Downtime> notIgnored = validated.stream().filter(e->e.getIgnore() == false).collect(Collectors.toList());
        logger.info("Using only non-ignored entries: " + (validated.size() - notIgnored.size()) + " are ignored in this period" );

        Allocation statisticAllocation = AllocationHierarchyBuilder.buildAllocationHierarchy();

        for(Downtime downtime: notIgnored){
            Allocation source = downtime.getAllocation();
            updateStatisticAllocation(source, statisticAllocation);
        }


        logger.info("Allocation raw: ");
        HierarchyPrinter.printHierarchy(statisticAllocation);

        allocationResolver.resolveAllocation(statisticAllocation);

        logger.info("Allocation resolved: ");
        HierarchyPrinter.printHierarchy(statisticAllocation);

        logger.info("For this month there is " + notIgnored.stream().mapToInt(e->e.getAllocation().getValue()).sum() + " ms of unignored");
        logger.info("For this month there is " + entries.stream().mapToInt(e->e.getAllocation().getValue()).sum() + " ms of all");

    }

    @Override
    public Map<String, Double> getStatistic() {
        return results;
    }

    private void updateStatisticAllocation(Allocation source, Allocation destination){

        for(Allocation sourceChild: source.getChildren()){

            Allocation targetChild = destination.getChildren().stream().filter(c->c.getAllocationType() == sourceChild.getAllocationType()).findFirst().orElse(null);

            if(targetChild != null){
                updateStatisticAllocation(sourceChild, targetChild);
            } else{
                logger.warn("Could not find target child of type " + sourceChild.getAllocationType());
            }

        }

        destination.setValue(destination.getValue() + source.getValue());

    }
}
