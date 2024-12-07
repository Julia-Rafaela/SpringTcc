package br.edu.fateczl.SpringTcc.persistence;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import br.edu.fateczl.SpringTcc.model.Categoria;
import br.edu.fateczl.SpringTcc.model.Estoque;
import br.edu.fateczl.SpringTcc.model.Produto;

@Repository
public class EstoqueDao implements IEstoqueDao {

	private GenericDao gDao;

	public EstoqueDao(GenericDao gDao) {
		this.gDao = gDao;
	}

	@Override
	public void atualizar(Estoque e) throws SQLException, ClassNotFoundException {
		try (Connection c = gDao.getConnection();
				PreparedStatement ps = c.prepareStatement(
						"UPDATE Estoque SET quantidade = ?, quantidadeMin = ?, quantidadeTot = ?, categoria = ? WHERE produto = ?")) {
			ps.setInt(1, e.getQuantidade());
			ps.setInt(2, e.getQuantidadeMin());
			ps.setInt(3, e.getQuantidadeTot());
			ps.setString(4, e.getCategoria().getCategoria());
			ps.setInt(5, e.getProduto().getCodigo());

			ps.executeUpdate();
		}
	}

	public List<Estoque> consultar(Integer codigoProduto, String categoriaProduto)
			throws SQLException, ClassNotFoundException {
		List<Estoque> estoques = new ArrayList<>();
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		System.out.println(categoriaProduto);
		System.out.println(codigoProduto);
		try {
			// Obter conexão
			c = gDao.getConnection();

			// Definir a consulta SQL e parâmetros com base nos valores fornecidos
			String sql;
			if (codigoProduto != 0) {
				// Se o código do produto for fornecido, use a função correspondente
				sql = "SELECT * FROM dbo.fn_produtosPorCodigo(?)";
				ps = c.prepareStatement(sql);
				ps.setInt(1, codigoProduto);
			} else if (categoriaProduto != null) {
				// Se a categoria for fornecida, use a função correspondente
				sql = "SELECT * FROM dbo.fn_produtosPorCategoria(?)";
				ps = c.prepareStatement(sql);
				System.out.println(categoriaProduto);
				ps.setString(1, categoriaProduto);
			} else {
				throw new IllegalArgumentException("Nenhum critério de pesquisa fornecido.");
			}

			// Executar a consulta
			rs = ps.executeQuery();

			while (rs.next()) {
				// Criar o objeto Produto
				Produto produto = new Produto();
				produto.setCodigo(rs.getInt("CodigoProduto"));
				produto.setNome(rs.getString("NomeProduto"));
				produto.setMarca(rs.getString("MarcaProduto"));

				// Criar o objeto Categoria
				Categoria categoria = new Categoria();
				categoria.setCategoria(rs.getString("CategoriaProduto"));

				// Criar o objeto Estoque
				Estoque estoque = new Estoque();
				estoque.setQuantidade(rs.getInt("Quantidade"));

				estoque.setProduto(produto);
				estoque.setCategoria(categoria);

				// Adicionar o estoque à lista
				estoques.add(estoque);
			}
		} finally {
			// Fechar ResultSet, PreparedStatement e Connection
			if (rs != null) {
				rs.close();
			}
			if (ps != null) {
				ps.close();
			}
			if (c != null) {
				c.close();
			}
		}

		return estoques;
	}

	@Override
	public List<Estoque> listarProdutosE() throws SQLException, ClassNotFoundException {
		List<Estoque> estoques = new ArrayList<>();
		Connection c = gDao.getConnection();

		String sql = "SELECT * FROM dbo.fn_produtosE()";
		PreparedStatement ps = c.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();

		// Processar o ResultSet
		while (rs.next()) {
			Produto produto = new Produto();
			produto.setCodigo(rs.getInt("CodigoProduto"));
			produto.setNome(rs.getString("NomeProduto"));
			produto.setMarca(rs.getString("MarcaProduto"));

			// Corrigir a obtenção da categoria
			Categoria categoria = new Categoria();
			categoria.setCategoria(rs.getString("CategoriaProduto"));

			Estoque estoque = new Estoque();
			estoque.setQuantidade(rs.getInt("quantidade"));
			estoque.setProduto(produto);
			estoque.setCategoria(categoria);

			estoques.add(estoque);
		}

		// Fechar recursos
		rs.close();
		ps.close();
		c.close();

		return estoques;
	}

	@Override
	public String iudEstoque(String op, Estoque e) throws SQLException, ClassNotFoundException {
		try (Connection c = gDao.getConnection();
	             CallableStatement cs = c.prepareCall("{CALL GerenciarEstoque(?, ?, ?, ?, ?)}")) {

	           
	            cs.setString(1, op);
	            cs.setInt(2, e.getProduto().getCodigo()); 
	            cs.setInt(3, e.getQuantidade()); 
	            cs.setInt(4, e.getQuantidadeMin()); 
	            
	  
	            cs.registerOutParameter(5, Types.VARCHAR);

	            cs.execute();

	            return cs.getString(5);
	        }
	    
	}

	@Override
	public int calcularQuantidadeTotal() throws SQLException, ClassNotFoundException {
		int total = 0;
		Connection c = gDao.getConnection();

		String sql = "SELECT dbo.calcular_quantidade_total() AS totalQuantidade";
		PreparedStatement ps = c.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();

		if (rs.next()) {
			total = rs.getInt("totalQuantidade");
		}
		
		rs.close();
		ps.close();
		c.close();

		return total;
	}

	public int getQuantidadeTot() throws SQLException, ClassNotFoundException {
		String sql = "SELECT SUM(quantidade) FROM Estoque";
		
		int total = 0;
		try (Connection conn = this.gDao.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {
			if (rs.next()) {
				total = rs.getInt(1);
			}
		}
		return total;
	}

}