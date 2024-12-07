<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<link rel="stylesheet" type="text/css"
	href='<c:url value="./resources/css/demanda.css" />'>
<title>Consultar Demanda</title>
</head>
<body
	style="background-image: url('./resources/imagens/imagem_fundo.png')"
	class="tela_estoque">
	<div class="menu">
		<jsp:include page="menu.jsp" />
	</div>
	<br />
	<div align="center" class="container">
		<form action="demanda" method="post" class="Item">
			<h2>Consultar Demanda</h2>
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
					</select>
					</td>
				</tr>
				<tr>
					<td style="display: flex; align-items: center;"><label
						for="mes">Mês:</label> <select id="mes" name="mes">
							<option value="janeiro">Janeiro</option>
							<option value="fevereiro">Fevereiro</option>
							<option value="março">Março</option>
							<option value="abril">Abril</option>
							<option value="maio">Maio</option>
							<option value="junho">Junho</option>
							<option value="julho">Julho</option>
							<option value="agosto">Agosto</option>
							<option value="setembro">Setembro</option>
							<option value="outubro">Outubro</option>
							<option value="novembro">Novembro</option>
							<option value="dezembro">Dezembro</option>
					</select></td>
				</tr>

			</table>
			<tr class="botoes2">
				<td><input type="submit" class="botoes2" name="botao"
					value="Calcular Previsao de Demanda"></td>
			</tr>
			<div align="center" class="erro">
				<c:if test="${not empty erro}">
					<h2>
						<b><c:out value="${erro}" /></b>
					</h2>
				</c:if>
			</div>
			<div align="center" class="sucesso">
				<c:if test="${not empty saida}">
					<h3>
						<b><c:out value="${saida}" /></b>
					</h3>
				</c:if>
			</div>
			<c:if test="${not empty demandas}">
				<table class="table_round">
					<thead>
						<tr>
							<th>Produto Codigo</th>
							<th>Produto Nome</th>
							<th>N° Mês</th>
							<th>Nome Mês</th>
							<th>QuantidadeVendas</th>
							<th>TotalVendas</th>
							<th>Demanda</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="d" items="${demandas}" varStatus="status">
							<tr>
								<td><c:out value="${d.produto.codigo}" /></td>
								<td><c:out value="${d.produto.nome}" /></td>
								<td><c:out value="${d.mes}" /></td>
								<td><c:out value="${d.nomeMes}" /></td>
								<td><c:out value="${d.quantidadeVendas}" /></td>
								<td><c:out value="${d.totalVendas}" /></td>
								<td><c:out value="${d.demanda}" /></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</c:if>

		</form>
	</div>
	<br />
</body>
</html>