package com.leserviteurs.backend_rest_grapql.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "personne")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Personne {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 100, message = "Le nom ne doit pas dépasser 100 caractères")
    @Column(nullable = false, length = 100)
    private String nom;
    
    @NotBlank(message = "Le prénom est obligatoire")
    @Size(max = 100, message = "Le prénom ne doit pas dépasser 100 caractères")
    @Column(nullable = false, length = 100)
    private String prenom;
    
    @Past(message = "La date de naissance doit être dans le passé")
    @Column(name = "date_naissance")
    private LocalDate dateNaissance;
    
    @Size(max = 255, message = "L'adresse ne doit pas dépasser 255 caractères")
    @Column(length = 255)
    private String adresse;
    
    @Size(max = 20, message = "Le téléphone ne doit pas dépasser 20 caractères")
    @Column(length = 20)
    private String telephone;
}