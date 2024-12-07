package br.edu.fateczl.SpringTcc.persistence;

import java.sql.SQLException;
import java.util.List;

import br.edu.fateczl.SpringTcc.model.Estoque;

public interface IEstoqueDao {


	List<Estoque> listarProdutosE() throws SQLException, ClassNotFoundException;

	List<Estoque> consultar(Integer codigoProduto, String categoriaProduto) throws SQLException, ClassNotFoundException;

	void atualizar(Estoque e) throws SQLException, ClassNotFoundException;

	String iudEstoque(String op, Estoque e) throws SQLException, ClassNotFoundException;

	int calcularQuantidadeTotal() throws SQLException, ClassNotFoundException;

}
