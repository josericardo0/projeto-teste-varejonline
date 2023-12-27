package br.comvarejonline.service;

import br.comvarejonline.model.Produto;
import br.comvarejonline.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public List<Produto> listarProdutos() {
        return produtoRepository.findAll();
    }

    public Optional<Produto> buscarProdutoPorId(Long id) {
        return produtoRepository.findById(id);
    }

    public Produto criarProduto(Produto produto) {
        return produtoRepository.save(produto);
    }

    public Optional<Produto> atualizarProduto(Long id, Produto produtoAtualizado) {
        Optional<Produto> produtoOptional = produtoRepository.findById(id);
        if (produtoOptional.isPresent()) {
            produtoAtualizado.setId(id);
            return Optional.of(produtoRepository.save(produtoAtualizado));
        }
        return Optional.empty();
    }

    public boolean produtoExistePorCodigoDeBarras(String codigoDeBarras) {
        return produtoRepository.existsByCodigoDeBarras(codigoDeBarras);
    }
}
