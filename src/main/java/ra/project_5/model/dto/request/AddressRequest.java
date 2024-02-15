package ra.project_5.model.dto.request;

import lombok.*;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Data
public class AddressRequest {
    private String fullAddress;
    @Length(min = 9,max = 15)
    private String phone;
    @Length(max = 50)
    private String receiveName;
}
