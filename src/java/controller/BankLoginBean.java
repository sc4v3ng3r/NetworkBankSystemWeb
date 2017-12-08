/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import model.Account;

/**
 * 
 * @author scavenger
 */

@ManagedBean(name = "BankLoginBean")

public class BankLoginBean implements Serializable {
    //private Account m_account;
    private String m_accountNumber = "";
    private final Repository m_repository = Repository.getInstance();

    public BankLoginBean() {
    
    }

    public String getAccountNumber() {
        return m_accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.m_accountNumber = accountNumber;
    }
    
    public String login(){
        
        try{
            long id = Long.parseLong(m_accountNumber);
            Account ac = m_repository.getAccountById(id);
            
            if (ac == null){
                System.out.println("CONTA INVALIDA!");
                invalidAccountMessage();
               return "";
            }
            FacesContext.getCurrentInstance().
                getExternalContext().getFlash().put("CURRENT_ACCOUNT", m_accountNumber);
            
            return "operationScreen.xhtml";  
            
            
        } catch(NumberFormatException ex){
            invalidAccountMessage();
            return "";
        }
        

    }
    
    public void invalidAccountMessage(){
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                        "Erro!", "Conta inválida.") );
    }
}
