<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css"
	href='<c:url value = "./resources/css/produtomaisvendido.css" />'>
<title>Consulta de Produtos Mais Vendidos</title>
</head>
<body>
	<div class="menu">
		<jsp:include page="menu.jsp"></jsp:include>
	</div>
	<div class="container">
		<h2>Consulta de Produtos Mais Vendidos por Mês</h2>

		<form action="produtomaisvendido" method="post">
			<label for="mes" class="m">Escolha o Mês:</label> <select name="mes"
				id="mes">
				<option value="1">Janeiro</option>
				<option value="2">Fevereiro</option>
				<option value="3">Março</option>
				<option value="4">Abril</option>
				<option value="5">Maio</option>
				<option value="6">Junho</option>
				<option value="7">Julho</option>
				<option value="8">Agosto</option>
				<option value="9">Setembro</option>
				<option value="10">Outubro</option>
				<option value="11">Novembro</option>
				<option value="12">Dezembro</option>
			</select>
			<button class="btn-consultar">Consultar</button>
		</form>

		<c:if test="${not empty consultas}">
			<h3 style="margin-top: 30px;">
				Produto Mais Vendido em
				<c:choose>
					<c:when test="${param.mes == '1'}">Janeiro</c:when>
					<c:when test="${param.mes == '2'}">Fevereiro</c:when>
					<c:when test="${param.mes == '3'}">Março</c:when>
					<c:when test="${param.mes == '4'}">Abril</c:when>
					<c:when test="${param.mes == '5'}">Maio</c:when>
					<c:when test="${param.mes == '6'}">Junho</c:when>
					<c:when test="${param.mes == '7'}">Julho</c:when>
					<c:when test="${param.mes == '8'}">Agosto</c:when>
					<c:when test="${param.mes == '9'}">Setembro</c:when>
					<c:when test="${param.mes == '10'}">Outubro</c:when>
					<c:when test="${param.mes == '11'}">Novembro</c:when>
					<c:when test="${param.mes == '12'}">Dezembro</c:when>
				</c:choose>
			</h3>
			<table class="table_produtos_vendidos">
				<thead>
					<tr>
						<th>Nome do Produto</th>
						<th>Quantidade Vendida</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="produto" items="${consultas}">
						<tr>
							<td>${produto.nome}</td>
							<td>${produto.quantidadeVendida}</td>
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
	</div>
</body>
</html>