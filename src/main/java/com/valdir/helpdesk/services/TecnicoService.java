package com.valdir.helpdesk.services;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.valdir.helpdesk.domain.Pessoa;
import com.valdir.helpdesk.domain.Tecnico;
import com.valdir.helpdesk.domain.dtos.TecnicoDto;
import com.valdir.helpdesk.repositories.PessoaRepository;
import com.valdir.helpdesk.repositories.TecnicoRepository;
import com.valdir.helpdesk.services.exceptions.DataIntegrityViolationException;
import com.valdir.helpdesk.services.exceptions.ObjectnotFoundException;

@Service
public class TecnicoService {

    @Autowired
    private TecnicoRepository tecnicoRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    public Tecnico findById(Integer id) {
        try {
            if (id == null) {
                throw new ObjectnotFoundException("Objeto não encontrado Id: " + id);
            }
            Optional<Tecnico> find = tecnicoRepository.findById(id);
            if (find.isPresent()) {
                return find.get();
            }
            return null;
        } catch (Exception e) {
            throw new ObjectnotFoundException("Objeto não encontrado Id: " + id + ", " + e.getMessage(), e.getCause());
        }
    }

    public List<Tecnico> findAll() {
        try {
            List<Tecnico> find = tecnicoRepository.findAll();
            if (!find.isEmpty()) {
                return find;
            }
            throw new ObjectnotFoundException("Não existe tecnicos cadastrados");
        } catch (Exception e) {
            throw new ObjectnotFoundException("Erro ao buscar Tecnicos");
        }
    }

    public Tecnico create(TecnicoDto tecnicoDto) {
        try {
            tecnicoDto.setId(null);
            tecnicoDto.setSenha(encoder.encode(tecnicoDto.getSenha()));
            validaPorCpfEEmail(tecnicoDto);
            Tecnico newObj = new Tecnico(tecnicoDto);
            {
                return tecnicoRepository.save(newObj);
            }
        } catch (Exception e) {
            if (e.getClass() == DataIntegrityViolationException.class) {
                throw new DataIntegrityViolationException(e.getMessage());
            } else {
                throw new ObjectnotFoundException("Erro ao criar Tecnico " + e.getMessage());
            }
        }
    }

    private void validaPorCpfEEmail(TecnicoDto tecnicoDto) {
        Optional<Pessoa> obj = pessoaRepository.findByCpf(tecnicoDto.getCpf());
        if (obj.isPresent() && !obj.get().getId().equals(tecnicoDto.getId())) {
            throw new DataIntegrityViolationException("Cpf ja cadastrado no sitema");
        }

        obj = pessoaRepository.findByEmail(tecnicoDto.getEmail());
        if (obj.isPresent() && !obj.get().getId().equals(tecnicoDto.getId())) {
            throw new DataIntegrityViolationException("Email ja cadastrado no sitema");
        }
    }

    public Tecnico update(Integer id, @Valid TecnicoDto tecnicoDto) {
        try {
            tecnicoDto.setId(id);
            Tecnico oldObj = findById(id);
            if (!tecnicoDto.getSenha().equals(oldObj.getSenha())) {
                tecnicoDto.setSenha(encoder.encode(tecnicoDto.getSenha()));
            }
            validaPorCpfEEmail(tecnicoDto);
            oldObj = new Tecnico(tecnicoDto);
            return tecnicoRepository.save(oldObj);

        } catch (Exception e) {
            throw new ObjectnotFoundException("Erro ao atualizar Tecnico" + e.getMessage());
        }
    }

    public void delete(Integer id) {
        try {
            Optional<Tecnico> obj = tecnicoRepository.findById(id);
            if (obj.isPresent()) {
                if (obj.get().getChamados().size() > 0) {
                    throw new DataIntegrityViolationException("Tecnico orderns de serviço e não pode ser deletado");
                } else {
                    tecnicoRepository.deleteById(id);
                }
            }

        } catch (Exception e) {
            throw new ObjectnotFoundException("Erro ao deletar Tecnico");
        }

    }
}
