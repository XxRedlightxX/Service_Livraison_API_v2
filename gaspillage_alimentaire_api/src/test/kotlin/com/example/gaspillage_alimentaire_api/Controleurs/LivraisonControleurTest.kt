package com.example.gaspillage_alimentaire_api.Controleurs

import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@AutoConfigureMockMvc
class LivraisonControleurTest {

    @Test
    //@GetMapping("/evaluations/{code}")
    fun `Étant donné un avis dont le code est '0009' et qui n'est pas inscrit au service lorsqu'on effectue une requête GET de recherche par code alors on obtient un code de retour 404 et le message d'erreur « L'avis avec le numéro '0009' n'est pas inscrit au service »`() {
        TODO("Méthode non-implémentée")
    }

    @Test
    //@GetMapping("/evaluations/{code}")
    fun `Étant donné un avis dont le code est '0001' lorsqu'on effectue une requête GET de recherche par code alors on obtient un JSON qui contient un avis dont le code est '0001' et un code de retour 200`() {
        TODO("Méthode non-implémentée")
    }

    @Test
    //@GetMapping("/livraisons/{code}")
    fun `Étant donné le numéro de livraison dont le code est '03295', lorsqu'on effectue une requête GET de recherche par code alors on obtient un JSON qui contient un numéro dont le code est '03295' et un code de retour 200`() {
        TODO("Méthode non-implémentée")
    }

    @Test
    //@PostMapping("/livraisons")
    fun `Étant donnée le numéro de livraison dont le code est '04590' et qui n'est pas inscrit au service lorsqu'on effectue une requête POST pour l'ajouter alors on obtient un JSON qui contient un numéro de livraison dont le code est '04590' et un code de retour 201` (){
        TODO("Méthode non-implémentée")
    }

    @Test
    //@PutMapping("/livraisons/{code}")
    fun `Étant donnée le numéro de livraison dont le code est '04590' et qui est inscrit au service et dont l'état est 'en route' lorsqu'on effectue une requête PUT pour modifier l'état pour « livrée » alors on obtient un JSON qui contient un numéro de livraison dont le code est '04590' et l'état est « livrée » ainsi qu'un code de retour 200` (){
        TODO("Méthode non-implémentée")
    }

    @Test
    //@DeleteMapping("/livraisons/{code}")
    fun `Étant donnée le numéro de livraison dont le code est '04590' et qui est inscrit au service lorsqu'on effectue une requête DELETE alors on obtient un code de retour 204` (){
        TODO("Méthode non-implémentée")
    }
}