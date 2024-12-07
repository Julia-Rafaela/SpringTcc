package br.edu.fateczl.SpringTcc.persistence;

import java.sql.SQLException;

import java.util.List;

import br.edu.fateczl.SpringTcc.model.Produto;

public interface IProdutoDao {

	public String iudProduto(String acao, Produto p) throws SQLException, ClassNotFoundException;

	void inserir(Produto p) throws SQLException, ClassNotFoundException;

	void atualizar(Produto p) throws SQLException, ClassNotFoundException;

	Produto consultar(Produto p) throws SQLException, ClassNotFoundException;

	List<Produto> listar() throws SQLException, ClassNotFoundException;

	List<Produto> listarComPrecoVenda() throws SQLException, ClassNotFoundException;

}