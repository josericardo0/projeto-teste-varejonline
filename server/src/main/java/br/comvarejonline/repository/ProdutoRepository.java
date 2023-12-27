package br.comvarejonline.repository;
import br.comvarejonline.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    boolean existsByCodigoDeBarras(String codigoDeBarras);
}
