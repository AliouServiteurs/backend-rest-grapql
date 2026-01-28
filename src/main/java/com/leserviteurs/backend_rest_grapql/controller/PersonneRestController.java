package com.leserviteurs.backend_rest_grapql.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.leserviteurs.backend_rest_grapql.dto.PersonneDTO;
import com.leserviteurs.backend_rest_grapql.service.PersonneService;

@RestController
@RequestMapping("/api/personnes")
@Slf4j
@AllArgsConstructor
public class PersonneRestController {

    private final PersonneService personneService;

    /**
     * CREATE - Créer une nouvelle personne
     * POST /api/personnes
     * 
     * @param personneDTO Les données de la personne à créer
     * @return 201 Created avec la personne créée
     */
    @PostMapping
    public ResponseEntity<PersonneDTO> createPersonne(@Valid @RequestBody PersonneDTO personneDTO) {
        log.info("REST API - Requête POST pour créer une personne : {}", personneDTO);

        PersonneDTO createdPersonne = personneService.create(personneDTO);

        log.info("REST API - Personne créée avec succès, ID : {}", createdPersonne.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(createdPersonne);
    }

    /**
     * UPDATE - Modifier une personne existante
     * PUT /api/personnes/{id}
     * 
     * @param id          L'identifiant de la personne à modifier
     * @param personneDTO Les nouvelles données
     * @return 200 OK avec la personne modifiée
     */
    @PutMapping("/{id}")
    public ResponseEntity<PersonneDTO> updatePersonne(
            @PathVariable Long id,
            @Valid @RequestBody PersonneDTO personneDTO) {

        log.info("REST API - Requête PUT pour modifier la personne ID : {}", id);

        PersonneDTO updatedPersonne = personneService.update(id, personneDTO);

        log.info("REST API - Personne modifiée avec succès, ID : {}", id);

        return ResponseEntity.ok(updatedPersonne);
    }

    /**
     * DELETE - Supprimer une personne
     * DELETE /api/personnes/{id}
     * 
     * @param id L'identifiant de la personne à supprimer
     * @return 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePersonne(@PathVariable Long id) {
        log.info("REST API - Requête DELETE pour supprimer la personne ID : {}", id);

        personneService.delete(id);

        log.info("REST API - Personne supprimée avec succès, ID : {}", id);

        return ResponseEntity.noContent().build();
    }
// utiliser pour reinitialiser la table : probleme de developpment
    @DeleteMapping("/reset")
    public ResponseEntity<String> resetTable() {
        log.warn("Requête de réinitialisation de la table");
        personneService.resetTable();
        return ResponseEntity.ok("Table personne réinitialisée avec succès");
    }
}