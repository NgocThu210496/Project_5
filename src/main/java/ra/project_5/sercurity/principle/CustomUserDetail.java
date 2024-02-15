package ra.project_5.sercurity.principle;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.yaml.snakeyaml.nodes.CollectionNode;

import java.util.Collection;
@Getter
@Setter
@Builder
public class CustomUserDetail implements UserDetails {
    //can cac thuoc tinh cua CustomUserDetail
    private long id;
    private String userName;
    private String password;
    private String email;
    private String phone;
    private String fullName;
    private boolean status;
    private Collection<? extends GrantedAuthority> authorities;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

    @Override
    public boolean isAccountNonExpired() { //het han account
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
