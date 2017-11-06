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
public class SpecialAccount extends Account {
    private double m_credit;
    
    protected SpecialAccount(double credit) {
        super(ACCOUNT_SPECIAL);
        setTypeDescription(ACCOUNT_TYPE_SPECIAL);
        setCredit(credit); 
    }
    
    public final void setCredit(double credit){ m_credit = credit;}
    
    public final double getCredit(){ return m_credit; }
    
    public double getBalanceTotal() {
        double credit = getCredit();
        return credit + super.getBalance();
    }
    
    @Override
    public String toString(){
        return  "Número: " + getNumber() + ";" +
                "Tipo: " + getTypeDesciription() + ";" +
                "Saldo Total: " + getBalanceTotal() + ";" +
                "Saldo Parcial:" + getBalance() + ";" + 
                "Crédito: " + getCredit();
    }
    
}
