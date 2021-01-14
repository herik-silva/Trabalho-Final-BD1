package Model.Entidades;

/**
 * Classe Abstrata que representa uma Pessoa.
 */
public abstract class Pessoa {
    // Atributos
    private String cpf;
    private String nome;
    private String tel_contato;
    private String data_nascimento;
    private String endereco;

    /**
     * Construtor de Pessoa. Recebe como parâmetro o cpf, nome, tel_contato,
     * data_nascimento e endereco.
     * @param cpf
     * @param nome
     * @param tel_contato
     * @param data_nascimento
     * @param endereco
     */
    public Pessoa(String cpf, String nome, String tel_contato, String data_nascimento, String endereco) {
        this.cpf = cpf;
        this.nome = nome;
        this.tel_contato = tel_contato;
        this.data_nascimento = data_nascimento;
        this.endereco = endereco;
    }

    // Getters e Setters
    
    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTel_contato() {
        return tel_contato;
    }

    public void setTel_contato(String tel_contato) {
        this.tel_contato = tel_contato;
    }

    public String getData_nascimento() {
        return data_nascimento;
    }

    public void setData_nascimento(String data_nascimento) {
        this.data_nascimento = data_nascimento;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
}
