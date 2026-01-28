package com.leserviteurs.backend_rest_grapql.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.leserviteurs.backend_rest_grapql.model.Personne;

import jakarta.transaction.Transactional;

import java.util.List;

@Repository
public interface PersonneRepository extends JpaRepository<Personne, Long> {

    // Méthodes de recherche pour GraphQL
    List<Personne> findByNomContainingIgnoreCase(String nom);

    List<Personne> findByPrenomContainingIgnoreCase(String prenom);

    List<Personne> findByTelephoneContaining(String telephone);

    // Recherche combinée (optionnelle mais utile)
    @Query("SELECT p FROM Personne p WHERE " +
            "(:nom IS NULL OR LOWER(p.nom) LIKE LOWER(CONCAT('%', :nom, '%'))) AND " +
            "(:prenom IS NULL OR LOWER(p.prenom) LIKE LOWER(CONCAT('%', :prenom, '%'))) AND " +
            "(:telephone IS NULL OR p.telephone LIKE CONCAT('%', :telephone, '%'))")
    List<Personne> searchPersonnes(
            @Param("nom") String nom,
            @Param("prenom") String prenom,
            @Param("telephone") String telephone);

    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE personne AUTO_INCREMENT = 1", nativeQuery = true)
    void resetAutoIncrement();

}