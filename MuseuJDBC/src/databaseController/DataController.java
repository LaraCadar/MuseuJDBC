package databaseController;

import conexaoBanco.PoolConexaoBD;
import conexaoBanco.PoolConexaoBDException;
import dao.ObraDAO;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Vector;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/dataController")
public class DataController extends HttpServlet {
	private static final long serialVersionUID = 1L;       
  
    public DataController() {
        super();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	try {
		PoolConexaoBD.criaPoolAplicacao("nome", "com.mysql.jdbc.Driver","museuBanco.sql", "localhost", "", "", 10, 10) ;
		Connection con = PoolConexaoBD.alocaConexao("nome");
	} catch (PoolConexaoBDException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		response.getWriter().append("---").append(request.getContextPath());
	}

}
