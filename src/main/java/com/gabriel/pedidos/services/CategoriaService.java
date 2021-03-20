package com.gabriel.pedidos.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.gabriel.pedidos.domain.Categoria;
import com.gabriel.pedidos.domain.dto.CategoriaDTO;
import com.gabriel.pedidos.repositories.CategoriaRepository;
import com.gabriel.pedidos.services.exceprions.ObjectNotFoundException;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository repo;

	public Categoria find(Integer id) {
		return repo.findById(id).orElseThrow(() -> new ObjectNotFoundException(
				"Categoria cnão encontrada! id: " + id + ", Tipo: " + Categoria.class.getName()));
	}

	public Categoria insert(Categoria categoria) {
		categoria.setId(null);
		return repo.save(categoria);
	}

	public Categoria update(Categoria categoria) {
		find(categoria.getId());
		return repo.save(categoria);
	}

	public List<CategoriaDTO> findAll() {
		return repo.findAll().stream().map(CategoriaDTO::instanceOf).collect(Collectors.toList());
	}

	public void delete(int categoriaId) {
		Categoria categoria = find(categoriaId);
		repo.delete(categoria);
	}
	
	public Page<Categoria> findPage(Integer page, Integer linePerPage, String orderBy, String direction){
		PageRequest pr = PageRequest.of(page, linePerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pr);
	}

}
