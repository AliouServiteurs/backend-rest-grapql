package com.leserviteurs.backend_rest_grapql.service;

import java.util.List;

import com.leserviteurs.backend_rest_grapql.dto.PersonneDTO;

public interface PersonneService {

    // CREATE - Pour REST (POST)
    PersonneDTO create(PersonneDTO personneDTO);

    // UPDATE - Pour REST (PUT)
    PersonneDTO update(Long id, PersonneDTO personneDTO);

    // DELETE - Pour REST (DELETE)
    void delete(Long id);

    // READ ALL - Pour GraphQL (Query)
    List<PersonneDTO> findAll();

    // READ ONE 
    PersonneDTO findById(Long id);

    // // Nouvelle méthode (pour GraphQL)
    // PersonneDTO findByIdOrNull(Long id);

    // SEARCH - Pour GraphQL (Query avec filtres)
    List<PersonneDTO> search(String nom, String prenom, String telephone);

    // Réinitialiser la base de donnee
    void resetTable();
}