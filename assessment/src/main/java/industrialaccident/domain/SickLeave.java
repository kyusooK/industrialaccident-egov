package industrialaccident.domain;

import industrialaccident.AssessmentApplication;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "SickLeave_table")
@Data
public class SickLeave {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long accessmentId;

    private Long accidentId;

    private String businessCode;

    private String employeeId;

    private Long averageSalary;

    private Integer period;

    private String status;

    private Date date;

    @PostPersist
    public void onPostPersist() {}

    public static SickLeaveRepository repository() {
        SickLeaveRepository sickLeaveRepository = AssessmentApplication.applicationContext.getBean(
            SickLeaveRepository.class
        );
        return sickLeaveRepository;
    }

    public void applySalary(ApplySalaryCommand applySalaryCommand) {
        
        this.setAverageSalary(applySalaryCommand.getAverageSalary());

        AverageSalaryApplied averageSalaryApplied = new AverageSalaryApplied(this);
        averageSalaryApplied.publishAfterCommit();
    }

    public void createSickLeaveBenefit(
        CreateSickLeaveBenefitCommand createSickLeaveBenefitCommand) {
        
        this.setAccessmentId(createSickLeaveBenefitCommand.getAccessmentId());
        this.setAccidentId(createSickLeaveBenefitCommand.getAccidentId());
        this.setBusinessCode(createSickLeaveBenefitCommand.getBusinessCode());
        this.setEmployeeId(createSickLeaveBenefitCommand.getEmployeeId());
        this.setStatus("급여처리 생성됨");

        SickLeaveBenefitCreated sickLeaveBenefitCreated = new SickLeaveBenefitCreated(this);
        sickLeaveBenefitCreated.publishAfterCommit();
    }

    public void requestSickLeaveBenefit(
        RequestSickLeaveBenefitCommand requestSickLeaveBenefitCommand) {

        this.setAccessmentId(requestSickLeaveBenefitCommand.getId());
        this.setAccidentId(requestSickLeaveBenefitCommand.getId());
        this.setBusinessCode(requestSickLeaveBenefitCommand.getBusinessCode());
        this.setEmployeeId(requestSickLeaveBenefitCommand.getEmployeeId());
        this.setPeriod(requestSickLeaveBenefitCommand.getPeriod());
        this.setStatus("휴업급여 요청됨");

        SickLeaveBenefitRequested sickLeaveBenefitRequested = new SickLeaveBenefitRequested(this);
        sickLeaveBenefitRequested.publishAfterCommit();
    }

}
