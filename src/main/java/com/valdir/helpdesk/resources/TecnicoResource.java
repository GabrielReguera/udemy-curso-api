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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.valdir.helpdesk.domain.Tecnico;
import com.valdir.helpdesk.domain.dtos.TecnicoDto;
import com.valdir.helpdesk.services.TecnicoService;

@RestController
@RequestMapping(value = "/tecnicos")
public class TecnicoResource {

    @Autowired
    private TecnicoService service;

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> findById(@PathVariable Integer id) {
        Tecnico find = service.findById(id);
        return ResponseEntity.ok().body(new TecnicoDto(find));
    }

    @GetMapping
    public ResponseEntity<List<Object>> findAll() {
        List<Tecnico> list = service.findAll();
        return ResponseEntity.ok().body(list.stream().map(x -> new TecnicoDto(x)).collect(Collectors.toList()));
    }

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody TecnicoDto tecnicoDto) {
        Tecnico newObj = service.create(tecnicoDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newObj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Object> update(@PathVariable Integer id, @Valid @RequestBody TecnicoDto tecnicoDto){
        Tecnico obj = service.update(id, tecnicoDto);
        return ResponseEntity.ok().body(new TecnicoDto(obj));
    }
}
