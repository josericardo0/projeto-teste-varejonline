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
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoRepository produtoRepository;
    private final MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;

    public ProdutoController(ProdutoRepository produtoRepository, MovimentacaoEstoqueRepository movimentacaoEstoqueRepository) {
        this.produtoRepository = produtoRepository;
        this.movimentacaoEstoqueRepository = movimentacaoEstoqueRepository;
    }

    @GetMapping
    public ResponseEntity<List<Produto>> listarProdutos() {
        List<Produto> produtos = produtoRepository.findAll();
        return new ResponseEntity<>(produtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarProdutoPorId(@PathVariable Long id) {
        Optional<Produto> produto = produtoRepository.findById(id);
        return produto.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<?> criarProduto(@RequestBody Produto produto) {
        if (!verificarPerfilGerente()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Apenas o perfil GERENTE pode cadastrar produtos.");
        }

        if (produtoRepository.existsByCodigoDeBarras(produto.getCodigoDeBarras())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Já existe um produto com esse código de barras");
        }

        Produto novoProduto = produtoRepository.save(produto);

        if (novoProduto.getSaldoInicial() > 0) {
            MovimentacaoEstoque movimentoSaldoInicial = new MovimentacaoEstoque();
            movimentoSaldoInicial.setTipoMovimento(TipoMovimento.SALDO_INICIAL);
            movimentoSaldoInicial.setProduto(novoProduto);
            movimentoSaldoInicial.setQuantidade(novoProduto.getSaldoInicial());
            movimentoSaldoInicial.setData(LocalDate.now());

            movimentacaoEstoqueRepository.save(movimentoSaldoInicial);
        }
        return new ResponseEntity<>(novoProduto, HttpStatus.CREATED);
    }



        @PutMapping("/{id}")
        public ResponseEntity<?> atualizarProduto(@PathVariable Long id, @RequestBody Produto produtoAtualizado) {
            if (!verificarPerfilGerente()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Apenas o perfil GERENTE pode editar produtos.");
            }

            Optional<Produto> produtoOptional = produtoRepository.findById(id);
            if (produtoOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Produto produtoExistente = produtoOptional.get();
            if (produtoExistente.getSaldoInicial() != produtoAtualizado.getSaldoInicial()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Saldo Inicial não pode ser alterado");
            }

            produtoAtualizado.setId(id);
            Produto produtoAtualizadoDb = produtoRepository.save(produtoAtualizado);
            return ResponseEntity.ok(produtoAtualizadoDb);
        }

        private boolean verificarPerfilGerente() {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null) {
                return authentication.getAuthorities().stream()
                        .anyMatch(auth -> auth.getAuthority().equals("ROLE_GERENTE"));
            }
            return false;
        }
}
