package br.edu.fateczl.SpringTcc.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrevisaoDemanda {
	
	Produto produto;
	int mes;
	String nomeMes;
	int quantidadeVendas;
	int totalVendas;
	int demanda;
	int ano;
	
	
	@Override
	public String toString() {
		return String.valueOf(demanda);
	}

}
