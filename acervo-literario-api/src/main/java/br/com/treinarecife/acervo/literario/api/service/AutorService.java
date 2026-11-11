
package br.com.treinarecife.acervo.literario.api.service;

import br.com.treinarecife.acervo.literario.api.entity.Autor;
import br.com.treinarecife.acervo.literario.api.repository.AutorRepository;
import br.com.treinarecife.acervo.literario.api.repository.LivroRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AutorService {

    private final AutorRepository autorRepository;
    private final LivroRepository livroRepository;

    // Injeção de dependências pelo construtor
    public AutorService(
            AutorRepository autorRepository,
            LivroRepository livroRepository) {

        this.autorRepository = autorRepository;
        this.livroRepository = livroRepository;
    }

    // Listar todos os autores
    @Transactional(readOnly = true)
    public List<Autor> listarTodos() {
        return autorRepository.findAll();
    }

    // Buscar autor pelo ID
    @Transactional(readOnly = true)
    public Autor buscarPorId(Long id) {

        return autorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Autor não encontrado com o ID: " + id
                ));
    }

    // Cadastrar novo autor
    @Transactional
    public Autor salvar(Autor autor) {
        return autorRepository.save(autor);
    }

    // Atualizar autor existente
    @Transactional
    public Autor atualizar(Long id, Autor autorAtualizado) {

        Autor autorExistente = buscarPorId(id);

        autorExistente.setNome(autorAtualizado.getNome());
        autorExistente.setNacionalidade(
                autorAtualizado.getNacionalidade()
        );

        return autorRepository.save(autorExistente);
    }

    // Excluir autor com validação de negócio
    @Transactional
    public void excluir(Long id) {

        // Verifica se o autor existe
        Autor autor = buscarPorId(id);

        // Verifica se existem livros associados ao autor
        boolean possuiLivros = livroRepository.existsByAutor_Id(id);

        // Impede a exclusão caso existam livros cadastrados
        if (possuiLivros) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Não é possível excluir o autor " +
                    autor.getNome() +
                    ", pois existem livros cadastrados vinculados a ele."
            );
        }

        // Se não possuir livros, permite a exclusão
        autorRepository.delete(autor);
    }
}
