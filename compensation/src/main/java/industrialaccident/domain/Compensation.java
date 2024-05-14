package industrialaccident.domain;

import industrialaccident.CompensationApplication;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Compensation_table")
@Data
//<<< DDD / Aggregate Root
public class Compensation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long sickLeaveId;

    private Long assessmentId;

    private Long accidentId;

    private String employeeId;

    private Long amount;

    private String method;

    private Date date;

    private String status;

    @PostPersist
    public void onPostPersist() {}

    public static CompensationRepository repository() {
        CompensationRepository compensationRepository = CompensationApplication.applicationContext.getBean(
            CompensationRepository.class
        );
        return compensationRepository;
    }

    //<<< Clean Arch / Port Method
    public void processCompensation(
        ProcessCompensationCommand processCompensationCommand
    ) {
        //implement business logic here:

        CompensationPaid compensationPaid = new CompensationPaid(this);
        compensationPaid.publishAfterCommit();
    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public void createCompensation(
        CreateCompensationCommand createCompensationCommand
    ) {
        this.setSickLeaveId(createCompensationCommand.getSickLeaveId());
        this.setAssessmentId(createCompensationCommand.getAssessmentId());
        this.setAccidentId(createCompensationCommand.getAccidentId());
        this.setEmployeeId(createCompensationCommand.getEmployeeId());
        this.setAmount(createCompensationCommand.getAverageSalary());
        this.setStatus("보상처리 접수됨");

        CompensationCreated compensationCreated = new CompensationCreated(this);
        compensationCreated.publishAfterCommit();
    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
