
package br.com.treinarecife.acervo.literario.api.service;

import br.com.treinarecife.acervo.literario.api.entity.Autor;
import br.com.treinarecife.acervo.literario.api.entity.Biografia;

import br.com.treinarecife.acervo.literario.api.mapper.BiografiaMapper;

import br.com.treinarecife.acervo.literario.api.repository.AutorRepository;
import br.com.treinarecife.acervo.literario.api.repository.BiografiaRepository;

import br.com.treinarecife.acervo.literario.api.request.BiografiaRequestDTO;
import br.com.treinarecife.acervo.literario.api.response.BiografiaResponseDTO;

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
    public List<BiografiaResponseDTO> listarTodas() {

        return biografiaRepository.findAll()
                .stream()
                .map(BiografiaMapper::toResponse)
                .toList();
    }

    // Buscar biografia pelo ID
    @Transactional(readOnly = true)
    public BiografiaResponseDTO buscarPorId(Long id) {

        return biografiaRepository.findById(id)
                .map(BiografiaMapper::toResponse)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Biografia não encontrada com o ID: " + id
                ));
    }

    // Buscar biografia pelo ID do autor
    @Transactional(readOnly = true)
    public BiografiaResponseDTO buscarPorAutor(Long autorId) {

        return biografiaRepository.findByAutor_Id(autorId)
                .map(BiografiaMapper::toResponse)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Biografia não encontrada para o autor informado."
                ));
    }

    // Cadastrar biografia
    @Transactional
    public BiografiaResponseDTO salvar(BiografiaRequestDTO dados) {

        // Validar se o ID do autor foi informado
        if (dados.autorId() == null) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "É obrigatório informar o ID do autor."
            );
        }

        // Verificar se o autor existe
        Autor autor = autorRepository.findById(dados.autorId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Autor não encontrado com o ID: " + dados.autorId()
                ));

        // Verificar se o autor já possui uma biografia
        if (biografiaRepository.findByAutor_Id(autor.getId()).isPresent()) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Este autor já possui uma biografia cadastrada."
            );
        }

        // Converter RequestDTO para Entidade
        Biografia biografia = BiografiaMapper.toEntity(dados, autor);

        // Salvar biografia
        Biografia biografiaSalva = biografiaRepository.save(biografia);

        // Converter Entidade para ResponseDTO
        return BiografiaMapper.toResponse(biografiaSalva);
    }

    // Atualizar biografia
    @Transactional
    public BiografiaResponseDTO atualizar(
            Long id,
            BiografiaRequestDTO dados) {

        // Verificar se a biografia existe
        Biografia biografia = biografiaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Biografia não encontrada com o ID: " + id
                ));

        // Validar o ID do autor
        if (dados.autorId() == null) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "É obrigatório informar o ID do autor."
            );
        }

        // Verificar se o autor informado existe
        Autor autor = autorRepository.findById(dados.autorId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Autor não encontrado com o ID: " + dados.autorId()
                ));

        // Verificar se outro registro já utiliza esse autor
        biografiaRepository.findByAutor_Id(autor.getId())
                .ifPresent(biografiaEncontrada -> {

                    if (!biografiaEncontrada.getId().equals(id)) {

                        throw new ResponseStatusException(
                                HttpStatus.CONFLICT,
                                "Este autor já possui outra biografia cadastrada."
                        );
                    }
                });

        // Atualizar os dados da biografia existente
        biografia.setTexto(dados.texto());
        biografia.setDataNascimento(dados.dataNascimento());
        biografia.setAutor(autor);

        // Salvar alterações
        Biografia biografiaAtualizada =
                biografiaRepository.save(biografia);

        // Retornar ResponseDTO
        return BiografiaMapper.toResponse(biografiaAtualizada);
    }

    // Excluir biografia
    @Transactional
    public void excluir(Long id) {

        // Verificar se a biografia existe
        Biografia biografia = biografiaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Biografia não encontrada com o ID: " + id
                ));

        biografiaRepository.delete(biografia);
    }
}
