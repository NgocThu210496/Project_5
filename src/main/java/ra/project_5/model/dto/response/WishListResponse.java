package ra.project_5.model.dto.response;

import lombok.*;
import ra.project_5.model.entity.Product;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class WishListResponse {
    private List<Product> products;
}
