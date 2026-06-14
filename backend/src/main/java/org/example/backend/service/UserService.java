package org.example.backend.service;

import org.example.backend.dto.AuthResponse;
import org.example.backend.dto.LoginRequest;
import org.example.backend.dto.RegisterRequest;
import org.example.backend.entity.User;
import org.example.backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse register(RegisterRequest request) {
        String username = request.getUsername().trim();
        String email = request.getEmail().trim().toLowerCase();

        if (userRepository.existsByUsername(username)) {
            return new AuthResponse(false, "Username sudah digunakan.");
        }

        if (userRepository.existsByEmail(email)) {
            return new AuthResponse(false, "Email sudah digunakan.");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER");

        User savedUser = userRepository.save(user);

        return new AuthResponse(
                true,
                "Registrasi berhasil.",
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getRole()
        );
    }

    public User authenticate(LoginRequest request) {
        String input = request.getUsernameOrEmail().trim();
        Optional<User> optionalUser = userRepository.findByUsername(input);

        if (optionalUser.isEmpty()) {
            optionalUser = userRepository.findByEmail(input.toLowerCase());
        }

        if (optionalUser.isEmpty()) {
            return null;
        }

        User user = optionalUser.get();

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return null;
        }

        return user;
    }

    public AuthResponse login(LoginRequest request) {
        String input = request.getUsernameOrEmail().trim();
        Optional<User> optionalUser = userRepository.findByUsername(input);

        if (optionalUser.isEmpty()) {
            optionalUser = userRepository.findByEmail(input.toLowerCase());
        }

        if (optionalUser.isEmpty()) {
            return new AuthResponse(false, "Username/email tidak terdaftar.");
        }

        User user = optionalUser.get();

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return new AuthResponse(false, "Password salah.");
        }

        return new AuthResponse(
                true,
                "Login berhasil.",
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole()
        );
    }
}
