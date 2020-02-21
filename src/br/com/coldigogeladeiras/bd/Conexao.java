package br.com.coldigogeladeiras.bd;

import java.sql.Connection;

public class Conexao {
	
	private Connection conexao;
	
	public Connection abrirConexao() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");//Seleciona driver JDBC
			conexao = java.sql.DriverManager.getConnection("jdbc:mysql://localhost/bdcoldigo?" + 
			"user=maycon&password=as&useTimezone=true&serverTimezone=UTC"); //Passas os parametros da conexão atual
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		//retorna o objeto com a conexao do banco para quem solicitou 
		return conexao;
	}
	
	public void fecharConexao() {
		
		try {
			conexao.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}

}
