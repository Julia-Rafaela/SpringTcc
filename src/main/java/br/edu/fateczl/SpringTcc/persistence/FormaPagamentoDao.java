package br.edu.fateczl.SpringTcc.persistence;



import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import br.edu.fateczl.SpringTcc.model.FormaPagamento;


@Repository
public class FormaPagamentoDao {

    private GenericDao gDao;

    public FormaPagamentoDao(GenericDao gDao) {
        this.gDao = gDao;
    }


    public List<FormaPagamento> listar() throws SQLException, ClassNotFoundException {
        List<FormaPagamento> formasPagamento = new ArrayList<>();
        Connection conn = gDao.getConnection();
        String sql = "SELECT formaPagamento FROM FormaPagamento";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            FormaPagamento fp = new FormaPagamento();
            fp.setFormaPagamento(rs.getString("formaPagamento"));
            formasPagamento.add(fp);
        }
        rs.close();
        ps.close();
        conn.close();
        return formasPagamento;
    }
}

