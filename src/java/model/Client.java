/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

//import banksystem.model.accountfactory.Account;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author scavenger
 */
public class Client {
    
    private String m_name;
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
    
    public int getId(){ return this.hashCode();}
    
    @Override
    public String toString(){
        return "Nome: " + m_name +
                "\nConta " + m_account;
    }


}
