package industrialaccident.domain;

import industrialaccident.AccidentApplication;
import industrialaccident.domain.MedicalBenefitApplied;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Accident_table")
@Data
//<<< DDD / Aggregate Root
public class Accident {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String businessCode;

    private String employeeId;

    private String name;

    private String hospitalCode;

    private Integer period;

    private String doctorNote;

    private String accidentType;

    private String status;

    private Date applyDt;

    @PostPersist
    public void onPostPersist() {
        MedicalBenefitApplied medicalBenefitApplied = new MedicalBenefitApplied(
            this
        );
        medicalBenefitApplied.publishAfterCommit();
    }

    public static AccidentRepository repository() {
        AccidentRepository accidentRepository = AccidentApplication.applicationContext.getBean(
            AccidentRepository.class
        );
        return accidentRepository;
    }

    public void apply() {
        //
    }

    //<<< Clean Arch / Port Method
    public void applyMedicalBenefit(ApplyMedicalBenefitCommand applyMedicalBenefitCommand) {
        
        this.setBusinessCode(applyMedicalBenefitCommand.getBusinessCode());
        this.setEmployeeId(applyMedicalBenefitCommand.getEmployeeId());
        this.setHospitalCode(applyMedicalBenefitCommand.getHospitalCode());
        this.setDoctorNote(applyMedicalBenefitCommand.getDoctorNote());
        this.setAccidentType(applyMedicalBenefitCommand.getAccidentType());
        this.setStatus("요양급여신청됨");
    }

    public void applySickLeaveBenefit(
        ApplySickLeaveBenefitCommand applySickLeaveBenefitCommand
    ) {
        this.setBusinessCode(applySickLeaveBenefitCommand.getBusinessCode());
        this.setEmployeeId(applySickLeaveBenefitCommand.getEmployeeId());
        this.setPeriod(applySickLeaveBenefitCommand.getPeriod());
        this.setStatus("휴업급여 신칭됨");

        SickLeaveBenefitApplied sickLeaveBenefitApplied = new SickLeaveBenefitApplied(this);
        sickLeaveBenefitApplied.publishAfterCommit();


        industrialaccident.external.RequestSickLeaveBenefitCommand requestSickLeaveBenefitCommand = new industrialaccident.external.RequestSickLeaveBenefitCommand();
        
        requestSickLeaveBenefitCommand.setId(getId());
        requestSickLeaveBenefitCommand.setBusinessCode(getBusinessCode());
        requestSickLeaveBenefitCommand.setEmployeeId(getEmployeeId());
        requestSickLeaveBenefitCommand.setPeriod(getPeriod());

        AccidentApplication.applicationContext
            .getBean(industrialaccident.external.SickLeaveService.class)
            .requestSickLeaveBenefit(getId(), requestSickLeaveBenefitCommand);
    }

}
//>>> DDD / Aggregate Root
