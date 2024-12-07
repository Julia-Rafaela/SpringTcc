<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<link rel="stylesheet" type="text/css"
	href='<c:url value="./resources/css/venda.css" />'>
<title>Venda</title>
<script>
	function validarForm() {
		var camposQuantidade = document
				.querySelectorAll('input[name^="quantidade"]');
		camposQuantidade.forEach(function(campo) {
			if (campo.value === '') {
				campo.value = '0';
			}
		});
		return true;
	}
</script>
</head>
<body
	style="background-image: url('<c:url value="./resources/imagens/imagem_fundo.png" />')"
	class="tela_venda">
	<div class="menu">
		<jsp:include page="menu.jsp" />
	</div>
	<br />
	<div align="center" class="container">
		<form action="venda" method="post" class="venda"
			onsubmit="return validarForm()">
			<p class="title">
			<h2>Gerenciar Venda</h2>
			<table>

				<tr>
					<td class="title">Código da Venda: <input class="cadastro"
						type="number" id="codigo" name="codigo" value="${codigo}"
						placeholder="Código da Venda"  onfocus="if (this.value === '') this.value = 0;"></td>
					<td class="title2">Data: <input class="cadastro" type="date"
						id="data" name="data" value="${data}" placeholder="Data">
						<input class="buscar" type="submit" id="botao" name="botao"
						value="Buscar"></td>
				</tr>
				<tr>
					<td class="title">Forma de Pagamento: <select
						id="formaPagamento" name="formaPagamento">
							<option value="">Selecione uma Forma de Pagamento</option>
							<c:forEach var="forma" items="${formasPagamento}">
								<option value="${forma.formaPagamento}"><c:out
										value="${forma.formaPagamento}" /></option>
							</c:forEach>
					</select>
					</td>
					<td class="title2">Valor Total: <input class="cadastro"
						type="text" id="valorTotal" name="valorTotal"
						value="${valorTotal}" placeholder="Valor Total" readonly></td>
				</tr>
			</table>
			<div class="botoes-container">
				<input type="submit" class="realizar-venda" name="botao"
					value="Realizar Venda"> <input type="submit"
					class="listar-itens" name="botao" value="Listar Itens"> <input
					type="submit" class="listar-vendas" name="botao"
					value="Listar Vendas">
			</div>
			<c:if test="${not empty vendas}">
				<table class="table_round">
					<thead>
						<tr>
							<th>Código</th>
							<th>Data</th>
							<th>Forma de Pagamento</th>
							<th>Produto</th>
							<th>Quantidade</th>
							<th>Preço</th>
							<th>Sub-Total</th>
							<th>Valor Total</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="v" items="${vendas}">
							<tr>
								<td><c:out value="${v.codigo}" /></td>
								<td><c:out value="${v.data}" /></td>
								<td><c:out value="${v.formaPagamento}" /></td>
								<td><c:out value="${v.produto.nome}" /></td>
								<td><c:out value="${v.item.quantidade}" /></td>
								<td><c:out value="${v.gerarLucro.precoVendaFormatado}" /></td>
								<td><c:out value="${v.subValorFormatado}" /></td>
								<td><c:out value="${v.valorTotalFormatado}" /></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</c:if>
			<c:if test="${not empty itens}">
				<table class="table_round">
					<thead>
						<tr>
							<th>Código</th>
							<th>Produto</th>
							<th>Preço Venda</th>
							<th>Quantidade</th>
							<th>Sub-Valor</th>
							<th>Valor Total a pagar</th>
							<th>Excluir</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="item" items="${itens}" varStatus="status">
							<tr>
								<td><input type="number" id="produto${status.index}"
									name="produto${status.index}"
									value='<c:out value="${item.produto.codigo}"/>'  readonly style="border: none; background: none; outline: none;">
								</td>
								<td><c:out value="${item.produto.nome}" /></td>
								<td><c:out value="${item.gerarLucro.precoVenda}" /></td>
								<td><input type="number" id="quantidade${status.index}"
									name="quantidade${status.index}"
									value='<c:out value="${item.item.quantidade}"/>' readonly style="border: none; background: none; outline: none;"></td>
								<td><input type="text" id="subValor${status.index}"
									name="subValor${status.index}"
									value='<c:out value="${item.subValor}"/>' readonly style="border: none; background: none; outline: none;"></td>
								<td><input type="text" id="valorTotal${status.index}"
									name="valorTotal${status.index}"
									value='<c:out value="${item.valorTotal}"/>' readonly style="border: none; background: none; outline: none;"></td>
								<td>
									<form action="venda" method="post">
										<input type="hidden" name="botao" value="Excluir"> <input
											type="hidden" name="produto" value="${item.produto.codigo}">
										<input class="btn-excluir" type="submit" value="Excluir"
											style="background-color: #ff6251">
									</form>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</c:if>
			<c:if test="${not empty erro}">
				<div class="erro">${erro}</div>
			</c:if>
			<c:if test="${not empty saida}">
				<div class="sucesso">${saida}</div>
			</c:if>
		</form>
	</div>
	<br />
</body>
<script>
    // Define o valor 0 automaticamente ao carregar a página, se estiver vazio
    window.onload = function() {
        var inputCodigo = document.getElementById('codigo');
        if (inputCodigo.value === '') {
            inputCodigo.value = 0;
        }
    };
</script>
</html>