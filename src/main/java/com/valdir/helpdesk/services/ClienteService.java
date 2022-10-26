package com.valdir.helpdesk.services;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.valdir.helpdesk.domain.Cliente;
import com.valdir.helpdesk.domain.Pessoa;
import com.valdir.helpdesk.domain.dtos.ClienteDto;
import com.valdir.helpdesk.repositories.ClienteRepository;
import com.valdir.helpdesk.repositories.PessoaRepository;
import com.valdir.helpdesk.services.exceptions.DataIntegrityViolationException;
import com.valdir.helpdesk.services.exceptions.ObjectnotFoundException;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    public Cliente findById(Integer id) {
        try {
            if (id == null) {
                throw new ObjectnotFoundException("Objeto não encontrado Id: " + id);
            }
            Optional<Cliente> find = clienteRepository.findById(id);
            if (find.isPresent()) {
                return find.get();
            }
            return null;
        } catch (Exception e) {
            throw new ObjectnotFoundException("Objeto não encontrado Id: " + id + ", " + e.getMessage(), e.getCause());
        }
    }

    public List<Cliente> findAll() {
        try {
            List<Cliente> find = clienteRepository.findAll();
            if (!find.isEmpty()) {
                return find;
            }
            throw new ObjectnotFoundException("Não existe clientes cadastrados");
        } catch (Exception e) {
            throw new ObjectnotFoundException("Erro ao buscar Clientes");
        }
    }

    public Cliente create(ClienteDto clienteDto) {
        try {
            clienteDto.setId(null);
            clienteDto.setSenha(encoder.encode(clienteDto.getSenha()));
            validaPorCpfEEmail(clienteDto);
            Cliente newObj = new Cliente(clienteDto);
            {
                return clienteRepository.save(newObj);
            }
        } catch (Exception e) {
            if (e.getClass() == DataIntegrityViolationException.class) {
                throw new DataIntegrityViolationException(e.getMessage());
            } else {
                throw new ObjectnotFoundException("Erro ao criar Cliente " + e.getMessage());
            }
        }
    }

    private void validaPorCpfEEmail(ClienteDto clienteDto) {
        Optional<Pessoa> obj = pessoaRepository.findByCpf(clienteDto.getCpf());
        if (obj.isPresent() && !obj.get().getId().equals(clienteDto.getId())) {
            throw new DataIntegrityViolationException("Cpf ja cadastrado no sitema");
        }

        obj = pessoaRepository.findByEmail(clienteDto.getEmail());
        if (obj.isPresent() && !obj.get().getId().equals(clienteDto.getId())) {
            throw new DataIntegrityViolationException("Email ja cadastrado no sitema");
        }
    }

    public Cliente update(Integer id, @Valid ClienteDto clienteDto) {
        try {
            clienteDto.setId(id);
            Cliente oldObj = findById(id);
            validaPorCpfEEmail(clienteDto);
            oldObj = new Cliente(clienteDto);
            return clienteRepository.save(oldObj);

        } catch (Exception e) {
            throw new ObjectnotFoundException("Erro ao atualizar Cliente" + e.getMessage());
        }
    }

    public void delete(Integer id) {
        try {
            Optional<Cliente> obj = clienteRepository.findById(id);
            if (obj.isPresent()) {
                if (obj.get().getChamados().size() > 0) {
                    throw new DataIntegrityViolationException("Cliente orderns de serviço e não pode ser deletado");
                } else {
                    clienteRepository.deleteById(id);
                }
            }

        } catch (Exception e) {
            throw new ObjectnotFoundException("Erro ao deletar Cliente");
        }

    }
}
