package com.example.model;

import java.util.List;

/**
 * Padrão Observer: quem implementa esta interface é avisado
 * sempre que a lista de produtos muda (salvar, atualizar, excluir).
 */
public interface ProdutoObserver {

    void onProdutosChanged(List<Produto> produtos);
}
