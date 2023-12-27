package br.comvarejonline.service;

import br.comvarejonline.model.MovimentacaoEstoque;
import br.comvarejonline.model.Produto;
import br.comvarejonline.model.TipoMovimento;
import br.comvarejonline.repository.MovimentacaoEstoqueRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MovimentacaoEstoqueService {

    private final MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;

    public MovimentacaoEstoqueService(MovimentacaoEstoqueRepository movimentacaoEstoqueRepository) {
        this.movimentacaoEstoqueRepository = movimentacaoEstoqueRepository;
    }

    public MovimentacaoEstoque lançarMovimentacaoEstoque(MovimentacaoEstoque movimentacaoEstoque) {
        return movimentacaoEstoqueRepository.save(movimentacaoEstoque);
    }

    public List<MovimentacaoEstoque> buscarPorTipoMovimento(TipoMovimento tipoMovimento) {
        return movimentacaoEstoqueRepository.findByTipoMovimento(tipoMovimento);
    }

    public List<MovimentacaoEstoque> buscarPorData(LocalDate inicio, LocalDate fim) {
        return movimentacaoEstoqueRepository.findByDataBetween(inicio, fim);
    }

    public List<MovimentacaoEstoque> buscarPorProduto(Produto produto) {
        return movimentacaoEstoqueRepository.findByProduto(produto);
    }

}

