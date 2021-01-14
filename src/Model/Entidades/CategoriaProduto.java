package Model.Entidades;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa as Categorias de Produtos no sistema.
 * @author herik
 */
public class CategoriaProduto {
    private int id;
    private String descricao;
    
    public static List<CategoriaProduto> categorias = new ArrayList<CategoriaProduto>();

    /**
     * Construtor de CategoriaProduto. Recebe como parâmetro o id e descrição.
     * @param id
     * @param descricao
     */
    public CategoriaProduto(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    // Getters e Setters
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}