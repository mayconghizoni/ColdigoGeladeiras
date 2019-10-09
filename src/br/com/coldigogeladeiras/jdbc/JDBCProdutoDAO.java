package br.com.coldigogeladeiras.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

import br.com.coldigogeladeiras.jdbcinterface.ProdutoDAO;
import br.com.coldigogeladeiras.modelo.Produto;

public class JDBCProdutoDAO implements ProdutoDAO{

	private Connection conexao;
	
	public JDBCProdutoDAO(Connection conexao) {
		this.conexao = conexao;
	}
	
	public boolean inserir(Produto produto) {
		
		
		//armazena o comando SQL dentro de uma String comando
		String comando = "INSERT INTO produtos "
				+ "(id, categoria, modelo, capacidade, valor, marcas_id) "
				+ "VALUES (?,?,?,?,?,?)";
		
		PreparedStatement p;
		
		try {
			
			//Prepara o comando para execução no BD em que nos conectamos
			p = this.conexao.prepareStatement(comando);
			
			//Substitui no comando os "?" pelos valores do produto
			p.setInt(1, produto.getId());
			p.setString(2, produto.getCategoria());
			p.setString(3, produto.getModelo());
			p.setInt(4, produto.getCapacidade());
			p.setFloat(5, produto.getValor());
			p.setInt(6, produto.getMarcaId());
			
			//executa comando no BD
			p.execute();
			
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public List<JsonObject> buscarPorNome(String nome) {
		
		//Inicia criação do comando SQL de busca
		String comando = "SELECT produtos.*, marcas.nome as marca FROM produtos "
				+ "INNER JOIN marcas ON produtos.marcas_id = marcas.id ";
		
		//Se o nome não estiver vazio...
		if(!nome.equals("")) {
			//concatena no camando o WHERE buscando no nome do produto o texto da variável
			comando += "WHERE modelo LIKE '%" + nome + "%' ";
		}
		//Finaliza o comando ordenando alfabeticamente por categoria, marca e depois modelo
		comando+= "ORDER BY categoria ASC, marcas.nome ASC, modelo ASC";		
		
		List<JsonObject> listaProdutos = new ArrayList<JsonObject>(); // Instancia obj de lista de objeto Json
		JsonObject produto = null; //Instancia obj do tipo Json com valor null
		
		try {
			
			//Instancia obj stmt com a conexao e cria uma Statment
			Statement stmt = conexao.createStatement();
			//Executa comando SQL usando obj stmt
			ResultSet rs = stmt.executeQuery(comando);
			
			while(rs.next()) {
				
				//Capta dados do bd e armazena em variáveis
				int id = rs.getInt("id");
				String categoria = rs.getString("categoria");
				String modelo = rs.getString("modelo");
				int capacidade = rs.getInt("capacidade");
				float valor = rs.getFloat("valor");
				String marcaNome = rs.getString("marca");
				
				//Verifica e transforma o valor de categoria em uma String
				if (categoria.equals("1")) {
					categoria = "Geladeira";
				}else if (categoria.equals("2")) {
					categoria = "Freezer";
				}
				
				//Adiciona valores das variaveis no obj Json
				produto = new JsonObject(); // Constroi obj do tipo JsonObj 
				produto.addProperty("id", id);
				produto.addProperty("categoria", categoria);
				produto.addProperty("modelo", modelo);
				produto.addProperty("capacidade", capacidade);
				produto.addProperty("valor", valor);
				produto.addProperty("marcaNome", marcaNome);
				
				//add obj json na lista de produtos
				listaProdutos.add(produto);
				
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		//retonra lista de produtos
		return listaProdutos;
	}
	
	public boolean deletar(int id){
		
		//armazena comando SQL numa string para ser usada posteriormente
		String comando = "DELETE FROM produtos WHERE id = ?"; 
		PreparedStatement p; //Inicia obj P
		
		try {
			
			//Monta e prepara p usando obj conexao com o comando preparedStatment passsando A string comando como parametro
			p = this.conexao.prepareStatement(comando); 
			//Substitui id na posição correspondente que falta no SQL 
			p.setInt(1, id);
			//Executa comando SQL
			p.execute();
			
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		//Em caso se sucesso retorna true
		return true;
	}
	
}
