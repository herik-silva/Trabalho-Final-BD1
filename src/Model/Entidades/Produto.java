package Model.Entidades;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa um Produto no sistema.
 * @author herik
 */
public class Produto {
    // Atributos
    private int codigo;
    private String nome;
    private float preco;
    private int estoque;
    private int id_categoria;
    
    // Lista de Produtos
    public static List<Produto> produtos = new ArrayList<Produto>();

    /**
     * Construtor de Produto. Recebe como parâmetro o codigo, nome, preço, estoque
     * e id_categoria.
     * @param codigo
     * @param nome
     * @param preco
     * @param estoque
     * @param id_categoria
     */
    public Produto(int codigo, String nome, float preco, int estoque, int id_categoria) {
        this.codigo = codigo;
        this.nome = nome;
        this.preco = preco;
        this.estoque = estoque;
        this.id_categoria = id_categoria;
    }

    /**
     * Construtor de Produto. Recebe como parâmetro o código, nome e id_categoria.
     * @param codigo
     * @param nome
     * @param id_categoria
     */
    public Produto(int codigo, String nome, int id_categoria) {
        this.codigo = codigo;
        this.nome = nome;
        this.id_categoria = id_categoria;
    }
    
    /**
     * Construtor padrão de Produto.
     */
    public Produto(){}

    // Getters e Setters
    
    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public float getPreco() {
        return preco;
    }

    public void setPreco(float preco) {
        this.preco = preco;
    }

    public int getEstoque() {
        return estoque;
    }

    public void setEstoque(int estoque) {
        this.estoque = estoque;
    }

    public int getId_categoria() {
        return id_categoria;
    }

    public void setId_categoria(int id_categoria) {
        this.id_categoria = id_categoria;
    }
}