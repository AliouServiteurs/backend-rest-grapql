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
import com.leserviteurs.backend_rest_grapql.validation.ValidationUtils;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class PersonneServiceImpl implements PersonneService {

    private final PersonneRepository personneRepository;
    private final PersonneMapper personneMapper;

    /**
     * CREATE - Créer une nouvelle personne
     */
    @Override
    public PersonneDTO create(PersonneDTO personneDTO) {
        log.info("Création d'une nouvelle personne : {}", personneDTO);

        // ========== VALIDATIONS MÉTIER ==========

        // Validation Nom
        if (!ValidationUtils.isValidNom(personneDTO.getNom())) {
            throw new IllegalArgumentException(
                    "Le nom ne doit contenir que des lettres et des espaces (pas de chiffres ni symboles)");
        }

        // Validation Prénom
        if (!ValidationUtils.isValidNom(personneDTO.getPrenom())) {
            throw new IllegalArgumentException(
                    "Le prénom ne doit contenir que des lettres et des espaces (pas de chiffres ni symboles)");
        }

        // Validation Téléphone
        if (personneDTO.getTelephone() != null && !personneDTO.getTelephone().isEmpty()) {
            if (!ValidationUtils.isValidTelephone(personneDTO.getTelephone())) {
                throw new IllegalArgumentException(
                        "Le numéro de téléphone doit être un numéro sénégalais valide (9 chiffres commençant par 7)");
            }

            // ========== VÉRIFIER UNICITÉ (CORRIGÉ) ==========
            String telephoneCleaned = personneDTO.getTelephone().replaceAll("\\s+", "");

            Optional<Personne> existingPersonne = personneRepository.findByTelephoneCleaned(telephoneCleaned);

            if (existingPersonne.isPresent()) {
                throw new IllegalArgumentException(
                        "Ce numéro de téléphone existe déjà");
            }
        }

        // Validation Adresse
        if (personneDTO.getAdresse() != null && !personneDTO.getAdresse().isEmpty()) {
            if (!ValidationUtils.isValidAdresse(personneDTO.getAdresse())) {
                throw new IllegalArgumentException(
                        "L'adresse contient des caractères invalides. Seuls les lettres, chiffres, espaces, tirets, virgules et points sont autorisés");
            }
        }

        // Vérifier la date de naissance
        if (personneDTO.getDateNaissance() != null) {
            LocalDate today = LocalDate.now();

            if (personneDTO.getDateNaissance().isAfter(today)) {
                throw new IllegalArgumentException(
                        "La date de naissance ne peut pas être dans le futur");
            }

            int age = Period.between(personneDTO.getDateNaissance(), today).getYears();

            if (age < 1) {
                throw new IllegalArgumentException("La personne doit avoir au moins 1 an");
            }

            if (age > 120) {
                throw new IllegalArgumentException("La date de naissance n'est pas réaliste");
            }
        }

        // Normaliser les données
        personneDTO.setNom(personneDTO.getNom().trim().toUpperCase());
        personneDTO.setPrenom(capitalizeFirstLetter(personneDTO.getPrenom().trim()));

        if (personneDTO.getAdresse() != null && !personneDTO.getAdresse().isEmpty()) {
            personneDTO.setAdresse(personneDTO.getAdresse().trim());
        }

        if (personneDTO.getTelephone() != null && !personneDTO.getTelephone().isEmpty()) {
            personneDTO.setTelephone(
                    ValidationUtils.formatTelephone(
                            personneDTO.getTelephone().replaceAll("\\s+", "")));
        }

        // ========== FIN VALIDATIONS ==========

        Personne personne = personneMapper.toEntity(personneDTO);
        Personne savedPersonne = personneRepository.save(personne);

        log.info("Personne créée avec l'ID : {}", savedPersonne.getId());

        return personneMapper.toDTO(savedPersonne);
    }

    /**
     * UPDATE - Modifier une personne existante
     */
    @Override
    public PersonneDTO update(Long id, PersonneDTO personneDTO) {
        log.info("Modification de la personne avec l'ID : {}", id);

        // 1. Vérifier si la personne existe
        Personne existingPersonne = personneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Personne non trouvée avec l'ID : " + id));

        // ========== VALIDATIONS POUR LA MODIFICATION ==========

        // Validation Nom
        if (personneDTO.getNom() == null || personneDTO.getNom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom ne peut pas être vide");
        }
        if (!ValidationUtils.isValidNom(personneDTO.getNom())) {
            throw new IllegalArgumentException(
                    "Le nom ne doit contenir que des lettres et des espaces (pas de chiffres ni symboles)");
        }

        // Validation Prénom
        if (personneDTO.getPrenom() == null || personneDTO.getPrenom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le prénom ne peut pas être vide");
        }
        if (!ValidationUtils.isValidNom(personneDTO.getPrenom())) {
            throw new IllegalArgumentException(
                    "Le prénom ne doit contenir que des lettres et des espaces (pas de chiffres ni symboles)");
        }

        // Validation Date de naissance
        if (personneDTO.getDateNaissance() != null) {
            LocalDate today = LocalDate.now();

            if (personneDTO.getDateNaissance().isAfter(today)) {
                throw new IllegalArgumentException(
                        "La date de naissance ne peut pas être dans le futur");
            }

            int age = Period.between(personneDTO.getDateNaissance(), today).getYears();

            if (age < 1) {
                throw new IllegalArgumentException("La personne doit avoir au moins 1 an");
            }

            if (age > 120) {
                throw new IllegalArgumentException("La date de naissance n'est pas réaliste");
            }
        }

        // Validation Téléphone
        if (personneDTO.getTelephone() != null && !personneDTO.getTelephone().isEmpty()) {
            if (!ValidationUtils.isValidTelephone(personneDTO.getTelephone())) {
                throw new IllegalArgumentException(
                        "Le numéro de téléphone doit être un numéro sénégalais valide (9 chiffres commençant par 7)");
            }

            // ========== VÉRIFIER UNICITÉ (CORRIGÉ) ==========
            String telephoneCleaned = personneDTO.getTelephone().replaceAll("\\s+", "");

            Optional<Personne> existingTelephone = personneRepository.findByTelephoneCleaned(telephoneCleaned);

            // Vérifier si le téléphone existe ET qu'il n'appartient pas à la personne
            // actuelle
            if (existingTelephone.isPresent() && !existingTelephone.get().getId().equals(id)) {
                throw new IllegalArgumentException(
                        "Ce numéro de téléphone est déjà utilisé par une autre personne");
            }
        }

        // Validation Adresse
        if (personneDTO.getAdresse() != null && !personneDTO.getAdresse().isEmpty()) {
            if (!ValidationUtils.isValidAdresse(personneDTO.getAdresse())) {
                throw new IllegalArgumentException(
                        "L'adresse contient des caractères invalides. Seuls les lettres, chiffres, espaces, tirets, virgules et points sont autorisés");
            }
        }

        // Normaliser les données
        personneDTO.setNom(personneDTO.getNom().trim().toUpperCase());
        personneDTO.setPrenom(capitalizeFirstLetter(personneDTO.getPrenom().trim()));

        if (personneDTO.getAdresse() != null && !personneDTO.getAdresse().isEmpty()) {
            personneDTO.setAdresse(personneDTO.getAdresse().trim());
        }

        if (personneDTO.getTelephone() != null && !personneDTO.getTelephone().isEmpty()) {
            personneDTO.setTelephone(
                    ValidationUtils.formatTelephone(
                            personneDTO.getTelephone().replaceAll("\\s+", "")));
        }

        // ========== FIN VALIDATIONS ==========

        personneMapper.updateEntityFromDTO(personneDTO, existingPersonne);
        Personne updatedPersonne = personneRepository.save(existingPersonne);

        log.info("Personne modifiée avec succès : {}", id);

        return personneMapper.toDTO(updatedPersonne);
    }

    private String capitalizeFirstLetter(String text) {
        if (text == null || text.isEmpty())
            return text;
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }

    @Override
    public void delete(Long id) {
        log.info("Suppression de la personne avec l'ID : {}", id);
        if (!personneRepository.existsById(id)) {
            throw new ResourceNotFoundException("Personne non trouvée avec l'ID : " + id);
        }
        personneRepository.deleteById(id);
        log.info("Personne supprimée avec succès : {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonneDTO> findAll() {
        log.info("Récupération de toutes les personnes");
        List<Personne> personnes = personneRepository.findAll();
        return personnes.stream().map(personneMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PersonneDTO findById(Long id) {
        log.info("Récupération de la personne avec l'ID : {}", id);
        Personne personne = personneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Personne non trouvée avec l'ID : " + id));
        return personneMapper.toDTO(personne);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonneDTO> search(String nom, String prenom, String telephone) {
        log.info("Recherche de personnes avec filtres - Nom: {}, Prénom: {}, Tél: {}", nom, prenom, telephone);
        List<Personne> personnes = personneRepository.searchPersonnes(nom, prenom, telephone);
        return personnes.stream().map(personneMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public void resetTable() {
        log.warn("RESET de la table personne");
        personneRepository.deleteAll();
        personneRepository.resetAutoIncrement();
        log.info("Table réinitialisée");
    }
}