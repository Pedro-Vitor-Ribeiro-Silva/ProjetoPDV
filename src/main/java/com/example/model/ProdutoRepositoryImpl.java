package com.example.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

import java.util.List;
import java.util.Optional;

/**
 * Implementação concreta do Repository usando JPA / Hibernate.
 * Única classe do sistema que conversa com o banco de dados (EntityManager).
 */
public class ProdutoRepositoryImpl implements ProdutoRepository {

    private final EntityManagerFactory emf;

    // A dependência é recebida pelo construtor
    public ProdutoRepositoryImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Produto salvar(Produto produto) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();               // 1. Início da transação atômica
            em.persist(produto);      // 2. Operação de inserção
            tx.commit();              // 3. Efetivação das alterações no banco
            return produto;
        } catch (Exception ex) {
            if (tx.isActive()) {
                tx.rollback();        // 4. Reversão integral em caso de falha
            }
            throw new RuntimeException("Falha ao salvar produto: " + ex.getMessage(), ex);
        } finally {
            em.close();               // 5. Prevenção de Connection Leak
        }
    }

    @Override
    public Produto atualizar(Produto produto) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Produto atualizado = em.merge(produto); // merge: objeto Detached volta a ser Managed
            tx.commit();
            return atualizado;
        } catch (Exception ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Falha ao atualizar produto: " + ex.getMessage(), ex);
        } finally {
            em.close();
        }
    }

    @Override
    public void excluir(Long id) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Produto produto = em.find(Produto.class, id); // busca o produto (estado Managed)
            if (produto != null) {
                em.remove(produto);                       // marca como Removed
            }
            tx.commit();                                  // apaga do banco
        } catch (Exception ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Falha ao excluir produto: " + ex.getMessage(), ex);
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<Produto> buscarPorId(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return Optional.ofNullable(em.find(Produto.class, id));
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<Produto> buscarPorCodigoBarras(String codigoBarras) {
        EntityManager em = emf.createEntityManager();
        try {
            // Consulta parametrizada (:codigoBarras) -> evita SQL Injection
            List<Produto> resultado = em.createQuery(
                            "SELECT p FROM Produto p WHERE p.codigoBarras = :codigoBarras", Produto.class)
                    .setParameter("codigoBarras", codigoBarras)
                    .getResultList();
            return resultado.stream().findFirst();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Produto> listarTodos() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT p FROM Produto p ORDER BY p.id", Produto.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
