package com.gabriel.pedidos.domain.dto;

import com.gabriel.pedidos.domain.Categoria;
import com.gabriel.pedidos.domain.converter.Converter;

public class CategoriaDTO implements Converter<Categoria, CategoriaDTO> {

	private Integer id;
	private String nome;

	private String produtosNomeAny;
//	private String produtosNomeAll;
////	@Only(index="0")
//	private String produtosNomeOnly;

	public CategoriaDTO() {
	}

	public static CategoriaDTO instanceOf(Categoria cat) {
		return new CategoriaDTO().from(cat);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getProdutosNomeAny() {
		return produtosNomeAny;
	}

	public void setProdutosNomeAny(String produtosNomeAny) {
		this.produtosNomeAny = produtosNomeAny;
	}

	@Override
	public String toString() {
		return "CategoriaDTO [id=" + id + ", nome=" + nome + ", produtosNomeAny=" + produtosNomeAny + "]";
	}

}
