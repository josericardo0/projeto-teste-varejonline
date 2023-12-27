package br.comvarejonline.repository;

import br.comvarejonline.model.MovimentacaoEstoque;
import br.comvarejonline.model.Produto;
import br.comvarejonline.model.TipoMovimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MovimentacaoEstoqueRepository extends JpaRepository<MovimentacaoEstoque, Long> {

    List<MovimentacaoEstoque> findByTipoMovimento(TipoMovimento tipoMovimento);

    List<MovimentacaoEstoque> findByDataBetween(LocalDate inicio, LocalDate fim);

    List<MovimentacaoEstoque> findByProduto(Produto produto);

    List<MovimentacaoEstoque> findByProdutoAndTipoMovimento(Produto produto, TipoMovimento tipoMovimento);


}

