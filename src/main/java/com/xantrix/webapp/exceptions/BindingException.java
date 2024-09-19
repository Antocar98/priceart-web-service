package com.xantrix.webapp.exceptions;

public class BindingException extends Exception
{

	private static final long serialVersionUID = -1646083143194195402L;

	private String messaggio;

	public BindingException()
	{
		super();
	}

	public BindingException(String messaggio)
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