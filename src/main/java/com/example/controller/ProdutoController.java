package com.example.controller;

import com.example.model.Produto;
import com.example.model.ProdutoObserver;
import com.example.model.ProdutoService;
import com.example.view.MainFrame;
import com.example.view.ProdutoFormView;
import com.example.view.ProdutoListView;

import javax.swing.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * Controller: liga a View ao Model.
 * - escuta os cliques dos botões
 * - converte o texto digitado em números
 * - chama o Service
 * - troca de tela (CardLayout)
 * Também é um Observer: é avisado quando a lista de produtos muda.
 */
public class ProdutoController implements ProdutoObserver {

    private final ProdutoService service;
    private final MainFrame mainFrame;
    private final ProdutoListView listView;
    private final ProdutoFormView formView;

    // Guarda o produto que está sendo editado (null = cadastro novo)
    private Produto produtoEmEdicao = null;

    public ProdutoController(ProdutoService service, MainFrame mainFrame,
                             ProdutoListView listView, ProdutoFormView formView) {
        this.service = service;
        this.mainFrame = mainFrame;
        this.listView = listView;
        this.formView = formView;

        service.addObserver(this); // se inscreve para receber as notificações
        registrarEventos();
        listView.getTableModel().setProdutos(service.listarTodos()); // carga inicial
    }

    private void registrarEventos() {
        listView.getBtnNovo().addActionListener(e -> novo());
        listView.getBtnEditar().addActionListener(e -> editar());
        listView.getBtnExcluir().addActionListener(e -> excluir());
        formView.getBtnSalvar().addActionListener(e -> salvar());
        formView.getBtnCancelar().addActionListener(e -> voltarParaLista());
    }

    private void novo() {
        produtoEmEdicao = null;
        formView.limpar();
        mainFrame.mostrarTela(MainFrame.TELA_FORM);
    }

    private void editar() {
        Produto selecionado = getProdutoSelecionado();
        if (selecionado == null) {
            JOptionPane.showMessageDialog(mainFrame, "Selecione um produto na tabela.");
            return;
        }
        produtoEmEdicao = selecionado;
        formView.preencher(selecionado);
        mainFrame.mostrarTela(MainFrame.TELA_FORM);
    }

    private void excluir() {
        Produto selecionado = getProdutoSelecionado();
        if (selecionado == null) {
            JOptionPane.showMessageDialog(mainFrame, "Selecione um produto na tabela.");
            return;
        }
        int resposta = JOptionPane.showConfirmDialog(mainFrame,
                "Deseja excluir o produto \"" + selecionado.getNome() + "\"?",
                "Confirmar exclusão", JOptionPane.YES_NO_OPTION);

        if (resposta == JOptionPane.YES_OPTION) {
            try {
                service.excluir(selecionado.getId());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void salvar() {
        try {
            // Converte o texto dos campos em objetos Java
            Produto produto = new Produto();
            produto.setCodigoBarras(formView.getCodigoBarras());
            produto.setNome(formView.getNome());
            produto.setPreco(new BigDecimal(formView.getPreco().replace(",", ".")));
            produto.setQuantidadeEstoque(Integer.parseInt(formView.getEstoque()));

            if (produtoEmEdicao == null) {
                service.salvar(produto);                 // cadastro novo
            } else {
                produto.setId(produtoEmEdicao.getId());  // mantém o mesmo ID
                service.atualizar(produto);              // edição
            }

            JOptionPane.showMessageDialog(mainFrame, "Produto salvo com sucesso!");
            voltarParaLista();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(mainFrame, "Preço e estoque devem ser números válidos.",
                    "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            // Mensagens das regras de negócio vindas do Service
            JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void voltarParaLista() {
        produtoEmEdicao = null;
        formView.limpar();
        mainFrame.mostrarTela(MainFrame.TELA_LISTA);
    }

    private Produto getProdutoSelecionado() {
        int linha = listView.getTabela().getSelectedRow();
        if (linha < 0) {
            return null;
        }
        return listView.getTableModel().getProduto(linha);
    }

    // Chamado pelo Service (Observer) sempre que os produtos mudam
    @Override
    public void onProdutosChanged(List<Produto> produtos) {
        listView.getTableModel().setProdutos(produtos);
    }
}
