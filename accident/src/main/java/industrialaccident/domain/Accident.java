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
            .requestSickLeaveBenefit(getId(), requestSickLeaveBenefitCommand, "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJpbUdLRnR6NHhXLU9iNHhjVlYyUE5DamtMeWdFNW41U1l0QlhHVjRGZnhjIn0.eyJleHAiOjE3MTU2NTQ5NzcsImlhdCI6MTcxNTY1NDkxNywiYXV0aF90aW1lIjoxNzE1NjUxNjc3LCJqdGkiOiJmMGQ2NWRmMy0zZWQwLTQxNmQtOWRjZi1jNmI1NzBjN2ExMWUiLCJpc3MiOiJodHRwczovLzkwOTAta3l1c29vay1pbmR1c3RyaWFsYWNjaWQtYWo5bDZia2tyeXAud3MtdXMxMTAuZ2l0cG9kLmlvL3JlYWxtcy9tYXN0ZXIiLCJhdWQiOiJlZ292Iiwic3ViIjoiNmQ5NGY5YjctYTZhMC00MTYyLTk4NGYtMzI2NWE5NzgzM2I2IiwidHlwIjoiSUQiLCJhenAiOiJlZ292Iiwibm9uY2UiOiI3ZjhhNzIxYS0yMmJhLTQwYmQtOWY0ZC02ZmZmYzM4MjI2OTciLCJzZXNzaW9uX3N0YXRlIjoiYTYxNzZlZGItZWM2NS00NGViLWE0NjUtZmIzNDJlN2ZmMmUwIiwiYXRfaGFzaCI6ImY4bmt3MXlrbWpDRnpBZ0VrMDhpb3ciLCJhY3IiOiIwIiwic2lkIjoiYTYxNzZlZGItZWM2NS00NGViLWE0NjUtZmIzNDJlN2ZmMmUwIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhZG1pbiJ9.PYyo5P2pX-6qtMxJyG9nIO2Br76k0N3M3cifKiFtJa0ZZH2nTXInk-IIGuP5aKwNMc5SVYcKjeS59wJ450wJA3F9zaN2YGEu1tDoRjflegaSrQOBOBwE1RX4IomqQ3yacfH7F1g3Q4xahRZCFbjjNWdebUQVTkSVHVK5h8NtPe3SwzUYpTbbAlt9YOK9nANnOB-DTxD46hXIhWpZRUl3AJ4c1AF4O1IO_JI4pWov9OOWt41whCo0E3ZZhHr2O4DVc4Bic590e3Opz_Wzsvjd4a2jJs2w7StHFzO1lfURl_0-g3U2fuvrsRAcpTiEidjwLD9L_slUrFTYgX3v3QEErw");
    }

}
//>>> DDD / Aggregate Root
