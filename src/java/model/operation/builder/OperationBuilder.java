/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.operation.builder;

/**
 *
 * @author scavenger
 */
public interface OperationBuilder {
    
    /*Tais atributos sao modoficados em tempo de execucao!*/
    //protected Account m_currentAccount;
    //protected long m_date;
    //protected double m_beforeOperationClientBalance;
    
    public Operation build();
    
}
