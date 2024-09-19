package com.xantrix.webapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xantrix.webapp.entities.Listini;

public interface ListinoRepository  extends JpaRepository<Listini, String>
{
}