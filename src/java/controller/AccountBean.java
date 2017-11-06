/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import database.TestClientDataBase;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.view.ViewScoped;
import model.AccountFactory;
import model.Client;

/**
 *
 * @author scavenger
 */

@SessionScoped
@ManagedBean(name = "NewAccountBean")
public class AccountBean {
    
    private String m_accountType = "";
    private String m_clientName = "";
    private String debug = "";
    private TestClientDataBase m_db = new TestClientDataBase();

    public String getAccountType() {
        return this.m_accountType;
    }

    public String getClientName() {
        return this.m_clientName;
    }
    
    public void setAccountType(String accountType) {
        this.m_accountType = accountType;
    }

    public void setClientName(String clientName) {
        this.m_clientName = clientName;
    }
            
    public void yourAction(){
        this.setDebug("Cliente: " + getClientName());
        m_accountType = "";
        m_clientName="";
    }

    public String getDebug() {
        return this.debug;
    }

    public void setDebug(String string) {
        this.debug = string;
    }
    
    public String callNewAccountPage(){
        return "criarConta.jsp";
    }
    
    public String confirmNewAccount(){
        // valida os dados inseridos!
        // adiciona ao banco de dados
        Client client = new Client(m_clientName);
        AccountFactory factory = new AccountFactory();
        
        client.setAccount( factory.createAccount(m_accountType));
        m_db.add(client);
        System.out.println("Criado : " + client);
        return returnIndex();
    }
    public String returnIndex(){
        return "index.jsp";
    }
    
    public String visualizarContas(){
        return "";
    }
    
    
}
