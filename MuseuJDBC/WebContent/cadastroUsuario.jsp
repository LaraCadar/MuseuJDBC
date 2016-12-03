<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">	
		<link type="text/css" href="style.css" rel="stylesheet"/>
		<title>Insert title here</title>
	</head>
	
	<body>
	<header>
				<div id="logo">
				</div>
				<div id="menu">			
					<nav>
						<ul>
							<li><a href="index.jsp" target="_self">Login</a></li>
							<li><a href="cadastroObras.jsp" target="_self">Cadastro de Obras</a></li>
							<li><a class="active" href="cadastroUsuario.jsp" target="_self">Cadastro de Usuario</a></li>
						</ul>			
					</nav>
				</div>
			</header>
			
			
			<br><br>
			
			<div class="cadastroUsuario">
			<form method="POST" action="usuariocontroller">
				<fieldset>
					<legend>Cadastro de Usuário</legend>
						<br>Nome:
						<br><input type="text" name="nameUser" required placeholder="Nome Completo" autofocus><br>
						<br>Data de Nascimento:
						<br><input type="date" name="birthDate" required"><br>
						<br>Sexo:
						<br><input type="radio" name="sex" value="Male" id="Male" required> <label for="Male">M</label>
						<input type="radio" name="sex" value="Female" id="Female" required> <label for="Female">F</label><br>
						<br>CPF:
						<br><input type="text" name="cpf" required placeholder="CPF"><br>
						<br>Nome de usuario:
						<br><input type="text" name="user" required placeholder="usuario"><br>
						<br><br>Email:
						<br><input type="email" name="email" required placeholder="Email"><br>
						<br>Senha:
						<br><input type="password" name="password" required placeholder="Senha"><br>
						<br>Tipo de usuário:
						<br><select name="typeOfUser" required>
						<option value="Administrador">Administrador</option>
						<option value="Funcionário">Funcionário</option>
						<option value="Visitante">Visitante</option>
						</select>						
						<br><br><br>
						<input type="submit" value="Submit">
						<button type="reset" value="Reset">Reset</button>
						<a href="index.html" class="send"><button>Voltar</a> </button>
				</fieldset>
			</form>			
		</div>
	</body>
</html>