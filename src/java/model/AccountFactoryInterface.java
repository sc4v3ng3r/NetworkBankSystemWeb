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
public interface AccountFactoryInterface {
    
    public Account createAccount(int type);
    
    public Account createAccount(String type);
}
