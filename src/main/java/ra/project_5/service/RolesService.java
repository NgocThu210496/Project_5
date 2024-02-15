package ra.project_5.service;

import ra.project_5.model.entity.ERoles;
import ra.project_5.model.entity.Roles;

import java.util.List;
import java.util.Optional;

public interface RolesService {
    Optional<Roles>findByName(ERoles name);
    List<Roles>getListRole();
}
