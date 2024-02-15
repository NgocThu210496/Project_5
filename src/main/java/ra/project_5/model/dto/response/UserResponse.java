package ra.project_5.model.dto.response;

import lombok.*;
import ra.project_5.model.entity.Roles;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserResponse {
    private Long id;
    private String userName;
    private String password;
    private String email;
    private String fullName;
    private String address;
    private String phone;
    private Set<Roles> listRoles;
}
