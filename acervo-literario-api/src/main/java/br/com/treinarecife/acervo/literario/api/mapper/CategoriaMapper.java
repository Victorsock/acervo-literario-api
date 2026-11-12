
package br.com.treinarecife.acervo.literario.api.mapper;

import br.com.treinarecife.acervo.literario.api.entity.Categoria;
import br.com.treinarecife.acervo.literario.api.request.CategoriaRequestDTO;
import br.com.treinarecife.acervo.literario.api.response.CategoriaResponseDTO;

public class CategoriaMapper {

    private CategoriaMapper() {
        // Impede a instanciação da classe
    }

    // Converter RequestDTO para Entidade
    public static Categoria toEntity(CategoriaRequestDTO request) {

        Categoria categoria = new Categoria();

        categoria.setNome(request.nome());

        return categoria;
    }

    // Converter Entidade para ResponseDTO
    public static CategoriaResponseDTO toResponse(Categoria categoria) {

        return new CategoriaResponseDTO(
                categoria.getId(),
                categoria.getNome()
        );
    }
}
