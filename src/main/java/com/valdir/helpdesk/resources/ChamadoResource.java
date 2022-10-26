package com.valdir.helpdesk.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.valdir.helpdesk.domain.Chamado;
import com.valdir.helpdesk.domain.dtos.ChamadoDto;
import com.valdir.helpdesk.services.ChamadoService;

@RestController
@RequestMapping(value = "/chamados")
public class ChamadoResource {

    @Autowired
    private ChamadoService service;

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> findById(@PathVariable Integer id) {
        Chamado obj = service.findById(id);
        return ResponseEntity.ok().body(new ChamadoDto(obj));
    }

    @GetMapping
    public ResponseEntity<List<Object>> findAll(){
        List<Chamado> objs = service.findAll();
        return ResponseEntity.ok().body(objs.stream().map(x -> new ChamadoDto(x)).collect(Collectors.toList()));
    }

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody ChamadoDto chamadoDto){
        Chamado obj = service.create(chamadoDto);
        URI uri = ServletUriComponentsBuilder
        .fromCurrentRequest().
        path("/{id}")
        .buildAndExpand(obj.getId())
        .toUri();
        return ResponseEntity.created(uri).build();
    }
}
