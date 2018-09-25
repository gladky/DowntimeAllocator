package ch.cern.cms.daq.downtimeallocator;

import ch.cern.cms.daq.downtimeallocator.model.Downtime;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class DowntimeDTO {

    private Long all;
    private Long validated;
    private Long ignored;
    private Long neww;
    private Float percentage;
    private List<Downtime> downtimeList;

}
