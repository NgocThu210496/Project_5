package ra.project_5.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductStatusTrueResponse {
    private long productId;
    private String sku;
    private String productName;
    private String description;
    private boolean status;
}
