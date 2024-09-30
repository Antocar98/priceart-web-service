package com.xantrix.webapp.services;

import lombok.extern.java.Log;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xantrix.webapp.entities.DettListini;
import com.xantrix.webapp.repository.PrezziRepository;

import java.util.concurrent.ThreadLocalRandom;

@Service
@Transactional(readOnly = true)
@Log
public class PrezziServiceImpl implements PrezziService
{
	@Autowired
	PrezziRepository prezziRepository;

	@Override
	public DettListini SelPrezzo(String CodArt, String Listino)
	{
		return prezziRepository.selByCodArtAndList(CodArt, Listino);
	}
	
	@Override
	@Transactional
	public void DelPrezzo(String CodArt, String IdList) 
	{
		prezziRepository.delRowDettList(CodArt, IdList);
	}

}
