package br.edu.fateczl.SpringTcc.controller;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.edu.fateczl.SpringTcc.model.GerarLucro;
import br.edu.fateczl.SpringTcc.model.Produto;
import br.edu.fateczl.SpringTcc.persistence.GerarLucroDao;
import br.edu.fateczl.SpringTcc.persistence.ProdutoDao;

@Controller
public class GerarLucroController {

    @Autowired
    private GerarLucroDao gerarLucroDao;

    @Autowired
    private ProdutoDao produtoDao;

    @GetMapping("/gerarLucro")
    public String showGerarLucroPage(Model model) throws ClassNotFoundException, SQLException {
        List<Produto> produtos = produtoDao.listar(); // Buscar todos os produtos
        model.addAttribute("produtos", produtos); // Adicionar produtos ao modelo
        model.addAttribute("lucro", new GerarLucro()); // Objeto vazio para vinculação do formulário
        model.addAttribute("precoVenda", "0,00"); // Inicia precoVenda formatado com 2 casas decimais
        return "gerarLucro";
    }

    @PostMapping("/gerarLucro")
    public String processGerarLucro(
            @RequestParam String botao,
            @RequestParam(required = false) Integer produto,
            @RequestParam(required = false) String precoCompra,
            @RequestParam(required = false) String margemLucro,
            @RequestParam(required = false) String precoVenda,
            Model model) throws ClassNotFoundException, SQLException {

        String saida = "";
        String erro = "";
        GerarLucro lucro = new GerarLucro();
        double precoVendaValue = 0;
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

        // Adiciona atributos de entrada ao modelo para manter valores
        model.addAttribute("precoCompra", precoCompra);
        model.addAttribute("margemLucro", margemLucro);
        model.addAttribute("produto", produto);

        try {
            if ("Calcular".equals(botao)) {
                if (precoCompra != null && !precoCompra.isEmpty() && margemLucro != null && !margemLucro.isEmpty()) {
                    precoCompra = precoCompra.replace("R$", "").replace(",", ".").trim();
                    double precoCompraValue = Double.parseDouble(precoCompra);
                    int margemLucroValue = Integer.parseInt(margemLucro);
                    lucro = gerarLucroDao.calcularPrecoVenda(precoCompraValue, margemLucroValue);
                    precoVendaValue = lucro.getPrecoVenda();
                } else {
                    erro = "Preços e margem de lucro são obrigatórios para o cálculo.";
                }
            } else if ("Aplicar ao Produto".equals(botao)) {
                if (produto != null && precoCompra != null && !precoCompra.isEmpty() &&
                    margemLucro != null && !margemLucro.isEmpty() && precoVenda != null && !precoVenda.isEmpty()) {
                    precoCompra = precoCompra.replace("R$", "").replace(",", ".").trim();
                    precoVenda = precoVenda.replace("R$", "").replace(",", ".").trim();

                    double precoCompraValue = Double.parseDouble(precoCompra);
                    int margemLucroValue = Integer.parseInt(margemLucro);
                    lucro.setPrecoCompra(precoCompraValue);
                    lucro.setMargemLucro(margemLucroValue);
                    lucro.setPrecoVenda(Double.parseDouble(precoVenda));
                    Produto p = new Produto();
                    p.setCodigo(produto);
                    lucro.setProduto(p);
                    saida = gerarLucroDao.inserirGerarLucro(lucro);

                    // Limpar os campos após aplicar ao produto
                    model.addAttribute("precoVenda", "0,00");
                    model.addAttribute("precoCompra", "");
                    model.addAttribute("margemLucro", "");
                    model.addAttribute("produto", null); // ou use "" para manter o valor vazio
                } else {
                    erro = "Todos os campos são obrigatórios para a aplicação ao produto.";
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            erro = e.getMessage();
        } catch (NumberFormatException e) {
            erro = "Formato de número inválido. Verifique os valores inseridos.";
        }

        // Formatar o preço de venda com duas casas decimais
        String precoVendaFormatado = decimalFormat.format(precoVendaValue);
        model.addAttribute("precoVenda", precoVendaFormatado);
        model.addAttribute("saida", saida);
        model.addAttribute("erro", erro);

        List<Produto> produtos = produtoDao.listar();
        model.addAttribute("produtos", produtos);

        return "gerarLucro";
    }
}