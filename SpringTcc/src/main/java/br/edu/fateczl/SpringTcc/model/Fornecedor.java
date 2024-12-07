package br.edu.fateczl.SpringTcc.model;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Fornecedor {
    
	 int Codigo;
	 String nome;
	 Categoria categoria;
	 String endereco;
	 String numero;
	 String bairro;
	 String cidade;
	 Estado Estado;
	 String telefone;
	 String email;
	 
	 @Override
	 public String toString() {
		return nome;
	}
}
