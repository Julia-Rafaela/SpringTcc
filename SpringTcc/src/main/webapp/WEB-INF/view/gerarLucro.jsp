<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-br">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" type="text/css"
	href="<c:url value='/resources/css/gerarLucro.css' />">
<title>Gerar Lucro</title>
</head>
<body class="tela_gerar_lucro">
	<div class="menu">
		<jsp:include page="menu.jsp"></jsp:include>
	</div>

	<div class="container">
		<h2>Gerar Lucro</h2>
		<form action="gerarLucro" method="post">
			<div class="label-input-container">
				<label for="produto">Produto:</label> <select id="produto"
					name="produto">
					<option value="">Selecione o Produto</option>
					<c:forEach items="${produtos}" var="produto">
						<option value="${produto.codigo}">${produto.nome}</option>
					</c:forEach>
				</select>
			</div>

			<div class="label-input-container">
				<label for="precoCompra">Preço de Compra (R$):</label> <input
					type="text" id="precoCompra" name="precoCompra"
					placeholder="Digite o preço de compra (R$)" value="${precoCompra}">
			</div>

			<div class="label-input-container">
				<label for="margemLucro">Margem de Lucro (%):</label> <input
					type="number" id="margemLucro" name="margemLucro"
					placeholder="Digite a margem de lucro (%)" value="${margemLucro}">
			</div>

			<div class="label-input-container">
				<label for="precoVenda">Preço de Venda (R$):</label> <input
					type="text" id="precoVenda" name="precoVenda" value="${precoVenda}"
					readonly>
			</div>

			<div class="botoes">
				<input type="submit" name="botao" value="Calcular"
					class="botao-calcular"> <input type="submit" name="botao"
					value="Aplicar ao Produto" class="botao-aplicar">
			</div>
		</form>
	</div>
</body>
<c:if test="${not empty erro}">
	<div class="erro">${erro}</div>
</c:if>
<c:if test="${not empty saida}">
	<div class="sucesso">${saida}</div>
</c:if>
</html>