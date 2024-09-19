package com.xantrix.webapp.appconf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties("application")
@Getter
@Setter
public class AppConfig
{
	private String listino;
	private double sconto = 0.00;
	private int tipo;

}