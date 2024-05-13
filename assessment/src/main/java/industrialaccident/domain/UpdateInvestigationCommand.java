package industrialaccident.domain;

import java.time.LocalDate;
import java.util.*;


import lombok.Data;

@Data
public class UpdateInvestigationCommand {

    private Long assessmentId;
    private Long assessorId;
    private String results;
    private String comments;
}
