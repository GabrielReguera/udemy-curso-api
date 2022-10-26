package com.valdir.helpdesk.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.valdir.helpdesk.domain.Chamado;
import com.valdir.helpdesk.domain.Cliente;
import com.valdir.helpdesk.domain.Tecnico;
import com.valdir.helpdesk.domain.dtos.ChamadoDto;
import com.valdir.helpdesk.domain.enums.Prioridade;
import com.valdir.helpdesk.domain.enums.Status;
import com.valdir.helpdesk.repositories.ChamadoRepository;
import com.valdir.helpdesk.services.exceptions.ObjectnotFoundException;

@Service
public class ChamadoService {

    @Autowired
    private ChamadoRepository chamadoRepository;

    @Autowired
    private TecnicoService tecnicoService;

    @Autowired
    private ClienteService clienteService;

    public Chamado findById(Integer id) {
        try {
            Optional<Chamado> find = chamadoRepository.findById(id);
            return find.orElseThrow(() -> new ObjectnotFoundException("Objeto não encontrado!"));
        } catch (Exception e) {
            throw new ObjectnotFoundException("Erro ao bucar chamado pelo Id");
        }
    }

    public List<Chamado> findAll() {
        try {
            List<Chamado> list = chamadoRepository.findAll();
            if (list != null) {
                return list;
            }
            throw new ObjectnotFoundException("Lista de chamado vazia");
        } catch (Exception e) {
            throw new ObjectnotFoundException("Problemas ao buscar chamados " + e.getMessage());
        }
    }

    public Chamado create(@Valid ChamadoDto chamadoDto) {
        try {
            return chamadoRepository.save(newChamado(chamadoDto));
        } catch (Exception e) {
            throw new ObjectnotFoundException("Erro no create do chamado");
        }
    }

    public Chamado newChamado(ChamadoDto chamadoDto) {
        try {
            if (chamadoDto != null) {
                Tecnico tecnico = tecnicoService.findById(chamadoDto.getTecnico());
                Cliente cliente = clienteService.findById(chamadoDto.getCliente());

                Chamado chamado = new Chamado();
                if (chamadoDto.getId() != null) {
                    chamado.setId(chamadoDto.getId());
                }

                if(chamadoDto.getStatus().equals(2)){
                    chamado.setDataFechamento(LocalDate.now());
                }

                chamado.setTecnico(tecnico);
                chamado.setCliente(cliente);
                chamado.setPrioridade(Prioridade.toEnum(chamadoDto.getPrioridade()));
                chamado.setStatus(Status.toEnum(chamadoDto.getStatus()));
                chamado.setTitulo(chamadoDto.getTitulo());
                chamado.setObservacoes(chamadoDto.getObservacoes());
                return chamado;
            }
            throw new ObjectnotFoundException("Chamado está vazio");
        } catch (Exception e) {
            throw new ObjectnotFoundException("Problema ao fazer um novo chamado");
        }
    }

    public Chamado update(Integer id, @Valid ChamadoDto chamadoDto) {
        try {
            chamadoDto.setId(id);
            Chamado oldObj = findById(id);
            if (oldObj != null) {
                oldObj = newChamado(chamadoDto);

                return chamadoRepository.save(oldObj);
            }
            throw new ObjectnotFoundException("Chamado não encontrado'");
        } catch (Exception e) {
            throw new ObjectnotFoundException("Erro ao atualizar chamado");
        }
    }
}
