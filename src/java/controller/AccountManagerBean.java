/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import model.Account;
import model.AccountManager;
import model.operation.builder.BankingOperationBuilder;
import model.Client;
import model.operation.builder.Operation;


/**
 *
 * @author scavenger
 */

@ManagedBean(name = "AccountManagerBean")
public class AccountManagerBean implements Serializable {

    private Client m_client = null;
    private String m_accountType = "";
    private String m_clientName = "";
    private String debug = "";
    private String m_credit = "";
    private final Repository m_repository = Repository.getInstance();
    
    // fazedor de contas bancarias!
    private final AccountManager m_manager = AccountManager.getInstance();
    //AccountFactory m_factory = new AccountFactory();
    
    //private ClientDAO m_dao = new ClientDAO();

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

    public String getDebug() {
        return this.debug;
    }

    public void setDebug(String string) {
        this.debug = string;
    }

    public String callNewAccountPage() {
        return "criarConta.jsp";
    }

    public String confirmNewAccount() {
        // validar os dados inseridos!
        // adiciona ao banco de dados

        m_client = new Client(m_clientName);
        Account acc = null;
        
        switch(m_accountType){
            case Account.ACCOUNT_TYPE_NORMAL:
                acc = m_manager.getNormalAccount();
                break;
            
            case Account.ACCOUNT_TYPE_SPECIAL:
                acc = m_manager.getEspecialAccount(2000);
                break;
        }
        
        m_client.setAccount(acc);

        Operation op = new BankingOperationBuilder()
                .setType(Operation.OPERATION_TYPE_CREATION)
                .setTypeDescription(Operation.OPERATION_CREATION)
                .setName(Operation.OPERATION_CREATION)
                .setOwner(m_client)
                .build();

        op.execute();
        
        System.out.println("Criado : " + m_client);
        
        m_repository.add(m_client);
        
        // usar repository
        //m_dao.add(m_client);

        System.out.println("CRIAMOS OPERATION\n" + op.getDetails());
        m_client = null;
        return returnIndex();
    }

    public String returnIndex() {
        return "index.jsp";
    }

    public String visualizarContas() {
        return "";
    }

    public Client getClient() {
        return this.m_client;
    }

    public void setClient(Client client) {
        this.m_client = client;
    }

    public void setCredit(String c){
        m_credit = c;
    }
    
    public String getCredit(){
        return m_credit;
    }
    
}
