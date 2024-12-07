<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css"
	href='<c:url value="/resources/css/estoque.css" />'>
<title>Gerenciar Estoque</title>
<script>
	function validarForm() {
		var camposQuantidade = document
				.querySelectorAll('input[name^="quantidade"]');
		camposQuantidade.forEach(function(campo) {
			if (campo.value === '' || isNaN(campo.value)) {
				campo.value = '0';
			}
		});
		return true;
	}
</script>
</head>
<body
	style="background-image: url('${pageContext.request.contextPath}/resources/imagens/imagem_fundo.png');"
	class="tela_estoque">
	<div class="menu">
		<jsp:include page="menu.jsp" />
	</div>
	<br />
	<div align="center" class="container">
		<form action="estoque" method="post" class="estoque"
			onsubmit="return validarForm()">
			<h2 class="title">Gerenciar Estoque</h2>
			<div class="label-input-container">
				<label>Produto:</label> <select id="produto" name="produto">
					<option value="0">Escolha o Produto</option>
					<c:forEach var="p" items="${produtos}">
						<option value="${p.codigo}"><c:out value="${p.nome}" /></option>
					</c:forEach>
				</select> <input type="submit" id="botaoBuscar" name="botao" value="Buscar">
			</div>
			<div class="label-input-container">
				<label>Categoria:</label> <select id="categoria" name="categoria">
					<option value="0">Escolha a Categoria</option>
					<c:forEach var="c" items="${categorias}">
						<option value="${c.categoria}"><c:out
								value="${c.categoria}" /></option>
					</c:forEach>
				</select>
			</div>
			<div class="label-input-container">
				<label>Quantidade Mínima:</label> <input type="number"
					id="quantidadeMin" name="quantidadeMin"
					placeholder="Quantidade Mínima"> <label>Quantidade
					Total:</label> <input type="number" id="quantidadeTot" name="quantidadeTot"
					readonly value="${quantidadeTot}">
			</div>
			<div class="botoes2">
				<input type="submit" id="botaoCadastrar" name="botao"
					value="Cadastrar"> <input class="alterar" type="submit"
					id="botaoAlterar" name="botao" value="Alterar"> <input
					type="submit" id="botaoListar" name="botao" value="Listar">
			</div>
			<c:if test="${not empty estoques}">
				<table class="table_round">
					<thead>
						<tr>
							<th>Produto Código</th>
							<th>Produto Nome</th>
							<th>Marca Produto</th>
							<th>Categoria Produto</th>
							<th>QuantidadeMin</th>
							<th>Quantidade</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="e" items="${estoques}" varStatus="status">
							<tr>
								<td><input type="number" id="produto${status.index}"
									name="produto${status.index}" value="${e.produto.codigo}"
									readonly style="border: none; background: none; outline: none;"></td>
								<td><c:out value="${e.produto.nome}" /></td>
								<td><c:out value="${e.produto.marca}" /></td>
								<td><c:out value="${e.categoria.categoria}" /></td>
								<td><c:out value="${e.quantidadeMin}" /></td>
								<td><input type="number" id="quantidade${status.index}"
									name="quantidade${status.index}" value="${e.quantidade}"></td>
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
</body>
</html>
