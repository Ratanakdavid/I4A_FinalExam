package com.example.terrain_rentals.controller;

import com.example.terrain_rentals.model.Profile;
import com.example.terrain_rentals.service.PdfService;
import com.example.terrain_rentals.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    @Autowired private ProfileService profileService;
    @Autowired private PdfService pdfService;

    @GetMapping
    public List<Profile> getAllProfiles() { return profileService.findAll(); }

    @GetMapping("/stats")
    public Map<String, Long> getStats() { return profileService.getStats(); }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Profile createProfile(
            @ModelAttribute Profile profile, 
            @RequestParam("photoFile") MultipartFile photoFile) {
        return profileService.saveProfileWithPhoto(profile, photoFile);
    }
    @DeleteMapping("/{id}")
    public void deleteProfile(@PathVariable Long id) {
        profileService.deleteById(id);
}

    @GetMapping("/{id}/pdf")
    public void downloadPdf(@PathVariable Long id, HttpServletResponse response) throws Exception {
        response.setContentType("application/pdf");
        Profile p = profileService.findById(id);
        pdfService.generateProfilePdf(p, response.getOutputStream());
    }
}