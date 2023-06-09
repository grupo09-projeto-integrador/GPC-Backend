//------------------Package----------------------
package br.com.uniamerica.gpc.GPCbackend.controller;

//------------------Imports----------------------


import br.com.uniamerica.gpc.GPCbackend.entity.Movimentacao;
import br.com.uniamerica.gpc.GPCbackend.repository.MovimentacaoRepository;
import br.com.uniamerica.gpc.GPCbackend.service.MovimentacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

//------------------------------------------------
@Controller
@RequestMapping("/movimentacoes")
public class MovimentacaoController {
    @Autowired
    private MovimentacaoRepository movimentacaoRepository;
    @Autowired
    private MovimentacaoService movimentacaoService;

    @GetMapping(value = "/listar")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(this.movimentacaoRepository.findAll());
    }

    @GetMapping(value = "/dataEntrada")
    public ResponseEntity<?> getByDataEmprestimo(@RequestParam("dataEntrada") LocalDate dataEmprestimo) {
        final List<Movimentacao> movimentacao = this.movimentacaoRepository.findByDataEmprestimo(dataEmprestimo);
        return movimentacao == null ? ResponseEntity.badRequest().body("Nenhuma movimentação encontrada com data de empréstimo") : ResponseEntity.ok(movimentacao);
    }

    @GetMapping(value = "/dataDevolucao")
    public ResponseEntity<?> getByDataDevolucao(@RequestParam("dataDevolucao") LocalDate dataDevolucao) {
        final List<Movimentacao> movimentacao = this.movimentacaoRepository.findByDataDevolucao(dataDevolucao);
        return movimentacao == null ? ResponseEntity.badRequest().body("Nenhuma movimentação encontrada") : ResponseEntity.ok(movimentacao);
    }

    @GetMapping("pdf/{dataEntrada}/{dataDevolucao}")
    public ResponseEntity<?> getByDataCriacaoPdf(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataEntrada,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataDevolucao) {
        return ResponseEntity.ok(movimentacaoRepository.findMovementsBetweenDates(dataEntrada,dataDevolucao));
    }

    @GetMapping(value = "/beneficiario")
    public ResponseEntity<?> getByBeneficiario(@RequestParam("beneficiario") String nome) {
        final List<Movimentacao> movimentacao = this.movimentacaoRepository.findByBeneficiarioNome(nome);
        return movimentacao == null ? ResponseEntity.badRequest().body("Nenhuma movimentação encontrada") : ResponseEntity.ok(movimentacao);
    }

    @GetMapping(value = "/beneficiario-id")
    public ResponseEntity<?> getByBeneficiarioId(@RequestParam("beneficiario-id") Long id) {
        final List<Movimentacao> movimentacao = this.movimentacaoRepository.findByBeneficiarioId(id);
        return movimentacao == null ? ResponseEntity.badRequest().body("Nenhuma movimentação encontrada") : ResponseEntity.ok(movimentacao);
    }

    @GetMapping(value = "/categoria-id")
    public ResponseEntity<?> getByCategoriaId(@RequestParam("categoria-id") Long id) {
        final List<Movimentacao> movimentacao = this.movimentacaoRepository.findByAtivoCategoriaId(id);
        return movimentacao == null ? ResponseEntity.badRequest().body("Nenhuma movimentação encontrada") : ResponseEntity.ok(movimentacao);
    }

    @GetMapping(value = "/ativo-patrimonio")
    public ResponseEntity<?> getByAtivoPatrimonio(@RequestParam("ativo-patrimonio") Long id) {
        final List<Movimentacao> movimentacao = this.movimentacaoRepository.findByAtivoPatrimonio(id);
        return movimentacao == null ? ResponseEntity.badRequest().body("Nenhuma movimentação encontrada") : ResponseEntity.ok(movimentacao);
    }

    @GetMapping(value = "/ativo-id")
    public ResponseEntity<?> getByAtivoId(@RequestParam("ativo-id") Long id) {
        final List<Movimentacao> movimentacao = this.movimentacaoRepository.findByAtivoId(id);
        return movimentacao == null ? ResponseEntity.badRequest().body("Nenhuma movimentação encontrada") : ResponseEntity.ok(movimentacao);
    }

    @GetMapping
    public ResponseEntity<?> getById(@RequestParam("id") Long id) {
        final Movimentacao movimentacao = this.movimentacaoRepository.findById(id).orElse(null);
        return movimentacao == null ? ResponseEntity.badRequest().body("Nenhuma  movimentação encontrada") : ResponseEntity.ok(movimentacao);
    }


    @PostMapping
    public ResponseEntity<?> novaMovimentacao(@RequestBody Movimentacao movimentacao) {
        try {
            final Movimentacao movimentacaoBanco = this.movimentacaoService.novaMovimentacao(movimentacao);
            return ResponseEntity.ok(String.format("Movimentação [ %s ] iniciada com sucesso", movimentacaoBanco.getId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<?> editar(@RequestParam("id") Long id, @RequestBody @Validated Movimentacao movimentacao) {
        try {
            final Movimentacao movimentacaoBanco = this.movimentacaoService.editar(id, movimentacao);
            return ResponseEntity.ok(String.format("Movimentação [ %s ] atualizada com sucesso!", movimentacaoBanco.getId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deletar(
            @RequestParam("id") final Long id
    ) {
        try {
            final Movimentacao mov = this.movimentacaoService.desativar(id);
            return ResponseEntity.ok(String.format("Movimentação [ %s ] desativada!", mov.getId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}

