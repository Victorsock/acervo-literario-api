
package br.com.treinarecife.acervo.literario.api.repository;

import br.com.treinarecife.acervo.literario.api.entity.Biografia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BiografiaRepository extends JpaRepository<Biografia, Long> {

    Optional<Biografia> findByAutor_Id(Long autorId);

}
