<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">	
		<link type="text/css" href="style.css" rel="stylesheet"/>
		<title>Cadastro Obras</title>
	</head>
	
	<body>
	<header>
				<div id="logo">
				
				</div>
				<div id="menu">			
					<nav>
						<ul>		
							<li><a href="index.jsp" target="_self">Login</a></li>
							<li><a class="active" href="cadastroObras.jsp" target="_self">Cadastro de Obras</a></li>
							<li><a href="cadastroUsuario.jsp" target="_self">Cadastro de Usuario</a></li>
						</ul>			
					</nav>
				</div>
			</header>		
		<div class="cadastroObra">
			<form method="POST" action="obrascontroller">
				<fieldset>
					<legend>Cadastro de Obra</legend>
						<br>Nome da Obra:
						<br><input type="text" name="nameObject" required placeholder="Nome da Obra" autofocus>
						<br><br>Tipo da Obra:
						<br><select name="typeObject" required>
							<option value="Escrita">Escrita</option>
							<option value="Escultura">Escultura</option>
							<option value="Pintura">Pintura</option>
							<option value="Fotografia">Fotografia</option>
							<option value="Arquitetura">Arquitetura</option>
						</select>
						<br><br>Data em que a obra foi feita:
						<br><input type="date" name="dateObject" required>
						<br><br>Nome do Artista:
						<br><input type="text" name="artist" required placeholder="Nome do artista">
						<br><br>Origem:
						<br><input type="radio" name="source" value="Compra" id="Compra" required> <label for="Compra"> Compra</label>
						<br><input type="radio" name="source" value="Doação" id="Doação" required> <label for="Doação"> Doação</label>
						<br><input type="radio" name="source" value="Outros" id="Outros" required> <label for="Outros"> Outros</label>
						<br><br>Descrição da Obra:
						<br><input type="textarea" name="description" required placeholder="Descrição">
						<br><br><br>
						<input type="submit" value="Submit">
						<button type="reset" value="Reset">Reset</button>
						<a href="index.html" class="send"><button>Voltar</a></button>
				</fieldset>
			</form>			
		</div>
	</body>
	</body>
</html>