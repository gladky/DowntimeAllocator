package ch.cern.cms.daq.downtimeallocator;

import ch.cern.cms.daq.downtimeallocator.model.Allocation;
import ch.cern.cms.daq.downtimeallocator.model.AllocationResolver;
import ch.cern.cms.daq.downtimeallocator.model.AllocationType;
import ch.cern.cms.daq.downtimeallocator.helpers.HierarchyPrinter;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AllocationResolverTest {


    private Logger logger = LoggerFactory.getLogger(AllocationResolverTest.class);

    @Test
    public void simpleTest() {
        Allocation root = new Allocation(AllocationType.Downtime);
        Allocation interventionTime = new Allocation(AllocationType.InterventionTime);
        Allocation delayFromSanity = new Allocation(AllocationType.DelayFromSanityCheck);
        Allocation reactionTime = new Allocation(AllocationType.ReactionTime);
        Allocation recoveryTime = new Allocation(AllocationType.RecoveryTime);

        root.withChild(interventionTime).withChild(delayFromSanity).withAutomatic(false); // TODO: root will be manual (downtime from expert)
        interventionTime.withChild(reactionTime).withChild(recoveryTime).withAutomatic(true);
        reactionTime.withAutomatic(false);
        delayFromSanity.withAutomatic(false);
        recoveryTime.withAutomatic(false);

        root.setValue(10000);
        reactionTime.setValue(1000);
        delayFromSanity.setValue(9000);

        AllocationResolver resolver = new AllocationResolver();

        resolver.resolveAllocation(root);

        logger.info("Allocation hierarchy built: " + root);

        HierarchyPrinter.printHierarchy(root);
    }

}