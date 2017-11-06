/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author scavenger
 */

public interface Operation {
    /**
     * O método execute() executa uma operação. 
     * @return true se a operação foi executada com sucesso. 
     */
    public boolean execute(); 
    
    /**
     * Com método getDetails() obteremos todos os detalhes de uma
     * respectiva operação.
     * @return String com os detalhes da operação.
     */
    public String getDetails();
    
    /**
     * O método debug() imprime na saída padrão
     * os detalhes de uma determinada operação.
     * 
     */
    public void debug();
}