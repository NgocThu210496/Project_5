package ra.project_5.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.project_5.model.entity.ERoles;
import ra.project_5.model.entity.Roles;
import ra.project_5.repository.RolesRepository;
import ra.project_5.service.RolesService;

import java.util.List;
import java.util.Optional;
@Service
public class RolesServiceImp implements RolesService {
    @Autowired
    private RolesRepository rolesRepository;
    @Override
    public Optional<Roles> findByName(ERoles name) {
        return rolesRepository.findByName(name);
    }

    @Override
    public List<Roles> getListRole() {
        return rolesRepository.findAll();
    }
}
