/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

//import banksystem.model.accountfactory.Account;
//import banksystem.model.accountfactory.SpecialAccount;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author scavenger Classe utilizada para representar Operações bancárias que
 * movimentam valores entre contas, DEPÓSITO, SAQUE e TRANSFERÊNCIAS
 * (MOVIMENTAÇÕES BANCÁRIAS).
 */
public class BankingMovimentation extends BankingOperation {

    protected double m_value;

    public BankingMovimentation(int Operationtype, double value,
            Client ownerClient) {
        super(Operationtype, ownerClient);
        this.setValue(value);
        
    }

    public void setValue(double value) {
        this.m_value = value;
    }
    
    public double getValue() {
        return this.m_value;
    }

    @Override
    public String getDetails() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyy HH:mm:ss");
        Date date = new Date(this.getDate());

        return "CLIENTE: " + this.m_client.getName()
                + ";OPERAÇÃO: " + this.getTypeDescription()
                + ";ID OPERAÇÃO: " + this.getId() 
                + ";CONTA NÚMERO: " + this.m_client.getAccount().getNumber()
                + ";CONTA TIPO: " + this.m_client.getAccount().getAccountDescription()
                + ";VALOR: " + this.getValue()
                + ";REALIZADA EM: " + this.getDateString();
    }

    @Override
    public boolean execute() {
        Account currentAccount = m_client.getAccount();
        m_locker.lock();

        switch (this.getType()) {

            case OPERATION_TYPE_DEPOSIT:
                if (m_value <= 0) {
                    return false;
                }
                m_locker.lock();
                    try {
                        currentAccount.setBalance(
                            (currentAccount.getBalance() + m_value));

                        currentAccount.addOperation(this);
                        return true;
                    } finally{
                        m_locker.unlock();
                    }
                   
               
            case OPERATION_TYPE_DRAFT:
                switch (currentAccount.getType()) {

                    case Account.ACCOUNT_NORMAL:
                        m_locker.lock();
                        try {
                            if (currentAccount.getBalance() >= m_value) {
                                currentAccount.setBalance((currentAccount.getBalance() - m_value));
                                currentAccount.addOperation(this);
                                return true;
                            }
                            
                        } finally {
                            m_locker.unlock();
                        }
                        break;
                            
                    case Account.ACCOUNT_SPECIAL:
                        try {
                            double balance = currentAccount.getBalance();
                            if (m_value <= balance) {

                                currentAccount.setBalance(balance - m_value);
                                currentAccount.addOperation(this);
                                return true;
                            } else if (m_value <= ((SpecialAccount) currentAccount).getBalanceTotal()) {
                                double balanceTotal = ((SpecialAccount) currentAccount).getBalanceTotal();
                                currentAccount.setBalance(balance - m_value);
                                currentAccount.addOperation(this);
                                return true;
                              }
                            
                        } finally{
                            m_locker.lock();
                        }
                        break;
                }

            default:
                break;
        }
        return false;
    }
    
    @Override
    public void debug(){
        System.out.println(getDetails());
    }
    
    @Override 
    public String toString(){
        
        return "CLIENTE: " + this.m_client.getName()
                + ";OPERAÇÃO: " + this.getTypeDescription()
                + ";ID OPERAÇÃO: " + this.getId()
                + ";CONTA NÚMERO: " + this.m_client.getAccount().getNumber()
                + ";CONTA TIPO: " + this.m_client.getAccount().getAccountDescription()
                + ";VALOR: " + this.getValue()
                + ";REALIZADA EM: " + this.getDateString();   
    }
}
