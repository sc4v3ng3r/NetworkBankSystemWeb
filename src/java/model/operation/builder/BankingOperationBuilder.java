/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.operation.builder;

import model.Client;


/**
 *
 * @author scavenger
 */
public class BankingOperationBuilder implements OperationBuilder{
    protected String m_name;
    protected String m_operationTypeDescription = "";
    protected int m_operationType;
    protected Client m_client;
    
    public BankingOperationBuilder(){}
    public BankingOperationBuilder(int type, Client owner){
        setType(type);
        setOwner(owner);
        
        switch(type){
           case Operation.OPERATION_TYPE_CREATION:
               setTypeDescription(Operation.OPERATION_CREATION);
               setName(Operation.OPERATION_CREATION);
            break;
            
           case Operation.OPERATION_TYPE_DEPOSIT:
               setTypeDescription(Operation.OPERATION_DESCRIPTION_CREDIT);
               setName(Operation.OPERATION_DEPOSIT);
               
           case Operation.OPERATION_TYPE_DRAFT:
               setTypeDescription(Operation.OPERATION_DESCRIPTION_DEBIT);
               setName(Operation.OPERATION_DRAFT);
           
           case Operation.OPERATION_TYPE_QUERY:
               setTypeDescription(Operation.OPERATION_QUERY);
               setName(Operation.OPERATION_QUERY);
           
           case Operation.OPERATION_TYPE_EXTRACT:
               setTypeDescription(Operation.OPERATION_EXTRACT);
               setName(Operation.OPERATION_EXTRACT);
           
           case Operation.OPERATION_TYPE_TRANSFER:
               setName(Operation.OPERATION_TRANSFER);
            break;
        }
    }
    
    @Override
    public Operation build() {
        return new BankingOperation(this);
    }
    
    public BankingOperationBuilder setName(String name) {
        this.m_name = name;
        return this;
    }


    public BankingOperationBuilder setType(int type) {
        this.m_operationType = type;
        return this;
    }

    public BankingOperationBuilder setTypeDescription(String description) {
        this.m_operationTypeDescription = description;
        return this;
    }

    public BankingOperationBuilder setOwner(Client owner) {
        this.m_client = owner;
        return this;
    }
    
}
