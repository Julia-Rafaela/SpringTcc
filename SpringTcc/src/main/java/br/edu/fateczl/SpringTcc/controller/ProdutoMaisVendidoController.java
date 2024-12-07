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

import br.edu.fateczl.SpringTcc.model.Produto;
import br.edu.fateczl.SpringTcc.persistence.ConsultaDao;
import br.edu.fateczl.SpringTcc.persistence.GenericDao;
import br.edu.fateczl.SpringTcc.persistence.ProdutoDao;
import br.edu.fateczl.SpringTcc.persistence.VendaDao;

@Controller
public class ProdutoMaisVendidoController {
	 @Autowired
	    GenericDao gDao;

	    @Autowired
	    ProdutoDao pDao;

	    @Autowired
	    VendaDao vDao;

	    @Autowired
	    ConsultaDao pvDao;

	    @RequestMapping(name = "produtomaisvendido", value = "/produtomaisvendido", method = RequestMethod.GET)
	    public ModelAndView indexGet(@RequestParam Map<String, String> allRequestParam, ModelMap model) {
	        List<Produto> produtos = new ArrayList<>();
	        String erro = "";

	        try {
	            produtos = pDao.listar(); // Lista de produtos
	        } catch (SQLException | ClassNotFoundException e) {
	            erro = e.getMessage();
	        }

	        model.addAttribute("produtos", produtos);
	        model.addAttribute("erro", erro);

	        return new ModelAndView("produtomaisvendido");
	    }

	    @RequestMapping(name = "produtomaisvendido", value = "/produtomaisvendido", method = RequestMethod.POST)
	    public ModelAndView indexPost(@RequestParam Map<String, String> allRequestParam, ModelMap model) {
	        int idMes = Integer.parseInt(allRequestParam.get("mes")); // Obtendo ID do mês
	        List<Produto> produtos = new ArrayList<>();
	        String erro = "";
	        List<Map<String, Object>> consultas = new ArrayList<>(); // Lista para armazenar resultados

	        try {
	            consultas = pvDao.listar(idMes); // Chama a lista de produtos vendidos
	            produtos = pDao.listar(); // Lista de produtos
	        } catch (SQLException | ClassNotFoundException e) {
	            erro = e.getMessage();
	        }

	        model.addAttribute("consultas", consultas); // Adiciona a consulta ao modelo
	        model.addAttribute("produtos", produtos); // Adiciona a lista de produtos ao modelo
	        model.addAttribute("erro", erro); // Adiciona mensagem de erro ao modelo

	        return new ModelAndView("produtomaisvendido");
	    }
	}