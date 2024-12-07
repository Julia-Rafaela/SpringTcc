package br.edu.fateczl.SpringTcc.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.stereotype.Repository;

@Repository
public class GenericDao {
	
	private Connection c;
	
	public Connection getConnection() throws ClassNotFoundException, SQLException {	
		String hostName= "localhost";
		String dbName = "CrudTcc";
		String user = "sa";
		String senha = "Lab.redes2024";
		Class.forName("net.sourceforge.jtds.jdbc.Driver");//
		c = DriverManager.getConnection(String.format(
				"jdbc:jtds:sqlserver://%s:1433;databaseName=%s;user=%s;password=%s;", hostName, dbName, user, senha)
				);
		return c;
	}

}