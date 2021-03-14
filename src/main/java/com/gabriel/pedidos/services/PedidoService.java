package com.gabriel.pedidos.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gabriel.pedidos.domain.Pedido;
import com.gabriel.pedidos.repositories.PedidoRepository;
import com.gabriel.pedidos.services.exceprions.ObjectNotFoundException;

@Service
public class PedidoService {
	@Autowired
	private PedidoRepository repo;

	public Pedido buscar(Integer id) {
		return repo.findById(id).orElseThrow(() -> new ObjectNotFoundException(
				"Pedido não encontrada! id: " + id + ", Tipo: " + Pedido.class.getName()));
	}
}
