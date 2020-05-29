package com.produtos.apirest.dtos;

import java.math.BigDecimal;

import javax.validation.constraints.NotEmpty;

public class ProdutoDto {

	
	private long id;
	
	private String nome;
	
	private BigDecimal quantidade;
	
	@Override
	public String toString() {
		return "ProdutoDto [id=" + id + ", nome=" + nome + ", quantidade=" + quantidade + ", valor=" + valor + "]";
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@NotEmpty(message = "Nome não pode ser vazio.")
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	
	public BigDecimal getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(BigDecimal quantidade) {
		this.quantidade = quantidade;
	}

	
	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	private BigDecimal valor;
}
