package ra.project_5.model.dto.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestAdmin {
    private long categoryId;
    @NotNull(message = "product Name Not null")
    private String productName;
    private String description;
    @Min(0)
    private BigDecimal unitPrice;
    @Digits(message = "Format: ###.##",integer = 10,fraction = 2)
    private int quantity;
    private String image;
}
