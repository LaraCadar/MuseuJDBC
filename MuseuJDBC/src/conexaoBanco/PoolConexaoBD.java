package conexaoBanco;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Hashtable;
import java.util.Vector;


/**
 * Gerenciador de conex�es ao banco de dados
 * Mant�m um pool de conex�es ativas com o banco de maneira que n�o
 * seja necess�rio se estabelecer uma nova conx�o a cada requisi��o recebida
 * @author Cris
 *
 */
public class PoolConexaoBD {

	/**
	 * Pool de conex�es de uma a�plica��o espec�fica
	 * Assim, cada aplica��o Web pode manter um conjunto distinto de conex�es
	 * (caso contr�rio, todas as aplica��es que forem executadas em uma mesma
	 * inst�ncia da m�quina virtual ter�o que compartilhar o mesmo Pool).
	 * @author Cris
	 *
	 */
	private static class PoolConexaoBDAplicacao{
		private String nomeAplicacao = null; // Nome da aplica��o
		private Vector nomePool = null; // Pool de conex�es dessa aplica��o

		//Url, usuario e senha para abrir novas conex�es
		private String nomeUrl = null; 
		private String nomeUsuario = null;
		private String senha = null;

		/**
		 * Tamanho m�ximo desse pool: dessa forma evitamos que , caso haja
		 * um erro na aplica��o, novas cone��es sejam abertas indevidamente
		 * at� derrubar o servidor de banco de dados
		 */
		private int tamanhoMaximoPool = 0;

		//timeout para o estabelecimento de novas conex�es
		private int timeout = 0;

		private Driver driverJDBC = null; // Driver jdbc
		private int numeroConexoesAlocadas = 0; // N�mero corrente de conex�es alocadas

		/**
		 * Inicializa estruturas locais: cadastra driver jdbc e 
		 * abre a primeira conex�o com a base de dados
		 * @throws PoolConexaoBDException 
		 */
		public PoolConexaoBDAplicacao(String nomeAplicacao, String driverJdbc, String nomeBD,
				String url, String usuario, String senha, int tamanhoMaximoPool, int timeout) throws PoolConexaoBDException{
			if(nomeAplicacao != null){
				if((driverJdbc != null) && (nomeBD != null) && (url != null) && 
						(usuario != null) && (senha != null) && (tamanhoMaximoPool > 0) && 
						(timeout > 0)){
					this.nomeAplicacao = nomeAplicacao;
					this.nomePool = new Vector<Connection>();
					this.nomeUrl = url;
					this.nomeUsuario = usuario;
					this.senha = senha;
					this.tamanhoMaximoPool = tamanhoMaximoPool;
					this.timeout = timeout;

					//Carregando / cadastrando o driver JDBC
					try{
						driverJDBC = (Driver)Class.forName(driverJdbc).newInstance();
						DriverManager.registerDriver(driverJDBC);
					} catch(Exception e) {
						throw new PoolConexaoBDException(nomeAplicacao, "N�o foi poss�vel carregar/cadastrar driver jdbc");
					}

					//Inicializando pool com uma conex�o
					try {
						nomePool.addElement(alocaNovaConexao());
						numeroConexoesAlocadas = 1;
					} catch(Exception e) {
						destroiPool();
						throw new PoolConexaoBDException(nomeAplicacao, "N�o foi poss�vel criar a primeira conex�o: " + e.getMessage());
					}
				}
				else throw new PoolConexaoBDException(nomeAplicacao, "par�metros inv�lidos");
			}
		}

		/**
		 * Aloca uma nova conex�o com a base de dados
		 * OBS: esse m�todo precisa ser "synchronized" para evitar
		 * probelmas de concorr�ncia no acesso ao Pool
		 */
		private synchronized Connection alocaNovaConexao() throws PoolConexaoBDException {
			Connection conexao = null;
			try {
				conexao = DriverManager.getConnection(nomeUrl, nomeUsuario, senha);
			} catch (Exception e) {
				throw new PoolConexaoBDException(nomeAplicacao, " N�o foi poss�vel abrir nova conex�o: " + " a raz�o foi \"" + e.getMessage() + "\"");
			}

			return conexao;		
		}
		
		/**
		 * Retorna, se poss�vel, uma conex�o com a base de dados
		 * (ou diretamente do pool ou uma nova conex�o)
		 * OBS: esse m�todo precisa ser synchronized para evitar
		 * problemas de concorr�ncia no acesso ao Pool
		 */
		public synchronized Connection alocaConexao () throws PoolConexaoBDException {
			Connection conexao = null;
			if(nomePool != null) {
				if(nomePool.size() > 0) {
					//Se o pool de conex�es n�o estiver vazio, podemos
					//retirra dele uma conex�o previamente estabelecida
					conexao = (Connection) nomePool.firstElement();
					nomePool.removeElementAt(0);
					
					try {
						if(conexao.isClosed()) {
							conexao = null;
						
						}
					} catch (Exception e) { conexao = null;}
					
					if(conexao == null) {
						conexao = alocaConexao();
					}
				}
				else if(numeroConexoesAlocadas < tamanhoMaximoPool){
					//... caso contr�rio, se ainda n�o tivemos atingido o tamanho
					//m�ximo do pool, podemos estabelecer uma nova conex�o
					conexao = alocaNovaConexao();
				}
				else {
					throw new PoolConexaoBDException(nomeAplicacao, "n�mero m�ximo de conex�es atingido");
				}
			}
			
			if(conexao != null) {
				numeroConexoesAlocadas ++;
			}
			
			return conexao;
		}
		
		/**
		 * Libera uma conex�o previamente alocada (retorna essa conex�o para o pool)
		 * OBS: esse m�todo precisa ser synchronized para evitar problemas de 
		 * concorr�ncia no acesso ao Pool
		 */
		@SuppressWarnings("unchecked")
		public synchronized void liberaConexao(Connection conexao) {
			if(nomePool != null) {
				try {
					if(!conexao.isClosed()) {
						nomePool.addElement(conexao);
					}
				} catch (Exception e) {
					
				}
				numeroConexoesAlocadas --;
			}
		}
		
		/**
		 * Retorna o n�mero corrente de conex�es alocadas
		 */
		public synchronized int obtemNumeroConexoesAlocadas() {
			return numeroConexoesAlocadas;
		}
		
		/**
		 * Destr�i pool de conex�es (fechando todas as conex�es abertas)
		 */
		public synchronized void destroiPool() throws PoolConexaoBDException {
			if(nomeAplicacao != null) {
				for(int i = 0; i < nomePool.size(); i++) {
					Connection conexao = (Connection) nomePool.elementAt(i);
					try {
						if(conexao != null) {
							conexao.close();
						}
					} catch(Exception e) {}
				}
				
				nomePool.removeAllElements();
				
				try{
					DriverManager.deregisterDriver(driverJDBC);
				} catch(Exception e) {
					throw new PoolConexaoBDException(nomeAplicacao, "n�o foi poss�vel descadastrar driver jdbc");
				}
				
			}
		}
	}

	
	//HashTable com mapeamento entre nomes das aplica��es e seus respectivos pools
	private static Hashtable poolsAplicacoes = new Hashtable();
	
	/**
	 * Cria um pool a partir dos seguintes dados: nome da aplica��o,
	 * nome do driver de conexao a base de dados, nome da base, url / usuario / senha
	 * de conex�o, tamanho m�ximo do pool e timeout de conex�o a base de dados.
	 * @throws PoolConexaoBDException 
	 */
	public  static synchronized void criaPoolAplicacao(String nomeAplicacao, String driverJdbc,
														String nomeBD, String url, String usuario, String senha,
														int tamanhoMaximoPool, int timeOut) throws PoolConexaoBDException {
		if(nomeAplicacao != null) {
			PoolConexaoBDAplicacao l_pool = (PoolConexaoBDAplicacao)poolsAplicacoes.get(nomeAplicacao);
			if(l_pool != null) {
				l_pool.destroiPool();
			}
			
			l_pool = new PoolConexaoBDAplicacao(nomeAplicacao, driverJdbc, nomeBD, url, usuario, senha, tamanhoMaximoPool, timeOut);
			poolsAplicacoes.put(nomeAplicacao, l_pool);
		}
	}
	
	//Veja PoolConexaoBDAplicacao.alocaConexao
	public static Connection alocaConexao(String nomeAplicacao) throws PoolConexaoBDException {
		PoolConexaoBDAplicacao l_pool = (PoolConexaoBDAplicacao) poolsAplicacoes.get(nomeAplicacao);
		if(l_pool != null) {
			return l_pool.alocaConexao();
		}
		else {
			return null;
		}
	}
	
	//veja PoolConexaoBDAplicacao.obtemNumConsAlocadas
	public static int obtemNumConexoesAlocadas(String nomeAplicacao) {
		PoolConexaoBDAplicacao l_pool = (PoolConexaoBDAplicacao) poolsAplicacoes.get(nomeAplicacao);
		
		if(l_pool != null) {
			return l_pool.obtemNumeroConexoesAlocadas();
		}
		else {
			return -1;
		}
	}
	
	//Veja PoolConexaoBDAplicacao.liberaConexao
	public static void liberaConexao(String nomeAplicacao, Connection conexao) {
		PoolConexaoBDAplicacao l_pool = (PoolConexaoBDAplicacao) poolsAplicacoes.get(nomeAplicacao);
		if(l_pool != null) {
			l_pool.liberaConexao(conexao);
		}
	}
	
	//Vide PoolConexaoBDAplicacao.destroiPool
	public static synchronized void destroiPool(String nomeAplicacao) throws PoolConexaoBDException {
		PoolConexaoBDAplicacao l_pool = (PoolConexaoBDAplicacao) poolsAplicacoes.get(nomeAplicacao);
		
		if(l_pool != null) {
			l_pool.destroiPool();
			poolsAplicacoes.remove(nomeAplicacao);
		}
	}
}
