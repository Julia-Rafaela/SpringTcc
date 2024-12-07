<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css"
	href='<c:url value="./resources/css/produto.css" />'>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
<title>Produto</title>
</head>
<body class="tela_produto">
	<div class="menu">
		<jsp:include page="menu.jsp"></jsp:include>
	</div>
	<br />

	<div align="center" class="container">
		<form action="produto" method="post">
			<h2>Cadastro de Produto</h2>
				<table class="campos">
					<tr>
						<td class="title">Código:</td>
						<td><input class="cadastro1" type="text" id="codigo"
							name="codigo" value='<c:out value="${produto.codigo}" />'></td>
						<td><input type="submit" class="buscar" id="botao"
							name="botao" value="Buscar" style="margin-left:-20px; margin-top:-2px;"></td>
					</tr>
					<tr>
						<td class="title">Nome:</td>
						<td colspan="2"><input class="cadastro1" type="text"
							id="nome" name="nome" value='<c:out value="${produto.nome}" />'></td>
					</tr>
					<tr>
						<td class="title">Data de Validade:</td>
						<td colspan="2"><input class="cadastro" type="date"
							id="dataValidade" name="dataValidade"
							value='<c:out value="${produto.dataValidade}" />'></td>
					</tr>
					<tr>
						<td class="title">Marca:</td>
						<td colspan="2"><input class="cadastro1" type="text"
							id="marca" name="marca"
							value='<c:out value="${produto.marca}" />'></td>
					</tr>
					<tr>
						<td class="title">Categoria:</td>
						<td colspan="2"><select class="input_data" id="categoria"
							name="categoria">
								<option value="">Selecione uma Categoria</option>
								<c:forEach var="categoria" items="${categorias}">
									<option value="${categoria.categoria}"
										<c:if test="${categoria.categoria == produto.categoria.categoria}">selected</c:if>>
										<c:out value="${categoria.categoria}" />
									</option>
								</c:forEach>
						</select></td>
					</tr>
					<tr>
						<td class="title">Descrição:</td>
						<td colspan="2"><input class="cadastro1" type="text"
							id="descricao" name="descricao"
							value='<c:out value="${produto.descricao}" />'></td>
					</tr>
					</table>
					<table>
					<tr class="botoes">
						<td><input type="submit" id="cadastrar" name="botao"
							value="Cadastrar"></td>
						<td><input type="submit" id="alterar" name="botao"
							value="Alterar"></td>
						<td><input type="submit" id="listar" name="botao"
							value="Listar"></td>
					</tr>
					<tr>
						<td colspan="3" align="center"><a href="gerarLucro"
							class="button">Gerar Preço com Lucro</a></td>
					</tr>
				</table>
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
				<br />
				<br />
		</form>
	</div>

	<c:if test="${not empty tipoTabela && tipoTabela eq 'Listar'}">
		<c:if test="${not empty produtos}">
			<div align="center">
				<table class="table_round">
					<thead>
						<tr>
							<th>Alterar</th>
							<th>Código</th>
							<th>Nome</th>
							<th>Descrição</th>
							<th>Data de Validade</th>
							<th>Status</th>
							<th>Marca</th>
							<th>Categoria</th>
							<th>Preço</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="p" items="${produtos}">
							<tr>
								<td><input type="radio" name="opcao" value="${p.codigo}"
									onclick="editarProduto(this.value)"
									<c:if test="${p.codigo eq codigoEdicao}">checked</c:if> /></td>
								<td><c:out value="${p.codigo}" /></td>
								<td><c:out value="${p.nome}" /></td>
								<td><c:out value="${p.descricao}" /></td>
								<td><c:out value="${p.dataValidade}" /></td>
								<td><c:out value="${p.status}" /></td>
								<td><c:out value="${p.marca}" /></td>
								<td><c:out value="${p.categoria.categoria}" /></td>
								<td><c:out value="${p.precoFormatado}" /></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<script>
				function editarProduto(codigo) {
					window.location.href = 'produto?cmd=alterar&codigo='
							+ codigo;
				}
			</script>
		</c:if>
	</c:if>
</body>
</html>