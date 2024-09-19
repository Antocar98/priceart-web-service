package com.xantrix.webapp.services;

import java.util.Optional;
import com.xantrix.webapp.entities.Listini;

public interface ListinoService 
{
	public Optional<Listini> SelById(String Id);
	
	public void InsListino(Listini listino);

	public void DelListino(Listini listino);
}
