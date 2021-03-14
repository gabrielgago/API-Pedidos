package com.gabriel.pedidos.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gabriel.pedidos.domain.Cliente;
import com.gabriel.pedidos.repositories.ClienteRepository;
import com.gabriel.pedidos.services.exceprions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repo;

	public Cliente buscar(Integer id) {
		return repo.findById(id).orElseThrow(() -> new ObjectNotFoundException(
				"Cliente não encontrada! id: " + id + ", Tipo: " + Cliente.class.getName()));
	}

}
