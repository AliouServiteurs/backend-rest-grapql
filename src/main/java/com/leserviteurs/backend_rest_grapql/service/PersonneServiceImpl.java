package com.leserviteurs.backend_rest_grapql.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leserviteurs.backend_rest_grapql.dto.PersonneDTO;
import com.leserviteurs.backend_rest_grapql.exception.ResourceNotFoundException;
import com.leserviteurs.backend_rest_grapql.mapper.PersonneMapper;
import com.leserviteurs.backend_rest_grapql.model.Personne;
import com.leserviteurs.backend_rest_grapql.repository.PersonneRepository;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j // Pour les logs
@AllArgsConstructor
public class PersonneServiceImpl implements PersonneService {

    private final PersonneRepository personneRepository;
    private final PersonneMapper personneMapper;

    /**
     * CREATE - Créer une nouvelle personne
     * Utilisé par REST POST /api/personnes
     */
    @Override
    public PersonneDTO create(PersonneDTO personneDTO) {
        log.info("Création d'une nouvelle personne : {}", personneDTO);

        // ========== VALIDATIONS MÉTIER ==========

        // Vérifier si le téléphone existe déjà
        if (personneDTO.getTelephone() != null && !personneDTO.getTelephone().isEmpty()) {
            String telephoneNormalized = personneDTO.getTelephone().replaceAll("\\s+", "");

            boolean telephoneExists = personneRepository
                    .findByTelephoneContaining(telephoneNormalized)
                    .stream()
                    .anyMatch(p -> p.getTelephone().equals(telephoneNormalized));

            if (telephoneExists) {
                throw new IllegalArgumentException(
                        "Ce numéro de téléphone existe déjà");
            }
        }

        // Vérifier la date de naissance
        if (personneDTO.getDateNaissance() != null) {
            LocalDate today = LocalDate.now();

            // Vérifier que la date n'est pas dans le futur
            if (personneDTO.getDateNaissance().isAfter(today)) {
                throw new IllegalArgumentException(
                        "La date de naissance ne peut pas être dans le futur");
            }

            // Optionnel : Vérifier un âge minimum si besoin
            int age = Period.between(personneDTO.getDateNaissance(), today).getYears();
            if (age < 1) {
                throw new IllegalArgumentException(
                        "La personne doit avoir au moins 1 an");
            }
        }

        // Normaliser les données
        if (personneDTO.getNom() != null) {
            personneDTO.setNom(personneDTO.getNom().trim().toUpperCase());
        }
        if (personneDTO.getPrenom() != null) {
            personneDTO.setPrenom(capitalizeFirstLetter(personneDTO.getPrenom().trim()));
        }
        if (personneDTO.getAdresse() != null && !personneDTO.getAdresse().isEmpty()) {
            personneDTO.setAdresse(personneDTO.getAdresse().trim());
        }
        if (personneDTO.getTelephone() != null && !personneDTO.getTelephone().isEmpty()) {
            personneDTO.setTelephone(personneDTO.getTelephone().replaceAll("\\s+", ""));
        }

        // ========== FIN VALIDATIONS ==========

        // Conversion DTO → Entity
        Personne personne = personneMapper.toEntity(personneDTO);

        // Sauvegarde en base de données
        Personne savedPersonne = personneRepository.save(personne);

        log.info("Personne créée avec l'ID : {}", savedPersonne.getId());

        return personneMapper.toDTO(savedPersonne);
    }

    // Méthode utilitaire
    private String capitalizeFirstLetter(String text) {
        if (text == null || text.isEmpty())
            return text;
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }

    /**
     * UPDATE - Modifier une personne existante
     * Utilisé par REST PUT /api/personnes/{id}
     */
    @Override
    public PersonneDTO update(Long id, PersonneDTO personneDTO) {
        log.info("Modification de la personne avec l'ID : {}", id);

        // 1. Vérifier si la personne existe
        Personne existingPersonne = personneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Personne non trouvée avec l'ID : " + id));

        // ========== VALIDATIONS POUR LA MODIFICATION ==========

        // 2. Valider chaque champ AVANT modification

        // Validation : Nom (obligatoire)
        if (personneDTO.getNom() == null || personneDTO.getNom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom ne peut pas être vide");
        }

        // Validation : Prénom (obligatoire)
        if (personneDTO.getPrenom() == null || personneDTO.getPrenom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le prénom ne peut pas être vide");
        }

        // Validation : Date de naissance (doit être dans le passé)
        if (personneDTO.getDateNaissance() != null &&
                personneDTO.getDateNaissance().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "La date de naissance ne peut pas être dans le futur");
        }

        // Validation : Téléphone (unicité si changé)
        if (personneDTO.getTelephone() != null &&
                !personneDTO.getTelephone().equals(existingPersonne.getTelephone())) {

            boolean telephoneExists = personneRepository
                    .findByTelephoneContaining(personneDTO.getTelephone())
                    .stream()
                    .anyMatch(p -> !p.getId().equals(id) &&
                            p.getTelephone().equals(personneDTO.getTelephone()));

            if (telephoneExists) {
                throw new IllegalArgumentException(
                        "Ce numéro de téléphone est déjà utilisé par une autre personne");
            }
        }

        // 3. Normaliser les données
        personneDTO.setNom(personneDTO.getNom().trim().toUpperCase());
        personneDTO.setPrenom(capitalizeFirstLetter(personneDTO.getPrenom().trim()));
        if (personneDTO.getAdresse() != null) {
            personneDTO.setAdresse(personneDTO.getAdresse().trim());
        }

        // ========== FIN VALIDATIONS ==========

        // 4. Mise à jour des champs
        personneMapper.updateEntityFromDTO(personneDTO, existingPersonne);

        // 5. Sauvegarde des modifications
        Personne updatedPersonne = personneRepository.save(existingPersonne);

        log.info("Personne modifiée avec succès : {}", id);

        return personneMapper.toDTO(updatedPersonne);
    }

    /**
     * DELETE - Supprimer une personne
     * Utilisé par REST DELETE /api/personnes/{id}
     */
    @Override
    public void delete(Long id) {
        log.info("Suppression de la personne avec l'ID : {}", id);

        // Vérifier si la personne existe
        if (!personneRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Personne non trouvée avec l'ID : " + id);
        }

        // Suppression
        personneRepository.deleteById(id);

        log.info("Personne supprimée avec succès : {}", id);
    }

    /**
     * READ ALL - Récupérer toutes les personnes
     * Utilisé par GraphQL Query: allPersonnes
     */
    @Override
    @Transactional(readOnly = true)
    public List<PersonneDTO> findAll() {
        log.info("Récupération de toutes les personnes");

        List<Personne> personnes = personneRepository.findAll();

        // Conversion List<Entity> → List<DTO>
        return personnes.stream()
                .map(personneMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * READ ONE - Récupérer une personne par ID
     * Utilisé par REST - Lance une exception si non trouvée
     */
    @Override
    @Transactional(readOnly = true)
    public PersonneDTO findById(Long id) {
        log.info("Récupération de la personne avec l'ID : {}", id);

        Personne personne = personneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Personne non trouvée avec l'ID : " + id));

        return personneMapper.toDTO(personne);
    }

   

    /**
     * SEARCH - Rechercher des personnes avec filtres
     * Utilisé par GraphQL Query: searchPersonnes
     */
    @Override
    @Transactional(readOnly = true)
    public List<PersonneDTO> search(String nom, String prenom, String telephone) {
        log.info("Recherche de personnes avec filtres - Nom: {}, Prénom: {}, Tél: {}",
                nom, prenom, telephone);

        List<Personne> personnes = personneRepository.searchPersonnes(nom, prenom, telephone);

        // Conversion List<Entity> → List<DTO>
        return personnes.stream()
                .map(personneMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void resetTable() {
        log.warn("RESET de la table personne");
        personneRepository.deleteAll();
        personneRepository.resetAutoIncrement();
        log.info("Table réinitialisée");
    }
}