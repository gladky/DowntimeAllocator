package ch.cern.cms.daq.downtimeallocator.statistics;

import java.util.Date;
import java.util.Map;

public interface Statistic {

    void bulidStatistic(Date start, Date end);

    Map<String, Double> getStatistic();
}
