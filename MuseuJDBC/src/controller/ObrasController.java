package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ObrasController
 */
@WebServlet("/ObrasController")
public class ObrasController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ObrasController() {
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
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		String nome, tipo, data, artista, origem, descrição;
		nome = request.getParameter("nameObject");
		tipo=request.getParameter("typeObject");
		data=request.getParameter("dateObject");	
		artista = request.getParameter("artist");
		origem=request.getParameter("source");
		descrição=request.getParameter("description");			
		
		
		if(nome.isEmpty() || tipo.isEmpty() || data.isEmpty() || artista.isEmpty() || origem.isEmpty() || descrição.isEmpty()){
			response.getWriter().println("Campos Obrigatórios: ");
			if(nome.isEmpty() ){
				response.getWriter().println(" < Nome >");
			}
			if(tipo.isEmpty()){
				response.getWriter().println(" < Tipo de Obra > ");
			}
			if(data.isEmpty() ){
				response.getWriter().println("< Data da Obra >");
			}

			if(artista.isEmpty() ){
				response.getWriter().println(" < Artista >");
			}
			if(origem.isEmpty()){
				response.getWriter().println(" < Origem > ");
			}
			if(descrição.isEmpty() ){
				response.getWriter().println("< Descrição >");
			}
		}
		else{
			request.getRequestDispatcher("resultadoObra.jsp").forward(request, response);			
		}
	}

}
