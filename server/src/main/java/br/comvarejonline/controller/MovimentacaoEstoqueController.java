package br.comvarejonline.controller;

import br.comvarejonline.model.MovimentacaoEstoque;
import br.comvarejonline.model.Produto;
import br.comvarejonline.model.TipoMovimento;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import br.comvarejonline.repository.MovimentacaoEstoqueRepository;
import br.comvarejonline.repository.ProdutoRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/movimentacoes")
public class MovimentacaoEstoqueController {

    private final MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;
    private final ProdutoRepository produtoRepository;

    public MovimentacaoEstoqueController(MovimentacaoEstoqueRepository movimentacaoEstoqueRepository, ProdutoRepository produtoRepository) {
        this.movimentacaoEstoqueRepository = movimentacaoEstoqueRepository;
        this.produtoRepository = produtoRepository;
    }


    @PostMapping("/lancar-movimentacao")
    public ResponseEntity<?> lançarMovimentacaoEstoque(@RequestBody MovimentacaoEstoque movimentacaoEstoque) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isGerente = authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_GERENTE"));

        if ((movimentacaoEstoque.getTipoMovimento() == TipoMovimento.SALDO_INICIAL ||
                movimentacaoEstoque.getTipoMovimento() == TipoMovimento.AJUSTE_ENTRADA ||
                movimentacaoEstoque.getTipoMovimento() == TipoMovimento.AJUSTE_SAIDA ||
                movimentacaoEstoque.getTipoMovimento() == TipoMovimento.SAIDA ||
                movimentacaoEstoque.getTipoMovimento() == TipoMovimento.ENTRADA) && !isGerente) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Somente gerentes podem realizar esse tipo de movimentação");
        }


        // Verifica se já existe um lançamento do tipo SALDO_INICIAL para o produto
        if (movimentacaoEstoque.getTipoMovimento() == TipoMovimento.SALDO_INICIAL) {
            Produto produto = movimentacaoEstoque.getProduto();
            List<MovimentacaoEstoque> movimentacoesSaldoInicial = movimentacaoEstoqueRepository.findByProdutoAndTipoMovimento(produto, TipoMovimento.SALDO_INICIAL);
            if (!movimentacoesSaldoInicial.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Já existe um lançamento do tipo SALDO_INICIAL para este produto");
            }
        }

        // Salva a movimentação no banco de dados
        MovimentacaoEstoque novaMovimentacao = movimentacaoEstoqueRepository.save(movimentacaoEstoque);
        return new ResponseEntity<>(novaMovimentacao, HttpStatus.CREATED);
    }

    @GetMapping("/tipo/{tipoMovimento}")
    public ResponseEntity<List<MovimentacaoEstoque>> buscarPorTipoMovimento(@PathVariable TipoMovimento tipoMovimento) {
        List<MovimentacaoEstoque> movimentacoes = movimentacaoEstoqueRepository.findByTipoMovimento(tipoMovimento);
        return new ResponseEntity<>(movimentacoes, HttpStatus.OK);
    }

    @GetMapping("/data")
    public ResponseEntity<List<MovimentacaoEstoque>> buscarPorData(@RequestParam("inicio") LocalDate inicio,
                                                                   @RequestParam("fim") LocalDate fim) {
        List<MovimentacaoEstoque> movimentacoes = movimentacaoEstoqueRepository.findByDataBetween(inicio, fim);
        return new ResponseEntity<>(movimentacoes, HttpStatus.OK);
    }

    @GetMapping("/produto/{produtoId}")
    public ResponseEntity<List<MovimentacaoEstoque>> buscarPorProduto(@PathVariable Long produtoId) {
        Optional<Produto> produtoOptional = produtoRepository.findById(produtoId);
        if (produtoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Produto produto = produtoOptional.get();
        List<MovimentacaoEstoque> movimentacoes = movimentacaoEstoqueRepository.findByProduto(produto);
        return new ResponseEntity<>(movimentacoes, HttpStatus.OK);
    }
}
