package br.edu.fateczl.SpringTcc.model;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Estado {
	
    int id;

	String sigla;
	
	@Override
	public String toString() {
		return sigla ;
	}
}
