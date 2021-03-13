package com.gabriel.pedidos;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.gabriel.pedidos.domain.Categoria;
import com.gabriel.pedidos.domain.Cidade;
import com.gabriel.pedidos.domain.Cliente;
import com.gabriel.pedidos.domain.Endereco;
import com.gabriel.pedidos.domain.Estado;
import com.gabriel.pedidos.domain.Produto;
import com.gabriel.pedidos.domain.enums.TipoCliente;
import com.gabriel.pedidos.repositories.CategoriaRepository;
import com.gabriel.pedidos.repositories.CidadeRepository;
import com.gabriel.pedidos.repositories.ClienteRepository;
import com.gabriel.pedidos.repositories.EnderecoRepository;
import com.gabriel.pedidos.repositories.EstadoRepository;
import com.gabriel.pedidos.repositories.ProdutoRepository;

@SpringBootApplication
public class PedidosApplication implements CommandLineRunner {

	@Autowired
	private CategoriaRepository categoriaRepository;
	@Autowired
	private ProdutoRepository produtoRepository;
	@Autowired
	private CidadeRepository cidadeRepository;
	@Autowired
	private EstadoRepository estadoRepository;
	@Autowired
	private EnderecoRepository enderecoRepository;
	@Autowired
	private ClienteRepository clienteRepository;

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

		cat1.addProdutos(p1, p2, p3);
		cat2.addProdutos(p2);

		p1.addCategorias(cat1);
		p2.addCategorias(cat1, cat2);
		p3.addCategorias(cat1);

		categoriaRepository.saveAll(List.of(cat1, cat2));
		produtoRepository.saveAll(List.of(p1, p2, p3));

		Estado est1 = new Estado(null, "Minas Gerais");
		Estado est2 = new Estado(null, "São Paulo");

		Cidade c1 = new Cidade(null, "Uberlândia", est1);
		Cidade c2 = new Cidade(null, "São Paulo", est2);
		Cidade c3 = new Cidade(null, "Campinas", est2);

		est1.addCidades(c1);
		est2.addCidades(c2, c3);

		estadoRepository.saveAll(List.of(est1, est2));
		cidadeRepository.saveAll(List.of(c1, c2, c3));
		
		Cliente cli1 = new Cliente(null, "Maria Silva", "maria@gmail.com", "14555658521", TipoCliente.PESSOAFISICA);
		cli1.addTelefones("2133512322","2133584855");
		
		Endereco e1 = new Endereco(null, "Rua Flores", "300", "apto 203", "Jardim", "21511212", cli1, c1);
		Endereco e2 = new Endereco(null, "Avenida Matos", "105", "sala 800", "Centro", "21111518", cli1, c2);
		
		cli1.addEnderecos(e1, e2);
		
		clienteRepository.saveAll(List.of(cli1));
		enderecoRepository.saveAll(List.of(e1, e2));
	}

}
