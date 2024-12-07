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

import br.edu.fateczl.SpringTcc.model.PrevisaoDemanda;
import br.edu.fateczl.SpringTcc.model.Produto;

@Repository
public class PrevisaoDemandaDao {

	private GenericDao gDao;

	public PrevisaoDemandaDao(GenericDao gDao) {
		this.gDao = gDao;
	}

	public List<PrevisaoDemanda> listarVendasUltimosTresMeses(int mesAtual, int codigoProduto)
			throws SQLException, ClassNotFoundException {
		List<PrevisaoDemanda> vendas = new ArrayList<>();

		String sql = "SELECT * FROM dbo.ObterVendasUltimosTresMesesComNome(?, ?)";

		try (Connection c = gDao.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

			ps.setInt(1, mesAtual);
			ps.setInt(2, codigoProduto);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					PrevisaoDemanda venda = new PrevisaoDemanda();
					Produto p = new Produto();
					p.setCodigo(rs.getInt("produto"));
					p.setNome(rs.getString("nome"));

					venda.setProduto(p);
					venda.setMes(rs.getInt("mes"));
					venda.setNomeMes(rs.getString("nomeMes"));
					venda.setQuantidadeVendas(rs.getInt("quantidadeVendas"));
					venda.setTotalVendas(rs.getInt("totalVendas"));
					venda.setDemanda(rs.getInt("demanda"));

					vendas.add(venda);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}

		return vendas;
	}

	public String consultarSomaVendasUltimosTresMesesProduto(PrevisaoDemanda d)
			throws SQLException, ClassNotFoundException {
		String mensagemSaida;

		try (Connection c = gDao.getConnection();
				CallableStatement cs = c.prepareCall("{CALL ConsultarSomaVendasUltimosTresMesesProduto(?, ?, ?)}")) {

			cs.setInt(1, d.getMes());
			cs.setInt(2, d.getProduto().getCodigo());
			cs.registerOutParameter(3, Types.VARCHAR);

			cs.execute();

			mensagemSaida = cs.getString(3);
		}

		return mensagemSaida;
	}

}
