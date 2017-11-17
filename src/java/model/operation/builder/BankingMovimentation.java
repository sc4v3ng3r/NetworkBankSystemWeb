/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.operation.builder;

//import banksystem.model.accountfactory.Account;
//import banksystem.model.accountfactory.SpecialAccount;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Table;
import model.Account;
import model.operation.builder.BankingMovimentation;
import model.Client;
import model.SpecialAccount;

/**
 *
 * @author scavenger Classe utilizada para representar Operações bancárias que
 * movimentam valores entre contas, DEPÓSITO, SAQUE e TRANSFERÊNCIAS
 * (MOVIMENTAÇÕES BANCÁRIAS).
 */
@Entity
@Table(name = "bakingMovimentation", schema = "MyBank")
public class BankingMovimentation extends BankingOperation {

    protected double m_value;
    
    protected BankingMovimentation(BankingMovimentationBuilder builder){
        super(builder);
        setValue(builder.m_value);
        /*setType(builder.m_operationType);
        setTypeDesctiption(builder.m_operationTypeDescription);
        setName(builder.m_name);
        setValue(builder.m_value);
        setClient(builder.m_client);
        setAccount(m_client.getAccount());*/
    }
    
    protected BankingMovimentation(){}
    
    public BankingMovimentation(int Operationtype,
            Client ownerClient) {
        super(Operationtype, ownerClient);   
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
                + "\nOPERAÇÃO: " + this.getName() 
                + "\nOPERAÇÃO TIPO: " + this.getTypeDescription()
                + "\nID OPERAÇÃO: " + this.getId() 
                + "\nCONTA NÚMERO: " + this.m_client.getAccount().getNumber()
                + "\nCONTA TIPO: " + this.m_client.getAccount().getAccountDescription()
                + "\nVALOR: " + this.getValue()
                + "\nREALIZADA EM: " + this.getDateString() + "\n\n";
    }
    
    @Override
    public boolean execute() {
        Account currentAccount = m_client.getAccount();
        //m_locker.lock();
        if (m_value <= 0){
            return false;
        }
        
        switch (this.getType()) {

            case OPERATION_TYPE_DEPOSIT:
                
                m_locker.lock();
                    try {
                        this.setBeforeOperationValue( currentAccount.getBalance() );
                        currentAccount.setBalance(
                            (currentAccount.getBalance() + m_value));

                        currentAccount.addOperation(this);
                        setDate(System.currentTimeMillis());
                        return true;
                    } finally{
                        m_locker.unlock();
                    }

               
            case OPERATION_TYPE_DRAFT:
                switch ( currentAccount.getType() ) {

                    case Account.ACCOUNT_NORMAL:
                        m_locker.lock();
                        try {
                            double balance = currentAccount.getBalance();
                            if (currentAccount.getBalance() >= m_value) {
                                this.setBeforeOperationValue(balance);
                                currentAccount.setBalance( (currentAccount.getBalance() - m_value) );
                                currentAccount.addOperation(this);
                                setDate(System.currentTimeMillis());
                                return true;
                            }
                            
                        } finally {
                            m_locker.unlock();
                        }
                        break;
                            
                    case Account.ACCOUNT_SPECIAL:
                        try {
                            double balance = currentAccount.getBalance();
                            double balanceTotal = ((SpecialAccount) currentAccount).getBalanceTotal();
                            if (m_value <= balance) {
                                setBeforeOperationValue( balanceTotal );
                                currentAccount.setBalance(balance - m_value);
                                currentAccount.addOperation(this);
                                setDate(System.currentTimeMillis());
                                return true;
                            } else if (m_value <= balanceTotal
                                    /*((SpecialAccount) currentAccount).getBalanceTotal()*/) {
                                //double balanceTotal = ((SpecialAccount) currentAccount).getBalanceTotal();
                                setBeforeOperationValue(balanceTotal);
                                currentAccount.setBalance(balance - m_value);
                                currentAccount.addOperation(this);
                                setDate(System.currentTimeMillis());
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
        return getDetails();
    }
}
