
package br.com.treinarecife.acervo.literario.api.service;

import br.com.treinarecife.acervo.literario.api.entity.Categoria;
import br.com.treinarecife.acervo.literario.api.mapper.CategoriaMapper;
import br.com.treinarecife.acervo.literario.api.repository.CategoriaRepository;
import br.com.treinarecife.acervo.literario.api.request.CategoriaRequestDTO;
import br.com.treinarecife.acervo.literario.api.response.CategoriaResponseDTO;

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
    public List<Categoria> listarTodas() {

        return categoriaRepository.findAll();
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
    public Categoria salvar(Categoria categoria) {

        // Validar nome da categoria
        if (categoria.getNome() == null ||
                categoria.getNome().isBlank()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O nome da categoria é obrigatório."
            );
        }

        // Verificar se já existe uma categoria com o mesmo nome
        if (categoriaRepository.findByNome(categoria.getNome()).isPresent()) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Já existe uma categoria cadastrada com esse nome."
            );
        }

        return categoriaRepository.save(categoria);
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
