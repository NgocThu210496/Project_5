package ra.project_5.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.project_5.model.dto.response.UserResponse;
import ra.project_5.model.entity.User;

import java.nio.file.OpenOption;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUserNameAndStatus(String userName,boolean status);
    List<User>findByFullNameContaining(String name);


}
