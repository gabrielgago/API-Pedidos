package com.gabriel.pedidos.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gabriel.pedidos.domain.Categoria;
import com.gabriel.pedidos.repositories.CategoriaRepository;
import com.gabriel.pedidos.services.exceprions.ObjectNotFoundException;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository repo;

	public Categoria buscar(Integer id) {
		return repo.findById(id).orElseThrow(() -> new ObjectNotFoundException(
				"Categoria cnão encontrada! id: " + id + ", Tipo: " + Categoria.class.getName()));
	}

}
