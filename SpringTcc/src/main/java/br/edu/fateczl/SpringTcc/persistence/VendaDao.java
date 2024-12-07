package br.edu.fateczl.SpringTcc.persistence;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import br.edu.fateczl.SpringTcc.model.FormaPagamento;
import br.edu.fateczl.SpringTcc.model.GerarLucro;
import br.edu.fateczl.SpringTcc.model.Item;
import br.edu.fateczl.SpringTcc.model.Produto;
import br.edu.fateczl.SpringTcc.model.Venda;

@Repository
public class VendaDao {

	private GenericDao gDao;

	public VendaDao(GenericDao gDao) {
		this.gDao = gDao;
	}

	public List<Venda> consultarVenda(int codigoVenda, String dataVenda) throws SQLException, ClassNotFoundException {
		List<Venda> vendas = new ArrayList<>();
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		System.out.println(codigoVenda);
		System.out.println(dataVenda);

		try {
			
			c = gDao.getConnection();

			
			String sql;

			

			if (codigoVenda != 0) {
				
				sql = "SELECT * FROM dbo.fn_vendasPorCodigo(?)";
				ps = c.prepareStatement(sql);
				ps.setInt(1, codigoVenda);
			} else if (dataVenda != null && !dataVenda.isEmpty()) {
				
				sql = "SELECT * FROM dbo.fn_vendasPorData(?)";
				ps = c.prepareStatement(sql);
				ps.setString(1, dataVenda);
			} else {
				throw new IllegalArgumentException("Nenhum critério de pesquisa fornecido.");
			}

			
			rs = ps.executeQuery();

			while (rs.next()) {
				Venda detalhe = new Venda();
				detalhe.setCodigo(rs.getInt("CodigoVenda"));
				detalhe.setData(rs.getString("DataVenda"));
				detalhe.setValorTotal(rs.getDouble("ValorTotal"));

				FormaPagamento fp = new FormaPagamento();
				fp.setFormaPagamento(rs.getString("PagamentoVenda"));
				detalhe.setFormaPagamento(fp);

				Item i = new Item();
				i.setQuantidade(rs.getInt("Quantidade"));
				detalhe.setItem(i);

				Produto p = new Produto();
				p.setNome(rs.getString("Produto"));
				detalhe.setProduto(p);

				GerarLucro g = new GerarLucro();
				g.setPrecoVenda(rs.getDouble("PrecoVenda"));
				detalhe.setGerarLucro(g);
				detalhe.setSubValor(rs.getDouble("SubValor"));

				vendas.add(detalhe);
			}
		} finally {
			
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

		return vendas;
	}

	public List<Venda> listarVendas() throws SQLException, ClassNotFoundException {
		List<Venda> vendas = new ArrayList<>();

		String sql = "SELECT * FROM dbo.fn_vendas()";

		try (Connection conn = gDao.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				Venda detalhe = new Venda();
				detalhe.setCodigo(rs.getInt("CodigoVenda"));
				detalhe.setData(rs.getString("DataVenda"));
				detalhe.setValorTotal(rs.getDouble("ValorTotal"));

				FormaPagamento fp = new FormaPagamento();
				fp.setFormaPagamento(rs.getString("PagamentoVenda"));
				detalhe.setFormaPagamento(fp);

				Item i = new Item();
				i.setQuantidade(rs.getInt("Quantidade"));
				detalhe.setItem(i);

				Produto p = new Produto();
				p.setNome(rs.getString("Produto"));
				detalhe.setProduto(p);

				GerarLucro g = new GerarLucro();
				detalhe.setGerarLucro(g);
				g.setPrecoVenda(rs.getDouble("PrecoVenda"));
				detalhe.setSubValor(rs.getDouble("SubValor"));

				vendas.add(detalhe);
			}
		}

		return vendas;
	}

	public List<Venda> listarItens() throws SQLException, ClassNotFoundException {
	    List<Venda> itens = new ArrayList<>();
	    String query = "SELECT * FROM dbo.fn_Itens()";

	    try (Connection conn = gDao.getConnection();
	         Statement stmt = conn.createStatement();
	         ResultSet rs = stmt.executeQuery(query)) {

	        while (rs.next()) {
	            Item item = new Item();
	            Venda v = new Venda();

	            Produto p = new Produto();
	            p.setCodigo(rs.getInt("CodigoProduto"));
	            p.setNome(rs.getString("NomeProduto"));
	            p.setMarca(rs.getString("MarcaProduto"));

	            GerarLucro g = new GerarLucro();
	            g.setPrecoVenda(rs.getDouble("PrecoVenda"));
	            v.setGerarLucro(g);
	            v.setSubValor(rs.getDouble("subValor"));
	            v.setValorTotal(rs.getDouble("valorTotal"));

	            item.setQuantidade(rs.getInt("quantidade"));
	            v.setItem(item);
	            v.setProduto(p);

	            itens.add(v);
	        }
	    }

	    return itens;
	}



	public String iudVenda(String op, Venda v) throws SQLException, ClassNotFoundException {
	    try (Connection c = gDao.getConnection();
	         CallableStatement cs = c.prepareCall("{CALL InserirVenda(?,?,?,?,?,?,?,?)}")) {

	        cs.setString(1, op);
	        cs.setInt(2, v.getCodigo());
	        cs.setString(3, v.getData());
	        cs.setString(4, v.getFormaPagamento().getFormaPagamento());
	        cs.setDouble(5, v.getSubValor());
	        cs.setInt(6, v.getItem().getProduto().getCodigo());
	        cs.setInt(7, v.getItem().getQuantidade());
	        cs.setDouble(8, v.getValorTotal());

	        cs.registerOutParameter(8, Types.VARCHAR);

	        cs.execute();

	        return cs.getString(8);
	    }
	}


	public String excluirItem(String op, Venda v) throws SQLException, ClassNotFoundException {
		try (Connection c = gDao.getConnection();
				CallableStatement cs = c.prepareCall("{CALL ExcluirItemVenda(?,?,?)}")) {

			cs.setString(1, op);
			cs.setInt(2, v.getProduto().getCodigo());
			

			cs.registerOutParameter(3, Types.VARCHAR);

			cs.execute();

			return cs.getString(3);
		}
	}

	public double getSubValor(int codigoVenda) throws SQLException, ClassNotFoundException {
		String sql = "SELECT " + "    i.produto AS CodigoProduto, " + "    (g.precoVenda * i.quantidade) AS SubValor "
				+ "FROM Venda v " + "JOIN Itens i ON v.item = i.produto " 
				+ "JOIN GerarLucro g ON i.produto = g.produto " + "WHERE v.codigo = ?"; 

		double subValor = 0.0;
		try (Connection conn = this.gDao.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, codigoVenda); 
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					subValor += rs.getDouble("SubValor");
				}
			}
		}
		return subValor;
	}

	public double getValorTotal(int codigoVenda) throws SQLException, ClassNotFoundException {
		
		String sql = "SELECT valorTotal AS valorTotal " + "FROM Venda WHERE codigo= ?";

		double valorTotal = 0.0;
		try (Connection conn = this.gDao.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			
			stmt.setInt(1, codigoVenda);

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					valorTotal = rs.getDouble("valorTotal");
				}
			}
		}
		return valorTotal;
	}
}