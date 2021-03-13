package com.gabriel.pedidos;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.gabriel.pedidos.domain.Categoria;
import com.gabriel.pedidos.domain.Produto;
import com.gabriel.pedidos.repositories.CategoriaRepository;
import com.gabriel.pedidos.repositories.ProdutoRepository;

@SpringBootApplication
public class PedidosApplication implements CommandLineRunner{

	@Autowired
	private CategoriaRepository categoriaRepository;
	@Autowired
	private ProdutoRepository produtoRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(PedidosApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		Categoria cat1 = new Categoria(null, "Informática");
		Categoria cat2 = new Categoria(null, "Escritório");
		
		Produto p1 = new Produto(null, "Computador", BigDecimal.valueOf(2000.00));
		Produto p2 = new Produto(null, "Impressora", BigDecimal.valueOf(800.00));
		Produto p3 = new Produto(null, "Mouse", BigDecimal.valueOf(80.00));
		
		cat1.getProdutos().addAll(List.of(p1,p2,p3));
		cat2.getProdutos().addAll(List.of(p2));
		categoriaRepository.saveAll(List.of(cat1, cat2));
		
		p1.getCategorias().addAll(List.of(cat1));
		p2.getCategorias().addAll(List.of(cat1, cat2));
		p3.getCategorias().addAll(List.of(cat1));
		
		produtoRepository.saveAll(List.of(p1,p2,p3));
		
	}

}
