
package br.com.treinarecife.acervo.literario.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record LivroRequestDTO(

        @NotBlank(message = "O título do livro é obrigatório.")
        String titulo,

        String isbn,

        Integer anoPublicacao,

        @NotNull(message = "O ID do autor é obrigatório.")
        Long autorId,

        @NotEmpty(message = "O livro deve possuir pelo menos uma categoria.")
        List<Long> categoriasIds

) {
}
