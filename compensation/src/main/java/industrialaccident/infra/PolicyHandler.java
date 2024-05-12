package industrialaccident.infra;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import industrialaccident.config.kafka.KafkaProcessor;
import industrialaccident.domain.*;
import javax.naming.NameParser;
import javax.naming.NameParser;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

//<<< Clean Arch / Inbound Adaptor
@Service
@Transactional
public class PolicyHandler {

    @Autowired
    CompensationRepository compensationRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='AverageSalaryApplied'"
    )
    public void wheneverAverageSalaryApplied_CompensationReceipt(
        @Payload AverageSalaryApplied averageSalaryApplied
    ) {
        AverageSalaryApplied event = averageSalaryApplied;
        System.out.println(
            "\n\n##### listener CompensationReceipt : " +
            averageSalaryApplied +
            "\n\n"
        );

        CreateCompensationCommand createCompensationCommand = new CreateCompensationCommand();

        compensationRepository.findById(event.getId()
        
        ).ifPresent(compensation->{
            createCompensationCommand.setSickLeaveId(event.getId());
            createCompensationCommand.setAssessmentId(event.getAccessmentId());
            createCompensationCommand.setAccidentId(event.getAccidentId());
            createCompensationCommand.setEmployeeId(event.getEmployeeId());
            createCompensationCommand.setAverageSalary(event.getAverageSalary());

            compensation.createCompensation(createCompensationCommand);

        });

    }
}
//>>> Clean Arch / Inbound Adaptor
