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
import br.edu.fateczl.SpringTcc.model.Estoque;
import br.edu.fateczl.SpringTcc.model.Produto;
import br.edu.fateczl.SpringTcc.persistence.CategoriaDao;
import br.edu.fateczl.SpringTcc.persistence.EstoqueDao;
import br.edu.fateczl.SpringTcc.persistence.ProdutoDao;

@Controller
public class EstoqueController {

    @Autowired
    private ProdutoDao pDao;

    @Autowired
    private CategoriaDao cDao;

    @Autowired
    private EstoqueDao eDao;

    @RequestMapping(value = "/estoque", method = RequestMethod.GET)
    public ModelAndView estoqueGet(@RequestParam Map<String, String> allRequestParam, ModelMap model) {
        String cmd = allRequestParam.get("cmd");
        String produto = allRequestParam.get("produto");
        String categoria = allRequestParam.get("categoria");

        String saida = "";
        String erro = "";
        List<Categoria> categorias = new ArrayList<>();
        List<Produto> produtos = new ArrayList<>();
        List<Estoque> estoques = new ArrayList<>();

        try {
            categorias = listarCategoria();
            produtos = listarProdutos();

            if (cmd != null && cmd.equals("alterar") && produto != null && categoria != null) {
                estoques = buscarEstoque(Integer.parseInt(produto), categoria);
            }

        } catch (SQLException | ClassNotFoundException e) {
            erro = e.getMessage();
        } finally {
            model.addAttribute("saida", saida);
            model.addAttribute("erro", erro);
            model.addAttribute("categorias", categorias);
            model.addAttribute("produtos", produtos);
            model.addAttribute("estoques", estoques);
        }

        return new ModelAndView("estoque");
    }

    @RequestMapping(value = "/estoque", method = RequestMethod.POST)
    public ModelAndView estoquePost(@RequestParam Map<String, String> allRequestParam, ModelMap model) {
        String cmd = allRequestParam.get("botao");
        String quantidadeMin = allRequestParam.get("quantidadeMin");
        String categoria = allRequestParam.get("categoria");
        String produto = allRequestParam.get("produto");

        String saida = "";
        String erro = "";
        List<Categoria> categorias = new ArrayList<>();
        List<Produto> produtos = new ArrayList<>();
        List<Estoque> estoques = new ArrayList<>();
        int quantidadeTot = 0;  // Mantendo o total como 0 no início

        try {
            categorias = listarCategoria();
            produtos = listarProdutos();

            if (cmd != null) {
                if (cmd.equals("Cadastrar")) {
                    for (Map.Entry<String, String> entry : allRequestParam.entrySet()) {
                        String key = entry.getKey();
                        String value = entry.getValue();

                        if (key.startsWith("quantidade")) {
                            String produtoIndex = key.substring(10);
                            String produtoCodigo = value;

                            if (produtoCodigo != null && !produtoCodigo.isEmpty()) {
                                Estoque estoqueNovo = new Estoque();
                                estoqueNovo.setQuantidadeMin(Integer.parseInt(quantidadeMin));
                                estoqueNovo.setQuantidade(Integer.parseInt(produtoCodigo));

                                String produtoKey = "produto" + produtoIndex;
                                String produtoValue = allRequestParam.get(produtoKey);
                                if (produtoValue != null && !produtoValue.isEmpty()) {
                                    Produto p = new Produto();
                                    p.setCodigo(Integer.parseInt(produtoValue));
                                    estoqueNovo.setProduto(p);

                                    try {
                                        saida = cadastrarEstoque(estoqueNovo);
                                    } catch (SQLException | ClassNotFoundException e) {
                                        erro += "Erro ao cadastrar estoque: " + e.getMessage() + "<br>";
                                    }
                                }
                            }
                        }
                    }
                    quantidadeTot = eDao.getQuantidadeTot();
	            }

	            // Listar Estoques
	            else if (cmd.equals("Listar")) {
	                estoques = listarEstoques();
	                quantidadeTot = eDao.getQuantidadeTot();
	            }

	            // Alterar Estoque
	            else if (cmd.equals("Alterar")) {
	                for (Map.Entry<String, String> entry : allRequestParam.entrySet()) {
	                    String key = entry.getKey();
	                    String value = entry.getValue();

	                    if (key.startsWith("quantidade")) {
	                        String produtoIndex = key.substring(10);
	                        String produtoCodigo = value;

	                        if (produtoCodigo != null && !produtoCodigo.isEmpty()) {
	                            Estoque estoqueNovo = new Estoque();
	                            estoqueNovo.setQuantidadeMin(Integer.parseInt(quantidadeMin));
	                            estoqueNovo.setQuantidade(Integer.parseInt(produtoCodigo));

	                            String produtoKey = "produto" + produtoIndex;
	                            String produtoValue = allRequestParam.get(produtoKey);
	                            if (produtoValue != null && !produtoValue.isEmpty()) {
	                                Produto p = new Produto();
	                                p.setCodigo(Integer.parseInt(produtoValue));
	                                estoqueNovo.setProduto(p);

	                                try {
	                                    saida = alterarEstoque(estoqueNovo);
	                                } catch (SQLException | ClassNotFoundException e) {
	                                    erro += "Erro ao alterar estoque: " + e.getMessage() + "<br>";
	                                }
	                            }
	                        }
	                    }
	                }
	                quantidadeTot = eDao.getQuantidadeTot();
	            }

	            // Buscar Estoque
	            else if (cmd.equals("Buscar")) {
	                if (produto != null && !produto.isEmpty()) {
	                    estoques = buscarEstoque(Integer.parseInt(produto), categoria);
	                    quantidadeTot = eDao.getQuantidadeTot();
	                }
	            }
	        }

	    } catch (SQLException | ClassNotFoundException e) {
	        erro = e.getMessage();
	    } finally {
	        model.addAttribute("saida", saida);
	        model.addAttribute("erro", erro);
	        model.addAttribute("categorias", categorias);
	        model.addAttribute("produtos", produtos);
	        model.addAttribute("estoques", estoques);
	        model.addAttribute("quantidadeTot", quantidadeTot);
	    }

	    return new ModelAndView("estoque");
	}



    private String cadastrarEstoque(Estoque e) throws SQLException, ClassNotFoundException {
        return eDao.iudEstoque("I", e);
    }

    private List<Estoque> listarEstoques() throws SQLException, ClassNotFoundException {
        return eDao.listarProdutosE();
    }

    private List<Estoque> buscarEstoque(int produto, String categoria) throws SQLException, ClassNotFoundException {
        return eDao.consultar(produto, categoria);
    }

    private String alterarEstoque(Estoque e) throws SQLException, ClassNotFoundException {
        if (e == null) {
            return "Estoque não encontrado.";
        }
        return eDao.iudEstoque("U", e);
    }

    private List<Produto> listarProdutos() throws SQLException, ClassNotFoundException {
        return pDao.listar();
    }

    private List<Categoria> listarCategoria() throws SQLException, ClassNotFoundException {
        return cDao.listar();
    }
}

