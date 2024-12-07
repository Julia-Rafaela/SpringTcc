package br.edu.fateczl.SpringTcc.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.edu.fateczl.SpringTcc.model.Categoria;
import br.edu.fateczl.SpringTcc.model.Item;
import br.edu.fateczl.SpringTcc.model.Produto;
import br.edu.fateczl.SpringTcc.persistence.CategoriaDao;
import br.edu.fateczl.SpringTcc.persistence.ItemDao;
import br.edu.fateczl.SpringTcc.persistence.ProdutoDao;

@Controller
public class ItemController {

    @Autowired
    private ProdutoDao pDao;

    @Autowired
    private CategoriaDao cDao;

    @Autowired
    private ItemDao iDao;

    @RequestMapping(value = "/item", method = RequestMethod.GET)
    public ModelAndView itemGet(@RequestParam Map<String, String> allRequestParam, ModelMap model) {
        String cmd = allRequestParam.get("cmd");
        String produto = allRequestParam.get("produto");
        String categoria = allRequestParam.get("categoria");

        String saida = "";
        String erro = "";
        List<Categoria> categorias = new ArrayList<>();
        List<Produto> produtos = new ArrayList<>();
        List<Item> itens = new ArrayList<>();

        try {
            categorias = listarCategoria();
            produtos = listarProdutos();

            if ("alterar".equals(cmd) && produto != null && !produto.isEmpty() && categoria != null) {
                try {
                    itens = buscarItem(Integer.parseInt(produto), categoria);
                } catch (NumberFormatException e) {
                    erro = "Produto inválido: " + e.getMessage();
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            erro = "Erro ao processar a solicitação: " + e.getMessage();
        } finally {
            model.addAttribute("saida", saida);
            model.addAttribute("erro", erro);
            model.addAttribute("categorias", categorias);
            model.addAttribute("produtos", produtos);
            model.addAttribute("itens", itens);
        }

        return new ModelAndView("item");
    }

    @RequestMapping(value = "/item", method = RequestMethod.POST)
    public ModelAndView itemPost(@RequestParam Map<String, String> allRequestParam, ModelMap model) {
        String cmd = allRequestParam.get("botao");
        String erro = "";
        String saida = "";

        List<Categoria> categorias = new ArrayList<>();
        List<Produto> produtos = new ArrayList<>();
        List<Item> itens = new ArrayList<>();

        try {
            categorias = listarCategoria();
            produtos = listarProdutos();

            if ("Adicionar".equals(cmd)) {
                System.out.println("Parâmetros recebidos:");
                for (Map.Entry<String, String> entry : allRequestParam.entrySet()) {
                    System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
                }

                for (Map.Entry<String, String> entry : allRequestParam.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();

                    if (key.startsWith("produto")) {
                        String index = key.substring("produto".length());
                        String quantidadeKey = "quantidade" + index;
                        String quantidadeValue = allRequestParam.get(quantidadeKey);

                        System.out.println("Produto: " + value + ", Quantidade: " + quantidadeValue);

                        if (value != null && !value.isEmpty() && quantidadeValue != null && !quantidadeValue.isEmpty()) {
                            Item item = new Item();
                            item.setQuantidade(Integer.parseInt(quantidadeValue));

                            Produto p = new Produto();
                            p.setCodigo(Integer.parseInt(value));
                            item.setProduto(p);

                            try {
                                saida = cadastrarItem(item);
                                System.out.println("Saida após inserção: " + saida);
                            } catch (SQLException | ClassNotFoundException e) {
                                erro += "Erro ao cadastrar item: " + e.getMessage() + "<br>";
                            }
                        }
                    }
                }
            } else if ("Buscar".equals(cmd)) {
                String produto = allRequestParam.get("produto");
                String categoria = allRequestParam.get("categoria");

                if (produto != null && !produto.isEmpty()) {
                    try {
                        itens = buscarItem(Integer.parseInt(produto), categoria);
                    } catch (NumberFormatException e) {
                        erro = "Erro ao converter número do produto: " + e.getMessage();
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            erro = "Erro ao processar a solicitação: " + e.getMessage();
        } finally {
            model.addAttribute("saida", saida);
            model.addAttribute("erro", erro);
            model.addAttribute("categorias", categorias);
            model.addAttribute("produtos", produtos);
            model.addAttribute("itens", itens);
        }

        return new ModelAndView("item");
    }


    private String cadastrarItem(Item i) throws SQLException, ClassNotFoundException {
        return iDao.iudItem("I", i);
    }

    private List<Item> buscarItem(int produto, String categoria) throws SQLException, ClassNotFoundException {
        return iDao.consultar(produto, categoria);
    }

    private List<Produto> listarProdutos() throws SQLException, ClassNotFoundException {
        return pDao.listar();
    }

    private List<Categoria> listarCategoria() throws SQLException, ClassNotFoundException {
        return cDao.listar();
    }
}
