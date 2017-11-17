/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.operation.builder;

//import model.accountfactory.Account;
//import banksystem.model.accountfactory.SpecialAccount;
import model.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 *
 * @author scavenger
 * Essa classe é utilizada como representação de operações bancárias 
 * que não são MOVIMENTAÇÕES BANCÁRIAS, ou seja, não são operações
 * que envolvem VALORES em suas execuções, tais como CRIAR UMA CONTA,
 * SALDO DE UMA CONTA.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name="bankingOperation", 
       schema = "MyBank"/*, 
       uniqueConstraints = 
               { @UniqueConstraint(columnNames ="accountNumber", name = "FK_ACCOUNT"), 
                 @UniqueConstraint(columnNames = "clientId", name = "FK_CLIENT")
               }*/)

public class BankingOperation implements Operation, Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @OnDelete(action = OnDeleteAction.CASCADE)
   // @Column(name = "operationId")
    private long m_operationId;
    
    private String m_name;
    private String m_operationTypeDescription = "";
    private int m_operationType;
    
    /*foreignKey = @ForeignKey(name="FK_COMPANY__ROUTE")*/
    @ManyToOne(cascade = CascadeType.ALL, /*targetEntity = Account.class,*/
            fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "accountNumber", 
            foreignKey = @ForeignKey(name = "FK_ACCOUNT_NUMBER")/*, nullable = false, unique = true*/ )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Account m_currentAccount;
    
    private long m_date;
    protected transient final ReentrantLock m_locker = new ReentrantLock();
    
    @ManyToOne(cascade = CascadeType.ALL /*, targetEntity = Client.class*/)
    @JoinColumn(name = "clientId",
            foreignKey = @ForeignKey(name = "FK_CLIENT_ID")/*,nullable = false, unique = false*/)
    @OnDelete(action = OnDeleteAction.CASCADE)
    protected Client m_client;
    protected double m_beforeOperationClientBalance;
    
    protected BankingOperation(){}
    
    protected BankingOperation(int type, Client client) {
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
                setTypeDesctiption(OPERATION_DESCRIPTION_CREDIT);
                break;
                
            case OPERATION_TYPE_DRAFT:
                setName(OPERATION_DRAFT);
                setTypeDesctiption(OPERATION_DESCRIPTION_DEBIT);
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
    
    protected BankingOperation(BankingOperationBuilder builder){
        this.setType(builder.m_operationType);
        this.setTypeDesctiption(builder.m_operationTypeDescription);
        this.setClient(builder.m_client);
        this.setAccount( m_client.getAccount());
        this.setName(builder.m_name);
    }
    
    public void setId(long id){ m_operationId = id;}
    public long getId(){ return m_operationId; }
    
    public void setName(String name){ m_name = name;}
    public String getName(){ return m_name; }
    
    public void setClient(Client client){ m_client = client;} 
    public Client getClient(){ return m_client; }
    
    public void setAccount(Account account){ m_currentAccount = account;}
    public Account getAccount(){ return m_currentAccount;}
    
    public void setBeforeOperationValue(double value){
        m_beforeOperationClientBalance = value;
    }
    
    public double getBeforeOperationValue(){ return m_beforeOperationClientBalance; }
    
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
                                "\nOPERAÇÃO: " + this.getTypeDescription() +
                                "\nID OPERAÇÃO: " + this.getId() +
                                "\nCONTA NÚMERO: " + account.getNumber() +
                                "\nCONTA TIPO: " + account.getAccountDescription() +
                                    //verificar como isso aqui vai ficar!
                                "\nSALDO: " + this.m_client.getAccount().getBalance() + //this.m_beforeOperationClientBalance + //armengue! todo operacao guarda o estado anterior do saldo da conta!
                                "\nEXECUTADA EM: " + this.getDateString();
                    
                        case Account.ACCOUNT_SPECIAL:
                            double credit = ((SpecialAccount)account).getCredit();
                            return
                                "CLIENTE: " + this.m_client.getName() +
                                "\nOPERAÇÃO: " + this.getTypeDescription() +
                                "\nID OPERAÇÃO: " + this.getId() +
                                "\nCONTA NÚMERO: " + account.getNumber() +
                                "\nCONTA TIPO: " + account.getAccountDescription() +
                                "\nCRÉDITO: " +  credit +
                                    //verificar como isso aqui vai ficar!
                                "\nSALDO PARCIAL: " + this.m_beforeOperationClientBalance +
                                "\nSALDO TOTAL: " +  (credit + m_beforeOperationClientBalance) +//armengue! todo operacao guarda o estado anterior do saldo da conta!
                                "\nEXECUTADA EM: " + this.getDateString() + "\n\n";
                    }
                
                default:
                    return 
                    "CLIENTE: " + m_client.getName() +
                    "\nOPERAÇÃO: " + this.getTypeDescription() +
                    "\nID OPERAÇÃO: " + this.getId() +
                    "\nCONTA NÚMERO: " + account.getNumber() +
                    "\nCONTA TIPO: " + account.getAccountDescription() +
                    "\nEXECUTADA EM: " + this.getDateString() + "\n\n";
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
            setDate(System.currentTimeMillis());
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
    
    public void setDate(long date){ m_date = date; }
    public final long getDate(){ return m_date; }
    
    public final String getDateString(){ 
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyy HH:mm:ss");
        Date date = new Date(m_date);
        return dateFormatter.format(date); 
    }
    
    public void setType(int type){ m_operationType = type;}
    public int getType(){ return m_operationType;}
    
    @Override
    public String toString(){
        /*Account account = m_currentAccount;
        return "BankingOperation::toString DEBUG\n" 
                + " m_currentAccount = " + m_currentAccount + "\n"
                + " this.m_client.getAccount() " + this.m_client.getAccount();*/
        return this.getDetails();
    }
    
}
