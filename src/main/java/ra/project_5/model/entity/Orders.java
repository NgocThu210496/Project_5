package ra.project_5.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Orders {
    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderId;

    @Column(name = "serial_number", columnDefinition = "varchar(100)")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String serialNumber;

    @Column(name = "order_at", columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime orderAt;

    @Column(name = "total_price", precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EStatus status;

    @Column(name = "note", columnDefinition = "varchar(100)")
    private String note;

    @Column(name = "receive_name", columnDefinition = "varchar(100)")
    private String receiveName;

    @Column(name = "receive_address")
    private String receiveAddress;

    @Column(name = "receive_phone",columnDefinition = "varchar(15)")
    private String receivePhone;

    @Column(name = "created_at")
    @CreationTimestamp
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date created;

    @Column(name = "received_at")
    @CreationTimestamp
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate received;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userOder;

    @OneToMany(mappedBy = "ordersEntity")
    @JsonIgnore
    private List<OrderDetail> orderDetailEntityList;
}
