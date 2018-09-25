package ch.cern.cms.daq.downtimeallocator.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class builds and updates (at later stages, when hierarchy will be updated) Allocation hierarchy and it
 * responsible for enforcing interdependencies between allocation nodes.
 */
public class AllocationHierarchyBuilder {

    private static Logger logger = LoggerFactory.getLogger(AllocationHierarchyBuilder.class);

    public static Allocation buildAllocationHierarchy() {

        // 1
        Allocation root = new Allocation(AllocationType.Downtime).withAutomatic(true);


        // 2
        Allocation interventionTime = new Allocation(AllocationType.InterventionTime).withAutomatic(true);
        Allocation delayFromSanityCheck = new Allocation(AllocationType.DelayFromSanityCheck).withAutomatic(false);

        root.withChild(interventionTime).withChild(delayFromSanityCheck);


        // 3
        Allocation reactionTime = new Allocation(AllocationType.ReactionTime).withAutomatic(true);
        Allocation recoveryTime = new Allocation(AllocationType.RecoveryTime).withAutomatic(true);
        Allocation externalHelp = new Allocation(AllocationType.ExternalHelp).withAutomatic(false);
        Allocation wrongDecisionOverhead = new Allocation(AllocationType.WrongDecision).withAutomatic(true);
        Allocation improperToolsUsage = new Allocation(AllocationType.ImproperToolsUsageOverhead).withAutomatic(false);

        //4
        Allocation firstReaction = new Allocation(AllocationType.FirstReaction).withAutomatic(false);
        Allocation subsequentReaction = new Allocation(AllocationType.SubsequentReactions).withAutomatic(false);
        Allocation crutialJob = new Allocation(AllocationType.CurcialJobTime).withAutomatic(false);
        Allocation overheadFromBackground = new Allocation(AllocationType.OverheadFromBackgroundJobs).withAutomatic(false);
        Allocation evident = new Allocation(AllocationType.Evident).withAutomatic(false);
        Allocation estimate = new Allocation(AllocationType.Estimate).withAutomatic(false);

        reactionTime.withChild(firstReaction).withChild(subsequentReaction);
        recoveryTime.withChild(crutialJob).withChild(overheadFromBackground);
        wrongDecisionOverhead.withChild(evident).withChild(estimate);

        interventionTime.withChild(reactionTime).withChild(recoveryTime).withChild(externalHelp).withChild(wrongDecisionOverhead).withChild(improperToolsUsage).withAutomatic(true);

        logger.info("Allocation hierarchy built: " + root);

        return root;
    }


    public static Map<String,Integer> getMap(Allocation root){

        Map result = new LinkedHashMap();

        result.put(root.getAllocationType().name(), root.getValue());

        for(Allocation child: root.getChildren()){

            result.putAll(getMap(child));
        }

        return result;

    }

    public static Map<String,Integer> getUnassignedMap(Allocation root){

        Map result = new LinkedHashMap();

        result.put(root.getAllocationType().name(), root.getUnassigned());

        for(Allocation child: root.getChildren()){

            result.putAll(getUnassignedMap(child));
        }

        return result;

    }

    public static Map<String,Boolean> getEnabledMap(Allocation root){

        Map result = new LinkedHashMap();

        result.put(root.getAllocationType().name(), root.isAutomatic());

        for(Allocation child: root.getChildren()){

            result.putAll(getEnabledMap(child));
        }

        return result;

    }


}
