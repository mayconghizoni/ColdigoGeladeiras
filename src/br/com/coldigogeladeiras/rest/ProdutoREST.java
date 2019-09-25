package br.com.coldigogeladeiras.rest;

import java.sql.Connection;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import com.google.gson.Gson;
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
			
//			System.out.print(produtoParam);
			
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

}
