package ch.cern.cms.daq.downtimeallocator;

import ch.cern.cms.daq.downtimeallocator.model.Downtime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class DowntimeController {

    @Autowired
    DowntimeRespository downtimeRespository;


    @GetMapping("/all")
    public String browse(Model model,
                         @RequestParam(value = "start", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime start,
                         @RequestParam(value = "end", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime end,
                         @RequestParam(value = "ignored", required = false) Boolean ignored) {

        Iterable<Downtime> downtimes;

        if (start != null && end != null) {
            if (ignored != null)
                downtimes = downtimeRespository.findBetween(Date.from(start.toInstant()), Date.from(end.toInstant()), ignored);
            else
                downtimes = downtimeRespository.findBetween(Date.from(start.toInstant()), Date.from(end.toInstant()));
        } else {

            if (ignored != null)
                downtimes = downtimeRespository.findAllByIgnoreOrderByStartDesc(ignored);
            else
                downtimes = downtimeRespository.findAllByOrderByStartDesc();
        }


        // sort by size allocation
        /*List<Downtime> target = StreamSupport.stream(downtimes.spliterator(), false).sorted((c1,c2)->c1.getAllocation().getValue()<c2.getAllocation().getValue()?1:-1)
                .collect(Collectors.toList()); */

        // sort by date
        List<Downtime> target = StreamSupport.stream(downtimes.spliterator(), false)
                .collect(Collectors.toList());

        DowntimeDTO downtimeDTO = new DowntimeDTO();

        downtimeDTO.setAll(new Long(target.size()));
        downtimeDTO.setIgnored(target.stream().filter(d -> (d.getIgnore() != null && d.getIgnore() == true)).count());
        downtimeDTO.setValidated(target.stream().filter(d -> d.getStatus().equals("validated")).count());
        downtimeDTO.setNeww(target.stream().filter(d -> d.getStatus().equals("new")).count());
        downtimeDTO.setPercentage((1.0f * (downtimeDTO.getValidated())) / (downtimeDTO.getAll() * 1.0f));

        downtimeDTO.setDowntimeList(target);

        model.addAttribute("data", downtimeDTO);

        return "downtimes";
    }
}
