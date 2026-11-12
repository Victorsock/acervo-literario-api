
package br.com.treinarecife.acervo.literario.api.controller;

import br.com.treinarecife.acervo.literario.api.request.LivroRequestDTO;
import br.com.treinarecife.acervo.literario.api.response.LivroResponseDTO;
import br.com.treinarecife.acervo.literario.api.service.LivroService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/livros")
public class LivroController {

    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @GetMapping
    public ResponseEntity<List<LivroResponseDTO>> listarTodos() {
        return ResponseEntity.ok(livroService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LivroResponseDTO> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(livroService.buscarPorId(id));
    }

    @GetMapping("/buscar/titulo")
    public ResponseEntity<List<LivroResponseDTO>> buscarPorTitulo(
            @RequestParam String termo) {

        return ResponseEntity.ok(livroService.buscarPorTitulo(termo));
    }

    @GetMapping("/buscar/categoria")
    public ResponseEntity<List<LivroResponseDTO>> buscarPorCategoria(
            @RequestParam String nome) {

        return ResponseEntity.ok(livroService.buscarPorCategoria(nome));
    }

    @PostMapping
    public ResponseEntity<LivroResponseDTO> salvar(
            @Valid @RequestBody LivroRequestDTO dados) {

        LivroResponseDTO livroSalvo = livroService.salvar(dados);

        return ResponseEntity.status(HttpStatus.CREATED).body(livroSalvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LivroResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody LivroRequestDTO dados) {

        return ResponseEntity.ok(livroService.atualizar(id, dados));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        livroService.excluir(id);

        return ResponseEntity.noContent().build();
    }
}
