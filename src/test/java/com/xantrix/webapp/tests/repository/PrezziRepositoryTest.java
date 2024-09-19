package com.xantrix.webapp.repository;

import static org.assertj.core.api.Assertions.assertThat;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import javax.transaction.Transactional;
import com.xantrix.webapp.entities.DettListini;
import com.xantrix.webapp.entities.Listini;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties= {"profilo = list100", "seq= 1","ramo= main"})
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
public class PrezziRepositoryTest
{
    @Autowired
    private PrezziRepository prezziRepository;

    @Autowired
    private ListinoRepository listinoRepository;

    String IdList = "1";
    String CodArt = "098049301";
    BigDecimal Prezzo = new BigDecimal(3.27);

    @Test
    @Order(1)
    public void testInsListino()
    {
        Listini listinoTest = new Listini(IdList,"Listino Test 100","No");

        Set<DettListini> dettListini = new HashSet<>();
        DettListini dettListTest = new DettListini(CodArt,Prezzo, listinoTest);
        dettListini.add(dettListTest);

        listinoTest.setDettListini(dettListini);

        listinoRepository.save(listinoTest);

        assertThat(listinoRepository
                .findById(IdList))
                .isNotEmpty();

    }

    @Test
    @Order(2)
    public void testfindByCodArtAndIdList1()
    {
        BigDecimal price = prezziRepository.selByCodArtAndList(CodArt, IdList).getPrezzo();

        assertThat(price == Prezzo);
    }

    @Test
    @Transactional
    @Order(3)
    public void testDeletePrezzo()
    {

        prezziRepository.delRowDettList(CodArt, IdList);

        assertThat(prezziRepository
                .selByCodArtAndList(CodArt, IdList))
                .isNull();
    }

    @Test
    @Order(4)
    public void testDeleteListino()
    {
        Optional<Listini> listinoTest = listinoRepository.findById(IdList);

        listinoRepository.delete(listinoTest.get());

        assertThat(prezziRepository
                .selByCodArtAndList(CodArt, IdList))
                .isNull();
    }


}