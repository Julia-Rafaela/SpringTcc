<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<link rel="stylesheet" type="text/css"
	href='<c:url value="./resources/css/item.css" />'>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
<title>Item</title>
</head>
<body
	style="background-image: url('./resources/imagens/imagem_fundo.png')"
	class="tela_estoque">
	<div class="menu">
		<jsp:include page="menu.jsp" />
	</div>
	<br />
	<div align="center" class="container">
		<form action="item" method="post" class="Item">
			<h2>Adicionar Itens</h2>
			<table>
				<tr>
					<td style="display: flex; align-items: center;">
						<p id="produto">Produto:</p> <select class="cadastro" id="produto"
						name="produto">
							<option value="0">Escolha o Produto</option>
							<c:forEach var="p" items="${produtos}">
								<option value="${p.codigo}">
									<c:out value="${p.nome}" />
								</option>
							</c:forEach>
					</select> <input class="buscar" type="submit" id="botao" name="botao"
						value="Buscar">
					</td>
				</tr>
				<tr>
					<td style="display: flex; align-items: center;">
						<p class="title">Categoria:</p> <select class="cadastro"
						id="categoria" name="categoria">
							<option value="0">Escolha a Categoria</option>
							<c:forEach var="c" items="${categorias}">
								<option value="${c.categoria}">
									<c:out value="${c.categoria}" />
								</option>
							</c:forEach>
					</select>
					</td>
				</tr>
			</table>
			<c:if test="${not empty itens}">
				<table class="table_round">
					<thead>
						<tr>
							<th>Produto Codigo</th>
							<th>Produto Nome</th>
							<th>Marca Produto</th>
							<th>Categoria Produto</th>
							<th>Quantidade Estoque</th>
							<th>Quantidade</th>
							<th>Adicionar</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="i" items="${itens}" varStatus="status">
							<tr>
								<form action="item" method="post">
									<td><input type="number" id="produto${status.count}"
										name="produto${status.index}"
										value='<c:out value="${i.produto.codigo}" />' readonly readonly style="border: none; background: none; outline: none;">
									</td>
									<td><c:out value="${i.produto.nome}" /></td>
									<td><c:out value="${i.produto.marca}" /></td>
									<td><c:out value="${i.categoria.categoria}" /></td>
									<td><c:out value="${i.estoque.quantidade}" /></td>
									<td><input type="number" id="quantidade${status.index}"
										name="quantidade${status.index}"
										value='<c:out value="${i.quantidade}" />'></td>
									<td style="text-align: center;"><input type="hidden"
										name="botao" value="Adicionar">
										<button type="submit" class="adicionar">Adicionar</button></td>
								</form>

							</tr>
						</c:forEach>
					</tbody>
				</table>
			</c:if>
			<div class="erro" align="center">
				<c:if test="${not empty erro}">
					<h2>
						<b><c:out value="${erro}" /></b>
					</h2>
				</c:if>
			</div>
			<div class="sucesso" align="center">
				<c:if test="${not empty saida}">
					<h3>
						<b><c:out value="${saida}" /></b>
					</h3>
				</c:if>
			</div>
		</form>
	</div>
	<br />
</body>
</style>
</html>