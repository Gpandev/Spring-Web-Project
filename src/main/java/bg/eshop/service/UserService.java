package bg.eshop.service;

import bg.eshop.domain.models.service.UserServiceModel;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    UserServiceModel registerUser(UserServiceModel userServiceModel);

    UserServiceModel getUserByUsername(String username);

    UserServiceModel editUserProfile(UserServiceModel userServiceModel);

    List<UserServiceModel> getAllUsers();

    void addUserRole(String id, String role);
}
