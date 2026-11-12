
package br.com.treinarecife.acervo.literario.api.mapper;

import br.com.treinarecife.acervo.literario.api.entity.Autor;
import br.com.treinarecife.acervo.literario.api.request.AutorRequestDTO;
import br.com.treinarecife.acervo.literario.api.response.AutorResponseDTO;

public class AutorMapper {

    private AutorMapper() {
        // Impede a instanciação da classe
    }

    // Converter RequestDTO para Entidade
    public static Autor toEntity(AutorRequestDTO request) {

        Autor autor = new Autor();

        autor.setNome(request.nome());
        autor.setNacionalidade(request.nacionalidade());

        return autor;
    }

    // Converter Entidade para ResponseDTO
    public static AutorResponseDTO toResponse(Autor autor) {

        return new AutorResponseDTO(
                autor.getId(),
                autor.getNome(),
                autor.getNacionalidade()
        );
    }
}
