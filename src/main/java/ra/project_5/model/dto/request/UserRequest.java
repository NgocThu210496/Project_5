package ra.project_5.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    @Size(min = 6,max = 100,message = "User name needs to be between 6 - 100 characters")
    @NotNull(message = "user name cannot be left blank !")
    private String userName;
    @NotNull(message = "password cannot be left blank !")
    private String password;
    @Email
    @NotNull(message = "email cannot be left blank !")
    private String email;
    @NotNull(message = "fullName cannot be left blank !")
    private String fullName;
    @Pattern(regexp="(09|01[2|6|8|9])+([0-9]{8})\\b", message = "Invalid phone!")
    private String phone;
    @NotNull(message = "address cannot be left blank !")
    private String address;
    private Set<String> listRoles;
}
