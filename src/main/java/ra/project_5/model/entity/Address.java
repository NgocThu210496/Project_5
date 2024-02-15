package ra.project_5.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "address")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Address {
    @Id
    @Column(name ="address_id" )
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long addressId;

    @Column(name ="full_address" )
    private String fullAddress;

    @Column(name = "phone", columnDefinition = "varchar(15)")
    private String phone;

    @Column(name = "receive_name", columnDefinition = "varchar(50)")
    private String receiveName;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User userAddr;
}
