package ch.cern.cms.daq.downtimeallocator.helpers;

import ch.cern.cms.daq.downtimeallocator.model.Allocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HierarchyPrinter {

    private static Logger logger = LoggerFactory.getLogger(HierarchyPrinter.class);

    public static void printHierarchy(Allocation node){

        printNode(node, 0);

    }

    public static void printNode(Allocation node, int inten){

        String intendation = "";
        for(int i = 0; i< inten; i++){
            intendation += " ";
        }
        System.out.println(intendation + node.getAllocationType() + ", value: " +  node.getValue() + ", unassigned: " + node.getUnassigned() + ", warning: " + node.getAllocationProblem());

        node.getChildren().stream().forEach(c->printNode(c,inten + 2));
    }
}
