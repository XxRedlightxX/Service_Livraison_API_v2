package com.GaspillageZeroAPI.Controleurs

import com.GaspillageZeroAPI.Exceptions.ExceptionConflitRessourceExistante
import com.GaspillageZeroAPI.Exceptions.ExceptionErreurServeur
import com.GaspillageZeroAPI.Exceptions.ExceptionRequeteInvalide
import com.GaspillageZeroAPI.Exceptions.ExceptionRessourceIntrouvable
import com.GaspillageZeroAPI.Modèle.GabaritProduit
import com.GaspillageZeroAPI.Modèle.Produit
import com.GaspillageZeroAPI.Services.GabaritProduitService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.io.ByteArrayOutputStream
import java.security.Principal
import java.util.*
import javax.sql.rowset.serial.SerialBlob

@RestController
class GabaritProduitController(val service: GabaritProduitService) {

    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Gabaritproduits trouvés"),
        ApiResponse(responseCode = "404", description = "Gabaritproduits non trouvés"),
        ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    ])
    @Operation(summary = "Obtenir la liste des gabaritproduits")
    @GetMapping("/gabaritproduits")
    fun obtenirGabaritProduits(): ResponseEntity<List<GabaritProduit>> {
        try {
            val gabarits = service.chercherTous()

            if (gabarits.isNotEmpty()) {
                return ResponseEntity.ok(gabarits)
            } else {
                throw ExceptionRessourceIntrouvable("Gabaritproduits non trouvés")
            }
        } catch (e: Exception) {
            throw ExceptionErreurServeur("Erreur interne du serveur", e)
        }
    }

    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Gabaritproduit trouvé"),
        ApiResponse(responseCode = "404", description = "Gabaritproduit non trouvé"),
        ApiResponse(responseCode = "400", description = "Requête invalide ou données mal formées")
    ])
    @Operation(summary = "Obtenir un gabaritproduit par son ID")
    @GetMapping("/gabaritproduit/{idGabaritProduit}")
    fun obtenirGabaritProduitParCode(@PathVariable idGabaritProduit: Int): ResponseEntity<GabaritProduit> {
        return try {
            val gabarit = service.chercherParCode(idGabaritProduit)
            gabarit?.let {
                ResponseEntity.ok(gabarit)
            } ?: throw ExceptionRessourceIntrouvable("Gabaritproduit avec l'ID $idGabaritProduit non trouvé")
        } catch (e: IllegalArgumentException) {
            throw ExceptionRequeteInvalide("Requête invalide : ${e.message}")
        } catch (e: Exception) {
            throw ExceptionRessourceIntrouvable("Gabaritproduit avec l'ID $idGabaritProduit non trouvé")
        }
    }

    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Gabarits trouvés"),
        ApiResponse(responseCode = "404", description = "Gabarits non trouvés"),
        ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    ])
    @Operation(summary = "Obtenir la liste des gabarits avec le ID de l'épicerie")
    @GetMapping("/épicerie/{idÉpicerie}/gabaritproduits")
    fun obtenirGabaritsÉpicerie(@PathVariable idÉpicerie: Int): ResponseEntity<List<GabaritProduit>> {
        return try {
            val gabarits = service.chercherParÉpicerie(idÉpicerie)
                ?: throw ExceptionRessourceIntrouvable("Gabarits de l'épicerie avec l'ID $idÉpicerie non trouvés")
            ResponseEntity.ok(gabarits)
        } catch (e: Exception) {
            throw ExceptionErreurServeur("Erreur interne du serveur", e)
        }
    }

    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Gabaritproduit créé"),
        ApiResponse(responseCode = "400", description = "Requête invalide ou données mal formées"),
        ApiResponse(responseCode = "409", description = "Conflit: Violation de contrainte"),
        ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    ])
    @Operation(summary = "Ajouter un gabaritproduit")
    @PostMapping("/gabaritproduit")
    fun ajouterGabarit(@RequestBody gabaritProduit: GabaritProduit, principal: Principal): ResponseEntity<Void> {
        return try {
            service.ajouter(gabaritProduit, principal.name)
            ResponseEntity.status(HttpStatus.CREATED).build()
        } catch (e: DataIntegrityViolationException) {
            throw ExceptionConflitRessourceExistante("Conflit: Violation de contrainte")
        } catch (e: IllegalArgumentException) {
            throw ExceptionRequeteInvalide("Requête invalide ou données mal formées")
        } catch (e: Exception) {
            throw ExceptionErreurServeur("Erreur interne du serveur lors de l'ajout du gabaritproduit")
        }
    }

    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Gabaritproduit supprimé"),
        ApiResponse(responseCode = "404", description = "Gabaritproduit non trouvé"),
        ApiResponse(responseCode = "400", description = "Requête invalide")
    ])
    @Operation(summary = "Supprimer un gabaritproduit par son ID")
    @DeleteMapping("/gabaritproduit/{idGabaritProduit}")
    fun supprimerGabarit(@PathVariable idGabaritProduit: Int, principal: Principal): ResponseEntity<Void> {
        return try {
            if (service.supprimer(idGabaritProduit, principal.name)) {
                ResponseEntity.ok().build()
            } else {
                throw ExceptionRessourceIntrouvable("Gabaritproduit avec l'ID $idGabaritProduit non trouvé")
            }
        } catch (e: IllegalArgumentException) {
            throw ExceptionRequeteInvalide("Requête invalide : ${e.message}")
        } catch (e: Exception) {
            throw ExceptionRessourceIntrouvable("Gabaritproduit avec l'ID $idGabaritProduit non trouvé")
        }
    }


    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Gabaritproduit modifié"),
        ApiResponse(responseCode = "404", description = "Gabaritproduit non trouvé"),
        ApiResponse(responseCode = "400", description = "Requête invalide ou données mal formées"),
        ApiResponse(responseCode = "409", description = "Conflit: Violation de contrainte")
    ])
    @Operation(summary = "Modifier un gabaritproduit par son ID")
    @PutMapping("/gabaritproduit/{idGabaritProduit}")
    fun modifierGabarit(@PathVariable idGabaritProduit: Int, @RequestBody gabaritProduit: GabaritProduit, principal: Principal): ResponseEntity<Void> {
        return try {
            if (service.modifier(idGabaritProduit, gabaritProduit, principal.name)) {
                ResponseEntity.ok().build()
            } else {
                throw ExceptionRessourceIntrouvable("Gabaritproduit avec l'ID $idGabaritProduit non trouvé")
            }
        } catch (e: DataIntegrityViolationException) {
            throw ExceptionConflitRessourceExistante("Conflit: Violation de contrainte pour l'ID $idGabaritProduit")
        } catch (e: IllegalArgumentException) {
            throw ExceptionRequeteInvalide("Requête invalide ou données mal formées : ${e.message}")
        } catch (e: Exception) {
            throw ExceptionRessourceIntrouvable("Gabaritproduit avec l'ID $idGabaritProduit non trouvé")
        }
    }


}
