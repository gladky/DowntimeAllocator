package ch.cern.cms.daq.downtimeallocator.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class AllocationHierarchyBuilderTest {


    @Test
    public void test(){

        Allocation root = AllocationHierarchyBuilder.buildAllocationHierarchy();


        AllocationHierarchyBuilder.getMap(root).values().stream().forEach(System.out::println);



    }

}