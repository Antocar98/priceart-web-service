package com.xantrix.webapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xantrix.webapp.appconf.AppConfig;
import com.xantrix.webapp.entities.DettListini;
import com.xantrix.webapp.services.PrezziService;
import lombok.extern.java.Log;

import javax.validation.constraints.Min;

@RestController
@RequestMapping("/api/prezzi")
@Log
public class PrezziController
{
	@Autowired
	private PrezziService prezziService;
	
	@Autowired
	private AppConfig Config;
	
	// ------------------- SELECT PREZZO CODART ------------------------------------
	@RefreshScope // identidica i metodi che subiranno modifiche a causa del cambio dei file di configurazione
	@GetMapping(value = {"/{codart}/{idlist}", "/{codart}"})
	public @Min(value = (long) 0.01, message = "{Min.DettListini.prezzo.Validation}") BigDecimal getPriceCodArt(@PathVariable("codart") String CodArt, @PathVariable("idlist") Optional<String> optIdList)
	{
		// Inizializziamo retVal come BigDecimal a 0
		BigDecimal retVal = BigDecimal.valueOf(0);

		// Otteniamo l'ID del listino, se presente, altrimenti usiamo il valore di default
		String IdList = optIdList.orElse(Config.getListino());
		log.info("Listino di Riferimento: " + IdList);

		// Otteniamo il prezzo dal servizio
		DettListini prezzo = prezziService.SelPrezzo(CodArt, IdList);

		if (prezzo != null) {
			log.info("Prezzo Articolo: " + prezzo.getPrezzo());
			BigDecimal sconto = BigDecimal.valueOf(Config.getSconto());

			if (sconto.compareTo(BigDecimal.ZERO) > 0) {
				log.info("Attivato sconto: " + sconto + "%");
				// Calcoliamo il prezzo con lo sconto
				BigDecimal percentualeSconto = BigDecimal.ONE.subtract(sconto.divide(BigDecimal.valueOf(100)));
				retVal = prezzo.getPrezzo().multiply(percentualeSconto).setScale(2, RoundingMode.HALF_UP);
			} else {
				retVal = prezzo.getPrezzo();
			}

		} else {
			log.warning("Prezzo Articolo Assente!!");
		}

		return retVal;
	}
	
	// ------------------- DELETE PREZZO LISTINO ------------------------------------
	@RequestMapping(value = "/elimina/{codart}/{idlist}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deletePrice(@PathVariable("codart") String CodArt, 
			@PathVariable("idlist") String IdList)
	{
		log.info(String.format("Eliminazione prezzo listino %s dell'articolo %s",IdList,CodArt));

		HttpHeaders headers = new HttpHeaders();
		ObjectMapper mapper = new ObjectMapper();

		headers.setContentType(MediaType.APPLICATION_JSON);

		ObjectNode responseNode = mapper.createObjectNode();

		prezziService.DelPrezzo(CodArt, IdList);

		responseNode.put("code", HttpStatus.OK.toString());
		responseNode.put("message", "Eliminazione Prezzo Eseguita Con Successo");
		
		log.info("Eliminazione Prezzo Eseguita Con Successo");

		return new ResponseEntity<>(responseNode, headers, HttpStatus.OK);
	}
	

}
