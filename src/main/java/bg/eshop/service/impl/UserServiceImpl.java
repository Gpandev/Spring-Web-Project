package bg.eshop.service.impl;

import bg.eshop.domain.models.service.UserServiceModel;
import bg.eshop.repository.UserRepository;
import bg.eshop.service.RoleService;
import bg.eshop.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleService roleService, ModelMapper modelMapper, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.modelMapper = modelMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserServiceModel registerUser(UserServiceModel userServiceModel) {
        return null;
    }

    @Override
    public UserServiceModel getUserByUsername(String username) {
        return null;
    }

    @Override
    public UserServiceModel editUserProfile(UserServiceModel userServiceModel) {
        return null;
    }

    @Override
    public List<UserServiceModel> getAllUsers() {
        return null;
    }

    @Override
    public void setUserRole(String id, String role) {

    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return null;
    }
}
