/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 *
 * @author scavenger
 */
@Entity
@Table(name="client", schema="MyBank"/*,
        uniqueConstraints = @UniqueConstraint(
                columnNames = "accountNumber", name = "FK_CLIENT_ACCOUNT")*/  )
/*@NamedQueries(
        @NamedQuery(name="Client.getAll",query="SELECT c FROM client"  )
)*/
public class Client implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private long m_id;
    private String m_name;
    
    @OneToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name="accountNumber")
    private Account m_account;
    
    public Client(){}
    
    public Client(String name){
        this.setName(name);
    }
    
    public Client(String name, Account account){
        this.setName(name);
        this.setAccount(account);
    }
    
    public void setAccount(Account account){ 
        this.m_account = account;
    }
    
    public void setName(String name){ this.m_name = name; }
    
    public String getName(){ return m_name; }
    
    public Account getAccount(){ return m_account; }
    
    public long getId(){ return this.m_id;}
    
    public void setId(long id){ this.m_id = id;}
    
    @Override
    public String toString(){
        return "Nome: " + m_name +
                "\nID: " + m_id +
                "\nConta " + m_account;
    }
    
}
