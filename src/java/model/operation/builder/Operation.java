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
    
    public static final int OPERATION_TYPE_CREATION = 1;
    public static final int OPERATION_TYPE_QUERY = 2;
    public static final int OPERATION_TYPE_DEPOSIT = 3;
    public static final int OPERATION_TYPE_DRAFT = 4;
    public static final int OPERATION_TYPE_TRANSFER = 5;
    public static final int OPERATION_TYPE_DELETE = 6;
    public static final int OPERATION_TYPE_EXTRACT = 7;
    
    public static final String OPERATION_CREATION = "CRIAÇÃO";
    public static final String OPERATION_DELETE_ACCOUNT = "REMOÇÃO";
    public static final String OPERATION_QUERY = "CONSULTA";
    public static final String OPERATION_DEPOSIT = "DEPÓSITO";
    public static final String OPERATION_DRAFT = "SAQUE";
    public static final String OPERATION_TRANSFER = "TRANSFERÊNCIA";
    public static final String OPERATION_EXTRACT = "EXTRATO";
    
    public static final String OPERATION_DESCRIPTION_CREDIT = "CRÉDITO";
    public static final String OPERATION_DESCRIPTION_DEBIT = "DÉBITO";
    
}