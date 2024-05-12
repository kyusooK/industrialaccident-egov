package industrialaccident.domain;

import java.time.LocalDate;
import java.util.*;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class UpdateInvestigationCommand {

    @Id
    private Long id;
    private Long assessorId;
    private String results;
    private String comments;
}
