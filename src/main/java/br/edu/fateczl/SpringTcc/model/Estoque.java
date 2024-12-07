package br.edu.fateczl.SpringTcc.model;



import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Estoque {
	
	Produto produto;
	Categoria categoria;
	int quantidade;
	int quantidadeMin;
	int quantidadeTot;
	
	@Override
	public String toString() {
		return String.valueOf(quantidadeTot);
	}

}
