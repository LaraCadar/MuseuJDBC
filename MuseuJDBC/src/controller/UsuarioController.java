package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class usuarioController
 */
@WebServlet("/usuarioController")
public class UsuarioController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UsuarioController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String nome, data, sexo, cpf, usuario, email, senha, tipo;
		nome = request.getParameter("nameUser");
		data=request.getParameter("birthDate");
		sexo=request.getParameter("sex");	
		cpf = request.getParameter("cpf");
		usuario=request.getParameter("user");
		email=request.getParameter("email");	
		senha=request.getParameter("password");	
		tipo=request.getParameter("typeOfUser");			
		
		
		if(nome.isEmpty() || data.isEmpty() || sexo.isEmpty() || cpf.isEmpty() || usuario.isEmpty() || email.isEmpty() || senha.isEmpty() || tipo.isEmpty() ) {
			response.getWriter().println("Campos Obrigatórios: ");
			if(request.getParameter("nameUser").isEmpty() ){
				response.getWriter().println(" < Nome >");
			}
			if(data.isEmpty()){
				response.getWriter().println(" < Data de Nascimento > ");
			}
			if(sexo.isEmpty() ){
				response.getWriter().println("< Sexo >");
			}

			if(cpf.isEmpty() ){
				response.getWriter().println(" < CPF >");
			}
			if(usuario.isEmpty()){
				response.getWriter().println(" < Nome de usuário > ");
			}
			if(email.isEmpty() ){
				response.getWriter().println("< E-mail >");
			}
			if(senha.isEmpty()){
				response.getWriter().println(" < Senha > ");
			}
			if(tipo.isEmpty() ){
				response.getWriter().println("< Tipo de usuário >");
			}
		}
		else{
			request.getRequestDispatcher("resultadoUsuario.jsp").forward(request, response);			

		}
	}

}
