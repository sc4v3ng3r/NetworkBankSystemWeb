/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;
import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.behavior.FacesBehavior;
import javax.faces.context.FacesContext;
import model.Account;
import model.Client;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import web.bank.system.util.NewHibernateUtil;

/**
 * 
 * @author scavenger
 */

@ManagedBean(name = "BankLoginBean")

public class BankLoginBean implements Serializable {
    //private Account m_account;
    private String m_accountNumber = "";

    public BankLoginBean() {
    
    }

    public String getAccountNumber() {
        return m_accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.m_accountNumber = accountNumber;
    }
    
    public String login(){
        
        Session session = NewHibernateUtil.getSessionFactory().openSession();
        session.getTransaction().begin();
        Account ac = (Account) session.get(Account.class, new Long(m_accountNumber));
        
        if(ac == null){
            System.out.println("CONTA INVALIDA!");
            invalidAccountMessage();
            session.close();
            return "";
        }
        session.close();
        //Client client = getCurrentClient();
        
        //System.out.println("ENCONTRAMOS: " + client);
        
        FacesContext.getCurrentInstance().
                getExternalContext().getFlash().put("CURRENT_ACCOUNT", m_accountNumber);
        
        return "operationScreen.xhtml";  
    }
    /* Tem que fazer a consulta utilizando CRITERIA para
      recuperar o Client unico.
    
    */
    /*
        Criteria criteria = session.createCriteria(Client.class);
        Client client = (Client) criteria.add(
                Restrictions.eq("m_account", "%"+number)
        ).uniqueResult();
        */
    
    public void invalidAccountMessage(){
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                        "Erro!", "Conta inválida.") );
    }
}
