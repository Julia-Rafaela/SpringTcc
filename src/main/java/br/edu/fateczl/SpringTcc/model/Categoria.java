package br.edu.fateczl.SpringTcc.model;



import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Categoria {

	String categoria;

	@Override
	public String toString() {
		return categoria;
	}
}
