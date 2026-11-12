package br.com.treinarecife.acervo.literario.api.request;

import jakarta.validation.constraints.NotBlank;

public record AutorRequestDTO(

        @NotBlank(message = "O nome do autor é obrigatório.")
        String nome,

        String nacionalidade

) {
}
