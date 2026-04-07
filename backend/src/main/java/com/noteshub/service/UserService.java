package com.noteshub.service;

import com.noteshub.dto.UserDto;
import com.noteshub.entity.User;
import com.noteshub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserDto.Response createUser(UserDto.CreateRequest request) {
        if (userRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Name already taken: " + request.getName());
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered: " + request.getEmail());
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .passwordHash(sha256(request.getPassword()))
                .build();

        return toResponse(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public UserDto.Response getUserById(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        return toResponse(user);
    }

    private static String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }

    public static UserDto.Response toResponse(User user) {
        UserDto.Response res = new UserDto.Response();
        res.setId(user.getId());
        res.setName(user.getName());
        res.setEmail(user.getEmail());
        res.setCreatedAt(user.getCreatedAt());
        res.setUpdatedAt(user.getUpdatedAt());
        return res;
    }
}
