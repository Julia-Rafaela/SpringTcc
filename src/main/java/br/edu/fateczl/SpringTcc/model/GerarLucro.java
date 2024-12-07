package br.edu.fateczl.SpringTcc.model;



import java.text.DecimalFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GerarLucro {
	
	Produto produto;
	double precoCompra;
	int margemLucro;
	double precoVenda;

	
	@Override
	public String toString() {
		return String.valueOf(precoVenda);
	}
	 public String getPrecoVendaFormatado() {
	        DecimalFormat df = new DecimalFormat("#0.00");
	        return df.format(precoVenda);
	    }
}

