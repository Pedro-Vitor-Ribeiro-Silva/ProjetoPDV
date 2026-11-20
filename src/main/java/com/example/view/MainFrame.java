package com.example.view;

import javax.swing.*;
import java.awt.*;

/**
 * Janela principal. Usa CardLayout para alternar entre a lista e o formulário
 * (como um baralho: só uma "carta" aparece por vez).
 */
public class MainFrame extends JFrame {

    public static final String TELA_LISTA = "LISTA";
    public static final String TELA_FORM = "FORM";

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel painelCartas = new JPanel(cardLayout);

    public MainFrame(ProdutoListView listView, ProdutoFormView formView) {
        super("PDV - Cadastro de Produtos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(750, 450);
        setLocationRelativeTo(null); // centraliza na tela

        painelCartas.add(listView, TELA_LISTA);
        painelCartas.add(formView, TELA_FORM);
        add(painelCartas);
    }

    public void mostrarTela(String nomeTela) {
        cardLayout.show(painelCartas, nomeTela);
    }
}
