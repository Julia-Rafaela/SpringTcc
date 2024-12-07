package br.edu.fateczl.SpringTcc.persistence;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import br.edu.fateczl.SpringTcc.model.Categoria;

@Repository
public class CategoriaDao {

	private GenericDao gDao;

	public CategoriaDao(GenericDao gDao) {
		this.gDao = gDao;
	}

	public void inserir(Categoria c) throws SQLException, ClassNotFoundException {
		Connection conn = gDao.getConnection();
		String sql = "INSERT INTO Categoria (nome) VALUES (?)";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, c.getCategoria());
		ps.execute();
		ps.close();
		conn.close();
	}

	public Categoria consultar(Categoria c) throws SQLException, ClassNotFoundException {
		Connection conn = gDao.getConnection();
		String sql = "SELECT nome FROM Categoria WHERE nome = ?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, c.getCategoria());
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			c.setCategoria(rs.getString("categoria"));
		}
		rs.close();
		ps.close();
		conn.close();
		return c;
	}

	public void excluir(Categoria c) throws SQLException, ClassNotFoundException {
		Connection conn = gDao.getConnection();
		String sql = "DELETE FROM Categoria WHERE nome = ?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, c.getCategoria());
		ps.execute();
		ps.close();
		conn.close();
	}

	public List<Categoria> listar() throws SQLException, ClassNotFoundException {
		List<Categoria> categorias = new ArrayList<>();
		Connection conn = gDao.getConnection();
		String sql = "SELECT categoria FROM Categoria";
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			Categoria c = new Categoria();
			c.setCategoria(rs.getString("categoria"));
			categorias.add(c);
		}
		rs.close();
		ps.close();
		conn.close();
		return categorias;
	}
}
