package br.com.coldigogeladeiras.rest;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
		
			String json = new Gson().toJson(listaProdutos); // Converte lista de produtos para String Json
			return this.buildResponse(json); // Retorna Json para frontend
			
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
			
			String msg = "";
			if(retorno){ 
				msg = "Produto excluído com sucesso!"; //Se o retorno for verdadeiro armazena msg de sucesso
			}else {
				msg = "Erro ao excluir produto."; //Se não, armazena msg de erro
			}
			
			conec.fecharConexao(); //fecha conexao com bd
			
			return this.buildResponse(msg); //monta response e retorna via json para o front-end
			
		}catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
		
	}
	
	@GET
	@Path("/buscarPorId")
	@Consumes("application/*")
	@Produces(MediaType.APPLICATION_JSON)
	public Response buscarPorId(@QueryParam("id") int id) {
			
		try {
			Produto produto = new Produto();
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCProdutoDAO jdbcProduto = new JDBCProdutoDAO(conexao);
			
			produto = jdbcProduto.buscarPorId(id);
			
			conec.fecharConexao();
			
			return this.buildResponse(produto);
			
		}catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}

}
