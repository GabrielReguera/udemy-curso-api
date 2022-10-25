package com.valdir.helpdesk.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.valdir.helpdesk.domain.dtos.ClienteDto;
import com.valdir.helpdesk.domain.enums.Perfil;

@Entity
public class Cliente extends Pessoa {
	private static final long serialVersionUID = 1L;

	@JsonIgnore
	@OneToMany(mappedBy = "cliente")
	private List<Chamado> chamados = new ArrayList<>();

	public Cliente() {
		super();
		addPerfil(Perfil.CLIENTE);
	}

	public Cliente(Integer id, String nome, String cpf, String email, String senha) {
		super(id, nome, cpf, email, senha);
		addPerfil(Perfil.CLIENTE);
	}

	public Cliente(ClienteDto clienteDto) {
        this.id = clienteDto.getId();
        this.nome = clienteDto.getNome();
        this.cpf = clienteDto.getCpf();
        this.email = clienteDto.getEmail();
        this.senha = clienteDto.getSenha();
        this.perfis = clienteDto.getPerfis().stream().map(x -> x.getCodigo()).collect(Collectors.toSet());
        this.dataCriacao = clienteDto.getDataCriacao();
    }

	public List<Chamado> getChamados() {
		return chamados;
	}

	public void setChamados(List<Chamado> chamados) {
		this.chamados = chamados;
	}

}
