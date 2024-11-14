package ar.gym.gym.utils;


import ar.gym.gym.security.model.ERole;
import ar.gym.gym.security.model.Role;
import ar.gym.gym.security.model.User;
import ar.gym.gym.security.repository.RoleRepository;
import ar.gym.gym.security.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service

public class DataInitializerService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Inyectar el PasswordEncoder

    @PostConstruct
    public void init() {
        // Crear y guardar los roles
        for (ERole role : ERole.values()) {
            Role newRole = new Role();
            newRole.setName(role);
            roleRepository.save(newRole);
        }

        // Crear usuarios para cada rol
        createUser("admin", "admin@example.com", "adminn", Set.of(ERole.ROLE_ADMIN));
        createUser("trainer", "trainer@example.com", "trainer", Set.of(ERole.ROLE_TRAINER));
        createUser("nutritionist", "nutritionist@example.com", "nutritionist", Set.of(ERole.ROLE_NUTRITIONIST));
        createUser("client", "client@example.com", "client", Set.of(ERole.ROLE_CLIENT));
    }

    private void createUser(String username, String email, String password, Set<ERole> roles) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password)); // Encriptar la contraseña

        Set<Role> userRoles = new HashSet<>();
        for (ERole role : roles) {
            Role existingRole = roleRepository.findByName(role).orElseThrow(() -> new RuntimeException("Error: Role not found."));
            userRoles.add(existingRole);
        }
        user.setRoles(userRoles);
        userRepository.save(user);
    }

}
