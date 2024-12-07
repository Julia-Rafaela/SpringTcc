package br.edu.fateczl.SpringTcc.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Item {
	
	Produto produto;
	Categoria categoria;
	int quantidade;
	Estoque estoque;

	
	@Override
	public String toString() {
		return String.valueOf(quantidade);
	}
}
