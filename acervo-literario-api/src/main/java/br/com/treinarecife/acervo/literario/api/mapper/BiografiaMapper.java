
package br.com.treinarecife.acervo.literario.api.mapper;

import br.com.treinarecife.acervo.literario.api.entity.Autor;
import br.com.treinarecife.acervo.literario.api.entity.Biografia;

import br.com.treinarecife.acervo.literario.api.request.BiografiaRequestDTO;
import br.com.treinarecife.acervo.literario.api.response.BiografiaResponseDTO;

public class BiografiaMapper {

    private BiografiaMapper() {
        // Impede a instanciação da classe
    }

    // Converter RequestDTO para Entidade
    public static Biografia toEntity(
            BiografiaRequestDTO request,
            Autor autor) {

        Biografia biografia = new Biografia();

        biografia.setTexto(request.texto());
        biografia.setDataNascimento(request.dataNascimento());
        biografia.setAutor(autor);

        return biografia;
    }

    // Converter Entidade para ResponseDTO
    public static BiografiaResponseDTO toResponse(Biografia biografia) {

        return new BiografiaResponseDTO(
                biografia.getId(),
                biografia.getTexto(),
                biografia.getDataNascimento(),
                biografia.getAutor().getId()
        );
    }
}
