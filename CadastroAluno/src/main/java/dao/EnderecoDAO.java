package dao;

import model.Aluno;
import model.Endereco;
import jakarta.persistence.EntityManager;
import util.EntityManagerUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

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
            em.persist(endereco); //inserindo endereco no BD
            em.getTransaction().commit(); //confirma que é isso mesmo
            return endereco; //retorna o endereco com ID (gerado no BD)
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

    public void checarCEPScanner() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Digite o CEP:");
        String cep = sc.nextLine().trim();

        String jpql = "SELECT COUNT(e) FROM Endereco e WHERE e.cep = :cep";
        Long qtd = em.createQuery(jpql, Long.class)
                     .setParameter("cep", cep)
                     .getSingleResult();

        if (qtd > 0){
            System.out.println("CEP encontrado! Iniciando cadastro de aluno...");

            Aluno aluno = new Aluno();

            System.out.println("Insira o nome: ");
            aluno.setNome(sc.nextLine().trim());

            System.out.println("Insira o RA: ");
            aluno.setRa(sc.nextLine().trim());

            System.out.println("Insira o telefone: ");
            aluno.setTelefone(sc.nextLine().trim());

            System.out.println("Insira o email: ");
            aluno.setEmail(sc.nextLine().trim());

            System.out.println("Insira a data de nascimento: ");
            String dataTexto = sc.nextLine().trim();

            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                sdf.setLenient(false);
                Date data = sdf.parse(dataTexto);
                aluno.setData_nasc(data);
            } catch (ParseException e) {
                System.out.println("Data inválida! Use o formato dd/MM/yyyy.");
                return;
            }

            String jpqlEndereco = "SELECT e FROM Endereco e WHERE e.cep = :cep";
            Endereco endereco = em.createQuery(jpqlEndereco, Endereco.class)
                                  .setParameter("cep", cep)
                                  .getSingleResult();

            endereco.setAluno(aluno);

            em.getTransaction().begin();
            em.persist(aluno);
            em.getTransaction().commit();

            System.out.println("Aluno cadastrado e associado ao endereço!");

        } else {
            System.out.println("CEP não encontrado!");
        }

        em.close();
        sc.close();

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



