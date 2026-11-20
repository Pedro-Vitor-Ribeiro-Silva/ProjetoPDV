package com.example.model;

import javax.swing.SwingUtilities;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service: regras de negócio e validações.
 * Também é o "Subject" do padrão Observer (avisa os observadores quando algo muda).
 */
public class ProdutoService {

    private final ProdutoRepository repository;                     // depende da INTERFACE (DIP)
    private final List<ProdutoObserver> observers = new ArrayList<>();

    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }

    // ---------- Observer ----------

    public void addObserver(ProdutoObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(ProdutoObserver observer) {
        observers.remove(observer);
    }

    public void notificarObservadores() {
        List<Produto> listaAtualizada = repository.listarTodos();

        // Garante que a tela seja atualizada na thread gráfica do Swing (EDT)
        SwingUtilities.invokeLater(() -> {
            for (ProdutoObserver observer : observers) {
                observer.onProdutosChanged(listaAtualizada);
            }
        });
    }

    // ---------- Casos de uso ----------

    public Produto salvar(Produto produto) {
        validarRegrasDeNegocio(produto);

        // Validação de unicidade no repositório
        Optional<Produto> existente = repository.buscarPorCodigoBarras(produto.getCodigoBarras());
        if (existente.isPresent()) {
            throw new IllegalArgumentException("Já existe um produto com o código de barras informado.");
        }

        Produto salvo = repository.salvar(produto);
        notificarObservadores(); // Dispara o Observer!
        return salvo;
    }

    public Produto atualizar(Produto produto) {
        if (produto.getId() == null) {
            throw new IllegalArgumentException("Produto sem ID não pode ser atualizado.");
        }
        validarRegrasDeNegocio(produto);

        // O código de barras pode existir, desde que seja do próprio produto
        Optional<Produto> existente = repository.buscarPorCodigoBarras(produto.getCodigoBarras());
        if (existente.isPresent() && !existente.get().getId().equals(produto.getId())) {
            throw new IllegalArgumentException("Já existe outro produto com o código de barras informado.");
        }

        Produto atualizado = repository.atualizar(produto);
        notificarObservadores();
        return atualizado;
    }

    public void excluir(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Selecione um produto para excluir.");
        }
        repository.excluir(id);
        notificarObservadores();
    }

    public List<Produto> listarTodos() {
        return repository.listarTodos();
    }

    // ---------- Regras de negócio ----------

    private void validarRegrasDeNegocio(Produto produto) {
        if (produto == null) {
            throw new IllegalArgumentException("Produto não pode ser nulo.");
        }
        if (produto.getCodigoBarras() == null || produto.getCodigoBarras().isBlank()) {
            throw new IllegalArgumentException("Código de barras é obrigatório.");
        }
        if (produto.getNome() == null || produto.getNome().isBlank()) {
            throw new IllegalArgumentException("Nome é obrigatório.");
        }
        if (produto.getPreco() == null || produto.getPreco().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Preço deve ser maior que zero.");
        }
        if (produto.getQuantidadeEstoque() == null || produto.getQuantidadeEstoque() < 0) {
            throw new IllegalArgumentException("Quantidade em estoque não pode ser negativa.");
        }
    }
}
