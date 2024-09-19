package com.xantrix.webapp.exceptions;

public class NotFoundException  extends Exception
{

	private static final long serialVersionUID = -8729169303699924451L;

	private String messaggio = "Elemento Ricercato Non Trovato!";

	public NotFoundException()
	{
		super();
	}

	public NotFoundException(String messaggio)
	{
		super(messaggio);
		this.messaggio = messaggio;
	}

	public String getMessaggio()
	{
		return messaggio;
	}

	public void setMessaggio(String messaggio)
	{
		this.messaggio = messaggio;
	}

}