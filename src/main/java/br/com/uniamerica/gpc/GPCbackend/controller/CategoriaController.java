//------------------Package----------------------
package br.com.uniamerica.gpc.GPCbackend.controller;

//------------------Imports----------------------


import br.com.uniamerica.gpc.GPCbackend.entity.Ativo;
import br.com.uniamerica.gpc.GPCbackend.entity.Categoria;
import br.com.uniamerica.gpc.GPCbackend.entity.Endereco;
import br.com.uniamerica.gpc.GPCbackend.repository.CategoriaRepository;
import br.com.uniamerica.gpc.GPCbackend.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

//------------------------------------------------
@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private CategoriaService categoriaService;


    @GetMapping
    public ResponseEntity<?> getById(@RequestParam("id") Long id){
        final Categoria categoria = this.categoriaRepository.findById(id).orElse(null);
        return categoria == null ? ResponseEntity.badRequest().body("Nenhuma categoria encontrada") : ResponseEntity.ok(categoria);

    }

    @GetMapping("/listar")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(this.categoriaRepository.findAll());
    }


    @GetMapping("/ativo")
    public ResponseEntity<?> getByAtivos(){
        final List<Categoria> listaAtivos = this.categoriaRepository.findByAtivo();
        return listaAtivos == null ? ResponseEntity.badRequest().body("Nenhuma categoria encontrada") : ResponseEntity.ok(listaAtivos);

    }

    @GetMapping("/nomeCategoria")
    public ResponseEntity<?> getByNome(@RequestParam("nomeCategoria") String nomeCategoria) {
        final Categoria categoria = this.categoriaRepository.findByNome(nomeCategoria);

        if (categoria == null || categoria.getNomeCategoria() == null) {
            return ResponseEntity.badRequest().body("nome inválido");
        }

        return ResponseEntity.ok(categoria);
    }

    @GetMapping("pdf/dataCriacao/{startDate}/{endDate}")
    public ResponseEntity<?> getByDataCriacaoPdf(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(categoriaRepository.findByDataCriacaoBetweenPdf(startDate,endDate));
    }

    @GetMapping("listaespera")
    public  ResponseEntity<?> getByEspera(@RequestParam("nome")String nome){

        final List<Categoria> listaEspera = this.categoriaRepository.findByListaEspera(nome);
        return listaEspera == null ? ResponseEntity.badRequest().body("Nenhuma categoria encontrada") : ResponseEntity.ok(listaEspera);

    }

    @PostMapping
    public  ResponseEntity<?> cadastrarCategoria(@Validated @RequestBody Categoria categoria){

        try{

            this.categoriaService.cadastrar(categoria);
            return ResponseEntity.ok().body("Cadastrado com sucesso!");

        }catch (Exception e){

        return ResponseEntity.badRequest().body(e.getMessage());

        }
    }

    @PutMapping
    public ResponseEntity<?> editarCategoria(
            @RequestBody @Validated Categoria categotia
    ) {
        try {
            this.categoriaService.editar(categotia);
            return ResponseEntity.status(HttpStatus.OK).body("Categoria modificada com sucesso.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @DeleteMapping
    public ResponseEntity<?> inativarCategoria(@RequestParam("id")Long id){

        try{

            this.categoriaService.deletar(id);
            return ResponseEntity.ok().body("Inativado com sucesso!");

        }catch (Exception e){

            return ResponseEntity.badRequest().body(e.getMessage());

        }
    }

}

