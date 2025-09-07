package br.unipar.devbackend.cadastroaluno.dao;

import br.unipar.devbackend.cadastroaluno.model.Endereco;
import jakarta.persistence.EntityManager;

import java.util.List;

public class EnderecoDAO {

    EntityManager em;

    public EnderecoDAO(EntityManager em) {
        this.em = em;
    }

    public List<Endereco> findAll() {
        return em.createQuery("SELECT e FROM Endereco e", Endereco.class)
                .getResultList();
    }

    public Endereco findById(Long id) {
        return em.find(Endereco.class, id);
    }

    public Endereco inserirEndereco(Endereco endereco) {
        try {
            em.getTransaction().begin(); // abrir transacao com BD
            em.persist(endereco); //inserindo aluno no BD
            em.getTransaction().commit(); //confirma que é isso mesmo
            return endereco; //retorna o aluno com ID (gerado no BD)
        } catch (Exception ex) {
            em.getTransaction().rollback(); //desfazer a operação
            System.out.println("Algo de errado não deu certo: " + ex.getMessage());
            return null;
        } finally {
            if (em.isOpen()) {
                em.close();
                System.out.println("EntityManager fechado com sucesso!");
            }
        }
    }


    public Endereco editarEndereco(Endereco endereco) {
        try {
            em.getTransaction().begin(); // abrir transacao com BD
            em.merge(endereco); //update do aluno no BD
            em.getTransaction().commit(); //confirma que é isso mesmo
            return endereco; //retorna o aluno com ID (gerado no BD)
        } catch (Exception ex) {
            em.getTransaction().rollback(); //desfazer a operação
            System.out.println("Algo de errado não deu certo: " + ex.getMessage());
            return null;
        } finally {
            if (em.isOpen()) {
                em.close();
                System.out.println("EntityManager fechado com sucesso!");
            }
        }
    }

    public Boolean deletarEndereco(Endereco endereco) {
        try {
            em.getTransaction().begin(); // abrir transacao com BD
            em.remove(endereco); //delete do aluno no BD
            em.getTransaction().commit(); //confirma que é isso mesmo
            return true; //retorna que deu certo a exclusão
        } catch (Exception ex) {
            em.getTransaction().rollback(); //desfazer a operação
            System.out.println("Algo de errado não deu certo: " + ex.getMessage());
            return false;
        } finally {
            if (em.isOpen()) {
                em.close();
                System.out.println("EntityManager fechado com sucesso!");
            }
        }

    }
}




