package com.bankapplicationmicroservices.security_service.service;

import com.bankapplicationmicroservices.security_service.dto.RegisterRequest;
import com.bankapplicationmicroservices.security_service.entity.Role;
import com.bankapplicationmicroservices.security_service.entity.User;
import com.bankapplicationmicroservices.security_service.repository.RoleRepository;
import com.bankapplicationmicroservices.security_service.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository users;
    private final RoleRepository roles;
    private final PasswordEncoder encoder;

    public UserService(UserRepository users, RoleRepository roles, PasswordEncoder encoder) {
        this.users = users;
        this.roles = roles;
        this.encoder = encoder;
    }

    public void register(RegisterRequest req) {
        if (users.existsByUsername(req.getUsername()))
            throw new RuntimeException("username already exists");

        Set<Role> roleEntities = req.getRoles().stream()
                .map(r -> roles.findByName(r).orElseGet(() -> roles.save(new Role(null, r))))
                .collect(Collectors.toSet());

        User u = new User();
        u.setUsername(req.getUsername());
        u.setPassword(encoder.encode(req.getPassword()));
        u.setEnabled(true);
        u.setRoles(roleEntities);

        users.save(u);
    }

    public User find(String username) {
        return users.findByUsername(username).orElse(null);
    }
}
