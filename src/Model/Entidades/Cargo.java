package Model.Entidades;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa os Cargos de Funcionários no sistema.
 * @author herik
 */
public class Cargo{
    private int id;
    private String descricao;
    
    public static List<Cargo> cargos = new ArrayList<Cargo>();

    /**
     * Construtor de Cargo. Recebe como parâmetro o id e descrição.
     * @param id
     * @param descricao
     */
    public Cargo(int id, String descricao) {
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
