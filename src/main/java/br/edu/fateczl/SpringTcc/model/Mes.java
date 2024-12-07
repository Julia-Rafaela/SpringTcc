package br.edu.fateczl.SpringTcc.model;

import lombok.Getter;
import lombok.Setter;

public class Mes {

	
    int id;
	String nomeMes;
	
	@Override
	public String toString() {
		return nomeMes ;
	}
}

