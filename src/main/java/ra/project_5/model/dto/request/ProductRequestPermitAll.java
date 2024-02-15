package ra.project_5.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestPermitAll {
    private long productId;
    private long categoryId;
    private String productName;
    private String description;
    private BigDecimal unitPrice;
    private int quantity;
    private boolean status;
    private String image;
}
