package com.xantrix.webapp.services;

import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xantrix.webapp.entities.Listini;
import com.xantrix.webapp.repository.ListinoRepository;

@Service
@Transactional(readOnly = true)
public class ListinoServiceImpl implements ListinoService
{
	@Autowired
	ListinoRepository listinoRepository;

	@Override
	@Transactional
	public void InsListino(Listini listino)
	{
		listinoRepository.save(listino);
	}

	@Override
	@Transactional
	public void DelListino(Listini listino) 
	{
		listinoRepository.delete(listino);
	}

	@Override
	public Optional<Listini> SelById(String Id) 
	{
		return listinoRepository.findById(Id);
	}
	
	
}
