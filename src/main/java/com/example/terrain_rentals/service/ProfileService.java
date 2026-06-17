package com.example.terrain_rentals.service;

import com.example.terrain_rentals.model.Profile;
import com.example.terrain_rentals.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;

@Service
public class ProfileService {

    @Autowired private ProfileRepository profileRepository;
    private final String UPLOAD_DIR = "uploads/";

    public List<Profile> findAll() { return profileRepository.findAll(); }
    public Profile findById(Long id) { return profileRepository.findById(id).orElse(null); }

    public Profile saveProfileWithPhoto(Profile profile, MultipartFile file) {
        try {
            String year = String.valueOf(LocalDate.now().getYear());
            String dept = (profile.getProfileType() != null) ? profile.getProfileType().toString().substring(0, 3) : "USR";
            profile.setRegistrationNumber(year + "-" + dept + "-" + UUID.randomUUID().toString().substring(0, 3).toUpperCase());

            if (file != null && !file.isEmpty()) {
                Files.createDirectories(Paths.get(UPLOAD_DIR));
                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                Files.copy(file.getInputStream(), Paths.get(UPLOAD_DIR + fileName));
                profile.setImagePath(fileName);
            }
            return profileRepository.save(profile);
        } catch (IOException e) { throw new RuntimeException("Save failed", e); }
    }
    public void deleteById(Long id) {
        if (profileRepository.existsById(id)) {
        profileRepository.deleteById(id);
        }
    }

    public Map<String, Long> getStats() {
        List<Profile> all = profileRepository.findAll();
        Map<String, Long> stats = new HashMap<>();
        stats.put("total", (long) all.size());
        stats.put("students", all.stream().filter(p -> p.getProfileType() != null && "STUDENT".equalsIgnoreCase(p.getProfileType().toString())).count());
        stats.put("employees", all.stream().filter(p -> p.getProfileType() != null && "EMPLOYEE".equalsIgnoreCase(p.getProfileType().toString())).count());
        return stats;
    }
}