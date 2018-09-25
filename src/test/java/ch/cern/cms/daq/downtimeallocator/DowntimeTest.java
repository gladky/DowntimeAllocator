package ch.cern.cms.daq.downtimeallocator;

import ch.cern.cms.daq.downtimeallocator.model.Downtime;
import org.junit.Test;

import javax.xml.bind.DatatypeConverter;

public class DowntimeTest {

    @Test
    public void simpleTest(){

        Downtime downtime = new Downtime.DowntimeBuilder().build(DatatypeConverter.parseDateTime("2018-09-14T19:00:00").getTime(), 100000);


        System.out.println(downtime);


    }

}