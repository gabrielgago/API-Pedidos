package com.gabriel.pedidos.domain.enums;

import java.util.Optional;

public enum TipoCliente {

	PESSOAFISICA(1, "Pessoa Fisica"), PESSOAJURIDICA(2, "Pessoa Jurídica");

	private int cod;
	private String descricao;

	TipoCliente(int cod, String descricao) {
		this.cod = cod;
		this.descricao = descricao;
	}

	public int getCod() {
		return cod;
	}

	public String getDescricao() {
		return descricao;
	}

	public static Optional<TipoCliente> toEnum(Integer cod) {
		if (cod == null) {
			return Optional.empty();
		}
		for (TipoCliente x : TipoCliente.values()) {
			if (x.getCod() == cod)
				return Optional.of(x);
		}

		return Optional.empty();
	}

}
