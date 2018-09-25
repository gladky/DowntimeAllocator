package ch.cern.cms.daq.downtimeallocator;


import ch.cern.cms.daq.downtimeallocator.model.AllocationHierarchyBuilder;
import ch.cern.cms.daq.downtimeallocator.model.AllocationService;
import ch.cern.cms.daq.downtimeallocator.model.Downtime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.xml.bind.DatatypeConverter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class AllocationController {

    @Autowired
    AllocationService allocationService;

    @Autowired
    DowntimeRespository downtimeRespository;

    Logger logger = LoggerFactory.getLogger(AllocationController.class);

    @GetMapping("/allocation/new")
    public String newForm(Model model){

        Downtime downtime = new Downtime.DowntimeBuilder().build();

        downtimeRespository.save(downtime);

        AllocationDTO allocationDTO = buildDTO(downtime);
        model.addAttribute("allocation", allocationDTO);

        return "allocation-form";
    }



    @GetMapping("/allocation/{id}")
    public String updateForm(Model model, @PathVariable Long id) {

        Downtime downtime = downtimeRespository.findById(id).orElseThrow(RuntimeException::new);
        AllocationDTO allocationDTO = buildDTO(downtime);
        System.out.println("Requested: "+allocationDTO.getMergeMap());
        model.addAttribute("allocation", allocationDTO);
        return "allocation-form";
    }

    @PostMapping(value = "/shot")
    public ModelAndView saveAllocation(@ModelAttribute(value="prod") AllocationDTO allocationDTO) {


        Downtime downtime = downtimeRespository.findById(allocationDTO.getId()).orElseThrow(RuntimeException::new);

        allocationService.updateAllocations(downtime, allocationDTO.getMergeMap());

        downtime.setShifterId(allocationDTO.getShifterId());
        downtime.setSubsystem(allocationDTO.getSubsystem());

        downtime.setRcmsRecovery(allocationDTO.getRcmsRecovery());
        downtime.setSmoothDatatakingBefore(allocationDTO.getSmoothDatatakingBefore());
        downtime.setIgnore(allocationDTO.getIgnore());
        downtime.setUnidentified(allocationDTO.getUnidentified());
        downtime.setAutomaticUsed(allocationDTO.getAutomaticUsed());
        downtime.setAutomaticAvailable(allocationDTO.getAutomaticAvailable());
        downtime.setShortFirstAction(allocationDTO.getShortFirstAction());
        downtime.setTitle(allocationDTO.getTitle());
        downtime.setStatus(allocationDTO.getStatus());

        downtimeRespository.save(downtime);


        return new ModelAndView("redirect:/allocation/" + allocationDTO.getId());
    }

    @GetMapping(value="/allocation/next")
    public ModelAndView next(){

        Iterable<Downtime> downtimes = downtimeRespository.findAll();

        List<Downtime> d = StreamSupport.stream(downtimes.spliterator(), false).filter(e->e.getStatus().equalsIgnoreCase("new")).sorted((c1,c2)->c1.getAllocation().getValue()<c2.getAllocation().getValue()?1:-1)
                .collect(Collectors.toList());
        if(d.size() > 0){
            Long redirect = d.get(0).getId();
            return new ModelAndView("redirect:/allocation/" + redirect);
        } else{

            return new ModelAndView("redirect:/all/");
        }

    }

    // TODO: use DELETE
    @GetMapping(value = "/delete/{id}")
    public ModelAndView delete(Model model, @PathVariable Long id) {

        Downtime downtime = downtimeRespository.findById(id).orElseThrow(RuntimeException::new);

        downtimeRespository.delete(downtime);

        return new ModelAndView("redirect:/all/");
    }

    private AllocationDTO buildDTO(Downtime downtime){
        AllocationDTO allocationDto = new AllocationDTO();

        allocationDto.setSubsystem(downtime.getSubsystem());
        allocationDto.setShifterId(downtime.getShifterId());

        allocationDto.setRcmsRecovery(downtime.getRcmsRecovery());
        allocationDto.setSmoothDatatakingBefore(downtime.getSmoothDatatakingBefore());
        allocationDto.setIgnore(downtime.getIgnore());
        allocationDto.setUnidentified(downtime.getUnidentified());
        allocationDto.setAutomaticUsed(downtime.getAutomaticUsed());
        allocationDto.setAutomaticAvailable(downtime.getAutomaticAvailable());
        allocationDto.setShortFirstAction(downtime.getShortFirstAction());
        allocationDto.setStatus(downtime.getStatus());


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");


        Integer duration;
        if(downtime.getStart() != null) {
            if (downtime.getAllocation() == null || downtime.getAllocation().getValue() == 0) {

                duration = 60000;
            } else {
                duration = downtime.getAllocation().getValue();
            }
            Date endDate = new Date(downtime.getStart().getTime() + duration);

            String link = "http://daq-expert.cms/DAQExpert/?start=" +
                    sdf.format(downtime.getStart()) + "&end=" + sdf.format(endDate);

            allocationDto.setLink(link);

            // TODO: this should be done like with 'next' button - find previous and next
            List<Downtime> result = downtimeRespository.findAllByOrderByStartAsc();

            Downtime previous = result.stream().filter(c->c.getStart().getTime() < downtime.getStart().getTime()).sorted((c1,c2)->c1.getStart().getTime()>c2.getStart().getTime()?-1:1).findFirst().orElse(null);
            Downtime next = result.stream().filter(c->c.getStart().getTime() > downtime.getStart().getTime()).findFirst().orElse(null);

            Long prevoiusId = null, nextId = null;
            if(previous != null)
                prevoiusId = previous.getId();

            if(next != null){
                nextId = next.getId();
            }
            allocationDto.setPrevious("/allocation/"+prevoiusId);
            allocationDto.setNext("/allocation/"+nextId);
        }

        allocationDto.setDowntime(downtime);
        allocationDto.setId(downtime.getId());
        allocationDto.setMergeMap(AllocationHierarchyBuilder.getMap(downtime.getAllocation()));
        allocationDto.setUnassigned(AllocationHierarchyBuilder.getUnassignedMap(downtime.getAllocation()));
        allocationDto.setAutomatic(AllocationHierarchyBuilder.getEnabledMap(downtime.getAllocation()));
        allocationDto.setTitle(downtime.getTitle());

        logger.info("Next link: " + allocationDto.getNext());

        allocationDto.setDeleteLink("/delete/"+downtime.getId());



        return allocationDto;
    }
}
