package br.com.treinarecife.acervo.literario.api.repository;

import br.com.treinarecife.acervo.literario.api.entity.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {

}
