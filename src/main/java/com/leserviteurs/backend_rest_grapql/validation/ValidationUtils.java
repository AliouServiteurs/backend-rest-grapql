package com.leserviteurs.backend_rest_grapql.validation;

import java.util.regex.Pattern;

public class ValidationUtils {
    
    // Regex pour téléphone sénégalais (9 chiffres commençant par 7)
    private static final Pattern TELEPHONE_PATTERN = Pattern.compile("^7[0-8]\\d{7}$");
    
    // Regex pour nom/prénom (lettres et espaces uniquement, accents autorisés)
    private static final Pattern NOM_PATTERN = Pattern.compile("^[a-zA-ZÀ-ÿ\\s]+$");
    
    // Regex pour adresse (lettres, chiffres, espaces, tirets, virgules, points)
    private static final Pattern ADRESSE_PATTERN = Pattern.compile("^[a-zA-Z0-9À-ÿ\\s,.-]+$");
    
    /**
     * Valider un numéro de téléphone sénégalais
     * @param telephone Le numéro sans espaces (ex: "771234567")
     * @return true si valide
     */
    public static boolean isValidTelephone(String telephone) {
        if (telephone == null || telephone.isEmpty()) {
            return true; // Optionnel
        }
        
        // Supprimer tous les espaces
        String cleaned = telephone.replaceAll("\\s+", "");
        
        // Vérifier le format
        return TELEPHONE_PATTERN.matcher(cleaned).matches();
    }
    
    /**
     * Valider un nom ou prénom
     * @param nom Le nom/prénom
     * @return true si valide
     */
    public static boolean isValidNom(String nom) {
        if (nom == null || nom.trim().isEmpty()) {
            return false;
        }
        
        return NOM_PATTERN.matcher(nom.trim()).matches();
    }
    
    /**
     * Valider une adresse
     * @param adresse L'adresse
     * @return true si valide
     */
    public static boolean isValidAdresse(String adresse) {
        if (adresse == null || adresse.isEmpty()) {
            return true; // Optionnel
        }
        
        return ADRESSE_PATTERN.matcher(adresse.trim()).matches();
    }
    
    /**
     * Formater un numéro de téléphone pour l'affichage
     * Ex: "771234567" -> "77 123 45 67"
     */
    public static String formatTelephone(String telephone) {
        if (telephone == null || telephone.isEmpty()) {
            return telephone;
        }
        
        String cleaned = telephone.replaceAll("\\s+", "");
        
        if (cleaned.length() == 9) {
            return cleaned.substring(0, 2) + " " + 
                   cleaned.substring(2, 5) + " " + 
                   cleaned.substring(5, 7) + " " + 
                   cleaned.substring(7, 9);
        }
        
        return telephone;
    }
}