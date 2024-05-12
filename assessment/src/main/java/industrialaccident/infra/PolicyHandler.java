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
    AssessmentRepository assessmentRepository;

    @Autowired
    SickLeaveRepository sickLeaveRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='MedicalBenefitApplied'"
    )
    public void wheneverMedicalBenefitApplied_Receipt(@Payload MedicalBenefitApplied medicalBenefitApplied) {
        MedicalBenefitApplied event = medicalBenefitApplied;
        System.out.println(
            "\n\n##### listener Receipt : " + medicalBenefitApplied + "\n\n"
        );

        CreateInvestigationCommand createInvestigationCommand = new CreateInvestigationCommand();
        assessmentRepository.findById(event.getId()
        ).ifPresent(assessment->{

            createInvestigationCommand.setAccidentId(event.getId());
            createInvestigationCommand.setBusinessCode(event.getBusinessCode());
            createInvestigationCommand.setEmployeeId(event.getEmployeeId());
            createInvestigationCommand.setHospitalCode(event.getHospitalCode());
            createInvestigationCommand.setDoctorNote(event.getDoctorNote());
            createInvestigationCommand.setAccidentType(event.getAccidentType());

            assessment.createInvestigation(createInvestigationCommand);
        });

    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='InvestigationApproved'"
    )
    public void wheneverInvestigationApproved_CreateSickLeave(
        @Payload InvestigationApproved investigationApproved
    ) {
        InvestigationApproved event = investigationApproved;
        System.out.println(
            "\n\n##### listener CreateSickLeave : " +
            investigationApproved +
            "\n\n"
        );

        CreateSickLeaveBenefitCommand createSickLeaveBenefitCommand = new CreateSickLeaveBenefitCommand();
        // implement:  Map command properties from event

        sickLeaveRepository.findById(
            event.getId()
        // implement: Set the SickLeave Id from one of InvestigationApproved event's corresponding property

        ).ifPresent(sickLeave->{
            createSickLeaveBenefitCommand.setAccessmentId(event.getId());
            createSickLeaveBenefitCommand.setAccidentId(event.getAccidentId());
            createSickLeaveBenefitCommand.setBusinessCode(event.getBusinessCode());
            createSickLeaveBenefitCommand.setEmployeeId(event.getEmployeeId());
            
            sickLeave.createSickLeaveBenefit(createSickLeaveBenefitCommand);
        });

    }
}
//>>> Clean Arch / Inbound Adaptor
