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

import br.edu.fateczl.SpringTcc.model.Categoria;
import br.edu.fateczl.SpringTcc.model.Item;
import br.edu.fateczl.SpringTcc.model.Produto;

@Repository
public class ItemDao {

    private final GenericDao gDao;

    public ItemDao(GenericDao gDao) {
        this.gDao = gDao;
    }

    public List<Item> consultar(Integer codigoProduto, String categoriaProduto) throws SQLException, ClassNotFoundException {
        List<Item> itens = new ArrayList<>();
        try (Connection c = gDao.getConnection();
             PreparedStatement ps = createPreparedStatement(c, codigoProduto, categoriaProduto);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                // Criar o objeto Produto
                Produto produto = new Produto();
                produto.setCodigo(rs.getInt("CodigoProduto"));
                produto.setNome(rs.getString("NomeProduto"));
                produto.setMarca(rs.getString("MarcaProduto"));

                // Criar o objeto Categoria
                Categoria categoria = new Categoria();
                categoria.setCategoria(rs.getString("CategoriaProduto"));

                Item item = new Item();
                item.setQuantidade(rs.getInt("Quantidade"));

                item.setProduto(produto);
                item.setCategoria(categoria);

                // Adicionar o item à lista
                itens.add(item);
            }
        }

        return itens;
    }

    private PreparedStatement createPreparedStatement(Connection c, Integer codigoProduto, String categoriaProduto) throws SQLException {
        String sql;
        PreparedStatement ps;

        if (codigoProduto != null && codigoProduto > 0) {
            // Se o código do produto for fornecido, use a função correspondente
            sql = "SELECT * FROM dbo.fn_produtosPorCodigoI(?)";
            ps = c.prepareStatement(sql);
            ps.setInt(1, codigoProduto);
        } else if (categoriaProduto != null && !categoriaProduto.isEmpty()) {
            // Se a categoria for fornecida, use a função correspondente
            sql = "SELECT * FROM dbo.fn_produtosPorCategoriaI(?)";
            ps = c.prepareStatement(sql);
            ps.setString(1, categoriaProduto);
        } else {
            throw new IllegalArgumentException("Nenhum critério de pesquisa fornecido.");
        }

        return ps;
    }

    public String iudItem(String op, Item i) throws SQLException, ClassNotFoundException {
        try (Connection c = gDao.getConnection();
             CallableStatement cs = c.prepareCall("{CALL GerenciarItens(?, ?, ?, ?)}")) {

            // Definir os parâmetros de entrada
            cs.setString(1, op); // Operação (Inserir ou Atualizar)
            cs.setInt(2, i.getProduto().getCodigo()); // Código do produto
            cs.setInt(3, i.getQuantidade()); // Quantidade

            cs.registerOutParameter(4, Types.VARCHAR);

            cs.execute();

            return cs.getString(4);
            
         
        } 
    }  
}
