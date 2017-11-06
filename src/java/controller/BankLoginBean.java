/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import model.Account;

/**
 * 
 * @author scavenger
 */
@SessionScoped //define ate quando essa classe fica na memoria para cada sessao!
@ManagedBean(name = "BankAccountBean")
public class BankLoginBean {
    //private Account m_account;
    private String m_accountNumber = "";
    private String m_status = "";

    public BankLoginBean() {
    }

    public String getAccountNumber() {
        return m_accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.m_accountNumber = accountNumber;
    }

    public void setStatus(String status) {
        this.m_status = status;
    }

    public String getStatus() {
        return m_status;
    }
    
    
    public void yourAccount(){
        if (Integer.parseInt(m_accountNumber) == 10){
            setStatus("CONTA INVALIDA!");
        } else {
            setStatus("Sua conta é: " + m_accountNumber);
        }
    }
}
