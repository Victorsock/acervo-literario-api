
package br.com.treinarecife.acervo.literario.api.repository;

import br.com.treinarecife.acervo.literario.api.entity.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {

    // Buscar livros pelo nome da categoria
    List<Livro> findByCategoriasNome(String nome);

    // Buscar livros por trecho do título,
    // ignorando letras maiúsculas e minúsculas
    List<Livro> findByTituloContainingIgnoreCase(String termo);

    // Verificar se um autor possui livros cadastrados
    boolean existsByAutor_Id(Long autorId);

}
