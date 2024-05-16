package industrialaccident.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.Data;

@Data
public class ApplySickLeaveBenefitCommand {

    private Long sickLeaveId;
    private Long accidentId;
    private String businessCode;
    private String employeeId;
    private Integer period;
}
