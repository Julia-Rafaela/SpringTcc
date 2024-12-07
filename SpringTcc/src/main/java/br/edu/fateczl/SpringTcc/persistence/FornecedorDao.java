package br.edu.fateczl.SpringTcc.persistence;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import br.edu.fateczl.SpringTcc.model.Categoria;
import br.edu.fateczl.SpringTcc.model.Estado;
import br.edu.fateczl.SpringTcc.model.Fornecedor;
@Repository
public class FornecedorDao {
	 private GenericDao gDao;

	    public FornecedorDao(GenericDao gDao) {
	        this.gDao = gDao;
	    }
/*
	    public String iudFornecedor(String acao, Fornecedor f) throws SQLException, ClassNotFoundException {
	        Connection conn = gDao.getConnection();
	        String sql = "{CALL iudFornecedor(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
	        CallableStatement cs = conn.prepareCall(sql);
	        cs.setString(1, acao);
	        cs.setInt(2, f.getCodigo());
	        cs.setString(3, f.getNome());
	        cs.setString(4, f.getCategoria().getCategoria());
	        cs.setString(5, f.getEndereco());
	        cs.setString(6, f.getNumero());
	        cs.setString(7, f.getBairro());
	        cs.setString(8, f.getCidade());
	        cs.setString(9, f.getEstado().getSigla()); 
	        cs.setString(10, f.getTelefone());
	        cs.setString(11, f.getEmail());

	        cs.execute();
	        cs.close();
	        conn.close();
	        return "Operação realizada com sucesso!";
	    }

	    public Fornecedor consultar(Fornecedor f) throws SQLException, ClassNotFoundException {
	        Connection conn = gDao.getConnection();
	        String sql = "SELECT Nome, Categoria, Endereco, Numero, Bairro, Cidade, Estado, Telefone, Email FROM Fornecedor WHERE Codigo = ?";
	        PreparedStatement ps = conn.prepareStatement(sql);
	        ps.setInt(1, f.getCodigo());
	        ResultSet rs = ps.executeQuery();
	        if (rs.next()) {
	            f.setNome(rs.getString("Nome"));
	            Categoria c = new Categoria();
	            c.setId(rs.getInt("Categoria"));
	            f.setCategoria(c);
	            f.setEndereco(rs.getString("Endereco"));
	            f.setNumero(rs.getString("Numero"));
	            f.setBairro(rs.getString("Bairro"));
	            f.setCidade(rs.getString("Cidade"));
	            Estado e = new Estado();
	            e.setId(rs.getInt("Estado"));
	            f.setEstado(e);
	            f.setTelefone(rs.getString("Telefone"));
	            f.setEmail(rs.getString("Email"));
	        }
	        rs.close();
	        ps.close();
	        conn.close();
	        return f;
	    }

	    public List<Fornecedor> listar() throws SQLException, ClassNotFoundException {
	        List<Fornecedor> fornecedores = new ArrayList<>();
	        Connection conn = gDao.getConnection();
	        String sql = "SELECT Codigo, Nome, Categoria, Endereco, Numero, Bairro, Cidade, Estado, Telefone, Email FROM Fornecedor";
	        PreparedStatement ps = conn.prepareStatement(sql);
	        ResultSet rs = ps.executeQuery();
	        while (rs.next()) {
	            Fornecedor f = new Fornecedor();
	            f.setCodigo(rs.getInt("Codigo"));
	            f.setNome(rs.getString("Nome"));
	            Categoria c = new Categoria();
	            c.setCategoria(rs.getString("Categoria"));
	            f.setCategoria(c);
	            f.setEndereco(rs.getString("Endereco"));
	            f.setNumero(rs.getString("Numero"));
	            f.setBairro(rs.getString("Bairro"));
	            f.setCidade(rs.getString("Cidade"));
	            Estado e = new Estado();
	            e.setSigla(rs.getString("Estado"));
	            f.setEstado(e);
	            f.setTelefone(rs.getString("Telefone"));
	            f.setEmail(rs.getString("Email"));
	            fornecedores.add(f);
	        }
	        rs.close();
	        ps.close();
	        conn.close();
	        return fornecedores;
	    }
	*/
}
