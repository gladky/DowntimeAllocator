package ch.cern.cms.daq.downtimeallocator;

import ch.cern.cms.daq.downtimeallocator.model.Downtime;

import java.util.List;

public class DowntimeDTO {


    private Long all;
    private Long validated;
    private Long ignored;
    private Long neww;
    private Float percentage;

    private List<Downtime> downtimeList;

    public Long getAll() {
        return all;
    }

    public void setAll(Long all) {
        this.all = all;
    }

    public Long getValidated() {
        return validated;
    }

    public void setValidated(Long validated) {
        this.validated = validated;
    }

    public Long getIgnored() {
        return ignored;
    }

    public void setIgnored(Long ignored) {
        this.ignored = ignored;
    }

    public Long getNeww() {
        return neww;
    }

    public void setNeww(Long neww) {
        this.neww = neww;
    }

    public Float getPercentage() {
        return percentage;
    }

    public void setPercentage(Float percentage) {
        this.percentage = percentage;
    }


    public List<Downtime> getDowntimeList() {
        return downtimeList;
    }

    public void setDowntimeList(List<Downtime> downtimeList) {
        this.downtimeList = downtimeList;
    }
}
