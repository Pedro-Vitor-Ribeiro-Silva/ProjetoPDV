package com.example.view;

import javax.swing.*;
import java.awt.*;

/**
 * Tela de listagem: mostra os produtos em uma tabela.
 * Não tem regra de negócio, apenas componentes visuais.
 */
public class ProdutoListView extends JPanel {

    private final ProdutoTableModel tableModel = new ProdutoTableModel();
    private final JTable tabela = new JTable(tableModel);
    private final JButton btnNovo = new JButton("Novo");
    private final JButton btnEditar = new JButton("Editar");
    private final JButton btnExcluir = new JButton("Excluir");

    public ProdutoListView() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titulo = new JLabel("Produtos cadastrados");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botoes.add(btnNovo);
        botoes.add(btnEditar);
        botoes.add(btnExcluir);

        add(titulo, BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);
        add(botoes, BorderLayout.SOUTH);
    }

    public ProdutoTableModel getTableModel() { return tableModel; }
    public JTable getTabela() { return tabela; }
    public JButton getBtnNovo() { return btnNovo; }
    public JButton getBtnEditar() { return btnEditar; }
    public JButton getBtnExcluir() { return btnExcluir; }
}
