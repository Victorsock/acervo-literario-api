
package br.com.treinarecife.acervo.literario.api.entity;

// Imports do JPA
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;

// Imports do Java
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "autores")
public class Autor {

    // Identificador do autor
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nome do autor
    @Column(nullable = false)
    private String nome;

    // Nacionalidade do autor
    private String nacionalidade;

    // Relacionamento OneToOne com Biografia
    @OneToOne(
        mappedBy = "autor",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private Biografia biografia;

    // Relacionamento OneToMany com Livro
    @OneToMany(mappedBy = "autor")
    private List<Livro> livros = new ArrayList<>();

    // Construtor vazio
    public Autor() {
    }

    // Construtor com parâmetros
    public Autor(String nome, String nacionalidade) {
        this.nome = nome;
        this.nacionalidade = nacionalidade;
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNacionalidade() {
        return nacionalidade;
    }

    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    public Biografia getBiografia() {
        return biografia;
    }

    public void setBiografia(Biografia biografia) {
        this.biografia = biografia;
    }

    public List<Livro> getLivros() {
        return livros;
    }

    public void setLivros(List<Livro> livros) {
        this.livros = livros;
    }
}
