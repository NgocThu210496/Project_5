package ra.project_5.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private Long productId;
    private Long categoryName;
    private String sku;
    private String productName;
    private String description;
    private BigDecimal unitPrice;
    private int quantity;
    private boolean status;
    private String image;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date created;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date updated;
}
