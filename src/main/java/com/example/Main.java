package com.example;

import com.example.controller.ProdutoController;
import com.example.model.ProdutoRepository;
import com.example.model.ProdutoRepositoryImpl;
import com.example.model.ProdutoService;
import com.example.view.MainFrame;
import com.example.view.ProdutoFormView;
import com.example.view.ProdutoListView;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        // 1. Cria a fábrica de EntityManager (lê o arquivo META-INF/persistence.xml)
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pdvPU");

        // 2. Model: Repository -> Service
        ProdutoRepository repository = new ProdutoRepositoryImpl(emf);
        ProdutoService service = new ProdutoService(repository);

        // 3. View e Controller são criados na thread gráfica do Swing (EDT)
        SwingUtilities.invokeLater(() -> {
            ProdutoListView listView = new ProdutoListView();
            ProdutoFormView formView = new ProdutoFormView();
            MainFrame mainFrame = new MainFrame(listView, formView);

            new ProdutoController(service, mainFrame, listView, formView);

            mainFrame.setVisible(true);
        });

        // 4. Fecha a conexão com o banco quando o programa terminar
        Runtime.getRuntime().addShutdownHook(new Thread(emf::close));
    }
}
