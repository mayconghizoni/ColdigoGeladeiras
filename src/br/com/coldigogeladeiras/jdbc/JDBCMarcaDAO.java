package br.com.coldigogeladeiras.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.com.coldigogeladeiras.jdbcinterface.MarcaDAO;
import br.com.coldigogeladeiras.modelo.Marca;

public class JDBCMarcaDAO implements MarcaDAO{
	
	private Connection conexao;
	
	public JDBCMarcaDAO(Connection conexao) {
		this.conexao = conexao;
	}

	@Override
	public List<Marca> buscar(String nome){
		
		System.out.println(nome);
		
		//Criação da instrução SQL para busca de todas as marcas
		String comando = "SELECT * FROM marcas ";
		
		if(!nome.equals("")) {
			comando += "WHERE nome LIKE '%" + nome + "%' ";
		}
		
		comando += "ORDER BY nome ASC";
		
		//Criação de uma lista para armazenar cada marca encontrada
		List<Marca> listMarcas = new ArrayList<Marca>();
		
		//Criação do objeto marca com valor null (ou seja, sem instancia-lo)
		Marca marca = null;
		
		try {
			
			//Uso da conexao do banco para prepara-lo para uma instrução SQL
			Statement stmt = conexao.createStatement();
			
			//Execução da instrução criada previamente e armazenamento do resultado no objeto rs
			ResultSet rs = stmt.executeQuery(comando);
			
			//Enquanto houver uma próxima linha no resultado
			while(rs.next()) {
				
				//Criação de instancia da classe Marca
				marca = new Marca();
				marca.setId(rs.getInt("id"));
				marca.setNome(rs.getString("nome"));
				
				//Adição da instância contida no objeto Marca na lista de marcas
				listMarcas.add(marca);
				
			}
			
			
			//Caso alguma Exception seja gerada no try, recebe-a no objeto "ex"
		}catch(Exception ex){
			
			//Exibe a exceção na consele
			ex.printStackTrace();
			
		}
		
		//Retorna para quem chamou o método a lista criada
		return listMarcas;
	}

	
	public boolean inserir(Marca marca) {
		
		String comando = "INSERT INTO marcas (id, nome) VALUES (?, ?)";
		
		PreparedStatement p;
		
		try {
			
			p = this.conexao.prepareStatement(comando);
			
			p.setInt(1, marca.getId());
			p.setString(2, marca.getNome());
			
			p.execute();
			
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	
	
}
