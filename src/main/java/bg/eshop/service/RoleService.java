package bg.eshop.service;

import bg.eshop.domain.models.service.RoleServiceModel;

import java.util.Set;

public interface RoleService {

    void addRolesInDB();

    Set<RoleServiceModel> findAllRoles();

    RoleServiceModel findByAuthority(String authority);
}
