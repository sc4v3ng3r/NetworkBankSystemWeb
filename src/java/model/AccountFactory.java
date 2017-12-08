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

public class AccountFactory implements AccountFactoryInterface {

    @Override
    public Account createAccount(int type) {
        Account acc = null;
        switch(type){
            case Account.ACCOUNT_NORMAL:
                acc = new NormalAccount();
                break;
            case Account.ACCOUNT_SPECIAL:
                acc = new SpecialAccount(0);
                break;
        }
        
        return acc;
    }
    
    @Override
    public Account createAccount(String type) {
        switch(type){
            case Account.ACCOUNT_TYPE_NORMAL:
                return createAccount(Account.ACCOUNT_NORMAL);
                
            case Account.ACCOUNT_TYPE_SPECIAL:
                return createAccount(Account.ACCOUNT_SPECIAL);
                
        }
        return null;
    }
}
