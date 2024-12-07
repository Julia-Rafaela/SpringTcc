package br.edu.fateczl.SpringTcc.persistence;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DecimalFormat;
import org.springframework.stereotype.Repository;

import br.edu.fateczl.SpringTcc.model.GerarLucro;

@Repository
public class GerarLucroDao {

	private GenericDao gDao;

	public GerarLucroDao(GenericDao gDao) {
		this.gDao = gDao;
	}

	public String inserirGerarLucro(GerarLucro lucro) throws SQLException, ClassNotFoundException {
		Connection c = gDao.getConnection();
		String sql = "{CALL GerenciarGerarLucro(?, ?, ?, ?, ?)}";
		CallableStatement cs = c.prepareCall(sql);

		
		cs.setInt(1, lucro.getProduto().getCodigo());
		cs.setDouble(2, lucro.getPrecoCompra()); 
		cs.setInt(3, lucro.getMargemLucro()); 
		cs.setDouble(4, lucro.getPrecoVenda()); 
		cs.registerOutParameter(5, Types.VARCHAR); 

		cs.execute();
		String saida = cs.getString(5);

		cs.close();
		c.close();

		return saida;
	}

	
	public GerarLucro calcularPrecoVenda(double precoCompra, int margemLucro)
	        throws SQLException, ClassNotFoundException {
	    Connection c = gDao.getConnection();
	    String sql = "{CALL CalcularPrecoVenda(?, ?, ?, ?)}";
	    CallableStatement cs = c.prepareCall(sql);

	    cs.setDouble(1, precoCompra);
	    cs.setInt(2, margemLucro);
	    cs.registerOutParameter(3, Types.DECIMAL); 
	    cs.registerOutParameter(4, Types.VARCHAR); 

	    cs.execute();
	    double precoVenda = cs.getDouble(3);
	    String saida = cs.getString(4);

	    cs.close();
	    c.close();
	    
	    DecimalFormat df = new DecimalFormat("#.00");
	    String precoVendaFormatado = df.format(precoVenda);
	    
	    GerarLucro lucro = new GerarLucro();
	    lucro.setPrecoCompra(precoCompra);
	    lucro.setMargemLucro(margemLucro);
	    lucro.setPrecoVenda(Double.parseDouble(precoVendaFormatado.replace(",", "."))); // Converter para double

	    return lucro;
	}
}

