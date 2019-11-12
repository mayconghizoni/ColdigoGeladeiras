package br.com.coldigogeladeiras.rest;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import br.com.coldigogeladeiras.bd.Conexao;
import br.com.coldigogeladeiras.jdbc.JDBCProdutoDAO;
import br.com.coldigogeladeiras.jdbc.JDBCMarcaDAO;
import br.com.coldigogeladeiras.modelo.Produto;


@Path("produto")
public class ProdutoREST extends UtilRest {
	
	@POST
	@Path("/inserir")
	@Consumes("application/*")
	public Response inserir(String produtoParam) {
		
		try {
			
			Produto produto = new Gson().fromJson(produtoParam, Produto.class); //Converte o JSON recebido para um objeto	
			Conexao conec = new Conexao(); 
			Connection conexao = conec.abrirConexao(); //Inicia conexao com o BD
			
			JDBCMarcaDAO jdbcMarca = new JDBCMarcaDAO(conexao);
			boolean retornoExistencia = jdbcMarca.verificaExistencia(produto.getMarcaId());
			
			if(retornoExistencia) {
				
				JDBCProdutoDAO jdbcProduto = new JDBCProdutoDAO(conexao); 
				boolean retorno = jdbcProduto.inserir(produto); //O objeto jdbcProduto faz a inserção no bd e armazena o resultado num booleano
				
				String msg = "";
				
				if(retorno == true) { //Verifica se o valor retornado é true e armazena a mensagem a ser retonada pro cliente numa String
					msg = "Produto cadastrado com sucesso!"; 
				} else {
					msg = "Erro ao cadastrar produto!";
				}
				
				conec.fecharConexao(); // Fecha conexao com BD
				
				return this.buildResponse(msg); // Converte a mensagem para formato JSON e retorna esse valor
				
			}else {
				conec.fecharConexao();
				return this.buildErrorResponse("Marca seleciona não existe. Atualize a página do seu navegador e tente novamente. ");
			}
			
		}catch(Exception e){
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}
	
	@GET
	@Path("/buscar")
	@Consumes("application/*")
	@Produces(MediaType.APPLICATION_JSON)
	public Response buscarPorNome(@QueryParam("valorBusca") String nome) {
		try {
			
			List<JsonObject> listaProdutos = new ArrayList<JsonObject>();
			
			Conexao conec = new Conexao(); // Instancia obj conec 
			Connection conexao = conec.abrirConexao(); //Abre conexao com bd
			JDBCProdutoDAO jdbcProduto = new JDBCProdutoDAO(conexao);
			listaProdutos = jdbcProduto.buscarPorNome(nome); //Retorna lista de produtos passando "nome" como parametro do método
			conec.fecharConexao(); // Fecha conexao
		
//			String json = new Gson().toJson(listaProdutos); // Converte lista de produtos para String Json
			return this.buildResponse(listaProdutos); // Retorna Json para frontend
			
		}catch(Exception e){
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}
	
	@DELETE 
	@Path("/excluir/{id}") //Define url do método
	@Consumes("application/*") //Avisa que será consumido uma informação vinda da url
	public Response excluir(@PathParam("id") int id) { //Para funcionar o método precisa receber da url um parametro do tipo int / id
		
		try { 
			
			Conexao conec = new Conexao(); //Cria obj conec
			Connection conexao = conec.abrirConexao(); //Abre conexao com bd
			JDBCProdutoDAO jdbcProduto = new JDBCProdutoDAO(conexao); 
			
			boolean retorno = jdbcProduto.deletar(id); //Chama o método deletar passando id como parametro e retorna um booleano
			
			conec.fecharConexao(); //fecha conexao com bd
			
			if(retorno){ 
				return this.buildResponse("Produto excluído com sucesso!"); //monta response e retorna via json para o front-end
			}else {
				return this.buildErrorResponse("Erro ao excluir produto.");
			}			
			
		}catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
		
	}
	
	@GET 														//Define método de recebimento da informação
	@Path("/buscarPorId") 										//Define caminho do método
	@Consumes("application/*") 									//Anotação que define que esse método recebe uma informação do front
	@Produces(MediaType.APPLICATION_JSON) 						//Anotação que esse método gera um arquivo do tipo Json
	public Response buscarPorId(@QueryParam("id") int id) {		//Recebe id do front end do tipo inteiro
			
		try {
			Produto produto = new Produto();	//Cria um novo objeto produto do tipo produto
			Conexao conec = new Conexao();		//Cria objeto conec do tipo conexao
			Connection conexao = conec.abrirConexao();		//abre uma nova conexao com banco de dados a partir do método abrirConexao
			JDBCProdutoDAO jdbcProduto = new JDBCProdutoDAO(conexao); 	//instancia um objeto jdbcProduto passando como parametro pro seu Construtor o objeto conexao
			
			produto = jdbcProduto.buscarPorId(id); 		//A partir do método buscarPorId retorna um objeto do tipo produto e armazena no objeto produto. É passado como parâmetor o id.
			
			conec.fecharConexao(); 		//Fecha conexao com banco de dados
			
			return this.buildResponse(produto); //Com o método buildResponse monta a response no tipo JSon e retorna para o front. Passando como parametro o objeto produto.
			
		}catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}
	
	@PUT 											//Define o método que os dados deverão ser recebidos
	@Path("/alterar") 								//Define url que o método tem
	@Consumes("application/*") 						//Informa que este método recebe algo do frontend
	public Response alterar(String produtoParam) {	//Recebe produto do frontend coomo parametro
		
		try {
			
			Produto produto = new Gson().fromJson(produtoParam, Produto.class); //Converte arquivo Json recebido como parametro com o fromJson e o armazena em um objeto do tipo produto.
			Conexao conec = new Conexao(); //Cria um novo objeto do tipo conexão
			Connection conexao = conec.abrirConexao(); //Abre uma nova conexão com bd e armazena seus dados num objeto Connection
			JDBCProdutoDAO jdbcProduto = new JDBCProdutoDAO(conexao); //Cria uma nova instancia de JDBCPordutoDAO passando como parametro a conexao com o banco de dados.
			 
			boolean retorno = jdbcProduto.alterar(produto); //Chama o método alterar da classe JDBCProdutoDAO para fazer update nos dados no banco -- o seu retorno é armazenado em uma várialvel booleana

			//fecha conexao com banco de dados
			conec.fecharConexao();
			
			if(retorno) {
				return this.buildResponse("Produto alterado com sucesso!");
			}else {
				return this.buildErrorResponse("Erro ao alterar produto");
			}
			
			
		}catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
		
	}
	

}
