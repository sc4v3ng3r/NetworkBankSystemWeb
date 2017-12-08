/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.operation.builder;

//import banksystem.model.accountfactory.Account;
//import banksystem.model.accountfactory.SpecialAccount;
import javax.persistence.CascadeType;
import model.operation.builder.BankingMovimentation;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import model.Account;
import model.Client;
import model.SpecialAccount;

/**
 *
 * @author scavenger
 */
//@Entity
//@Table(name = "transfer", schema = "MyBank")
public class Transfer extends BankingMovimentation {
   // @OneToOne(optional = false, cascade = CascadeType.ALL)
   // @JoinColumn(name="clientId" )
    Client m_to;
    //double m_value;

    //public Transfer(Client from, Client to, double value) {
    //    super(OPERATION_TYPE_DRAFT,
    //            BankingMovimentation.BANKING_MOVIMENTATION_TRANSFER, 
    //            value, from);
    //    setClientTo(to);
    //    setClientFrom(from);
    //    m_value = value;
    //}
    
    /*protected Transfer(BankingTransferBuilder builder){
        setType(builder.);
    }*/
    transient Operation m_toTransferOperation;
    
    private Transfer(){}
    
    protected Transfer(BankingTransferBuilder builder){
        super(builder);
        setClientTo(builder.m_to);
        /*setType(builder.m_operationType);
        setTypeDesctiption(builder.m_operationTypeDescription);
        setName(builder.m_name);
        setClient(builder.m_client);
        setClientTo(builder.m_to);
        setValue(builder.m_value);*/
    }
    
    private Transfer(Client from, Client to, double value) {
        super(OPERATION_TYPE_TRANSFER, from);
        this.setTypeDesctiption(OPERATION_DESCRIPTION_DEBIT);
        this.setValue(value);
        setClientTo(to);
    }
    
    public Transfer(Client owner){
        super(OPERATION_TYPE_TRANSFER, owner);
        this.setTypeDesctiption(OPERATION_DESCRIPTION_DEBIT);
    }

    public void setClientTo(Client to) {
        m_to = to;
    }

    public Client getClientTo() {
        return m_to;
    }

    private void setToTransferOperation(Operation op){ m_toTransferOperation = op;}
    public Operation getToTransferOperation(){ return m_toTransferOperation;}
    
    @Override
    public String getDetails() {

        if (this.getTypeDescription().equals(OPERATION_DESCRIPTION_DEBIT)) {
            return "CLIENTE: " + m_client.getName() 
                    + "OPERAÇÃO: " + this.getName() + " | " + this.getType()
                    + "\nID OPERAÇÃO: " + this.getId()
                    + "\nVALOR: " + m_value
                    + "\nFAVORECIDO: " + m_to.getName()
                    + "\nFAVORECIDO CONTA: " + m_to.getAccount().getNumber()
                    + "\nREALIZADA EM: " + this.getDateString() + "\n\n";
        }
        
        return "CLIENTE: " + m_to.getName()
                + "\nOPERAÇÃO: " + this.getName() + " | " + this.getType()
                + "\nID OPERAÇÃO: " + this.getId()
                + "\nVALOR: " + m_value
                + "\nEMISSOR: " + this.m_client.getName()
                + "\nEMISSOR CONTA: " + this.m_client.getAccount().getNumber()
                + "\nREALIZADA EM: " + this.getDateString() + "\n\n";
    }

    @Override
    public boolean execute() {
        double fromClientBalance, toClientBalance;
        m_locker.lock();

        try {
            fromClientBalance = m_client.getAccount().getBalance();
            toClientBalance = m_to.getAccount().getBalance();

        } finally {
            m_locker.unlock();
        }

        switch (m_client.getAccount().getType()) {
            case Account.ACCOUNT_NORMAL:
                //System.out.println("CONTA NORMAL -> FROM!");
                m_locker.lock();
                try {
                    if (fromClientBalance >= m_value) {
                        m_beforeOperationClientBalance = fromClientBalance;
                        
                        Transfer toTransfer = new Transfer(m_to, m_client, m_value);
                        toTransfer.setTypeDesctiption(OPERATION_DESCRIPTION_CREDIT);
                        toTransfer.setName(OPERATION_TRANSFER);
                        toTransfer.setType(OPERATION_TYPE_TRANSFER);
                        
                        m_client.getAccount().setBalance((fromClientBalance - m_value));
                        m_client.getAccount().addOperation(this);

                        System.out.println(m_client + " -> " + m_value + " " + m_to);
                        m_to.getAccount().setBalance(toClientBalance + m_value);
                        // System.out.println(this.m_to.getOwner().getName() + " REgistrando!");
                        
                        m_to.getAccount().addOperation(toTransfer);
                        setToTransferOperation(toTransfer);
                        setDate(System.currentTimeMillis());
                        return true;
                    }

                    break;
                } finally {
                    m_locker.unlock();
                }

            case Account.ACCOUNT_SPECIAL:
                //Tenta fazer a transferencia SEM CREDITO!
                m_locker.lock();
                try {
                    if (fromClientBalance >= m_value) {
                        m_beforeOperationClientBalance = fromClientBalance;
                        Transfer toTransfer = new Transfer(m_to, m_client, m_value);
                        toTransfer.setTypeDesctiption(OPERATION_DESCRIPTION_CREDIT);
                        //toTransfer.setType(OPERATION_TYPE_DEPOSIT);
                        m_client.getAccount().setBalance((fromClientBalance - m_value));
                        //System.out.println(this.m_from.getOwner().getName() + " REgistrando!");
                        m_client.getAccount().addOperation(this);

                        m_to.getAccount().setBalance(toClientBalance + m_value);
                        m_to.getAccount().addOperation(toTransfer);
                        setToTransferOperation(toTransfer);
                        //m_to.registerLog(toTransfer);
                        setDate(System.currentTimeMillis());
                        return true;
                    } //tenta fazer a trasnferencia com o credito!
                    else if (m_value <= ((SpecialAccount) m_client.getAccount()).getBalanceTotal()) {
                        double balanceTotal = ((SpecialAccount) m_client.getAccount()).getBalanceTotal();
                        m_beforeOperationClientBalance = m_client.getAccount().getBalance();
                        Transfer toTransfer = new Transfer(m_to, m_client, m_value);
                        toTransfer.setTypeDesctiption(OPERATION_DESCRIPTION_CREDIT);

                        m_client.getAccount().setBalance(fromClientBalance - m_value);
                        m_client.getAccount().addOperation(this);

                        m_to.getAccount().setBalance(toClientBalance + m_value);
                        m_to.getAccount().addOperation(toTransfer);
                        setToTransferOperation(toTransfer);
                        setDate(System.currentTimeMillis());
                        return true;
                    }
                } finally {
                    m_locker.unlock();
                }
        }

        return false;
    }
    
    @Override
    public String toString(){
        return getDetails();
    }
}
