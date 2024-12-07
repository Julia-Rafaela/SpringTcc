package br.edu.fateczl.SpringTcc.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.edu.fateczl.SpringTcc.model.PrevisaoDemanda;
import br.edu.fateczl.SpringTcc.model.Produto;
import br.edu.fateczl.SpringTcc.persistence.PrevisaoDemandaDao;
import br.edu.fateczl.SpringTcc.persistence.ProdutoDao;

@Controller
public class PrevisaoDemandaController {

    @Autowired
    private PrevisaoDemandaDao demandaDao;

    @Autowired
    private ProdutoDao produtoDao;

    @GetMapping("/demanda")
    public ModelAndView demandaGet(ModelMap model) throws ClassNotFoundException, SQLException {
        List<Produto> produtos = listarProdutos();
        List<PrevisaoDemanda> demandas = new ArrayList<>();

        model.addAttribute("produtos", produtos);
        model.addAttribute("demandas", demandas);
        model.addAttribute("saida", "");
        model.addAttribute("erro", "");

        return new ModelAndView("demanda");
    }

    @PostMapping("/demanda")
    public ModelAndView demandaPost(@RequestParam Map<String, String> allRequestParam, ModelMap model) {

        String cmd = allRequestParam.get("botao");
        String produtoId = allRequestParam.get("produto");
        String mes = allRequestParam.get("mes");

        PrevisaoDemanda demanda = new PrevisaoDemanda();
        List<PrevisaoDemanda> demandas = new ArrayList<>();
        List<Produto> produtos = new ArrayList<>();
        String saida = "";
        String erro = "";

        try {
            produtos = listarProdutos();

            if (cmd != null && cmd.contains("Calcular Previsao de Demanda")) {
                Produto produto = new Produto();
                produto.setCodigo(Integer.parseInt(produtoId));

                demanda.setProduto(produto);
               
                demanda.setMes(converterMesParaNumero(mes));

             
                saida = demandaDao.consultarSomaVendasUltimosTresMesesProduto(demanda);
                demandas = demandaDao.listarVendasUltimosTresMeses(converterMesParaNumero(mes), Integer.parseInt(produtoId));
                System.out.println(mes);
                System.out.println(produtoId);
            }

        } catch (SQLException | ClassNotFoundException e) {
            erro = e.getMessage();
        } catch (IllegalArgumentException e) {
            erro = e.getMessage(); 
        } finally {
            model.addAttribute("saida", saida);
            model.addAttribute("erro", erro);
            model.addAttribute("produtos", produtos);
            model.addAttribute("demandas", demandas);
        }

        return new ModelAndView("demanda");
    }

    private List<Produto> listarProdutos() throws SQLException, ClassNotFoundException {
        return produtoDao.listar();
    }

    private int converterMesParaNumero(String mes) throws IllegalArgumentException {
        switch (mes.toLowerCase()) {
            case "janeiro": return 1;
            case "fevereiro": return 2;
            case "março": return 3;
            case "abril": return 4;
            case "maio": return 5;
            case "junho": return 6;
            case "julho": return 7;
            case "agosto": return 8;
            case "setembro": return 9;
            case "outubro": return 10;
            case "novembro": return 11;
            case "dezembro": return 12;
            default: throw new IllegalArgumentException("Mês inválido: " + mes);
        }
    }
}
