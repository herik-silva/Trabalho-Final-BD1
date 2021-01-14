package Model.Entidades;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa um Cliente no sistema.
 * @author herik
 */
public class Cliente extends Pessoa{
    // Atributos
    private int id;
    private String email;
    private String cartao;
    
    // Lista de Clientes
    public static List<Cliente> clientes = new ArrayList<Cliente>();
    
    /**
     * Construtor de Cliente. Recebe como parâmetro o cpf, nome, tel_contato,
     * data_nascimento e endereco.
     * @param cpf
     * @param nome
     * @param tel_contato
     * @param data_nascimento
     * @param endereco
     */
    public Cliente(String cpf, String nome, String tel_contato, String data_nascimento, String endereco) {
        super(cpf, nome, tel_contato, data_nascimento, endereco);
    }

    // Getters e Setters
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCartao() {
        return cartao;
    }

    public void setCartao(String cartao) {
        this.cartao = cartao;
    }
}
