/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import static model.Account.ACCOUNT_NORMAL;
import static model.Account.ACCOUNT_SPECIAL;
import static model.Account.ACCOUNT_TYPE_NORMAL;
import static model.Account.ACCOUNT_TYPE_SPECIAL;

/**
 *
 * @author scavenger
 */
public class AccountFactory {
    
    public Account createAccount(int type){
        return getAccount(type);
        
    }
    
    public Account createAccount(String type){
        return getAccount(type);
    }
    
    private Account getAccount(String type){
        Account ac = null;
        switch(type){
            case ACCOUNT_TYPE_NORMAL:
                ac = new NormalAccount();
                break;
            case ACCOUNT_TYPE_SPECIAL:
                ac = new SpecialAccount(0);
                break;
        }
        return ac;
    }
    private Account getAccount(int type){
        Account ac = null;
        
        switch(type) {
            case Account.ACCOUNT_NORMAL:
                ac = new NormalAccount();
                break;
                
            case Account.ACCOUNT_SPECIAL:
                ac = new SpecialAccount(0);
                break;
        }
        return ac;
    }
    
/*    
    public Account createAccount(int type, Client owner){
        Account account = getAccount(type);
        owner.setAccount(account);
        return account;
    }
  */  
}
