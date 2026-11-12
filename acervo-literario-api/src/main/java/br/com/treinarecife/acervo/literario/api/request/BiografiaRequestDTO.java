
package br.com.treinarecife.acervo.literario.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record BiografiaRequestDTO(

        @NotBlank(message = "O texto da biografia é obrigatório.")
        String texto,

        LocalDate dataNascimento,

        @NotNull(message = "O ID do autor é obrigatório.")
        Long autorId

) {
}
