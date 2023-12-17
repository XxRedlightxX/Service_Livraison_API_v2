package com.GaspillageZeroAPI.Controleurs

import com.GaspillageZeroAPI.Exceptions.DroitAccèsInsuffisantException
import com.GaspillageZeroAPI.Exceptions.ExceptionConflitRessourceExistante
import com.GaspillageZeroAPI.Exceptions.ExceptionRessourceIntrouvable
import com.GaspillageZeroAPI.Exceptions.LivraisonIntrouvableException
import com.GaspillageZeroAPI.Modèle.Livraison
import com.GaspillageZeroAPI.Modèle.Évaluation
import com.GaspillageZeroAPI.Services.LivraisonService
import com.GaspillageZeroAPI.Services.UtilisateurService
import com.GaspillageZeroAPI.Services.ÉvaluationService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

@RestController
class LivraisonControleur (val livraisonService: LivraisonService, val évaluationService : ÉvaluationService,
                           val utilisateurService: UtilisateurService)  {
    //Pour accéder à la documentation OpenApi, visitez le lien suivant pour en savoir plus : http://localhost:8080/swagger-ui/index.html

    @GetMapping("/evaluations")
    @Operation(summary = "Obtenir la liste des évaluations")
    @ApiResponse(responseCode = "200", description = "Liste des évaluations trouvées")
    @ApiResponse(responseCode = "404", description = "Liste des évaluations non-trouvées, veuillez réessayez...")
    fun obtenirTousÉvalutions() = évaluationService.obtenirÉvaluations()

    @GetMapping("/evaluations/{code}")
    @Operation(summary = "Obtenir une évaluation en cherchant par code")
    @ApiResponse(responseCode = "200", description = "Évaluation trouvée")
    @ApiResponse(responseCode = "404", description = "Évaluation non-trouvée, veuillez réessayez...")
    fun obtenirEvaluationParCode(@PathVariable code: Int) : Évaluation? {
        val avis = évaluationService.chercherParCodeÉvaluation(code)
        if(avis == null){
            throw LivraisonIntrouvableException("L'avis avec le code $code est introuvable")
        }
        return avis
    }

    @GetMapping("/utilisateur/{code_utilisateur}/commande/{idCommande}/livraisons")
    @Operation(summary = "Obtenir la liste des livraisons")
    @ApiResponse(responseCode = "200", description = "Liste des livraisons trouvées")
    @ApiResponse(responseCode = "404", description = "Liste des livraisons non-trouvées, veuillez réessayez...")
    fun obtenirLivraisons(@PathVariable code_utilisateur: Int,
                          @PathVariable idCommande: Int) = livraisonService.obtenirLivraisons()

    @GetMapping("/utilisateur/{code_utilisateur}/commande/{idCommande}/livraisons/{codeLivraison}")
    @Operation(summary = "Obtenir une livraison en cherchant par code")
    @ApiResponse(responseCode = "200", description = "Ce code signifie que la livraison a été trouvé avec le bon utilisateur")
    @ApiResponse(responseCode = "401", description = "Ce code est retourné lorsqu'un utilisateur non-authentifié tente de chercher une livraison par code.")
    @ApiResponse(responseCode = "403", description = "Ce code est retourné lorsqu'un individu non-autorisé tente de chercher une livraison par code d'un autre utilisateur.")
    @ApiResponse(responseCode = "500", description = "Ce code est retournée lorsqu'on essaye de rechercher une livraison qui n'existe pas et envoye une exception avec un message")
    fun obtenirLivraisonParCode(@PathVariable code_utilisateur: Int, @PathVariable idCommande: Int,
                                @PathVariable codeLivraison: Int, auth: Authentication?) : ResponseEntity<Livraison> {

        try {
            val code_util = auth?.name
            val validation = utilisateurService.validerCodeAuth0(code_utilisateur)
            if (validation == code_util) {
                val livraisonTrouvée =
                    livraisonService.obtenirLivraisonParCodeUtilisateurEtCommande(
                        code_utilisateur,
                        idCommande,
                        code_util,
                        codeLivraison
                    )
                return ResponseEntity.ok(livraisonTrouvée)
            } else if (code_util == null){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
            }
        } catch (e: Exception) {
            throw ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, "Livraison non-trouvée, veuillez réessayer!"
            )
        }
    }

    @PostMapping("/utilisateur/{code_utilisateur}/commande/{idCommande}/livraison")
    @Operation(summary = "Ajouté une livraison")
    @ApiResponse(responseCode = "201", description = "Ce code signifie que la livraison a bien été ajouté dans la base de données.")
    @ApiResponse(responseCode = "409", description = "Ce code est retourné lorsqu'on essaye d'ajouter une livraison qui existe déjà.")
    @ApiResponse(responseCode = "500", description = "Ce code signifie qu'il y a un problème interne dans le serveur.")
    fun inscrireLivraison(@PathVariable code_utilisateur: Int,
        @PathVariable idCommande: Int, @RequestBody livraison: Livraison,
                          uriComponentsBuilder: UriComponentsBuilder) : ResponseEntity<Livraison> {
        val livraisonExistante = livraison.code?.let { livraisonService.obtenirLivraisonExistanteParCode(it) }

        return if (livraisonExistante == 0) {
            val nouvelleLivraison = livraisonService.ajouterLivraison(livraison)
            val location: URI = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/utilisateur/{code_utilisateur}/commande/{idCommande}/livraisons/{codeLivraison}")
                .buildAndExpand(code_utilisateur, idCommande, livraison.code)
                .toUri()

            ResponseEntity.created(location).body(nouvelleLivraison)
        } else if (livraisonExistante == 1){
            throw ExceptionConflitRessourceExistante("La livraison avec le numéro de code ${livraison.code} est déjà inscrit au service.")
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    @PutMapping("/utilisateur/{code_utilisateur}/commande/{idCommande}/livraisons/{code}")
    @Operation(summary = "Modifier une livraison")
    @ApiResponse(responseCode = "204", description = "La livraison a été modifiée avec succès!")
    fun majLivraison(@PathVariable code_utilisateur: Int, @PathVariable idCommande: Int,
                     @PathVariable code: Int, @RequestBody livraison: Livraison) {
        livraisonService.modifierLivraison(code, livraison)
    }

    @DeleteMapping("/utilisateur/{code_utilisateur}/commande/{idCommande}/livraisons/{code}")
    @Operation(summary = "Supprimer une livraison")
    @ApiResponse(responseCode = "204", description = "Ce code signifie que la livraison a bien été supprimé et elle retourne une page sans contenu.")
    @ApiResponse(responseCode = "401", description = "Ce code signifie que l'utilisateur n'est pas autorisé de supprimer puisqu'il n'est pas authentifié.")
    @ApiResponse(responseCode = "403", description = "Ce code signifie qu'un utilisateur est interdit de supprimer une livraison, car il n'est pas le bon gérant.")
    @ApiResponse(responseCode = "500", description = "Ce code signifie qu'il y a un problème interne dans le serveur.")
    fun supprimerLivraison(@PathVariable code: Int)  {
          livraisonService.supprimerLivraison(code)
    }
}