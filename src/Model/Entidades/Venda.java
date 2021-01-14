package Model.Entidades;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa uma Venda no sistema.
 * @author herik
 */
public class Venda {
    // Atributos
    private int numero;
    private String data_venda;
    private int codigo_produto;
    private int id_cliente;
    private int id_funcionario;
    private float preco_venda;
    private int quantidade;
    
    // Lista de Vendas
    public static List<Venda> vendas = new ArrayList<Venda>();
    
    /**
     * Construtor de Venda. Recebe como parâmetro o numero, data_venda, codigo_produto,
     * id_cliente, id_funcionario, preco_venda e quantidade.
     * @param numero
     * @param data_venda
     * @param codigo_produto
     * @param id_cliente
     * @param id_funcionario
     * @param preco_venda
     * @param quantidade
     */
    public Venda(String data_venda, int codigo_produto, int id_cliente, int id_funcionario, float preco_venda, int quantidade) {
        this.data_venda = data_venda;
        this.codigo_produto = codigo_produto;
        this.id_cliente = id_cliente;
        this.id_funcionario = id_funcionario;
        this.preco_venda = preco_venda;
        this.quantidade = quantidade;
    }

    public Venda(int numero, String data_venda, int codigo_produto, int id_cliente, int id_funcionario, float preco_venda, int quantidade) {
        this.numero = numero;
        this.data_venda = data_venda;
        this.codigo_produto = codigo_produto;
        this.id_cliente = id_cliente;
        this.id_funcionario = id_funcionario;
        this.preco_venda = preco_venda;
        this.quantidade = quantidade;
    }
    
    // Getters e Setters

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getData_venda() {
        return data_venda;
    }

    public void setData_venda(String data_venda) {
        this.data_venda = data_venda;
    }

    public int getCodigo_produto() {
        return codigo_produto;
    }

    public void setCodigo_produto(int codigo_produto) {
        this.codigo_produto = codigo_produto;
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public int getId_funcionario() {
        return id_funcionario;
    }

    public void setId_funcionario(int id_funcionario) {
        this.id_funcionario = id_funcionario;
    }

    public float getPreco_venda() {
        return preco_venda;
    }

    public void setPreco_venda(float preco_venda) {
        this.preco_venda = preco_venda;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
