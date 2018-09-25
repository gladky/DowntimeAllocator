package ch.cern.cms.daq.downtimeallocator.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AllocationResolver {

    private Logger logger = LoggerFactory.getLogger(AllocationResolver.class);

    public void resolveAllocation(Allocation node){

        logger.info("Resolving node: " + node);

        if(node.getChildren().size() >0){
            node.getChildren().stream().forEach(c->this.resolveAllocation(c));
        }

        if(node.isAutomatic()){
            node.setValue(node.getChildren().stream().mapToInt(n -> n.getValue()).sum());
            node.setUnassigned(0);
        } else{

            if(node.getChildren().size() > 0){
                Integer childrenSum = node.getChildren().stream().mapToInt(n -> n.getValue()).sum();
                Integer unassigned = node.getValue() - childrenSum;
                node.setUnassigned(unassigned);
            }else{
                node.setUnassigned(0);
            }

        }

        Boolean allAssigned = true;
        for(Allocation child: node.getChildren()){
            if(child.getUnassigned() != 0){
                allAssigned = false;
            }
        }

        if(!allAssigned){
            node.setAllocationProblem(true);
        } else{
            if(node.getUnassigned() != 0){
                node.setAllocationProblem(true);
            } else{
                node.setAllocationProblem(false);
            }
        }

        logger.info("Node "+node.getAllocationType()+" resolved. value: " + node.getValue() + ", unassigned: " + node.getUnassigned());

    }
}
