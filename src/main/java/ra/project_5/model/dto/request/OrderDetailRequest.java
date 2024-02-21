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
public class OrderDetailRequest {
    private long productId;
    private BigDecimal price;
    private int quantity;
    private BigDecimal total_price;
    private String note;
    private String receive_name;
    private String receive_address;
    private String receive_phone;
}
