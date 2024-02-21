package ra.project_5.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RestController;
import ra.project_5.model.entity.Address;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
public interface AddressRepository extends JpaRepository<Address,Long> {
    @Query("select a from Address as a where a.userAddr.id = ?1")
    Set<Address> findAllByUsersIs(long user);
   // @Query("select a from Address a where a.userAddr.id= :userId and a.addressId = :addressId")
    Address findByUserAddr_IdAndAddressId(long userId, long addressId);
    Optional<Address> findByUserAddr_Id(long userId);
}
