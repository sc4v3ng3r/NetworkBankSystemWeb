/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

//import banksystem.model.accountfactory.Account;
//import banksystem.model.accountfactory.SpecialAccount;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author scavenger
 */
public class Transfer extends BankingMovimentation {
    Client m_to;
    //double m_value;
    
    /*public Transfer(Client from, Client to, double value) {
        super(OPERATION_TYPE_DRAFT,
                BankingMovimentation.BANKING_MOVIMENTATION_TRANSFER, 
                value, from);
        setClientTo(to);
        setClientFrom(from);
        m_value = value;
    }*/
    
    public Transfer(Client from, Client to, double value) {
        super(OPERATION_TYPE_TRANSFER, value, from);
        this.setTypeDesctiption(OPERATION_TYPE_DESCRIPTION_DEBIT);
        setClientTo(to);
    }
    
   
    public void setClientTo(Client to) { m_to = to; }
    public Client getClientTo(){ return m_to; }
    
    @Override
    public String getDetails(){
       
        if (this.getTypeDescription().equals(OPERATION_TYPE_DESCRIPTION_DEBIT) ){
            return
                "CLIENTE: " + m_client.getName() +
                ";OPERAÇÃO: " + this.getName() + " | " + this.getType() +
                ";ID OPERAÇÃO: " + this.getId() +
                ";VALOR: " + m_value +
                ";FAVORECIDO: " + m_to.getName() + 
                ";FAVORECIDO CONTA: " + m_to.getAccount().getNumber() +
                ";REALIZADA EM: " + this.getDateString();
        }
        return
            "CLIENTE: " + m_to.getName() +
            ";OPERAÇÃO: " + this.getName() + " | " + this.getType() +
            ";ID OPERAÇÃO: " + this.getId() +
            ";VALOR: " + m_value +
            ";EMISSOR: " + this.m_client.getName() +
            ";EMISSOR CONTA: " + this.m_client.getAccount().getNumber() +
            ";REALIZADA EM: " + this.getDateString();
    }
    
    @Override
    public boolean execute(){
        double fromClientBalance, toClientBalance;
        m_locker.lock();
        
        try{    
            fromClientBalance = m_client.getAccount().getBalance();
            toClientBalance = m_to.getAccount().getBalance();
        } finally{
            m_locker.unlock();
        }
        
        switch(m_client.getAccount().getType()){
            case Account.ACCOUNT_NORMAL:
                //System.out.println("CONTA NORMAL -> FROM!");
                m_locker.lock();
                try{
                    if ( fromClientBalance >= m_value) {
                        Transfer toTransfer = new Transfer(m_to, m_client, m_value);
                        toTransfer.setTypeDesctiption(OPERATION_TYPE_DESCRIPTION_CREDIT);
                        
                        m_client.getAccount().setBalance( (fromClientBalance - m_value) );
                        m_client.getAccount().addOperation(this);
                        
                        System.out.println(m_client + " -> " + m_value +  " " + m_to);
                        m_to.getAccount().setBalance(toClientBalance + m_value);
                        // System.out.println(this.m_to.getOwner().getName() + " REgistrando!");
                        m_to.getAccount().addOperation(toTransfer);
                        return true;
                    }
                    
                break;
                }
                finally{
                    m_locker.unlock();
                }
            
            case Account.ACCOUNT_SPECIAL:
                /*Tenta fazer a transferencia SEM CREDITO!*/
                m_locker.lock();
                try{
                    if (fromClientBalance >= m_value){
                        Transfer toTransfer = new Transfer(m_to, m_client, m_value);
                        toTransfer.setTypeDesctiption(OPERATION_TYPE_DESCRIPTION_CREDIT);
                        //toTransfer.setType(OPERATION_TYPE_DEPOSIT);
                        m_client.getAccount().setBalance((fromClientBalance - m_value));
                        //System.out.println(this.m_from.getOwner().getName() + " REgistrando!");
                        m_client.getAccount().addOperation(this);

                        m_to.getAccount().setBalance(toClientBalance + m_value);
                        m_to.getAccount().addOperation(toTransfer);
                        //m_to.registerLog(toTransfer);
                        return true;    
                    }
                    //tenta fazer a trasnferencia com o cretido!
                    else if (m_value <= ((SpecialAccount)m_client.getAccount()).getBalanceTotal()){
                        double balanceTotal = ((SpecialAccount)m_client.getAccount()).getBalanceTotal();
                        Transfer toTransfer = new Transfer(m_to, m_client, m_value);
                        toTransfer.setTypeDesctiption(OPERATION_TYPE_DESCRIPTION_CREDIT);
                        //toTransfer.setType(OPERATION_TYPE_DEPOSIT);
                        m_client.getAccount().setBalance(fromClientBalance - m_value);
                        // System.out.println(this.m_from.getOwner().getName() + " REgistrando!");
                        m_client.getAccount().addOperation(this);
                        m_to.getAccount().setBalance(toClientBalance + m_value);
                        //System.out.println(this.m_to.getOwner().getName() + " REgistrando!");
                        m_to.getAccount().addOperation(toTransfer);
                        return true;
                    }
                }
                finally{
                    m_locker.unlock();
                }
        }
        return false;
    }
}