
package br.com.treinarecife.acervo.literario.api.service;

import br.com.treinarecife.acervo.literario.api.entity.Autor;
import br.com.treinarecife.acervo.literario.api.entity.Categoria;
import br.com.treinarecife.acervo.literario.api.entity.Livro;

import br.com.treinarecife.acervo.literario.api.repository.AutorRepository;
import br.com.treinarecife.acervo.literario.api.repository.CategoriaRepository;
import br.com.treinarecife.acervo.literario.api.repository.LivroRepository;

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

    @Transactional(readOnly = true)
    public List<Livro> listarTodos() {
        return livroRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Livro buscarPorId(Long id) {
        return livroRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Livro não encontrado com o ID: " + id
                ));
    }

    @Transactional
    public Livro salvar(Livro livro) {

        // Regra 1: verificar se o livro possui um autor
        if (livro.getAutor() == null ||
                livro.getAutor().getId() == null) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O cadastro do livro exige um autor."
            );
        }

        // Verificar se o autor informado realmente existe
        Autor autor = autorRepository.findById(
                livro.getAutor().getId()
        ).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "O autor informado não foi encontrado."
        ));

        // Regra 2: verificar se o livro possui pelo menos uma categoria
        if (livro.getCategorias() == null ||
                livro.getCategorias().isEmpty()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O livro deve possuir pelo menos uma categoria."
            );
        }

        // Verificar se todas as categorias informadas existem
        List<Categoria> categoriasValidas = livro.getCategorias()
                .stream()
                .map(categoria -> categoriaRepository.findById(
                        categoria.getId()
                ).orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Uma das categorias informadas não foi encontrada."
                )))
                .toList();

        // Associar as entidades existentes ao livro
        livro.setAutor(autor);
        livro.setCategorias(categoriasValidas);

        return livroRepository.save(livro);
    }

    @Transactional
    public Livro atualizar(Long id, Livro livroAtualizado) {

        Livro livroExistente = buscarPorId(id);

        // Reutilizar as validações de cadastro
        if (livroAtualizado.getAutor() == null ||
                livroAtualizado.getAutor().getId() == null) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O cadastro do livro exige um autor."
            );
        }

        Autor autor = autorRepository.findById(
                livroAtualizado.getAutor().getId()
        ).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "O autor informado não foi encontrado."
        ));

        if (livroAtualizado.getCategorias() == null ||
                livroAtualizado.getCategorias().isEmpty()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O livro deve possuir pelo menos uma categoria."
            );
        }

        List<Categoria> categoriasValidas = livroAtualizado.getCategorias()
                .stream()
                .map(categoria -> categoriaRepository.findById(
                        categoria.getId()
                ).orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Uma das categorias informadas não foi encontrada."
                )))
                .toList();

        livroExistente.setTitulo(livroAtualizado.getTitulo());
        livroExistente.setIsbn(livroAtualizado.getIsbn());
        livroExistente.setAnoPublicacao(
                livroAtualizado.getAnoPublicacao()
        );
        livroExistente.setAutor(autor);
        livroExistente.setCategorias(categoriasValidas);

        return livroRepository.save(livroExistente);
    }

    @Transactional
    public void excluir(Long id) {
        Livro livro = buscarPorId(id);
        livroRepository.delete(livro);
    }

    @Transactional(readOnly = true)
    public List<Livro> buscarPorCategoria(String nomeCategoria) {
        return livroRepository.findByCategoriasNome(nomeCategoria);
    }

    @Transactional(readOnly = true)
    public List<Livro> buscarPorTitulo(String termo) {
        return livroRepository.findByTituloContainingIgnoreCase(termo);
    }
}
