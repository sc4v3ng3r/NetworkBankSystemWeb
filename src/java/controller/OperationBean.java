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
import javax.faces.context.FacesContext;
import model.Client;
import model.operation.builder.BankingMovimentation;
import model.operation.builder.BankingMovimentationBuilder;
import model.operation.builder.BankingOperation;
import model.operation.builder.Operation;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.primefaces.context.RequestContext;
import web.bank.system.util.NewHibernateUtil;

/**
 *
 * @author scavenger
 */
@ManagedBean(name = "OperationBean")
@SessionScoped
public class OperationBean implements Serializable {

    private Client m_currentClient;
    private String m_headerTxt = "";
    private String m_value = "";
    private final ClientDAO m_db = new ClientDAO();

    public OperationBean() {
        String accountNumber = (String) FacesContext.getCurrentInstance().
                getExternalContext().getFlash().get("CURRENT_ACCOUNT");

        m_currentClient = getCurrentClient(accountNumber);
        System.out.println("\n########################################################\n");
        System.out.println("OperationBean::CONSTRUCTOR recovery: " + m_currentClient);
        BankingOperation op = (BankingOperation) m_currentClient.getAccount().getOperationList().get(0);
        System.out.println(op);
        m_headerTxt = "Cliente: " + m_currentClient.getName()
                + "    |    Conta: " + m_currentClient.getAccount().getNumber();
    }

    private Client getCurrentClient(String accountNumber) {
        ClientDAO dao = new ClientDAO();
        List<Client> list = dao.getClientListFromDB();
        long number = Long.parseLong(accountNumber);
        Client client = null;

        for (Client c : list) {
            if (c.getAccount().getNumber() == number) {
                System.out.println("GOT IT!");
                client = c;
                break;
            }
        }
        return client;
    }

    public void setClient(Client c) {
        m_currentClient = c;
    }

    public void setValue(String value) {
        m_value = value;
    }

    public String getValue() {
        return m_value;
    }

    public String getHeader() {
        return m_headerTxt;
    }

    public Client getClient() {
        return m_currentClient;
    }

    private void closeDialog(String dialogName) {
        RequestContext context = RequestContext.getCurrentInstance();
        String command = "PF('" + dialogName + "').hide();";
        context.execute(command);
    }
    
    private void openDialog(String dialogName){
        RequestContext context = RequestContext.getCurrentInstance();
        String command = "PF('" + dialogName + "').show();";
        context.execute(command);
    }
    
    public String closeSession() {
        FacesContext.getCurrentInstance().
                getExternalContext().invalidateSession();

        return "index.jsp";
    }

    private void OperationSucess(String msg) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, msg, ""));
    }

    private void OperationFailed(String msg) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Erro!", msg));
    }

    /*SUBSTITUIR PELO DAO?!*/
    private void registerOperation(Operation operation) {
        Session s = NewHibernateUtil.getSessionFactory().openSession();
        s.getTransaction().begin();
        //s.update(m_currentClient);
        s.save((BankingMovimentation) operation);
        s.flush();
        s.getTransaction().commit();
        s.close();

    }
    
    public void execute(int type) {
        Operation op = null;

        switch (type) {
            case Operation.OPERATION_TYPE_DRAFT:
                
                double value = Double.parseDouble(m_value);
                System.out.println("OPERAÇÃO SAQUE!");
                op = new BankingMovimentationBuilder(Operation.OPERATION_TYPE_DRAFT, m_currentClient)
                        .setTypeDescription(Operation.OPERATION_DESCRIPTION_DEBIT)
                        .setName(Operation.OPERATION_DRAFT)
                        .setValue(value)
                        .build();
                        
                if (op.execute()){
                    OperationSucess("Saque realizado com sucesso!");
                    registerOperation(op);
                } else
                    OperationFailed("Saldo indisponível");
                
                closeDialog("DraftDialog");
                setValue("");
                break;

            case Operation.OPERATION_TYPE_DEPOSIT:
                value = Double.parseDouble(m_value);

                op = new BankingMovimentationBuilder(Operation.OPERATION_TYPE_DEPOSIT, m_currentClient)
                        .setName(Operation.OPERATION_DEPOSIT)
                        .setTypeDescription(Operation.OPERATION_DESCRIPTION_CREDIT)
                        .setValue(value)
                        .build();

                if (op.execute()) {
                    registerOperation(op);
                    OperationSucess("Depósito realizado com sucesso!");
                    
                } else 
                    OperationFailed("Depósito não realizado!");
                
                System.out.println("OPERAÇÃO DEPÓSITO");
                closeDialog("DepositDialog");
                setValue("");
                break;
            
            case Operation.OPERATION_TYPE_TRANSFER:
                value = Double.parseDouble(m_value);
                System.out.println("OPERAÇÃO TRANSFERÊNCIA");
                setValue("");
                break;
            default:
                break;
        }
    }
}
