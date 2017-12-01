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
public class BankingMovimentationBuilder extends BankingOperationBuilder {
    protected double m_value;
    
    public BankingMovimentationBuilder(int type, Client owner){
        super(type, owner);
    }
    
    @Override
    public Operation build() {
       return new BankingMovimentation(this);
    }
    
    public BankingMovimentationBuilder setValue(double value){ 
        m_value = value;
        return this;
    }
    
    @Override
    public BankingMovimentationBuilder setName(String name) {
        super.setName(name);
        return this;
    }

    @Override
    public BankingMovimentationBuilder setType(int type) {
        super.m_operationType = type;
        return this;
    }

    @Override
    public BankingMovimentationBuilder setTypeDescription(String description) {
        super.m_operationTypeDescription = description;
        return this;
    }

    @Override
    public BankingMovimentationBuilder setOwner(Client owner) {
        super.m_client = owner;
        return this;
    }
}
