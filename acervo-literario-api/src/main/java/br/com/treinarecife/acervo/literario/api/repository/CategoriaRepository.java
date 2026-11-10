
package br.com.treinarecife.acervo.literario.api.repository;

import br.com.treinarecife.acervo.literario.api.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    // Buscar categoria pelo nome
    Optional<Categoria> findByNome(String nome);

}
