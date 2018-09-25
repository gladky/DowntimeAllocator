package ch.cern.cms.daq.downtimeallocator;

import ch.cern.cms.daq.downtimeallocator.model.AllocationResolver;
import ch.cern.cms.daq.downtimeallocator.model.AllocationType;
import ch.cern.cms.daq.downtimeallocator.model.Downtime;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CSVImporter {
    private static final String SAMPLE_CSV_FILE_PATH = "./2018.csv";

    public List<Downtime> importRecords() throws IOException {

        List<Downtime> result = new ArrayList<>();
        try (
                Reader reader = Files.newBufferedReader(Paths.get(SAMPLE_CSV_FILE_PATH));

                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withDelimiter(';').withIgnoreSurroundingSpaces(true).withQuote(null));

        ) {
            for (CSVRecord csvRecord : csvParser) {
                String link = csvRecord.get(0);
                Date start = DatatypeConverter.parseDateTime(csvRecord.get(1)).getTime();
                Boolean stableBeams = Boolean.parseBoolean(csvRecord.get(2));
                Boolean identified = Boolean.parseBoolean(csvRecord.get(3));
                Boolean automatedAvailable = Boolean.parseBoolean(csvRecord.get(4));
                Boolean automatedUsed = Boolean.parseBoolean(csvRecord.get(5));
                Integer duration = Integer.parseInt(csvRecord.get(6));
                Integer shifterCount = Integer.parseInt(csvRecord.get(7));
                Integer shifterId = Integer.parseInt(csvRecord.get(8));
                Boolean resetOrResync = Boolean.parseBoolean(csvRecord.get(9));
                Integer subsystemCount = Integer.parseInt(csvRecord.get(10));
                String subsystem = csvRecord.get(11);
                Boolean rcmsRecovery = Boolean.parseBoolean(csvRecord.get(12));
                Boolean smoothDataTakingBefore = Boolean.parseBoolean(csvRecord.get(13));
                Boolean ignored = Boolean.parseBoolean(csvRecord.get(14));

                System.out.println("Record No - " + csvRecord.getRecordNumber());
                System.out.println("---------------");
                System.out.println("Start : " + start);
                System.out.println("SB : " + stableBeams);
                System.out.println("Shifter : " + shifterId);
                System.out.println("identified : " + identified);
                System.out.println("link : " + link);
                System.out.println("---------------\n");


                Downtime downtime = new Downtime.DowntimeBuilder().build(start, duration);
                downtime.setTitle("2018 - record " + csvRecord.getRecordNumber() + "[GEN 18 Sep]");
                downtime.getAllocation().setAutomatic(false);

                if(shifterCount == 1) {
                    downtime.setShifterId(Long.valueOf(shifterId));
                }

                if(subsystemCount == 1){
                    downtime.setSubsystem(subsystem);
                }

                downtime.setStableBeams(stableBeams);
                downtime.setUnidentified(!identified);
                downtime.setShortFirstAction(resetOrResync);
                downtime.setAutomaticAvailable(automatedAvailable);
                downtime.setAutomaticUsed(automatedUsed);
                downtime.setIgnore(ignored);
                downtime.setSmoothDatatakingBefore(smoothDataTakingBefore);
                downtime.setRcmsRecovery(rcmsRecovery);


                downtime.getAllocation().getChildren().stream().filter(a->a.getAllocationType()== AllocationType.DelayFromSanityCheck).findFirst().ifPresent(c->c.setValue(2000));



                new AllocationResolver().resolveAllocation(downtime.getAllocation());


                result.add(downtime);
            }

            return result;
        }
    }
}