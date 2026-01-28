# Rapport de Tests - Application Personne

## Date : 28 janv 2026
## Testeur : Aliou DIOP

## 1. Tests API REST

| Test | Statut | Commentaire |
|------|--------|-------------|
| POST personne valide | ✅ PASS | 201 Created |
| POST validation nom | ✅ PASS | 400 Bad Request |
| PUT modifier existant | ✅ PASS | 200 OK |
| DELETE supprimer | ✅ PASS | 204 No Content |

## 2. Tests API GraphQL

| Test | Statut | Commentaire |
|------|--------|-------------|
| allPersonnes | ✅ PASS | Liste complète |
| personneById(1) | ✅ PASS | Détails personne |
| personneById(999) | ✅ PASS | Erreur NOT_FOUND |
| searchPersonnes | ✅ PASS | Filtres fonctionnent |

## 3. Tests Validation

| Test | Statut | Résultat |
|------|--------|----------|
| Normalisation nom | ✅ PASS | MAJUSCULES |
| Capitalisation prénom | ✅ PASS | Première maj |
| Unicité téléphone | ✅ PASS | Erreur si doublon |

## 4. Conclusion

Tous les tests passent avec succès. L'application est prête pour la démonstration.