package ch.cern.cms.daq.downtimeallocator;

import ch.cern.cms.daq.downtimeallocator.model.Downtime;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Date;
import java.util.List;

@RepositoryRestResource(collectionResourceRel= "downtimes", path="downtimes")
public interface DowntimeRespository extends PagingAndSortingRepository<Downtime, Long> {


    List<Downtime> findByTitle(@Param("title") String title);

    List<Downtime> findAllByOrderByStartAsc();
    List<Downtime> findAllByIgnoreOrderByStartAsc(Boolean ignore);

    List<Downtime> findAllByOrderByStartDesc();
    List<Downtime> findAllByIgnoreOrderByStartDesc(Boolean ignore);


    @Query("select d from Downtime d where d.start <= :endDate and d.start >= :startDate order by d.start")
    List<Downtime> findBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate);


    @Query("select d from Downtime d where d.ignore = :ignore and d.start <= :endDate and d.start >= :startDate order by d.start")
    List<Downtime> findBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("ignore") Boolean ignore);


}
