package br.com.coldigogeladeiras.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import com.google.gson.Gson;

public class UtilRest {
	
	/*  Abaxio o metodo responsavel por enviar a resposta ao cliente sobre a transação realizada 
	 * (inclusão consulta edicao ou exclusao) caso ela seja realizada com sucesso.
	 * Repare que o método em questão aguarda que seja repassado um conteúdo que será referênciado por
	 * um objeto chamado result*/
	
	public Response buildResponse(Object result) {
		try {
			
			/*
			 * retorna o objeto de resposta com status 200 (ok), tendo em seu corpo
			 * valorResposta (que consiste no objeto convertido para JSON).
			 */
			
			String valorResposta = new Gson().toJson(result);
			return Response.ok(valorResposta).build();
		}catch(Exception ex) {
			ex.printStackTrace();
			//Se algo der errado acima, cria Response de erro
			return this.buildErrorResponse(ex.getMessage());
		}
	}
	
	/* Abaxio o metodo responsavel por enviar a resposta ao cliente sobre a transação realizada 
	 * (inclusão consulta edicao ou exclusao) caso ela não seja realizada com sucesso.
	 * Obseve que o método em questão aguarda que seja repassado um conteúdo que será referênciado por um
	 * objeto chamado rb.
	 */
	
	public Response buildErrorResponse(String str) {
		
		/*
		 * Abaixo o objeto rb recebe o status do erro.
		 */
		
		ResponseBuilder rb = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
		
		/*
		 * Define a entidade (objeto), que nesse caso é uma mensagem que será retornado para o cliente.
		 */
		
		rb = rb.entity(str);
		
		/*
		 * Define o tipo de retorno desta entidade(objeto), no caso é definido como texto simples;
		 */
		
		rb = rb.type("text/plain");
		
		/*
		 * Retorna um objeto de resposta com status 500 (erro) junto com a String contendo a mensagem de erro.
		 */
		
		return rb.build();
		
	}

}
