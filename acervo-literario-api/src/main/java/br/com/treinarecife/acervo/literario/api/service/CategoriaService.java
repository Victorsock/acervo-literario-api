
package br.com.treinarecife.acervo.literario.api.service;

import br.com.treinarecife.acervo.literario.api.entity.Categoria;
import br.com.treinarecife.acervo.literario.api.mapper.CategoriaMapper;
import br.com.treinarecife.acervo.literario.api.repository.CategoriaRepository;
import br.com.treinarecife.acervo.literario.api.request.CategoriaRequestDTO;
import br.com.treinarecife.acervo.literario.api.response.CategoriaResponseDTO;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    // Listar todas as categorias
    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> listarTodas() {

        return categoriaRepository.findAll().stream()
            .map(CategoriaMapper::toResponse)
            .toList();
    }

    // Buscar categoria pelo ID
    @Transactional(readOnly = true)
    public CategoriaResponseDTO buscarPorId(Long id) {

        return categoriaRepository.findById(id)
                .map(CategoriaMapper::toResponse)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Categoria não encontrada com o ID: " + id
                ));
    }

    // Cadastrar categoria
    @Transactional
    public CategoriaResponseDTO salvar(CategoriaRequestDTO dados) {

        // Validar nome da categoria
        if (dados.nome() == null ||
                dados.nome().isBlank()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O nome da categoria é obrigatório."
            );
        }

        // Verificar se já existe uma categoria com o mesmo nome
        if (categoriaRepository.findByNome(dados.nome()).isPresent()) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Já existe uma categoria cadastrada com esse nome."
            );
        }

        Categoria categoria = CategoriaMapper.toEntity(dados);

        Categoria categoriaSalva = categoriaRepository.save(categoria);

        return CategoriaMapper.toResponse(categoriaSalva);
    }

    // Atualizar categoria
    public CategoriaResponseDTO atualizar(Long id, CategoriaRequestDTO dados) {
        buscarPorId(id);
        
        Categoria categoria = CategoriaMapper.toEntity(dados);

        categoria.setNome(dados.nome());

        Categoria categoriaAtualizada = categoriaRepository.save(categoria);

        return CategoriaMapper.toResponse(categoriaAtualizada);

    }

    // Excluir categoria
    public void excluir(Long id) {

        CategoriaResponseDTO categoriaDTO = buscarPorId(id);

        categoriaRepository.deleteById(categoriaDTO.id());
    }
}
