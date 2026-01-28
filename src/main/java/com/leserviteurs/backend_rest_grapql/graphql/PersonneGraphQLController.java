package com.leserviteurs.backend_rest_grapql.graphql;

import com.leserviteurs.backend_rest_grapql.dto.PersonneDTO;
import com.leserviteurs.backend_rest_grapql.service.PersonneServiceImpl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@Slf4j
@AllArgsConstructor
public class PersonneGraphQLController {

    private final PersonneServiceImpl personneService;

    /**
     * Query GraphQL : allPersonnes
     * Récupère toutes les personnes de la base de données
     * 
     * Exemple de requête :
     * query {
     * allPersonnes {
     * id
     * nom
     * prenom
     * email
     * }
     * }
     */
    @QueryMapping
    public List<PersonneDTO> allPersonnes() {
        log.info("GraphQL Query - allPersonnes");

        List<PersonneDTO> personnes = personneService.findAll();

        log.info("GraphQL Query - {} personne(s) trouvée(s)", personnes.size());

        return personnes;
    }

    /**
     * Query GraphQL : personneById
     * Récupère une personne spécifique par son ID
     * 
     * Exemple de requête :
     * query {
     * personneById(id: 1) {
     * id
     * nom
     * prenom
     * dateNaissance
     * adresse
     * telephone
     * }
     * }
     */
    @QueryMapping
    public PersonneDTO personneById(@Argument Long id) {
        log.info("GraphQL Query - personneById avec ID : {}", id);

        // Si l'ID n'existe pas, findById() lance ResourceNotFoundException
        // Le GraphQLExceptionHandler va l'intercepter et créer une belle erreur
        PersonneDTO personne = personneService.findById(id);

        log.info("GraphQL Query - Personne trouvée : {}", personne);

        return personne;
    }

    /**
     * Query GraphQL : searchPersonnes
     * Recherche des personnes avec des filtres optionnels
     * 
     * Exemple de requête :
     * query {
     * searchPersonnes(nom: "Diop", prenom: "Moussa") {
     * id
     * nom
     * prenom
     * telephone
     * }
     * }
     * 
     * Tous les paramètres sont optionnels (peuvent être null)
     */
    @QueryMapping
    public List<PersonneDTO> searchPersonnes(
            @Argument String nom,
            @Argument String prenom,
            @Argument String telephone) {

        log.info("GraphQL Query - searchPersonnes avec filtres - Nom: {}, Prénom: {}, Tél: {}",
                nom, prenom, telephone);

        List<PersonneDTO> personnes = personneService.search(nom, prenom, telephone);

        log.info("GraphQL Query - {} personne(s) trouvée(s)", personnes.size());

        return personnes;
    }
}