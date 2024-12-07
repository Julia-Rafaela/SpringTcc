package br.edu.fateczl.SpringTcc.model;

import java.text.DecimalFormat;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Venda {

	int codigo;
	String data;
	FormaPagamento formaPagamento;
	double valorTotal;
	double subValor;
	Item item;
	int quantidade;
	GerarLucro gerarLucro;
	Produto produto;
	List<Item> itens;

	@Override
	public String toString() {
		return String.valueOf(valorTotal);
	}

	public String getSubValorFormatado() {
		DecimalFormat df = new DecimalFormat("#0.00");
		return df.format(subValor);
	}

	public String getPrecoVendaFormatado() {
		return gerarLucro != null ? gerarLucro.getPrecoVendaFormatado() : "";
	}

	public String getValorTotalFormatado() {
		DecimalFormat df = new DecimalFormat("#0.00");
		return df.format(valorTotal);
	}
}
