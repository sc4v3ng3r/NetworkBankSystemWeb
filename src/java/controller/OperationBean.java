/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import model.Client;
import model.operation.builder.BankingMovimentation;
import model.operation.builder.BankingMovimentationBuilder;
import model.operation.builder.BankingOperation;
import model.operation.builder.BankingOperationBuilder;
import model.operation.builder.BankingTransferBuilder;
import model.operation.builder.Operation;
import model.operation.builder.Transfer;
import org.hibernate.Session;
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
    private String m_queryOutput= "";
    private final ClientDAO m_db = new ClientDAO();
    
    private List<Client> m_currentList;
    private Long m_selectedAccountToTransfer;

    private final Map<String, Long> m_hashMap = new HashMap<>();
    
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
    
    public Long getSelectedAccountToTransfer(){
        return m_selectedAccountToTransfer;
    }
    
    public void setSelectedAccountToTransfer(Long account){ 
        m_selectedAccountToTransfer = account;
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
        s.save(operation);
        s.flush();
        s.getTransaction().commit();
        s.close();
    }
    
    // verificar se tudo isso Eh mesmo necessario!
    private void fillData() {
        m_currentList = m_db.getClientListFromDB();
        
        m_hashMap.clear();
        
        long currentAccount = m_currentClient.getAccount().getNumber();
        
        for(Client c: m_currentList){
            if (currentAccount == c.getAccount().getNumber()){
                //System.out.println("CONTA NAO PODE IR!");
                continue;
            }
            
            m_hashMap.put(c.getName(), c.getAccount().getNumber());
            //m_clientsToTransfer.add(c.getName());
        }
        
    }
    
    private Client findClientByAccountNumber(long number){
        Client c = null;
        for(int i =0; i < m_currentList.size(); i++){
            c = m_currentList.get(i);
            if (c.getAccount().getNumber() == number){
                return c;
            }
        }
        return null;
    }
    public Map<String, Long> getClientsHashMap(){ return m_hashMap; }
    
    private void updateElement(String element){
        RequestContext context = RequestContext.getCurrentInstance();
        context.update(element);
    }
    
    private void updateTransferDialog(){
        updateElement("transferForm");
    }
    
    public void initTransferDialog(){
       fillData();
       
       updateTransferDialog();
       openDialog("TransferDialog");
    }
    
    public ClientDAO getDB(){ return m_db;}
    public List<Client> getClientsToTransfer(){ return m_currentList; }//return m_clientsToTransfer;}
    
    
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
                        
                if ( op.execute() ){
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
                long toTransfer = 0;
                try{
                    toTransfer = getSelectedAccountToTransfer();
                    value = Double.parseDouble(m_value);
                    System.out.println("PROCURANDO POR CLIENTE DE CONTA "  +toTransfer);
                    
                    Client clientoTransfer = findClientByAccountNumber( toTransfer );
                    if (clientoTransfer == null)
                        break;
                    
                    op = new BankingMovimentationBuilder(Operation.OPERATION_TYPE_DRAFT, 
                            m_currentClient)
                            .setName(Operation.OPERATION_TRANSFER)
                            .setValue(value)
                            .setOwner(m_currentClient)
                            .setTypeDescription(Operation.OPERATION_DESCRIPTION_DEBIT)
                            .build();
                   
                    if ( op.execute() ){
                        registerOperation(op);
                        op = new BankingMovimentationBuilder(Operation.OPERATION_TYPE_DEPOSIT, 
                                clientoTransfer)
                            .setName(Operation.OPERATION_TRANSFER)
                            .setOwner(clientoTransfer)
                            .setValue(value)
                            .setTypeDescription(Operation.OPERATION_DESCRIPTION_CREDIT)
                            .build();
                        
                        op.execute();
                        registerOperation(op);
                        OperationSucess("Transferência Realizada!");
                        
                    } else OperationFailed("Transferência não realizada!");
                    
                    setValue("");
                    
                    
                } catch(Exception ex){
                    System.out.println("OLHA LA RAPAZ!");
                    
                    break;
                }
                
                //value = Double.parseDouble(m_value);
                
                // obeter a conta ou o cliente que vai receber a transferencia!
                //Long l = m_hashMap.get(getClientsToTransfer());
                System.out.println("Selecionou: " + getSelectedAccountToTransfer()
                + " Conta numero " );
                
                
                closeDialog("TransferDialog");
                break;
                
            case Operation.OPERATION_TYPE_QUERY:
                m_currentClient = m_db.getById(m_currentClient.getId());
                System.out.println(m_currentClient + "UPDATED BY ID!");
                
                op = new BankingOperationBuilder(Operation.OPERATION_TYPE_QUERY, m_currentClient)
                        .setName(Operation.OPERATION_QUERY)
                        .setTypeDescription(Operation.OPERATION_QUERY)
                        .build();
                
                if (op.execute()){
                    registerOperation(op);
                    setQueryOutput( op.getDetails());
                    updateElement("queryForm");
                    openDialog("queryDialog");
                }
                break;
                
            case Operation.OPERATION_TYPE_EXTRACT:
                m_currentClient = m_db.getById(m_currentClient.getId());
                op = new BankingOperationBuilder(Operation.OPERATION_TYPE_EXTRACT, m_currentClient)
                        .setName(Operation.OPERATION_EXTRACT)
                        .setTypeDescription(Operation.OPERATION_EXTRACT)
                        .build();
                
                List<Operation> operations = m_currentClient.getAccount().getOperationList();
                String output ="";
                
                for(Operation o: operations){
                    output+= o.getDetails();
                    output+= "</br> </br>";
                }
                if (op.execute()){
                    registerOperation(op);
                    setQueryOutput(output);
                    updateElement("scrollPanel");
                    updateElement("extractForm");
                    openDialog("extractDialog");
                } else OperationFailed("Não foi possível realizar extrato!");
                break;
                
            default:
                break;
        }
    }
    
    public void setQueryOutput(String query){ m_queryOutput = query;}
    
    public String getQueryOutput(){ return m_queryOutput;}
    
    public OperationBean getOperationBean(){ return this;}
}
