package com.GaspillageZeroAPI.DAO

import com.GaspillageZeroAPI.Modèle.Livraison
import com.GaspillageZeroAPI.Modèle.Utilisateur
import com.GaspillageZeroAPI.Modèle.Évaluation

interface LivraisonDAO: DAO<Livraison> {
    override fun chercherTous(): List<Livraison>
    override fun chercherParCode(code: Int): Livraison?
    fun chercherLivraisonExistanteParCode(code: Int): Int?
    fun chercherParUtilisateurCommandeEtLivraison(code: Int, commande_code: Int, utilisateur_code: String?, livraison_code: Int): Livraison?
    override fun ajouter(livraison: Livraison): Livraison?
    override fun modifier(code: Int, livraison: Livraison): Livraison?
    override fun supprimer(code: Int): Livraison?

    fun TrouverParCommandeCode(commandeCode: Int): List<Livraison>

}