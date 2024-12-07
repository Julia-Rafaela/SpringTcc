package br.edu.fateczl.SpringTcc.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProdutoMaisVendido {
	
	Mes mes;
	Produto produto;
	Venda venda;
	
	@Override
	public String toString() {
		return "ProdutoMaisVendido [mes=" + mes + ", produto=" + produto + ", venda=" + venda + "]";
	}

}
