package com.gabriel.pedidos.domain.enums;

import java.util.Optional;

public enum EstadoPagamento {

	PENDENTE(1, "Pendente"), QUITADO(2, "Quitado"), CANCELADO(3, "Cancelado");

	private int cod;
	private String descricao;

	private EstadoPagamento(int cod, String descricao) {
		this.cod = cod;
		this.descricao = descricao;
	}

	public int getCod() {
		return cod;
	}

	public String getDescricao() {
		return descricao;
	}

	public static Optional<EstadoPagamento> toEnum(Integer cod) {
		if (cod == null) {
			return Optional.empty();
		}
		for (EstadoPagamento x : EstadoPagamento.values()) {
			if (x.getCod() == cod)
				return Optional.of(x);
		}

		return Optional.empty();
	}
		
}
