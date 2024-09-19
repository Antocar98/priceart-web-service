package com.xantrix.webapp.services;

import com.xantrix.webapp.entities.DettListini;

public interface PrezziService
{
	public DettListini SelPrezzo(String CodArt, String Listino);
	
	public void DelPrezzo(String CodArt, String IdList);

}
