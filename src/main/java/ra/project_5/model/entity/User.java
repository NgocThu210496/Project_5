package ra.project_5.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "username",columnDefinition = "varchar(100)", unique = true, nullable = false)
    @NotNull(message = "userName not null")
    @NotEmpty(message = "userName not empty")
    @Length(min = 6,max = 100,message = "Min:6 - Max:100")
    private String userName;

    @Column(name = "password",nullable = false)
    private String password;

    @Column(name = "email",unique = true)
    @Email(message = "Email invalid format" )
    private String email;

    @Column(name = "fullname",columnDefinition = "varchar(100)")
    private String fullName;

    private String avatar;

    @Column(name = "phone", columnDefinition = "varchar(15)",unique = true)
    @Pattern(regexp = "(84|0[3|5|7|8|9])+([0-9]{8})", message = "Yêu cầu định dạng Việt Nam (10 số)")
    private String phone;

    @Column(name = "created_at")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date created;

    @Column(name = "updated_at")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date updated;

    @Column(name = "address", unique = true)
   // @NotNull(message = "Address not null")
  //  @NotEmpty(message = "Address not empty")
    private String address;

    @Column(name = "status",columnDefinition = "bit default 1")
    private boolean status;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<ShoppingCard> shopCart;


    @OneToMany(mappedBy = "userWish")
    @JsonIgnore
    private List<WishList> wishLists;

    @OneToMany(mappedBy = "userAddr")
    @JsonIgnore
    private List<Address> addressList;

    @OneToMany(mappedBy = "userOder")
    @JsonIgnore
    private Set<Orders> ordersList;

    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))  //khoa ngoai
    private Set<Roles> listRoles;

}
