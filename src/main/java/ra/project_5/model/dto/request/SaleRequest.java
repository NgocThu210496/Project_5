package ra.project_5.model.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleRequest {
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date from;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date to;
}
