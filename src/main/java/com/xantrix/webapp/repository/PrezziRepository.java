package com.xantrix.webapp.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.xantrix.webapp.entities.DettListini;

public interface PrezziRepository extends JpaRepository<DettListini, Long>
{
    @Modifying
    @Query(value = "DELETE FROM dettlistini WHERE CodArt = :codart AND IdList = :idlist", nativeQuery = true)
    void delRowDettList(@Param("codart") String CodArt, @Param("idlist") String IdList);

    //Query JPQL
    @Query(value = "SELECT b FROM Listini a JOIN a.dettListini b WHERE b.codArt = :codart AND a.id = :idlist")
    DettListini selByCodArtAndList(@Param("codart") String CodArt, @Param("idlist") String Listino);
}
