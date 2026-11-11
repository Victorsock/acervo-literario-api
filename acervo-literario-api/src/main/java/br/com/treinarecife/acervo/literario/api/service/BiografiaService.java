
package br.com.treinarecife.acervo.literario.api.service;

import br.com.treinarecife.acervo.literario.api.entity.Autor;
import br.com.treinarecife.acervo.literario.api.entity.Biografia;

import br.com.treinarecife.acervo.literario.api.repository.AutorRepository;
import br.com.treinarecife.acervo.literario.api.repository.BiografiaRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class BiografiaService {

    private final BiografiaRepository biografiaRepository;
    private final AutorRepository autorRepository;

    public BiografiaService(
            BiografiaRepository biografiaRepository,
            AutorRepository autorRepository) {

        this.biografiaRepository = biografiaRepository;
        this.autorRepository = autorRepository;
    }

    // Listar todas as biografias
    @Transactional(readOnly = true)
    public List<Biografia> listarTodas() {
        return biografiaRepository.findAll();
    }

    // Buscar biografia pelo ID
    @Transactional(readOnly = true)
    public Biografia buscarPorId(Long id) {

        return biografiaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Biografia não encontrada com o ID: " + id
                ));
    }

    // Buscar biografia pelo ID do autor
    @Transactional(readOnly = true)
    public Biografia buscarPorAutor(Long autorId) {

        return biografiaRepository.findByAutor_Id(autorId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Biografia não encontrada para o autor informado."
                ));
    }

    // Cadastrar biografia
    @Transactional
    public Biografia salvar(Biografia biografia) {

        // Verifica se foi informado um autor
        if (biografia.getAutor() == null ||
                biografia.getAutor().getId() == null) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "É obrigatório informar um autor para cadastrar a biografia."
            );
        }

        // Verifica se o autor existe
        Long autorId = biografia.getAutor().getId();

        Autor autor = autorRepository.findById(autorId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Autor não encontrado com o ID: " + autorId
                ));

        // Verifica se o autor já possui uma biografia
        if (biografiaRepository.findByAutor_Id(autorId).isPresent()) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Este autor já possui uma biografia cadastrada."
            );
        }

        // Associa o autor existente à biografia
        biografia.setAutor(autor);

        return biografiaRepository.save(biografia);
    }

    // Atualizar biografia
    @Transactional
    public Biografia atualizar(Long id, Biografia biografiaAtualizada) {

        Biografia biografiaExistente = buscarPorId(id);

        biografiaExistente.setTexto(biografiaAtualizada.getTexto());

        biografiaExistente.setDataNascimento(
                biografiaAtualizada.getDataNascimento()
        );

        return biografiaRepository.save(biografiaExistente);
    }

    // Excluir biografia
    @Transactional
    public void excluir(Long id) {

        Biografia biografia = buscarPorId(id);

        biografiaRepository.delete(biografia);
    }
}
