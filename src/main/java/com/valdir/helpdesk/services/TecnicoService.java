package com.valdir.helpdesk.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.valdir.helpdesk.domain.Tecnico;
import com.valdir.helpdesk.repositories.TecnicoRepository;

@Service
public class TecnicoService {

    @Autowired
    private TecnicoRepository tecnicoRepository;

    public Tecnico findById(Integer id) {
        try {
            if (id == null) {
                return null;
            }
            Optional<Tecnico> find = tecnicoRepository.findById(id);
            if (find.isPresent()) {
                return find.get();
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
