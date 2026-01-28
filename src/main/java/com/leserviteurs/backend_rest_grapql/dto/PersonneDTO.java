package com.leserviteurs.backend_rest_grapql.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonneDTO {
    
    private Long id;
    
    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 100)
    private String nom;
    
    @NotBlank(message = "Le prénom est obligatoire")
    @Size(max = 100)
    private String prenom;
    
    @Past(message = "La date de naissance doit être dans le passé")
    private LocalDate dateNaissance;
    
    @Size(max = 255)
    private String adresse;
    
    @Size(max = 20)
    private String telephone;
}