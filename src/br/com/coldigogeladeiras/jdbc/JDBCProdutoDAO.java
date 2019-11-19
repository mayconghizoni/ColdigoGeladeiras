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
		String comando = "SELECT produtos.*, marcas.nome AS marca FROM produtos "
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

	@Override
	public Produto buscarPorId(int id) {
		
		String comando = "SELECT * from produtos WHERE produtos.id = ?";	//Define comando SQL numa String
		Produto produto = new Produto(); 	//Cria um novo objeto do tipo produto
		
		try {
			
			PreparedStatement p = this.conexao.prepareStatement(comando); //prepara objeto p com a conexao e o comando cidato acima
			p.setInt(1, id); //altera as ? do comando pelo id nas respectivas posições
			ResultSet rs = p.executeQuery(); //Executa o comando já pronto e armazena o resultado em um obj do tipo ResultSet
			if (rs.next()) {
				
				String categoria = rs.getString("categoria"); //Pega String no campo categoria no bd
				String modelo = rs.getString("modelo"); //Pega String no campo modelo no bd
				int capacidade = rs.getInt("capacidade"); 	//pega int no campo capacidade no bd
				float valor = rs.getFloat("valor");		//Pega float no campo valor no bd
				int marcaId = rs.getInt("marcas_id");	//Pega int no campo marcas_id no bd
				
				//Seta valores acima dentro do objeto produto
				produto.setId(id); 
				produto.setCategoria(categoria);
				produto.setMarcaId(marcaId);
				produto.setModelo(modelo);
				produto.setCapacidade(capacidade);
				produto.setValor(valor);
				
			}
			 
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return produto; //Retorna objeto produto;
		
	}

	public boolean alterar(Produto produto) {
		
		//Seta o comando quase pronto numa string comando
		String comando = "UPDATE produtos "
				+ "SET categoria=?, modelo=?, capacidade=?, valor=?, marcas_id=?"
				+ " WHERE id=?";
		
		//Inicia objeto PreparedStatement
		PreparedStatement p;
		
		try {
			
			//monta objeto preparado para execução com conexao e o comando string setado acima 
			p = this.conexao.prepareStatement(comando);
			
			//Adiciona valores as posições correspondentes do ? do obj p
			p.setString(1, produto.getCategoria());
			p.setString(2, produto.getModelo());
			p.setInt(3, produto.getCapacidade());
			p.setFloat(4, produto.getValor());
			p.setInt(5, produto.getMarcaId());
			p.setInt(6, produto.getId());
			
			//Executa o comando de p com o método executeUpdate.
			p.executeUpdate();
			
		}catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	@Override
	public boolean verificaExistencia(int id) {
		
		String comando = "SELECT count(*) as cont_produtos FROM produtos WHERE id = ?";
		PreparedStatement p;
		int contagem = 0;
		
		try {
			
			p = this.conexao.prepareStatement(comando);
			p.setInt(1, id);
			ResultSet rs = p.executeQuery();
			
			if(rs.next()){
				contagem = rs.getInt("cont_produtos");
			}
			
			if(contagem > 0) {
				return true;
			}else {
				return false;
			}
						
			
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}
	}

	public boolean verificaProdutoDuplicado(Produto produto) {
		
		String comando = "SELECT produtos.modelo FROM produtos";
		PreparedStatement p;
		
		try {
			
			p = this.conexao.prepareStatement(comando);
			ResultSet rs = p.executeQuery();
			
			while(rs.next()) {
				
				if(rs.getString("modelo").equals(produto.getModelo())) {
					return false;
				}
				
			}
			
			return true;
			
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		
	}
	
}
