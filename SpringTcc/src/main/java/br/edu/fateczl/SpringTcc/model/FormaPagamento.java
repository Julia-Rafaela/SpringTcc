package br.edu.fateczl.SpringTcc.model;



import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormaPagamento {

	String formaPagamento;

	@Override
	public String toString() {
		return formaPagamento;
	}
}
