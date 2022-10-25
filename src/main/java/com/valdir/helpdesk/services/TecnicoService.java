package com.valdir.helpdesk.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.valdir.helpdesk.domain.Tecnico;
import com.valdir.helpdesk.domain.dtos.TecnicoDto;
import com.valdir.helpdesk.repositories.TecnicoRepository;
import com.valdir.helpdesk.services.exceptions.ObjectnotFoundException;

@Service
public class TecnicoService {

    @Autowired
    private TecnicoRepository tecnicoRepository;

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
            Tecnico newObj = new Tecnico(tecnicoDto);
            {
                return tecnicoRepository.save(newObj);
            }
        } catch (Exception e) {
            throw new ObjectnotFoundException("Erro ao criar Tecnico " + e.getMessage());
        }
    }
}
