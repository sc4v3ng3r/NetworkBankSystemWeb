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
import model.operation.builder.BankingMovimentationBuilder;
import model.operation.builder.BankingOperationBuilder;
import model.operation.builder.Operation;
import org.primefaces.context.RequestContext;
//import web.bank.system.util.NewHibernateUtil;

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
    private final Repository m_repository;
    private List<Client> m_currentList;
    private Long m_selectedAccountToTransfer;

    private final Map<String, Long> m_hashMap = new HashMap<>();
    
    public OperationBean() {
        String accountNumber = (String) FacesContext.getCurrentInstance().
                getExternalContext().getFlash().get("CURRENT_ACCOUNT");
        m_repository = Repository.getInstance();
        
        m_currentClient =  m_repository.getById( Long.parseLong(accountNumber)); 
        m_headerTxt = "Cliente: " + m_currentClient.getName()
                + "    |    Conta: " + m_currentClient.getAccount().getNumber();
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
    
    private void fillData() {
        m_currentList = m_repository.getClientListFromDB();
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
    
    /*UTILIZAR ESSE METODO DO REPOSITORY!*/
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
    
    public void setQueryOutput(String query){ m_queryOutput = query;}
    
    public String getQueryOutput(){ return m_queryOutput;}
    
    public void execute(int type) {
        Operation op = null;

        switch (type) {
            case Operation.OPERATION_TYPE_DRAFT:
                double value;
                
                try{
                    value = Double.parseDouble(m_value);
                    System.out.println("OPERAÇÃO SAQUE!");
                    
                    op = new BankingMovimentationBuilder(Operation.OPERATION_TYPE_DRAFT, m_currentClient)
                        .setTypeDescription(Operation.OPERATION_DESCRIPTION_DEBIT)
                        .setName(Operation.OPERATION_DRAFT)
                        .setValue(value)
                        .build();
                        
                if ( op.execute() ){
                    OperationSucess("Saque realizado com sucesso!");
                    m_repository.registerOperation(op);
                    
                } else
                    OperationFailed("Saldo indisponível");
                
                    closeDialog("DraftDialog");
                    setValue("");
                } catch(NumberFormatException ex){
                    OperationFailed("Valor Inválido");
                    break;
                    // MSG DE VALOR INVALIDO!
                }
                
                break;

            case Operation.OPERATION_TYPE_DEPOSIT:
                try{
                value = Double.parseDouble(m_value);

                op = new BankingMovimentationBuilder(Operation.OPERATION_TYPE_DEPOSIT, 
                        m_currentClient)
                        .setName(Operation.OPERATION_DEPOSIT)
                        .setTypeDescription(Operation.OPERATION_DESCRIPTION_CREDIT)
                        .setValue(value)
                        .build();

                if (op.execute()) {
                    
                    m_repository.registerOperation(op);
                    OperationSucess("Depósito realizado com sucesso!");
                    
                } else 
                    OperationFailed("Depósito não realizado!");
                
                System.out.println("OPERAÇÃO DEPÓSITO");
                closeDialog("DepositDialog");
                setValue("");
                
                } catch(NumberFormatException ex){
                    OperationFailed("Valor Inválido");
                    break;
                }
                break;
            
            case Operation.OPERATION_TYPE_TRANSFER:
                Long toTransfer = null;
                try{
                    toTransfer = getSelectedAccountToTransfer();
                    if (toTransfer == null){
                        OperationFailed("Selecione um cliente");
                        return;
                    }
                    
                    
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
                        
                        m_repository.registerOperation(op);
                        
                        op = new BankingMovimentationBuilder(Operation.OPERATION_TYPE_DEPOSIT, 
                                clientoTransfer)
                            .setName(Operation.OPERATION_TRANSFER)
                            .setOwner(clientoTransfer)
                            .setValue(value)
                            .setTypeDescription(Operation.OPERATION_DESCRIPTION_CREDIT)
                            .build();
                        
                        op.execute();
                        m_repository.registerOperation(op);
                        OperationSucess("Transferência Realizada!");
                        
                    } else OperationFailed("Transferência não realizada!");
                    
                    setValue("");
                    
                    
                } catch(NumberFormatException ex){
                    OperationFailed("Valor Inválido");
                    break;
                }
                closeDialog("TransferDialog");
                break;
                
            case Operation.OPERATION_TYPE_QUERY:
                
                op = new BankingOperationBuilder(Operation.OPERATION_TYPE_QUERY, m_currentClient)
                        .setName(Operation.OPERATION_QUERY)
                        .setTypeDescription(Operation.OPERATION_QUERY)
                        .build();
                
                if (op.execute()){
                    
                    m_repository.registerOperation(op);
                    setQueryOutput( op.getDetails());
                    updateElement("queryForm");
                    openDialog("queryDialog");
                }
                break;
                
            case Operation.OPERATION_TYPE_EXTRACT:
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
                    m_repository.registerOperation(op);
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
}
