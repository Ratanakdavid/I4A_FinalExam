package com.example.terrain_rentals.model;

import com.example.terrain_rentals.model.enums.ProfileType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String email;
    private String registrationNumber; 

    @Enumerated(EnumType.STRING)
    private ProfileType profileType;
    
    private String imagePath;
}