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
import br.edu.fateczl.SpringTcc.model.Produto;
import br.edu.fateczl.SpringTcc.persistence.CategoriaDao;
import br.edu.fateczl.SpringTcc.persistence.ProdutoDao;

@Controller
public class FornecedorController {
/*
    @Autowired
    private CategoriaDao categoriaDao;

    @Autowired
    private ProdutoDao produtoDao;

    @RequestMapping(name = "produto", value = "/produto", method = RequestMethod.GET)
    public ModelAndView produtoGet(@RequestParam Map<String, String> allRequestParam, ModelMap model) {

        String cmd = allRequestParam.get("cmd");
        String codigo = allRequestParam.get("codigo");

        Produto p = new Produto();
        if (codigo != null && !codigo.isEmpty()) {
            p.setCodigo(Integer.parseInt(codigo));
        }

        List<Produto> produtos = new ArrayList<>();
        List<Categoria> categorias = new ArrayList<>();
        String saida = "";
        String erro = "";

        try {
            if (cmd != null) {
                if (cmd.contains("alterar")) {
                    p = buscarProduto(p);
                } else if (cmd.contains("excluir")) {
                    p = buscarProduto(p);
                    saida = excluirProduto(p);
                }
            }

            produtos = listarProdutos();
            categorias = listarCategorias();

            if (cmd != null && cmd.contains("Listar")) {
                model.addAttribute("tipoTabela", "Listar");
            } else {
                model.addAttribute("tipoTabela", "");
            }

        } catch (SQLException | ClassNotFoundException e) {
            erro = e.getMessage();
        } finally {
            model.addAttribute("saida", saida);
            model.addAttribute("erro", erro);
            model.addAttribute("produto", p);
            model.addAttribute("produtos", produtos);
            model.addAttribute("categorias", categorias);
        }

        return new ModelAndView("produto");
    }

    @RequestMapping(name = "produto", value = "/produto", method = RequestMethod.POST)
    public ModelAndView produtoPost(@RequestParam Map<String, String> allRequestParam, ModelMap model) {

        String cmd = allRequestParam.get("botao");
        String codigo = allRequestParam.get("codigo");
        String nome = allRequestParam.get("nome");
        String descricao = allRequestParam.get("descricao");
        String marca = allRequestParam.get("marca");
        String dataValidade = allRequestParam.get("dataValidade");
        String status = allRequestParam.get("status");
        String categoria = allRequestParam.get("categoria");

        Produto p = new Produto();
        List<Produto> produtos = new ArrayList<>();
        List<Categoria> categorias = new ArrayList<>();
        String saida = "";
        String erro = "";

        if (cmd != null && !cmd.contains("Listar")) {
            if (codigo != null && !codigo.isEmpty()) {
                p.setCodigo(Integer.parseInt(codigo));
            }
        }

        try {
            categorias = listarCategorias();

            if (cmd != null) {
                if (cmd.contains("Cadastrar") || cmd.contains("Alterar")) {
                    p.setNome(nome);
                    p.setDescricao(descricao);
                    p.setDataValidade(dataValidade);
                    p.setStatus(status);
                    p.setMarca(marca);

                    Categoria c = new Categoria();
                    if (categoria != null && !categoria.isEmpty()) {
                        c.setCategoria(categoria);
                        p.setCategoria(c);
                    }
                    
                    status = p.getStatus();
                }

                if (cmd.contains("Cadastrar")) {
                    saida = cadastrarProduto(p);
                } else if (cmd.contains("Alterar")) {
                    saida = alterarProduto(p);
                    status = p.getStatus();
                } else if (cmd.contains("Excluir")) {
                    p = buscarProduto(p);
                    saida = excluirProduto(p);
                } else if (cmd.contains("Buscar")) {
                    p = buscarProduto(p);
                    status = p.getStatus();
                } else if (cmd.contains("Listar")) {
                    produtos = listarProdutos();
                    status = p.getStatus();
                    model.addAttribute("tipoTabela", "Listar");
                }
            }

        } catch (SQLException | ClassNotFoundException e) {
            erro = e.getMessage();
        } finally {
            model.addAttribute("saida", saida);
            model.addAttribute("erro", erro);
            model.addAttribute("produto", p);
            model.addAttribute("produtos", produtos);
            model.addAttribute("categorias", categorias);
            model.addAttribute("status", status);
       
        }

        return new ModelAndView("produto");
    }

    private String cadastrarProduto(Produto p) throws SQLException, ClassNotFoundException {
        return produtoDao.iudProduto("I", p);
    }

    private String alterarProduto(Produto p) throws SQLException, ClassNotFoundException {
        return produtoDao.iudProduto("U", p);
    }

    private String excluirProduto(Produto p) throws SQLException, ClassNotFoundException {
        return produtoDao.iudProduto("D", p);
    }

    private Produto buscarProduto(Produto p) throws SQLException, ClassNotFoundException {
        return produtoDao.consultar(p);
    }

    private List<Produto> listarProdutos() throws SQLException, ClassNotFoundException {
        return produtoDao.listarComPrecoVenda();
    }

    private List<Categoria> listarCategorias() throws SQLException, ClassNotFoundException {
        return categoriaDao.listar();
    }*/
}
