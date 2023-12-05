package com.GaspillageZeroAPI.Controleurs

import com.GaspillageZeroAPI.Modèle.Livraison
import com.GaspillageZeroAPI.Services.LivraisonService
import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@AutoConfigureMockMvc
class LivraisonControleurUtilisateursAuthentifiésTests {

    @MockBean
    lateinit var service: LivraisonService

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var mapper: ObjectMapper

    val livraison = Livraison(2, 2, 2, 2)

    @WithMockUser
    @Test
    //@GetMapping("/utilisateur/{code_utilisateur}/commande/{idCommande}/livraisons")
    fun `Étant donné un utilisateur authentifié et des livraisons inscrits au service lorsque l'utilisateur effectue une requête GET pour obtenir la liste de livraisons inscrits alors, il obtient la liste de livraisons et un code de retour 200` (){

        val liste_de_livraisons = listOf(Livraison(1,1,1,1), Livraison(2,2,2,2))
        Mockito.`when`(service.obtenirLivraisons()).thenReturn(liste_de_livraisons)

        mockMvc.perform(get("/utilisateur/1/commande/1/livraisons"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].code").value(1))
    }

    @WithMockUser
    @Test
    //@GetMapping("/utilisateur/{code_utilisateur}/commande/{idCommande}/livraisons/{codeLivraison}")
    fun `Étant donné un utilisateur authentifié et une livraison dont le code est '2', lorsque l'utilisateur effectue une requête GET de recherche par code alors, il obtient un JSON qui contient une livraison dont le code est '2' et un code de retour 200`() {
        Mockito.`when`(service.obtenirLivraisonParCode(2)).thenReturn(livraison)

        mockMvc.perform(get("/utilisateur/2/commande/2/livraisons/2"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code").value(2))
    }

    @WithMockUser
    @Test
    //@PostMapping("/utilisateur/{code_utilisateur}/commande/{idCommande}/livraison")
    fun `Étant donnée un utilisateur authentifié et une livraison avec le code '3', lorsqu'on inscrit une livraison au service avec le code '3' à l'aide d'une requête POST, on obtient le code 201` (){

        val nouvelleLivraison = Livraison(3, 3, 3, 3)
        Mockito.`when`(service.ajouterLivraison(nouvelleLivraison)).thenReturn(nouvelleLivraison)

        mockMvc.perform(post("/utilisateur/3/commande/3/livraison")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(nouvelleLivraison)))
            .andExpect(status().isCreated)
            .andExpect(
                header().string("Location", CoreMatchers.containsString("/utilisateur/3/commande/3/livraisons/3")))
    }
}