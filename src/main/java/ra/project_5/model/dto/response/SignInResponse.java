package ra.project_5.model.dto.response;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SignInResponse {
    private String userName;
  //  private String password;
    private String email;
    private String fullName;
    private final String TYPE = "Bearer";
    private Collection<? extends GrantedAuthority> authorities;
    private String accessToken;
    private String refreshToken;
}
