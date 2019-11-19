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
	public Response buscar(@QueryParam("valorBusca") String param) {
		
		try {
			
			List<Marca> listaMarcas = new ArrayList<Marca>();
			
			Conexao conec = new Conexao();//Instancia o objeto conec com os parâmetros da conexão com o bd
			Connection conexao = conec.abrirConexao();
			JDBCMarcaDAO jdbcMarca = new JDBCMarcaDAO(conexao);// Instancia objeto jdbcMarca para executar as instruções SQL 
			listaMarcas = jdbcMarca.buscar(param); // Armazena os dados retirados do método buscar na listaMarcar 
			
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
			
			conec.fecharConexao();
			
			if (retorno) {
				return this.buildResponse("Produto cadastrado com sucesso!");
			}else {
				return this.buildErrorResponse("Erro ao cadastrar marca!");
			}			
			

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
		
		boolean retornoExistencia = jdbcMarca.verificaExistencia(id);
		
		if(retornoExistencia) {
			boolean retornoIntegridade = jdbcMarca.verificaProdutosCadastrados(id);
			
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
		}else {
			conec.fecharConexao();
			return this.buildErrorResponse("Esta marca não existe. Atualize a página e verifique sua existência!");
		}
		
	}
	
	@GET
	@Path("/buscarPorId/{id}")
	@Consumes("application/*")
	@Produces(MediaType.APPLICATION_JSON)
	public Response buscarPorId(@PathParam("id") int id) {
		
		try {
			Marca marca = new Marca();
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCMarcaDAO jdbcMarca = new JDBCMarcaDAO(conexao);
			
			boolean retornoExistencia = jdbcMarca.verificaExistencia(id);
			
			if(retornoExistencia) {
				marca = jdbcMarca.buscarPorId(id);
				
				conec.fecharConexao();
				
				return this.buildResponse(marca);
			}else {
				conec.fecharConexao();
				return this.buildErrorResponse("Esta marca não existe. Atualize a página e verifique sua existência!");
			}
			
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
			
			boolean retornoExistencia = jdbcMarca.verificaExistencia(marca.getId());
			
			if(retornoExistencia) {
				
				boolean retorno = jdbcMarca.alterar(marca);
				
				conec.fecharConexao();
				
				if(retorno) {
					return this.buildResponse("Marca alterada com sucesso!");
				}else {
					return this.buildErrorResponse("Erro ao alterar marca.");
				}
				
			}else {
				conec.fecharConexao();
				return this.buildErrorResponse("Esta marca não existe. Atualize a página e verifique sua existência!");
			}
			
		}catch(Exception e){
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
		
	}
	
	@PUT
	@Path("/alterarStatus/{id}")
	@Consumes("application/*")
	public Response alterarStatus(@PathParam("id") int id) {
		
		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		JDBCMarcaDAO jdbcMarca = new JDBCMarcaDAO(conexao);
		
		boolean retornoExistencia = jdbcMarca.verificaExistencia(id);
		
		if(retornoExistencia) {
			boolean retornoIntegridade = jdbcMarca.verificaProdutosCadastrados(id);
			
			if(retornoIntegridade) {
				try {
					
					boolean ativo = jdbcMarca.verificaStatus(id);
					
					int statusAtualizado;
					
					if(ativo) {
						statusAtualizado = 0;
					}else {
						statusAtualizado = 1;
					}
					
					boolean retorno = jdbcMarca.ativoInativo(statusAtualizado, id);
					
					conec.fecharConexao();
					
					if(retorno){
						return this.buildResponse("Status alterado com sucesso!");
					}else{
						return this.buildErrorResponse("Erro ao alterar status da marca!");
					}
					
				}catch(Exception e) {
					e.printStackTrace();
					return this.buildErrorResponse(e.getMessage());
				}
			}else {
				conec.fecharConexao();
				return this.buildErrorResponse("Existem produtos cadastrados com essa marca. Não será possivel deixá-la inativa!");
			}
		}else {
			conec.fecharConexao();
			return this.buildErrorResponse("Esta marca não existe. Atualize a página e verifique sua existência!");
		}
	
	}
	
	
	
	
	
	

}
