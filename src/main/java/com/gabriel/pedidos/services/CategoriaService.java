package com.gabriel.pedidos.services;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gabriel.pedidos.domain.Categoria;
import com.gabriel.pedidos.repositories.CategoriaRepository;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository repo;
	
	public Categoria buscar(Integer id) {
		return repo.findById(id).orElseThrow(() -> new NoSuchElementException("Categoria com este id não existe na base de dados."));
	}
	
}
