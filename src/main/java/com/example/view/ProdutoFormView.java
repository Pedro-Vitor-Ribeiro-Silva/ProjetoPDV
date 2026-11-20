package com.example.view;

import com.example.model.Produto;

import javax.swing.*;
import java.awt.*;

/**
 * Tela de formulário: usada para cadastrar e editar um produto.
 * Só captura o que o usuário digita; quem decide o que fazer é o Controller.
 */
public class ProdutoFormView extends JPanel {

    private final JLabel lblTitulo = new JLabel("Novo Produto");
    private final JTextField txtCodigoBarras = new JTextField(20);
    private final JTextField txtNome = new JTextField(20);
    private final JTextField txtPreco = new JTextField(20);
    private final JTextField txtEstoque = new JTextField(20);
    private final JButton btnSalvar = new JButton("Salvar");
    private final JButton btnCancelar = new JButton("Cancelar");

    public ProdutoFormView() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(lblTitulo, gbc);
        gbc.gridwidth = 1;

        adicionarCampo("Código de barras:", txtCodigoBarras, 1, gbc);
        adicionarCampo("Nome:", txtNome, 2, gbc);
        adicionarCampo("Preço (ex: 10.50):", txtPreco, 3, gbc);
        adicionarCampo("Estoque:", txtEstoque, 4, gbc);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botoes.add(btnCancelar);
        botoes.add(btnSalvar);
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.EAST;
        add(botoes, gbc);
    }

    private void adicionarCampo(String texto, JTextField campo, int linha, GridBagConstraints gbc) {
        gbc.gridx = 0; gbc.gridy = linha;
        add(new JLabel(texto), gbc);
        gbc.gridx = 1;
        add(campo, gbc);
    }

    // Preenche os campos com os dados de um produto (usado ao editar)
    public void preencher(Produto produto) {
        lblTitulo.setText("Editar Produto");
        txtCodigoBarras.setText(produto.getCodigoBarras());
        txtNome.setText(produto.getNome());
        txtPreco.setText(produto.getPreco().toString());
        txtEstoque.setText(produto.getQuantidadeEstoque().toString());
    }

    public void limpar() {
        lblTitulo.setText("Novo Produto");
        txtCodigoBarras.setText("");
        txtNome.setText("");
        txtPreco.setText("");
        txtEstoque.setText("");
    }

    public String getCodigoBarras() { return txtCodigoBarras.getText().trim(); }
    public String getNome() { return txtNome.getText().trim(); }
    public String getPreco() { return txtPreco.getText().trim(); }
    public String getEstoque() { return txtEstoque.getText().trim(); }

    public JButton getBtnSalvar() { return btnSalvar; }
    public JButton getBtnCancelar() { return btnCancelar; }
}
