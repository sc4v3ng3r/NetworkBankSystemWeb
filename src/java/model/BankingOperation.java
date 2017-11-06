/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

//import model.accountfactory.Account;
//import banksystem.model.accountfactory.SpecialAccount;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author scavenger
 * Essa classe é utilizada como representação de operações bancárias 
 * que não são MOVIMENTAÇÕES BANCÁRIAS, ou seja, não são operações
 * que envolvem VALORES em suas execuções, tais como CRIAR UMA CONTA,
 * SALDO DE UMA CONTA.
 */
public class BankingOperation implements Operation {
    
    private String m_name;
    private String m_operationTypeDescription = "";
    private int m_operationType;
    
    private long m_date;
    
    protected final ReentrantLock m_locker = new ReentrantLock();
    protected Client m_client;
    protected double m_beforeOperationClientBalance;
    
    public transient static final int OPERATION_TYPE_CREATION = 1;
    public transient static final int OPERATION_TYPE_QUERY = 2;
    public transient static final int OPERATION_TYPE_DEPOSIT = 3;
    public transient static final int OPERATION_TYPE_DRAFT = 4;
    public transient static final int OPERATION_TYPE_TRANSFER = 5;
    public transient static final int OPERATION_TYPE_DELETE = 6;
    public transient static final int OPERATION_TYPE_EXTRACT = 7;
    
    public transient static final String OPERATION_CREATION = "CRIAÇÃO";
    public transient static final String OPERATION_DELETE_ACCOUNT = "REMOÇÃO";
    public transient static final String OPERATION_QUERY = "CONSULTA";
    public transient static final String OPERATION_DEPOSIT = "DEPÓSITO";
    public transient static final String OPERATION_DRAFT = "SAQUE";
    public transient static final String OPERATION_TRANSFER = "TRANSFERÊNCIA";
    public transient static final String OPERATION_EXTRACT = "EXTRATO";
    
    public transient static final String OPERATION_TYPE_DESCRIPTION_CREDIT = "CRÉDITO";
    public transient static final String OPERATION_TYPE_DESCRIPTION_DEBIT = "DÉBITO";
    
    public BankingOperation(int type, Client client) {
        setDate(System.currentTimeMillis());
        setType(type);
        setClient(client);
        
        switch(type){
            case OPERATION_TYPE_CREATION:
                //setTypeDesctiption(OPERATION_CREATION);
                setName(OPERATION_CREATION);
                break;
                
            case OPERATION_TYPE_DEPOSIT:
                setName(OPERATION_DEPOSIT);
                setTypeDesctiption(OPERATION_TYPE_DESCRIPTION_CREDIT);
                break;
                
            case OPERATION_TYPE_DRAFT:
                setName(OPERATION_DRAFT);
                setTypeDesctiption(OPERATION_TYPE_DESCRIPTION_DEBIT);
                break;
                
            case OPERATION_TYPE_QUERY:
                //setTypeDesctiption(OPERATION_QUERY);
                setName(OPERATION_QUERY);
                break;
                
            case OPERATION_TYPE_TRANSFER:
                setName(OPERATION_TRANSFER);
                break;
                
            case OPERATION_TYPE_DELETE:
                setName(OPERATION_DELETE_ACCOUNT);
                break;
                
            default:
                break;
        }
    
    }
    
    protected BankingOperation(Client client){
        setDate(System.currentTimeMillis());
        setClient(client);
    }
    
    public void setName(String name){ m_name = name;}
    public String getName(){ return m_name; }
    
    public void setClient(Client client){ m_client = client;} 
    public Client getClient(){ return m_client; }
    
    @Override
    public String getDetails() {
        m_locker.lock();
        try{
            Account account = this.m_client.getAccount();
            switch(this.getType()){
                case OPERATION_TYPE_QUERY: 
                
                    switch(this.m_client.getAccount().getType()){
                        case Account.ACCOUNT_NORMAL:
                            return
                                "CLIENTE: " + this.m_client.getName() +
                                ";OPERAÇÃO: " + this.getTypeDescription() +
                                ";ID OPERAÇÃO: " + this.getId() +
                                ";CONTA NÚMERO: " + account.getNumber() +
                                ";CONTA TIPO: " + account.getAccountDescription() +
                                    //verificar como isso aqui vai ficar!
                                ";SALDO: " + this.m_client.getAccount().getBalance() + //this.m_beforeOperationClientBalance + //armengue! todo operacao guarda o estado anterior do saldo da conta!
                                ";EXECUTADA EM: " + this.getDateString();
                    
                        case Account.ACCOUNT_SPECIAL:
                            double credit = ((SpecialAccount)account).getCredit();
                            return
                                "CLIENTE: " + this.m_client.getName() +
                                ";OPERAÇÃO: " + this.getTypeDescription() +
                                ";ID OPERAÇÃO: " + this.getId() +
                                ";CONTA NÚMERO: " + account.getNumber() +
                                ";CONTA TIPO: " + account.getAccountDescription() +
                                ";CRÉDITO: " +  credit +
                                    //verificar como isso aqui vai ficar!
                                ";SALDO PARCIAL: " + this.m_beforeOperationClientBalance +
                                ";SALDO TOTAL: " +  (credit + m_beforeOperationClientBalance) +//armengue! todo operacao guarda o estado anterior do saldo da conta!
                                ";EXECUTADA EM: " + this.getDateString();
                    }
                
                default:
                    return 
                    "CLIENTE: " + m_client.getName() +
                    ";OPERAÇÃO: " + this.getTypeDescription() +
                    ";ID OPERAÇÃO: " + this.getId() +
                    ";CONTA NÚMERO: " + account.getNumber() +
                    ";CONTA TIPO: " + account.getAccountDescription() +
                    ";EXECUTADA EM: " + this.getDateString();
            }
        }
        finally {
            m_locker.unlock();
        }
    }
    
    @Override
    public boolean execute() {
        m_locker.lock();
        try {
            m_beforeOperationClientBalance = m_client.getAccount().getBalance();
            m_client.getAccount().addOperation(this);
        } catch(NullPointerException ex){
            System.out.println("BankingOperation::execute() Something Wrong! \n" + ex);
            return false;
        } 
        finally{
            m_locker.unlock();
        }
        return true;
    }

    @Override
    public void debug() {
        System.out.println( getDetails() );
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public final void setTypeDesctiption(String type ){ m_operationTypeDescription = type; }
    public final String getTypeDescription(){ return m_operationTypeDescription; }
    
    private void setDate(long date){ m_date = date; }
    public final long getDate(){ return m_date; }
    public final String getDateString(){ 
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyy HH:mm:ss");
        Date date = new Date(m_date);
        return dateFormatter.format(date); 
    }
    
    public void setType(int type){ m_operationType = type;}
    public int getType(){ return m_operationType;}
    
    public final long getId(){ return this.hashCode(); }
    

    @Override
    public String toString(){
        return "BankingOperation::toString()";
    }
    
    
}