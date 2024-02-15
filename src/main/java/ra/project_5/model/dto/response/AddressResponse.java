package ra.project_5.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponse {
    private long userId;
    private long addressId;
    private String fullAddress;
    private String phone;
    private String receiveName;
}
