<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Insert title here</title>
	</head>
	<body>
		<div class="login">
			<form  method="POST" action="datacontroller">
				<fieldset>
					<legend>Login</legend>
					<br>Nome de Usuário:
					<br><input type="text" name="userid" required placeholder="Nome de Usuário" autofocus>
					<br><br>Senha:
					<br><input type="password" name="password" required placeholder="Senha">
					<br><br>Tipo de usuário:
					<br><select name="type_of_user" required>
						<option value="Administrador">Administrador</option>
						<option value="Funcionário">Funcionário</option>
						<option value="Visitante">Visitante</option>
					</select>
					<br><br><br>
					<input type="submit" value="Submit">
					<button type="reset" value="Reset">Reset</button>
				</fieldset>
			</form>
		</div>
		
	</body>
</html>