
package br.com.treinarecife.acervo.literario.api.controller;

import br.com.treinarecife.acervo.literario.api.request.BiografiaRequestDTO;
import br.com.treinarecife.acervo.literario.api.response.BiografiaResponseDTO;
import br.com.treinarecife.acervo.literario.api.service.BiografiaService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/biografias")
public class BiografiaController {

    private final BiografiaService biografiaService;

    public BiografiaController(BiografiaService biografiaService) {
        this.biografiaService = biografiaService;
    }

    @GetMapping
    public ResponseEntity<List<BiografiaResponseDTO>> listarTodas() {
        return ResponseEntity.ok(biografiaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BiografiaResponseDTO> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(biografiaService.buscarPorId(id));
    }

    @GetMapping("/autor/{autorId}")
    public ResponseEntity<BiografiaResponseDTO> buscarPorAutor(
            @PathVariable Long autorId) {

        return ResponseEntity.ok(biografiaService.buscarPorAutor(autorId));
    }

    @PostMapping
    public ResponseEntity<BiografiaResponseDTO> salvar(
            @Valid @RequestBody BiografiaRequestDTO dados) {

        BiografiaResponseDTO biografiaSalva = biografiaService.salvar(dados);

        return ResponseEntity.status(HttpStatus.CREATED).body(biografiaSalva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BiografiaResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody BiografiaRequestDTO dados) {

        return ResponseEntity.ok(biografiaService.atualizar(id, dados));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        biografiaService.excluir(id);

        return ResponseEntity.noContent().build();
    }
}
