package com.leserviteurs.backend_rest_grapql.graphql;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;

import com.leserviteurs.backend_rest_grapql.exception.ResourceNotFoundException;

/**
 * Gestionnaire d'exceptions pour GraphQL
 * Convertit les exceptions Java en erreurs GraphQL formatées
 */
@Component
@Slf4j
public class GraphQLExceptionHandler extends DataFetcherExceptionResolverAdapter {

    /**
     * Résout une exception en erreur GraphQL
     * 
     * @param ex  L'exception lancée
     * @param env L'environnement d'exécution GraphQL
     * @return Une erreur GraphQL formatée
     */
    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {

        // Gestion de ResourceNotFoundException (404)
        if (ex instanceof ResourceNotFoundException) {
            log.error("GraphQL - Ressource non trouvée : {}", ex.getMessage());

            return GraphqlErrorBuilder.newError()
                    .errorType(ErrorType.NOT_FOUND)
                    .message(ex.getMessage())
                    .path(env.getExecutionStepInfo().getPath())
                    .location(env.getField().getSourceLocation())
                    .extensions(Map.of(
                            "timestamp", LocalDateTime.now().toString(),
                            "resource", "Personne",
                            "operation", env.getField().getName()))
                    .build();
        }

        // Gestion de IllegalArgumentException (400)
        if (ex instanceof IllegalArgumentException) {
            log.error("GraphQL - Argument invalide : {}", ex.getMessage());

            return GraphqlErrorBuilder.newError()
                    .errorType(ErrorType.BAD_REQUEST)
                    .message(ex.getMessage())
                    .path(env.getExecutionStepInfo().getPath())
                    .location(env.getField().getSourceLocation())
                    .build();
        }

        // Pour les autres exceptions, laisser le handler par défaut gérer
        // (retourne INTERNAL_ERROR)
        log.error("GraphQL - Erreur interne : {}", ex.getMessage(), ex);
        return null;
    }
}