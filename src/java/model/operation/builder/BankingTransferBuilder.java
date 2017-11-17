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
public class BankingTransferBuilder extends BankingMovimentationBuilder {
    protected Client m_to;

    public BankingTransferBuilder(int type, Client owner) {
        super(type, owner);
    }
    
    @Override
    public Operation build() {
        return new Transfer(this);
    }
    
    @Override
    public BankingTransferBuilder setValue(double value){ 
        m_value = value;
        return this;
    }
    
    @Override
    public BankingTransferBuilder setName(String name) {
        super.m_name = name;
        return this;
    }


    @Override
    public BankingTransferBuilder setType(int type) {
        super.m_operationType = type;
        return this;
    }

    @Override
    public BankingTransferBuilder setTypeDescription(String description) {
        super.m_operationTypeDescription = description;
        return this;
    }

    @Override
    public BankingTransferBuilder setOwner(Client owner) {
        super.m_client = owner;
        return this;
    }
    
    public BankingTransferBuilder setTo(Client to){
        m_to = to;
        return this;
    }
    
}
