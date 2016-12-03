package conexaoBanco;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Hashtable;
import java.util.Vector;


/**
 * Gerenciador de conexões ao banco de dados
 * Mantém um pool de conexões ativas com o banco de maneira que não
 * seja necessário se estabelecer uma nova conxão a cada requisição recebida
 * @author Cris
 *
 */
public class PoolConexaoBD {

	/**
	 * Pool de conexões de uma a´plicação específica
	 * Assim, cada aplicação Web pode manter um conjunto distinto de conexões
	 * (caso contrário, todas as aplicações que forem executadas em uma mesma
	 * instância da máquina virtual terão que compartilhar o mesmo Pool).
	 * @author Cris
	 *
	 */
	private static class PoolConexaoBDAplicacao{
		private String nomeAplicacao = null; // Nome da aplicação
		private Vector nomePool = null; // Pool de conexões dessa aplicação

		//Url, usuario e senha para abrir novas conexões
		private String nomeUrl = null; 
		private String nomeUsuario = null;
		private String senha = null;

		/**
		 * Tamanho máximo desse pool: dessa forma evitamos que , caso haja
		 * um erro na aplicação, novas coneções sejam abertas indevidamente
		 * até derrubar o servidor de banco de dados
		 */
		private int tamanhoMaximoPool = 0;

		//timeout para o estabelecimento de novas conexões
		private int timeout = 0;

		private Driver driverJDBC = null; // Driver jdbc
		private int numeroConexoesAlocadas = 0; // Número corrente de conexões alocadas

		/**
		 * Inicializa estruturas locais: cadastra driver jdbc e 
		 * abre a primeira conexão com a base de dados
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
						throw new PoolConexaoBDException(nomeAplicacao, "Não foi possível carregar/cadastrar driver jdbc");
					}

					//Inicializando pool com uma conexão
					try {
						nomePool.addElement(alocaNovaConexao());
						numeroConexoesAlocadas = 1;
					} catch(Exception e) {
						destroiPool();
						throw new PoolConexaoBDException(nomeAplicacao, "Não foi possível criar a primeira conexão: " + e.getMessage());
					}
				}
				else throw new PoolConexaoBDException(nomeAplicacao, "parâmetros inválidos");
			}
		}

		/**
		 * Aloca uma nova conexão com a base de dados
		 * OBS: esse método precisa ser "synchronized" para evitar
		 * probelmas de concorrência no acesso ao Pool
		 */
		private synchronized Connection alocaNovaConexao() throws PoolConexaoBDException {
			Connection conexao = null;
			try {
				conexao = DriverManager.getConnection(nomeUrl, nomeUsuario, senha);
			} catch (Exception e) {
				throw new PoolConexaoBDException(nomeAplicacao, " Não foi possível abrir nova conexão: " + " a razão foi \"" + e.getMessage() + "\"");
			}

			return conexao;		
		}
		
		/**
		 * Retorna, se possível, uma conexão com a base de dados
		 * (ou diretamente do pool ou uma nova conexão)
		 * OBS: esse método precisa ser synchronized para evitar
		 * problemas de concorrência no acesso ao Pool
		 */
		public synchronized Connection alocaConexao () throws PoolConexaoBDException {
			Connection conexao = null;
			if(nomePool != null) {
				if(nomePool.size() > 0) {
					//Se o pool de conexões não estiver vazio, podemos
					//retirra dele uma conexão previamente estabelecida
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
					//... caso contrário, se ainda não tivemos atingido o tamanho
					//máximo do pool, podemos estabelecer uma nova conexão
					conexao = alocaNovaConexao();
				}
				else {
					throw new PoolConexaoBDException(nomeAplicacao, "número máximo de conexões atingido");
				}
			}
			
			if(conexao != null) {
				numeroConexoesAlocadas ++;
			}
			
			return conexao;
		}
		
		/**
		 * Libera uma conexão previamente alocada (retorna essa conexão para o pool)
		 * OBS: esse método precisa ser synchronized para evitar problemas de 
		 * concorrência no acesso ao Pool
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
		 * Retorna o número corrente de conexões alocadas
		 */
		public synchronized int obtemNumeroConexoesAlocadas() {
			return numeroConexoesAlocadas;
		}
		
		/**
		 * Destrói pool de conexões (fechando todas as conexões abertas)
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
					throw new PoolConexaoBDException(nomeAplicacao, "não foi possível descadastrar driver jdbc");
				}
				
			}
		}
	}

	
	//HashTable com mapeamento entre nomes das aplicações e seus respectivos pools
	private static Hashtable poolsAplicacoes = new Hashtable();
	
	/**
	 * Cria um pool a partir dos seguintes dados: nome da aplicação,
	 * nome do driver de conexao a base de dados, nome da base, url / usuario / senha
	 * de conexão, tamanho máximo do pool e timeout de conexão a base de dados.
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
