package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class LoginController
 */
@WebServlet("/LoginController")
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.reset();
		//response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String nome, senha, tipoUsuario;
		nome = request.getParameter("userid");
		senha=request.getParameter("password");
		tipoUsuario=request.getParameter("type_of_user");		
		if(nome.isEmpty() || senha.isEmpty() || tipoUsuario.isEmpty() ){
			response.getWriter().println("Campos Obrigatórios: ");
			if(nome.isEmpty() ){
				response.getWriter().println(" < Nome >");
			}
			if(senha.isEmpty()){
				response.getWriter().println(" < Senha > ");
			}
			if(tipoUsuario.isEmpty() ){
				response.getWriter().println("< Tipo de usuário >");
			}
		}
		else{
			request.getRequestDispatcher("resultadoLogin.jsp").forward(request, response);			
		}
				
	}

}
