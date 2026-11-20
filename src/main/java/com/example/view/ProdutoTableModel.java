package com.example.view;

import com.example.model.Produto;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Diz para a JTable quantas linhas/colunas existem e o que mostrar em cada célula.
 */
public class ProdutoTableModel extends AbstractTableModel {

    private final String[] colunas = {"ID", "Código de Barras", "Nome", "Preço (R$)", "Estoque"};
    private List<Produto> produtos = new ArrayList<>();

    public void setProdutos(List<Produto> novosProdutos) {
        this.produtos = new ArrayList<>(novosProdutos);
        // Notifica a JTable visual de que os dados mudaram, provocando o redesenho imediato
        fireTableDataChanged();
    }

    public Produto getProduto(int linha) {
        return produtos.get(linha);
    }

    @Override
    public int getRowCount() {
        return produtos.size();
    }

    @Override
    public int getColumnCount() {
        return colunas.length;
    }

    @Override
    public String getColumnName(int coluna) {
        return colunas[coluna];
    }

    @Override
    public Object getValueAt(int linha, int coluna) {
        Produto p = produtos.get(linha);
        switch (coluna) {
            case 0: return p.getId();
            case 1: return p.getCodigoBarras();
            case 2: return p.getNome();
            case 3: return p.getPreco();
            case 4: return p.getQuantidadeEstoque();
            default: return null;
        }
    }
}
