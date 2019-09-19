package br.com.coldigogeladeiras.bd;

import java.sql.Connection;

public class Conexao {
	
	private Connection conexao;
	
	public Connection abrirConexao() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conexao = java.sql.DriverManager.getConnection("jdbc:mysql://localhost/bdcoldigo?" + 
			"user=root&password=as&useTimezone=true&serverTimezone=UTC");
		}catch(Exception e) {
			e.printStackTrace();
		}
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
