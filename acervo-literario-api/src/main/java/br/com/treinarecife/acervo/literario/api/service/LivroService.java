
package br.com.treinarecife.acervo.literario.api.service;

import br.com.treinarecife.acervo.literario.api.entity.Autor;
import br.com.treinarecife.acervo.literario.api.entity.Categoria;
import br.com.treinarecife.acervo.literario.api.entity.Livro;

import br.com.treinarecife.acervo.literario.api.mapper.LivroMapper;

import br.com.treinarecife.acervo.literario.api.repository.AutorRepository;
import br.com.treinarecife.acervo.literario.api.repository.CategoriaRepository;
import br.com.treinarecife.acervo.literario.api.repository.LivroRepository;

import br.com.treinarecife.acervo.literario.api.request.LivroRequestDTO;
import br.com.treinarecife.acervo.literario.api.response.LivroResponseDTO;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class LivroService {

    private final LivroRepository livroRepository;
    private final AutorRepository autorRepository;
    private final CategoriaRepository categoriaRepository;

    public LivroService(
            LivroRepository livroRepository,
            AutorRepository autorRepository,
            CategoriaRepository categoriaRepository) {

        this.livroRepository = livroRepository;
        this.autorRepository = autorRepository;
        this.categoriaRepository = categoriaRepository;
    }

    // Listar todos os livros
    @Transactional(readOnly = true)
    public List<LivroResponseDTO> listarTodos() {

        return livroRepository.findAll()
                .stream()
                .map(LivroMapper::toResponse)
                .toList();
    }

    // Buscar livro pelo ID
    @Transactional(readOnly = true)
    public LivroResponseDTO buscarPorId(Long id) {

        return livroRepository.findById(id)
                .map(LivroMapper::toResponse)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Livro não encontrado com o ID: " + id
                ));
    }

    // Buscar livros pelo título
    @Transactional(readOnly = true)
    public List<LivroResponseDTO> buscarPorTitulo(String termo) {

        return livroRepository.findByTituloContainingIgnoreCase(termo)
                .stream()
                .map(LivroMapper::toResponse)
                .toList();
    }

    // Buscar livros pela categoria
    @Transactional(readOnly = true)
    public List<LivroResponseDTO> buscarPorCategoria(String nome) {

        return livroRepository.findByCategoriasNome(nome)
                .stream()
                .map(LivroMapper::toResponse)
                .toList();
    }

    // Cadastrar livro
    @Transactional
    public LivroResponseDTO salvar(LivroRequestDTO dados) {

        // Validar se o autor foi informado
        if (dados.autorId() == null) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "É obrigatório informar um autor para cadastrar o livro."
            );
        }

        // Validar se existem categorias informadas
        if (dados.categoriasIds() == null ||
                dados.categoriasIds().isEmpty()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O livro deve possuir pelo menos uma categoria."
            );
        }

        // Verificar se o autor existe
        Autor autor = autorRepository.findById(dados.autorId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Autor não encontrado com o ID: " + dados.autorId()
                ));

        // Buscar categorias informadas
        List<Categoria> categorias =
                categoriaRepository.findAllById(dados.categoriasIds());

        // Verificar se todas as categorias existem
        if (categorias.size() != dados.categoriasIds().stream().distinct().count()) {

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Uma ou mais categorias informadas não foram encontradas."
            );
        }

        // Converter RequestDTO para Entidade
        Livro livro = LivroMapper.toEntity(
                dados,
                autor,
                categorias
        );

        // Salvar livro
        Livro livroSalvo = livroRepository.save(livro);

        // Converter Entidade para ResponseDTO
        return LivroMapper.toResponse(livroSalvo);
    }

    // Atualizar livro
    @Transactional
    public LivroResponseDTO atualizar(Long id, LivroRequestDTO dados) {

        // Verificar se o livro existe
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Livro não encontrado com o ID: " + id
                ));

        // Validar autor
        if (dados.autorId() == null) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "É obrigatório informar um autor para o livro."
            );
        }

        // Validar categorias
        if (dados.categoriasIds() == null ||
                dados.categoriasIds().isEmpty()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O livro deve possuir pelo menos uma categoria."
            );
        }

        // Verificar se o autor existe
        Autor autor = autorRepository.findById(dados.autorId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Autor não encontrado com o ID: " + dados.autorId()
                ));

        // Buscar categorias
        List<Categoria> categorias =
                categoriaRepository.findAllById(dados.categoriasIds());

        // Verificar se todas as categorias existem
        if (categorias.size() != dados.categoriasIds().stream().distinct().count()) {

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Uma ou mais categorias informadas não foram encontradas."
            );
        }

        // Atualizar dados do livro existente
        livro.setTitulo(dados.titulo());
        livro.setIsbn(dados.isbn());
        livro.setAnoPublicacao(dados.anoPublicacao());
        livro.setAutor(autor);
        livro.setCategorias(categorias);

        // Salvar alterações
        Livro livroAtualizado = livroRepository.save(livro);

        // Retornar ResponseDTO
        return LivroMapper.toResponse(livroAtualizado);
    }

    // Excluir livro
    @Transactional
    public void excluir(Long id) {

        // Verificar se o livro existe
        buscarPorId(id);

        // Excluir livro
        livroRepository.deleteById(id);
    }
}
