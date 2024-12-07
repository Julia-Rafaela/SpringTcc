<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<link rel="stylesheet" type="text/css"
	href='<c:url value="./resources/css/menu.css"/>'>
<title>Menu com Dropdown</title>
</head>
<body>
	<nav>
		<a href="index"> <img src="./imagens/logo.webp" alt="Logo">
		</a>
		<ul class="dropdown-menu">
			<li><a href="index">Home</a></li>
			<li><a href="produto">Produto</a></li>
			<li><a href="estoque">Estoque</a></li>
			<li><a href="#">Vendas &#9660;</a>
				<ul class="dropdown-submenu">
					<li><a href="item">Inserir  &nbsp; Itens </a></li>
					<li><a href="venda">Venda</a></li>
				</ul></li>
			<li><a href="#">Consultas &#9660;</a>
				<ul class="dropdown-submenu">
					<li><a href="produtomaisvendido">Produtos mais Vendidos</a></li>
					<li><a href="demanda">Demanda</a></li>
				</ul></li>
		</ul>
	</nav>
</body>
</html>