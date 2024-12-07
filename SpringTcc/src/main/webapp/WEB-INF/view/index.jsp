<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
     <link rel="stylesheet" type="text/css" href='<c:url value="./resources/css/index.css" />'>
     
    <title>Home</title>
</head>
<body>
    <nav>
        <div class="menu">
            <jsp:include page="menu.jsp"></jsp:include>
        </div>
    </nav>
    <div class="container"> <!-- Classe atualizada -->
        <h2>Bem-vindo(a) ao Sistema de Perfumaria</h2>
        <p>Aqui, você pode gerenciar seus produtos de maneira eficiente, acompanhar
            suas vendas e otimizar seu estoque. Descubra a facilidade de
            organizar suas operações e maximize seu potencial de vendas com nossa
            plataforma intuitiva.</p>
    </div>
</body>
<style>
@charset "UTF-8";
.container {
    width: 60%; 
    max-width: 800px; 
    margin: 50px auto; 
    background-color: #ffffff; 
    border-radius: 20px; 
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
    padding: 30px; 
    text-align: center; 

}
h2 {
    color: #4a4a4a; 
    font-size: 28px; 
    font-weight: 700; 
    margin-bottom: 20px; 
    background-color: transparent;
}

p {
    color: #555; 
    font-size: 18px; 
    line-height: 1.6; 
    margin: 0; 
    background-color: transparent; 
}
</style>
</html>
