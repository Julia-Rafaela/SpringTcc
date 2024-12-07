package br.edu.fateczl.SpringTcc.persistence;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public class ConsultaDao {
	private GenericDao gDao;

	public ConsultaDao(GenericDao gDao) {
		this.gDao = gDao;
	}


	public List<Map<String, Object>> listar(int idMes) throws SQLException, ClassNotFoundException {
	    List<Map<String, Object>> vendidos = new ArrayList<>();
	    Connection c = gDao.getConnection();
	    String sql = "{CALL ProdutoMaisVendido(?, ?)}"; // Chamada da procedure

	    try (CallableStatement cs = c.prepareCall(sql)) {
	        cs.setInt(1, idMes); // Define o ID do mês
	        cs.registerOutParameter(2, Types.VARCHAR); // Para a saída da mensagem

	        // Executa a procedure
	        ResultSet rs = cs.executeQuery();
	        
	        while (rs.next()) {
	            Map<String, Object> vendaData = new HashMap<>();
	            vendaData.put("nome", rs.getString("nome"));
	            vendaData.put("quantidadeVendida", rs.getInt("quantidadeVendida"));

	            vendidos.add(vendaData);
	        }
	        
	        String mensagem = cs.getString(2); // Captura a mensagem de saída
	        System.out.println(mensagem); // Pode ser logada ou tratada conforme necessário
	    } catch (SQLException e) {

	    } finally {
	        c.close();
	    }
	    
	    return vendidos;
	}
}