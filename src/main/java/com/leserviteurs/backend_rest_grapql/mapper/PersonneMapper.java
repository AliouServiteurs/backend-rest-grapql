package com.leserviteurs.backend_rest_grapql.mapper;

import org.springframework.stereotype.Component;

import com.leserviteurs.backend_rest_grapql.dto.PersonneDTO;
import com.leserviteurs.backend_rest_grapql.model.Personne;

@Component
public class PersonneMapper {
    
    // Entity → DTO
    public PersonneDTO toDTO(Personne personne) {
        if (personne == null) {
            return null;
        }
        return PersonneDTO.builder()
                .id(personne.getId())
                .nom(personne.getNom())
                .prenom(personne.getPrenom())
                .dateNaissance(personne.getDateNaissance())
                .adresse(personne.getAdresse())
                .telephone(personne.getTelephone())
                .build();
    }
    
    // DTO → Entity
    public Personne toEntity(PersonneDTO dto) {
        if (dto == null) {
            return null;
        }
        return Personne.builder()
                .id(dto.getId())
                .nom(dto.getNom())
                .prenom(dto.getPrenom())
                .dateNaissance(dto.getDateNaissance())
                .adresse(dto.getAdresse())
                .telephone(dto.getTelephone())
                .build();
    }
    
    // Mise à jour d'une entité depuis un DTO (pour PUT)
    public void updateEntityFromDTO(PersonneDTO dto, Personne personne) {
        personne.setNom(dto.getNom());
        personne.setPrenom(dto.getPrenom());
        personne.setDateNaissance(dto.getDateNaissance());
        personne.setAdresse(dto.getAdresse());
        personne.setTelephone(dto.getTelephone());
    }
}