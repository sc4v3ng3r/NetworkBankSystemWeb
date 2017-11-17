/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;
//import banksystem.model.Operation;
import model.operation.builder.Operation;
import model.operation.builder.BankingOperation;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 *
 * @author scavenger
 */
@Entity
@Table(name="account", schema = "MyBank")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Account implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accountNumber")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private long m_number;
    
    //@OneToOne(mappedBy = "m_account")
    //private Client m_client;
    
    private double m_value;
    private byte m_type;
    private String m_typeDescription;
    
    @OneToMany( mappedBy = "m_currentAccount",
            targetEntity= BankingOperation.class,
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    //@JoinColumn(referencedColumnName = "accountNumber")
    private List<Operation> m_operationList;
    
    protected transient final ReentrantLock m_locker = new ReentrantLock();
    
    public transient static final byte ACCOUNT_NORMAL = 0;
    public transient static final byte ACCOUNT_SPECIAL = 1;
    
    public transient static final String ACCOUNT_TYPE_NORMAL = "NORMAL";
    public transient static final String ACCOUNT_TYPE_SPECIAL = "SPECIAL";
    
    public Account(byte type) {
        setType(type);
        m_operationList = new ArrayList<>();
    }
    
    public Account(){
        m_operationList = new ArrayList<>();
    }
    
    public Account(String type) {
        switch(type){
            case ACCOUNT_TYPE_NORMAL:
                setType(ACCOUNT_NORMAL);
                break;
            case ACCOUNT_TYPE_SPECIAL:
                setType(ACCOUNT_SPECIAL);
                break;
        }
        m_operationList = new ArrayList<>();
    }

    public final void setType(byte type){ m_type = type; }
    
    public void setBalance(double value) {
        m_value = value; 
    }
    
    public final void setNumber(long number){ m_number = number; }
    
    public final long getNumber(){ return m_number; }
    
    public final double getBalance() {
        return m_value; 
    }
    
    public final byte getType(){ return m_type; }
    
    public final String getAccountDescription() {
        if (m_type == ACCOUNT_NORMAL)
            return "NORMAL";
        
        else return "ESPECIAL";
    }
    
    public final void setTypeDescription(String desc){
        m_typeDescription = desc;
    }
    
    public final String getTypeDescription(){
        return m_typeDescription;
    }
    
    public final void setOperationList(List<Operation> list){
        m_operationList = list;
    }
    
    public final List<Operation> getOperationList(){
        return m_operationList;
    }
    
    @Override
    public String toString(){
        return "Número: " + m_number + "\n" +
                "Tipo: " + m_typeDescription + "\n" +
                "Saldo: " + m_value + "\n" + 
                "Operations: " + m_operationList;
    }
    
    public void addOperation(Operation op){
        m_operationList.add(op);
    }
    
    public Operation getOperationAt(int index){
        return m_operationList.get(index);
    }
    
    public int OperationsTotal(){
        return m_operationList.size();
    }
    
}
