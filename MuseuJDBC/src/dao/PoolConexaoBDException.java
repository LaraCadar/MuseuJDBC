package dao;

/**
 * Exceção para o gerenciador de conexões a banco de dados "PoolConexaoBD"
 * @author Cris
 *
 */
public class PoolConexaoBDException extends Exception{

	private static final long serialVersionUID = -7318769389012257165L;

	public PoolConexaoBDException(String mensagem) {
		super(mensagem);
	}
	
	public PoolConexaoBDException(String nomeAplicacao, String mensagem) {
		super("(" + nomeAplicacao + ")" + mensagem);
	}
	
	
}
