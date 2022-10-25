package com.valdir.helpdesk.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.valdir.helpdesk.domain.Cliente;
import com.valdir.helpdesk.domain.dtos.ClienteDto;
import com.valdir.helpdesk.services.ClienteService;

@RestController
@RequestMapping(value = "/clientes")
public class ClienteResource {

    @Autowired
    private ClienteService service;

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> findById(@PathVariable Integer id) {
        Cliente find = service.findById(id);
        return ResponseEntity.ok().body(new ClienteDto(find));
    }

    @GetMapping
    public ResponseEntity<List<Object>> findAll() {
        List<Cliente> list = service.findAll();
        return ResponseEntity.ok().body(list.stream().map(x -> new ClienteDto(x)).collect(Collectors.toList()));
    }

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody ClienteDto clienteDto) {
        Cliente newObj = service.create(clienteDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newObj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Object> update(@PathVariable Integer id, @Valid @RequestBody ClienteDto clienteDto) {
        Cliente obj = service.update(id, clienteDto);
        return ResponseEntity.ok().body(new ClienteDto(obj));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
