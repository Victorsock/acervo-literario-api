
package br.com.treinarecife.acervo.literario.api.response;

import java.util.List;

public record LivroResponseDTO(

        Long id,

        String titulo,

        String isbn,

        Integer anoPublicacao,

        AutorResumoDTO autor,

        List<CategoriaResumoDTO> categorias

) {

    public record AutorResumoDTO(

            Long id,

            String nome

    ) {
    }

    public record CategoriaResumoDTO(

            Long id,

            String nome

    ) {
    }
}
