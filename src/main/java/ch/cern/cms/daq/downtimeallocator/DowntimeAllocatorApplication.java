package ch.cern.cms.daq.downtimeallocator;

import ch.cern.cms.daq.downtimeallocator.model.AllocationResolver;
import ch.cern.cms.daq.downtimeallocator.model.AllocationType;
import ch.cern.cms.daq.downtimeallocator.model.Downtime;
import ch.cern.cms.daq.downtimeallocator.statistics.DowntimeDistributionStatistic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.xml.bind.DatatypeConverter;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class DowntimeAllocatorApplication {

	@Autowired
	private DowntimeRespository downtimeRespository;

	//TODO: temporary solution
	@Autowired
	private DowntimeDistributionStatistic downtimeDistributionStatistic;

	//TODO: temporary solution
	@Autowired
	private CSVImporter csvImporter;

	public static void main(String[] args) {
		SpringApplication.run(DowntimeAllocatorApplication.class, args);
	}

	@Bean
	CommandLineRunner init() {
		return (evt) -> {

			Downtime downtime = new Downtime.DowntimeBuilder().build(new Date(), 60000);
			downtime.setTitle("Initial downtime");
			downtime.setSubsystem("ECAL");
			downtime.getAllocation().setAutomatic(false);

			downtime.getAllocation().getChildren().stream().filter(a->a.getAllocationType()== AllocationType.InterventionTime).findFirst().ifPresent(c->c.getChildren().stream().filter(a->a.getAllocationType() == AllocationType.ReactionTime).findFirst().ifPresent(a->a.setValue(40000)));

			new AllocationResolver().resolveAllocation(downtime.getAllocation());
			//downtimeRespository.save(downtime);

			//List<Downtime> records = csvImporter.importRecords();
			//downtimeRespository.saveAll(records);

			downtimeDistributionStatistic.bulidStatistic(DatatypeConverter.parseDateTime("2018-08-01T00:00:00Z").getTime(), DatatypeConverter.parseDateTime("2018-09-01T00:00:00Z").getTime());


		};
	}
}
