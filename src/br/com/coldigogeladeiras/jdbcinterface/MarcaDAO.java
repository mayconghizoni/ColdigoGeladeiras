package br.com.coldigogeladeiras.jdbcinterface;

import java.util.List;

import br.com.coldigogeladeiras.modelo.Marca;

public interface MarcaDAO {

	public List<Marca> buscar(String nome);
	public List<Marca> buscarAtivos();
	public boolean inserir(Marca marca);
	public boolean deletar(int id);
	public boolean verificaExistencia(int id);
	public boolean verificaProdutosCadastrados(int id);
	public Marca buscarPorId(int id);
	public boolean alterar(Marca marca);
	public boolean ativoInativo(int status, int id);
	public boolean verificaStatus(int id);
	public boolean verificaMarcaDuplicada(Marca marca);
}
