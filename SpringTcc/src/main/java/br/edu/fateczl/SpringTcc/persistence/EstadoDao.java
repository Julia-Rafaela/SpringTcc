package br.edu.fateczl.SpringTcc.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import br.edu.fateczl.SpringTcc.model.Estado;
@Repository
public class EstadoDao {
    private GenericDao gDao;

    public EstadoDao(GenericDao gDao) {
        this.gDao = gDao;
    }

    public void inserir(Estado e) throws SQLException, ClassNotFoundException {
        Connection conn = gDao.getConnection();
        String sql = "INSERT INTO Estado (sigla) VALUES (?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getSigla());
            ps.executeUpdate();
        } finally {
            conn.close();
        }
    }

    public Estado consultarPorId(int id) throws SQLException, ClassNotFoundException {
        Estado estado = null;
        String sql = "SELECT * FROM Estado WHERE id = ?";

        System.out.println("Consultando estado com ID: " + id);

        try (Connection conn = gDao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    estado = new Estado();
                    estado.setId(rs.getInt("id"));
                    estado.setSigla(rs.getString("sigla"));
                    System.out.println("Estado encontrado: ID = " + estado.getId() + ", Sigla = " + estado.getSigla());
                } else {
                    System.out.println("Estado não encontrado para ID: " + id);
                }
            }
        }
        return estado;
    }
    public Estado consultarPorSigla(String sigla) throws SQLException, ClassNotFoundException {
        Estado estado = null;
        String sql = "SELECT * FROM Estado WHERE sigla = ?";

        System.out.println("Consultando estado com sigla: " + sigla);

        try (Connection conn = gDao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, sigla.trim()); // Remove espaços em branco
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    estado = new Estado();
                    estado.setId(rs.getInt("id"));
                    estado.setSigla(rs.getString("sigla"));
                    System.out.println("Estado encontrado: ID = " + estado.getId() + ", Sigla = " + estado.getSigla());
                } else {
                    System.out.println("Estado não encontrado para sigla: " + sigla);
                }
            }
        }
        return estado;
    
    }

    public void excluir(String sigla) throws SQLException, ClassNotFoundException {
        Connection conn = gDao.getConnection();
        String sql = "DELETE FROM Estado WHERE sigla = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sigla);
            ps.executeUpdate();
        } finally {
            conn.close();
        }
    }

    public List<Estado> listar() throws SQLException, ClassNotFoundException {
        List<Estado> estados = new ArrayList<>();
        Connection conn = gDao.getConnection();
        String sql = "SELECT id, sigla FROM Estado";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Estado e = new Estado();
                e.setId(rs.getInt("id"));
                e.setSigla(rs.getString("sigla"));
                estados.add(e);
            }
        } finally {
            conn.close();
        }
        return estados;
    }
}