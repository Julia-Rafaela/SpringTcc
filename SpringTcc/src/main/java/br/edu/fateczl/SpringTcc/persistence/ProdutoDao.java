package br.edu.fateczl.SpringTcc.persistence;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import br.edu.fateczl.SpringTcc.model.Categoria;
import br.edu.fateczl.SpringTcc.model.GerarLucro;
import br.edu.fateczl.SpringTcc.model.Produto;

@Repository
public class ProdutoDao implements IProdutoDao {

	private GenericDao gDao;

	public ProdutoDao(GenericDao gDao) {
		this.gDao = gDao;
	}

	@Override
	public void inserir(Produto p) throws SQLException, ClassNotFoundException {
		try (Connection c = gDao.getConnection();
				PreparedStatement ps = c.prepareStatement(
						"INSERT INTO Produto (codigo, nome, dataValidade, status, marca, descricao, categoria) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
			ps.setLong(1, p.getCodigo());
			ps.setString(2, p.getNome());
			ps.setString(3, p.getDataValidade());
			ps.setString(4, p.getStatus());
			ps.setString(5, p.getMarca());
			ps.setString(6, p.getDescricao());
			ps.setString(7, p.getCategoria().getCategoria());

			ps.execute();
		}
	}

	@Override
	public void atualizar(Produto p) throws SQLException, ClassNotFoundException {
		try (Connection c = gDao.getConnection();
				PreparedStatement ps = c.prepareStatement(
						"UPDATE Produto SET nome = ?, dataValidade = ?, status = ?, marca = ?, descricao = ?, categoria = ? WHERE codigo = ?")) {
			ps.setString(1, p.getNome());
			ps.setString(2, p.getDataValidade());
			ps.setString(3, p.getStatus());
			ps.setString(4, p.getMarca());
			ps.setString(5, p.getDescricao());
			ps.setString(6, p.getCategoria().getCategoria());
			ps.setLong(7, p.getCodigo());

			ps.executeUpdate();
		}
	}

	@Override
	public Produto consultar(Produto p) throws SQLException, ClassNotFoundException {
		try (Connection conn = gDao.getConnection();
				PreparedStatement ps = conn.prepareStatement(
						"SELECT p.codigo, p.nome, p.marca, p.descricao, p.dataValidade, p.status, c.categoria AS categoria FROM Produto p INNER JOIN Categoria c ON c.categoria = p.categoria WHERE p.codigo = ?")) {
			ps.setLong(1, p.getCodigo());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				p.setCodigo(rs.getInt("codigo"));
				p.setNome(rs.getString("nome"));
				p.setDescricao(rs.getString("descricao"));
				p.setDataValidade(rs.getString("dataValidade"));
				p.setStatus(rs.getString("status"));
				p.setMarca(rs.getString("marca"));

				Categoria categoria = new Categoria();
				categoria.setCategoria(rs.getString("categoria"));

				p.setCategoria(categoria);
			}
			rs.close();
		}
		return p;
	}

	@Override
	public List<Produto> listar() throws SQLException, ClassNotFoundException {
		List<Produto> produtos = new ArrayList<>();
		try (Connection c = gDao.getConnection();
				PreparedStatement ps = c.prepareStatement(
						"SELECT p.codigo, p.nome, p.descricao, p.dataValidade, p.status, p.marca, c.categoria AS categoria FROM Produto p INNER JOIN Categoria c ON c.categoria = p.categoria");
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				Produto p = new Produto();
				p.setCodigo(rs.getInt("codigo"));
				p.setNome(rs.getString("nome"));
				p.setDescricao(rs.getString("descricao"));
				p.setDataValidade(rs.getString("dataValidade"));
				p.setStatus(rs.getString("status"));
				p.setMarca(rs.getString("marca"));

				Categoria categoria = new Categoria();
				categoria.setCategoria(rs.getString("categoria"));

				p.setCategoria(categoria);

				produtos.add(p);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		return produtos;
	}


	@Override
	public List<Produto> listarComPrecoVenda() throws SQLException, ClassNotFoundException {
	    List<Produto> produtos = new ArrayList<>();

	    try (Connection c = gDao.getConnection();
	         PreparedStatement ps = c.prepareStatement("SELECT * FROM dbo.fn_produtos()");
	         ResultSet rs = ps.executeQuery()) {

	        while (rs.next()) {
	            Produto p = new Produto();
	            p.setCodigo(rs.getInt("CodigoProduto"));
	            p.setNome(rs.getString("NomeProduto"));
	            p.setMarca(rs.getString("MarcaProduto"));
	            p.setDataValidade(rs.getString("DataValidade"));
	            p.setStatus(rs.getString("StatusProduto"));
	            Categoria categoria = new Categoria();
	            categoria.setCategoria(rs.getString("CategoriaProduto"));
	            p.setCategoria(categoria);
	            p.setDescricao(rs.getString("DescricaoProduto"));

	            double precoVenda = rs.getDouble("PrecoVenda");
	            
	            
	            DecimalFormat df = new DecimalFormat("#0.00");
	            String precoFormatado = df.format(precoVenda);
	            
	            GerarLucro g = new GerarLucro();
	            g.setPrecoVenda(Double.parseDouble(precoFormatado.replace(",", ".")));
	            p.setPreco(g);

	            produtos.add(p);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw e;
	    }
	    return produtos;
	}




	@Override
	public String iudProduto(String op, Produto p) throws SQLException, ClassNotFoundException {
		try (Connection c = gDao.getConnection();
				CallableStatement cs = c.prepareCall("CALL GerenciarProduto(?,?,?,?,?,?,?,?,?)")) {

			cs.setString(1, op); 
			cs.setLong(2, p.getCodigo()); 
			cs.setString(3, p.getNome()); 
			cs.setString(4, p.getDataValidade()); 
			cs.setString(5, p.getStatus()); 
			cs.setString(6, p.getMarca()); 
			cs.setString(7, p.getDescricao()); 

			if (p.getCategoria() != null) {
			    cs.setString(8, p.getCategoria().getCategoria());
			} else {
			    cs.setNull(8, Types.VARCHAR); 
			}
		
			cs.registerOutParameter(9, Types.VARCHAR);

			cs.execute();

			return cs.getString(9);
			
		}
	}
}
