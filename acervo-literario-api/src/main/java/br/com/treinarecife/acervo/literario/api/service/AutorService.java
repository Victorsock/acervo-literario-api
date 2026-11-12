
package br.com.treinarecife.acervo.literario.api.service;

import br.com.treinarecife.acervo.literario.api.entity.Autor;

import br.com.treinarecife.acervo.literario.api.mapper.AutorMapper;

import br.com.treinarecife.acervo.literario.api.repository.AutorRepository;
import br.com.treinarecife.acervo.literario.api.repository.LivroRepository;

import br.com.treinarecife.acervo.literario.api.request.AutorRequestDTO;
import br.com.treinarecife.acervo.literario.api.response.AutorResponseDTO;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AutorService {

    private final AutorRepository autorRepository;
    private final LivroRepository livroRepository;

    public AutorService(
            AutorRepository autorRepository,
            LivroRepository livroRepository) {

        this.autorRepository = autorRepository;
        this.livroRepository = livroRepository;
    }

    // Listar todos os autores
    @Transactional(readOnly = true)
    public List<AutorResponseDTO> listarTodos() {

        return autorRepository.findAll()
                .stream()
                .map(AutorMapper::toResponse)
                .toList();
    }

    // Buscar autor pelo ID
    @Transactional(readOnly = true)
    public AutorResponseDTO buscarPorId(Long id) {

        return autorRepository.findById(id)
                .map(AutorMapper::toResponse)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Autor não encontrado com o ID: " + id
                ));
    }

    // Cadastrar autor
    @Transactional
    public AutorResponseDTO salvar(AutorRequestDTO dados) {

        // Validar nome do autor
        if (dados.nome() == null || dados.nome().isBlank()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O nome do autor é obrigatório."
            );
        }

        // Converter RequestDTO para Entidade
        Autor autor = AutorMapper.toEntity(dados);

        // Salvar autor
        Autor autorSalvo = autorRepository.save(autor);

        // Converter Entidade para ResponseDTO
        return AutorMapper.toResponse(autorSalvo);
    }

    // Atualizar autor
    @Transactional
    public AutorResponseDTO atualizar(Long id, AutorRequestDTO dados) {

        // Verificar se o autor existe
        Autor autor = autorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Autor não encontrado com o ID: " + id
                ));

        // Validar nome
        if (dados.nome() == null || dados.nome().isBlank()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O nome do autor é obrigatório."
            );
        }

        // Atualizar dados do autor existente
        autor.setNome(dados.nome());
        autor.setNacionalidade(dados.nacionalidade());

        // Salvar alterações
        Autor autorAtualizado = autorRepository.save(autor);

        // Retornar ResponseDTO
        return AutorMapper.toResponse(autorAtualizado);
    }

    // Excluir autor
    @Transactional
    public void excluir(Long id) {

        // Verificar se o autor existe
        AutorResponseDTO autor = buscarPorId(id);

        // Validar se existem livros vinculados ao autor
        boolean possuiLivros = livroRepository.existsByAutor_Id(id);

        if (possuiLivros) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Não é possível excluir o autor "
                            + autor.nome()
                            + ", pois existem livros cadastrados vinculados a ele."
            );
        }

        // Excluir autor
        autorRepository.deleteById(id);
    }
}
