package br.edu.fateczl.SpringTcc.model;

import java.text.DecimalFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Produto {

	int codigo;
	String nome;
	String dataValidade;
	String status;
	String marca;
	String descricao;
	Categoria categoria;
	GerarLucro preco;

	@Override
	public String toString() {
		return nome;
	}

	public String getPrecoFormatado() {
		DecimalFormat df = new DecimalFormat("#0.00");
		return df.format(this.preco.getPrecoVenda());
	}

}
