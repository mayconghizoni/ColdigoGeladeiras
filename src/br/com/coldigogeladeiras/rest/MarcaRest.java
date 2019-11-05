package br.com.coldigogeladeiras.rest;

import java.util.List;
import java.sql.Connection;
import java.util.ArrayList;

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

import br.com.coldigogeladeiras.bd.Conexao;
import br.com.coldigogeladeiras.jdbc.JDBCMarcaDAO;
import br.com.coldigogeladeiras.modelo.Marca;

@Path("marca")
public class MarcaRest extends UtilRest{

	@GET
	@Path("/buscar")
	@Consumes("application/*")
	@Produces(MediaType.APPLICATION_JSON)
	public Response buscar(@QueryParam("valorBusca") String nome) {
		
		try {
			List<Marca> listaMarcas = new ArrayList<Marca>();
			
			Conexao conec = new Conexao();//Instancia o objeto conec com os parâmetros da conexão com o bd
			Connection conexao = conec.abrirConexao();
			JDBCMarcaDAO jdbcMarca = new JDBCMarcaDAO(conexao);// Instancia objeto jdbcMarca para executar as instruções SQL 
			listaMarcas = jdbcMarca.buscar(nome); // Armazena os dados retirados do método buscar () na listaMarcar 
			
			conec.fecharConexao();
			
			return this.buildResponse(listaMarcas); //Retorna lista de marcas armazenadas em um JSON
			
		}catch(Exception e){
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
		
	}
	
	@POST
	@Path("/inserir")
	@Consumes("application/*")
	public Response inserir(String marcaParam) {
		
		try {
			Marca marca = new Gson().fromJson(marcaParam, Marca.class);
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			
			JDBCMarcaDAO jdbcMarca = new JDBCMarcaDAO(conexao);
			boolean retorno = jdbcMarca.inserir(marca);
			
			String msg = "";
			
			if (retorno) {
				msg = "Produto cadastrado com sucesso!";
			}else {
				msg = "Erro ao cadastrar produto!";
			}
			
			conec.fecharConexao();
			
			return this.buildResponse(msg);
		}catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}
	
	@DELETE
	@Path("/excluir/{id}")
	@Consumes("application/*")
	public Response excluir(@PathParam("id") int id) {
		
		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		JDBCMarcaDAO jdbcMarca = new JDBCMarcaDAO(conexao);
		
		boolean retornoIntegridade = jdbcMarca.verificaIntegridade(id);
		
		if(retornoIntegridade) {
			try {
				
				boolean retorno = jdbcMarca.deletar(id);
				
				conec.fecharConexao();
				
				if(retorno){
					return this.buildResponse("Marca excluída com sucesso!");
				}else{
					return this.buildErrorResponse("Erro ao excluir marca!");
				}
				
			}catch(Exception e) {
				e.printStackTrace();
				return this.buildErrorResponse(e.getMessage());
			}
		}else {
			conec.fecharConexao();
			return this.buildErrorResponse("Existem produtos cadastrados com essa marca. Não será possivel deletar a marca escolhida!");
		}
		
		
		
	}
	
	@GET
	@Path("/buscarPorId")
	@Consumes("application/*")
	@Produces(MediaType.APPLICATION_JSON)
	public Response buscarPorId(@QueryParam("id") int id) {
		
		System.out.println(id);
		
		try {
			Marca marca = new Marca();
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCMarcaDAO jdbcMarca = new JDBCMarcaDAO(conexao);
			
			marca = jdbcMarca.buscarPorId(id);
			
			conec.fecharConexao();
			
			return this.buildResponse(marca);
			
		}catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}

	}
	
	@PUT
	@Path("/alterar")
	@Consumes("application/*")
	public Response alterar(String marcaParam) {
		
		try {
			
			Marca marca = new Gson().fromJson(marcaParam, Marca.class);
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCMarcaDAO jdbcMarca = new JDBCMarcaDAO(conexao);
			
			boolean retorno = jdbcMarca.alterar(marca);
			
			conec.fecharConexao();
			
			if(retorno) {
				return this.buildResponse("Marca alterada com sucesso!");
			}else {
				return this.buildErrorResponse("Erro ao alterar marca.");
			}
			
		}catch(Exception e){
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
		
	}
	
	
	
	
	
	

}
