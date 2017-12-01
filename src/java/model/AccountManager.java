/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author scavenger
 */

public class AccountManager {
    private static AccountManager m_instance = null;
    private final AccountFactory m_factory;
    
    public static synchronized AccountManager getInstance(){
        if (m_instance == null)
            m_instance = new AccountManager();
            
        return m_instance;
    }
    
    private AccountManager(){
        m_factory = new AccountFactory();
    }
    
    public synchronized SpecialAccount getEspecialAccount(double credit){
        SpecialAccount acc = (SpecialAccount) m_factory.createAccount(Account.ACCOUNT_SPECIAL);
        acc.setCredit(credit);
        return acc;
    }
    
    public synchronized NormalAccount getNormalAccount(){
        NormalAccount acc = (NormalAccount) m_factory.createAccount(Account.ACCOUNT_NORMAL);
        return acc;
    }
    
}
