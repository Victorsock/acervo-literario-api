
package br.com.treinarecife.acervo.literario.api.mapper;

import br.com.treinarecife.acervo.literario.api.entity.Autor;
import br.com.treinarecife.acervo.literario.api.entity.Categoria;
import br.com.treinarecife.acervo.literario.api.entity.Livro;

import br.com.treinarecife.acervo.literario.api.request.LivroRequestDTO;
import br.com.treinarecife.acervo.literario.api.response.LivroResponseDTO;

import java.util.List;

public class LivroMapper {

    private LivroMapper() {
        // Impede a instanciação da classe
    }

    // Converter RequestDTO para Entidade
    public static Livro toEntity(
            LivroRequestDTO request,
            Autor autor,
            List<Categoria> categorias) {

        Livro livro = new Livro();

        livro.setTitulo(request.titulo());
        livro.setIsbn(request.isbn());
        livro.setAnoPublicacao(request.anoPublicacao());

        livro.setAutor(autor);
        livro.setCategorias(categorias);

        return livro;
    }

    // Converter Entidade para ResponseDTO
    public static LivroResponseDTO toResponse(Livro livro) {

        LivroResponseDTO.AutorResumoDTO autorResumo =
                new LivroResponseDTO.AutorResumoDTO(
                        livro.getAutor().getId(),
                        livro.getAutor().getNome()
                );

        List<LivroResponseDTO.CategoriaResumoDTO> categoriasResumo =
                livro.getCategorias()
                        .stream()
                        .map(categoria ->
                                new LivroResponseDTO.CategoriaResumoDTO(
                                        categoria.getId(),
                                        categoria.getNome()
                                )
                        )
                        .toList();

        return new LivroResponseDTO(
                livro.getId(),
                livro.getTitulo(),
                livro.getIsbn(),
                livro.getAnoPublicacao(),
                autorResumo,
                categoriasResumo
        );
    }
}
