package com.example.model;

import java.util.List;
import java.util.Optional;

/**
 * Interface do Repository (abstração).
 * O Service depende desta interface, e não da classe concreta (DIP).
 */
public interface ProdutoRepository {

    Produto salvar(Produto produto);

    Produto atualizar(Produto produto);

    void excluir(Long id);

    Optional<Produto> buscarPorId(Long id);

    Optional<Produto> buscarPorCodigoBarras(String codigoBarras);

    List<Produto> listarTodos();
}
