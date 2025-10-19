package in.SakshamProject.moneycount.service;

import in.SakshamProject.moneycount.dto.AuthDTO;
import in.SakshamProject.moneycount.dto.ProfileDTO;
import in.SakshamProject.moneycount.entity.ProfileEntity;
import in.SakshamProject.moneycount.repository.ProfileRepo;
import in.SakshamProject.moneycount.util.JWTUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {


    private final ProfileRepo profileRepo;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;


    public ProfileDTO registerProfile(ProfileDTO profileDTO) throws MessagingException {
        ProfileEntity newProfile = toEntity(profileDTO);
        newProfile.setActivationToken(UUID.randomUUID().toString());
        newProfile.setIsActive(false); // new users inactive by default
        newProfile = profileRepo.save(newProfile);

        // Send activation email
        String activationLink = "http://localhost:8000/activate?token=" + newProfile.getActivationToken();
        String subject = "Activate your Money-Count Account";
        String body = "Click the following link to activate your account: " + activationLink;
        emailService.sendEmail(newProfile.getEmail(), subject, body);

        return toDto(newProfile);
    }

    private ProfileEntity toEntity(ProfileDTO profileDTO) {
        return ProfileEntity.builder()
                .id(profileDTO.getId())
                .fullName(profileDTO.getFullName())
                .email(profileDTO.getEmail())
                .password(passwordEncoder.encode(profileDTO.getPassword())) // hashed password
                .profileImageUrl(profileDTO.getProfileImageUrl())
                .createdAt(profileDTO.getCreatedAt())
                .updateAt(profileDTO.getUpdateAt())
                .build();
    }

    private ProfileDTO toDto(ProfileEntity profileEntity) {
        return ProfileDTO.builder()
                .id(profileEntity.getId())
                .fullName(profileEntity.getFullName())
                .email(profileEntity.getEmail())
                .profileImageUrl(profileEntity.getProfileImageUrl())
                .createdAt(profileEntity.getCreatedAt())
                .updateAt(profileEntity.getUpdateAt())
                .build();
    }

    public boolean activateProfile(String activationToken) {
        return profileRepo.findByActivationToken(activationToken)
                .map(profile -> {
                    profile.setIsActive(true);
                    profile.setActivationToken(null); // token used, remove it
                    profileRepo.save(profile);
                    return true;
                }).orElse(false);
    }

    public boolean isAccountActive(String email) {
        return profileRepo.findByEmail(email)
                .map(ProfileEntity::getIsActive)
                .orElse(false);
    }

    public ProfileEntity getCurrentProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return profileRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Profile not found with email: " + email));
    }

    public ProfileDTO getPublicProfile(String email) {
        ProfileEntity currentUser;
        if (email == null) {
            currentUser = getCurrentProfile();
        } else {
            currentUser = profileRepo.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Profile not found with email: " + email));
        }
        return toDto(currentUser);
    }
    public Map<String, Object> authenticateAndGenerateToken(AuthDTO authDTO) {
        try {
            // 1️⃣ Check if user exists
            ProfileEntity user = profileRepo.findByEmail(authDTO.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            // 2️⃣ Check password using BCrypt encoder
            if (!passwordEncoder.matches(authDTO.getPassword(), user.getPassword())) {
                throw new RuntimeException("Invalid email or password");
            }

            // 3️⃣ Generate JWT token
            String token = jwtUtil.generateToken(user.getEmail());

            // 4️⃣ Return token + user info
            return Map.of(
                    "token", token,
                    "user", getPublicProfile(user.getEmail())
            );

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Invalid email or password");
        }
    }
}