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

import br.edu.fateczl.SpringTcc.model.FormaPagamento;
import br.edu.fateczl.SpringTcc.model.Item;
import br.edu.fateczl.SpringTcc.model.Produto;
import br.edu.fateczl.SpringTcc.model.Venda;
import br.edu.fateczl.SpringTcc.persistence.FormaPagamentoDao;
import br.edu.fateczl.SpringTcc.persistence.GerarLucroDao;
import br.edu.fateczl.SpringTcc.persistence.ItemDao;
import br.edu.fateczl.SpringTcc.persistence.VendaDao;

@Controller
public class VendaController {

	@Autowired
	private VendaDao vDao;

	@Autowired
	private ItemDao iDao;

	@Autowired
	private GerarLucroDao glDao;

	@Autowired
	private FormaPagamentoDao fpDao;

	@RequestMapping(value = "/venda", method = RequestMethod.GET)
	public ModelAndView vendaGet(@RequestParam Map<String, String> allRequestParam, ModelMap model)
			throws ClassNotFoundException, SQLException {

		String codigo = allRequestParam.get("codigo");

		String saida = "";
		String erro = "";
		List<Venda> vendas = new ArrayList<>();
		List<Venda> itens = new ArrayList<>();
		List<FormaPagamento> formasPagamento = listarFormaPagamento();

		model.addAttribute("saida", saida);
		model.addAttribute("erro", erro);
		model.addAttribute("codigo", codigo);
		model.addAttribute("vendas", vendas);
		model.addAttribute("itens", itens);
		model.addAttribute("formasPagamento", formasPagamento);

		return new ModelAndView("venda");
	}

	@RequestMapping(value = "/venda", method = RequestMethod.POST)
	public ModelAndView vendaPost(@RequestParam Map<String, String> allRequestParam, ModelMap model) {
		String cmd = allRequestParam.get("botao");
		String codigo = allRequestParam.get("codigo");
		String data = allRequestParam.get("data");
		String formaPagamento = allRequestParam.get("formaPagamento");

		double valorTotal = 0;
		String saida = "";
		String erro = "";
		List<Venda> vendas = new ArrayList<>();
		List<Venda> itens = new ArrayList<>();
		List<FormaPagamento> formasPagamento = new ArrayList<>();

		try {
			formasPagamento = listarFormaPagamento();

			if (cmd != null) {
				if (cmd.equals("Realizar Venda")) {
					realizarVenda(allRequestParam, codigo, data, formaPagamento);
					codigo = "";
				} else if (cmd.equals("Listar Itens")) {
					itens = listarItens();
					codigo = "";
					data = "";
				} else if (cmd.equals("Listar Vendas")) {
					vendas = listarVendas();
					valorTotal = 0;
				} else if (cmd.equals("Buscar")) {
					vendas = buscarVendas(Integer.parseInt(codigo), data);
					if (!vendas.isEmpty()) {
						Venda vendaEncontrada = vendas.get(0);
						data = vendaEncontrada.getData();
						formaPagamento = vendaEncontrada.getFormaPagamento().getFormaPagamento();
					}

				} else if ("Excluir".equals(cmd)) {
					Venda v = new Venda();
					String produto = allRequestParam.get("produto");

	                if (produto != null && !produto.isEmpty()) {
	                	Produto pe = new Produto();
	                    pe.setCodigo(Integer.parseInt(produto));
	                    v.setProduto(pe);
	                    
	                    saida = excluirItem(v);
	                }
				}
			}

			if (codigo != null && !codigo.isEmpty()) {
				valorTotal = vDao.getValorTotal(Integer.parseInt(codigo));
			}

		} catch (

		SQLException e) {
			erro += "Erro de SQL: " + e.getMessage() + "<br>";
		} catch (ClassNotFoundException e) {
			erro += "Classe não encontrada: " + e.getMessage() + "<br>";
		} catch (NumberFormatException e) {
			erro += "Erro ao converter número: " + e.getMessage() + "<br>";
		} catch (Exception e) {
			erro += "Erro inesperado: " + e.getMessage() + "<br>";
		} finally {
			model.addAttribute("saida", saida);
			model.addAttribute("erro", erro);
			model.addAttribute("codigo", codigo);
			model.addAttribute("data", data);
			model.addAttribute("formaPagamento", formaPagamento);
			model.addAttribute("vendas", vendas);
			model.addAttribute("itens", itens);
			model.addAttribute("valorTotal", valorTotal);
			model.addAttribute("formasPagamento", formasPagamento);
		}

		return new ModelAndView("venda");
	}

	private void realizarVenda(Map<String, String> allRequestParam, String codigo, String data, String formaPagamento) {
		try {
			for (Map.Entry<String, String> entry : allRequestParam.entrySet()) {
				String key = entry.getKey();
				if (key.startsWith("quantidade")) {
					String produtoIndex = key.substring(10);
					String quantidadeStr = entry.getValue();

					if (quantidadeStr != null && !quantidadeStr.isEmpty()) {
						Venda vendaNova = new Venda();
						vendaNova.setCodigo(Integer.parseInt(codigo));
						vendaNova.setData(data);

						FormaPagamento fp = new FormaPagamento();
						fp.setFormaPagamento(formaPagamento);
						vendaNova.setFormaPagamento(fp);

						double subValor = vDao.getSubValor(Integer.parseInt(codigo));
						vendaNova.setSubValor(subValor);

						Item item = new Item();
						item.setQuantidade(Integer.parseInt(quantidadeStr));

						String produtoKey = "produto" + produtoIndex;
						String produtoValue = allRequestParam.get(produtoKey);
						if (produtoValue != null && !produtoValue.isEmpty()) {
							Produto produto = new Produto();
							produto.setCodigo(Integer.parseInt(produtoValue));
							item.setProduto(produto);

							vendaNova.setItem(item);

							String subValorKey = "subValor" + produtoIndex;
							String subValorStr = allRequestParam.get(subValorKey);
							if (subValorStr != null && !subValorStr.isEmpty()) {
								vendaNova.setSubValor(Double.parseDouble(subValorStr.replace(",", ".")));
							}

							cadastrarVenda(vendaNova);
						}
					}
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao realizar venda: " + e.getMessage());
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Classe não encontrada ao realizar venda: " + e.getMessage());
		} catch (NumberFormatException e) {
			throw new RuntimeException("Erro ao converter número na venda: " + e.getMessage());
		}
	}

	private List<Venda> buscarVendas(int codigo, String data) throws ClassNotFoundException, SQLException {
		return vDao.consultarVenda(codigo, data);
	}

	private String excluirItem(Venda v) throws SQLException, ClassNotFoundException {
		return vDao.excluirItem("D", v);
	}

	private String cadastrarVenda(Venda v) throws SQLException, ClassNotFoundException {
		return vDao.iudVenda("I", v);
	}

	private List<Venda> listarVendas() throws SQLException, ClassNotFoundException {
		return vDao.listarVendas();
	}

	private List<Venda> listarItens() throws SQLException, ClassNotFoundException {
		return vDao.listarItens();
	}

	private List<FormaPagamento> listarFormaPagamento() throws SQLException, ClassNotFoundException {
		return fpDao.listar();
	}
}