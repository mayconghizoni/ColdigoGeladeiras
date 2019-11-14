package br.com.coldigogeladeiras.modelo;

import java.io.Serializable;

public class Marca implements Serializable{

	private static final long serialVersionUID = 1L;

	private int id;
	private String nome;
	private int status;
	
	public int getId(){
		return id;
	}
	
	public String getNome(){
		return nome;
	}
	
	public void setNome(String nome){
		this.nome = nome;
	}
	
	public void setId(int id) { //Adicionado por Maycon Ghizoni (QUESTINAR NA VALIDAÇÃO)
		this.id = id;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
}
