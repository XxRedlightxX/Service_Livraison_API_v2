package com.GaspillageZeroAPI.Controleurs

import com.GaspillageZeroAPI.Modèle.Adresse
import com.GaspillageZeroAPI.Modèle.Commande
import com.GaspillageZeroAPI.Services.CommandeService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.web.bind.annotation.*

@RestController
class CommandeControleur(val service: CommandeService) {

    @Operation(summary = "Obtenir la liste de toutes les commandes")
    @GetMapping("/commandes")
    fun obtenirCommandes() = service.chercherTous()

    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Commande trouvée"),
        ApiResponse(responseCode = "404", description = "commande non trouvé")
    ])

    @Operation(summary = "Obtenir la commande par le ID de celui-ci")
    @GetMapping("/commande/{idCommande}")
    fun obtenirCommandeparCode(@PathVariable idCommande: Int) = service.chercherParCode(idCommande)

    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Commande trouvée"),
        ApiResponse(responseCode = "404", description = "commande non trouvé")
    ])

    @Operation(summary = "Permet d'ajouter une commande à la BD")
    @GetMapping("/commandes/utilisateur/{idUtilisateur}")
    fun obtenirCommandesParUtilisateur(@PathVariable idUtilisateur: Int) = service.chercherCommandesParUtilisateur(idUtilisateur)

    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Commande trouvée"),
        ApiResponse(responseCode = "404", description = "commande non trouvé")
    ])

    @Operation(summary = "Permet d'obtenir d'obtenir la commande {idCommande} de l'utilisateur {idUtilisateur}")
    @GetMapping("/commandes/utilisateur/{idUtilisateur}/commande/{idCommande}")
    fun obtenirCommandeParUtilisateur(@PathVariable idUtilisateur: Int, @PathVariable idCommande: Int) = service.chercherCommandeParUtilisateur(idUtilisateur, idCommande)

    @GetMapping("/commandeDetail/utilisateur/{idUtilisateur}/commande/{idCommande}")
    fun obtenirCommandeDetailParUtilisateur(@PathVariable idUtilisateur: Int, @PathVariable idCommande: Int) = service.chercherCommandeDetailParUtilisateur(idUtilisateur, idCommande)

    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Commande trouvée"),
        ApiResponse(responseCode = "404", description = "commande non trouvé")
    ])

    @Operation(summary = "Permet d'obtenir toutes les commande faites pour une épicerie ayant le {idÉpicerie}")
    @GetMapping("/commandes/épicerie/{idÉpicerie}")
    fun obtenirCommandesParÉpicerie(@PathVariable idÉpicerie: Int) = service.chercherCommandesParÉpicerie(idÉpicerie)

    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Commande trouvée"),
        ApiResponse(responseCode = "404", description = "commande non trouvé")
    ])

    @Operation(summary = "Permet d'obtenir une commande ayant le {idCommande} à l'épicerie ayant le {idÉpicerie}")
    @GetMapping("/commandes/épicerie/{idÉpicerie}/commande/{idCommande}")
    fun obtenirCommandeParÉpicerie(@PathVariable idÉpicerie: Int, @PathVariable idCommande: Int) = service.chercherCommandeParÉpicerie(idÉpicerie, idCommande)


    @GetMapping("/commandeDetail/épicerie/{idUtilisateur}/commande/{idCommande}")
    fun obtenirCommandeDetailParÉpicerie(@PathVariable idÉpicerie: Int, @PathVariable idCommande: Int) = service.chercherCommandeDetailParÉpicerie(idÉpicerie, idCommande)
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "La commande à été ajouter à la base de données"),
        ApiResponse(responseCode = "409", description = "Le produit existe déjà dans la base de données")
    ])
    @Operation(summary = "Permet d'ajouter une commande à la base de données")
    @PostMapping("/commande")
    fun ajouterCommande(@RequestBody commande: Commande) {
        service.ajouter(commande)
    }

    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "la commande à été retiré avec succès!"),
        ApiResponse(responseCode = "404", description = "La commande est introuvable")
    ])

    @Operation(summary = "Permet de retirer une commande de la base de données")
    @DeleteMapping("/commande/delete/{idCommande}")
    fun suppimerCommande(@PathVariable idCommande: Int) {
        service.supprimer(idCommande)
    }

    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "La commande à été modifié avec succès!"),
        ApiResponse(responseCode = "404", description = "La commande est malheureusement introuvable.")
    ])

    @Operation(summary = "Permet de modifier les informations d'une commande")
    @PutMapping("/commande/modifier/{idCommande}")
    fun modifierCommande(@PathVariable idCommande: Int, @RequestBody commande: Commande){
        service.modifier(idCommande, commande)
    }


}