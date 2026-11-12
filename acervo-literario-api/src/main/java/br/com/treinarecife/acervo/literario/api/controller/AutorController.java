
package br.com.treinarecife.acervo.literario.api.controller;

import br.com.treinarecife.acervo.literario.api.request.AutorRequestDTO;
import br.com.treinarecife.acervo.literario.api.response.AutorResponseDTO;
import br.com.treinarecife.acervo.literario.api.service.AutorService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/autores")
public class AutorController {

    private final AutorService autorService;

    public AutorController(AutorService autorService) {
        this.autorService = autorService;
    }

    @GetMapping
    public ResponseEntity<List<AutorResponseDTO>> listarTodos() {
        return ResponseEntity.ok(autorService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AutorResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(autorService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<AutorResponseDTO> salvar(
            @Valid @RequestBody AutorRequestDTO dados) {

        AutorResponseDTO autorSalvo = autorService.salvar(dados);

        return ResponseEntity.status(HttpStatus.CREATED).body(autorSalvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AutorResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody AutorRequestDTO dados) {

        return ResponseEntity.ok(autorService.atualizar(id, dados));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        autorService.excluir(id);

        return ResponseEntity.noContent().build();
    }
}
