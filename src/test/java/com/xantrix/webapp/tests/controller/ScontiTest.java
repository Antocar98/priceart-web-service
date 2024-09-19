package com.xantrix.webapp.tests.controller;

import com.xantrix.webapp.PriceArtService;
import com.xantrix.webapp.entities.DettListini;
import com.xantrix.webapp.entities.Listini;
import com.xantrix.webapp.repository.ListinoRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(properties= {"profilo = list1", "seq= 1","ramo= main"})
@ContextConfiguration(classes = PriceArtService.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ScontiTest
{
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private ListinoRepository listinoRepository;

    String IdList = "1";
    String CodArt = "000034901";
    Double Prezzo = 1.00;

    @BeforeAll
    public void setup()
    {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        //Inserimento Dati Listino 100
        InsertDatiListino(IdList,"Listino Test 100",CodArt,Prezzo);
    }

    private void InsertDatiListino(String IdList, String Descrizione, String CodArt, double Prezzo)
    {

        Listini listinoTest = new Listini(IdList,Descrizione,"No");

        Set<DettListini> dettListini = new HashSet<>();
        DettListini dettListTest = new DettListini(CodArt, BigDecimal.valueOf(Prezzo), listinoTest);
        dettListini.add(dettListTest);

        listinoTest.setDettListini(dettListini);

        listinoRepository.save(listinoTest);
    }



    @Test
    @Order(1)
    public void testGetPrzCodArt() throws Exception
    {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/prezzi/" + CodArt)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value("0.9")) //<-- Prezzo con applicato lo sconto del 10%
                .andReturn();
    }

    @Test
    @Order(2)
    public void testDelPrezzo() throws Exception
    {
        String Url = String.format("/api/prezzi/elimina/%s/%s/",CodArt,IdList);

        mockMvc.perform(MockMvcRequestBuilders.delete(Url)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200 OK"))
                .andExpect(jsonPath("$.message").value("Eliminazione Prezzo Eseguita Con Successo"))
                .andDo(print());
    }

    @AfterAll
    public void ClearData()
    {
        Optional<Listini> listinoTest = listinoRepository.findById(IdList);
        listinoTest.ifPresent(listini -> listinoRepository.delete(listini));

    }
}

