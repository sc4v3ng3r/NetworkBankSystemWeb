/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author scavenger
 */
@Entity
@Table(name="specialAccount", schema = "MyBank")
/*
@AttributeOverrides(value = {
    @AttributeOverride(name ="accountNumber", column = @Column(name="accountNumber") ),
    @AttributeOverride(name="m_value", column = @Column(name="value")),
    @AttributeOverride(name="m_type", column = @Column(name="type")),
    @AttributeOverride(name="m_typeDescription", column = @Column(name="typeDescription")),
    @AttributeOverride(name="m_credit", column = @Column(name="credit")),
   
    })*/
public class SpecialAccount extends Account {
    private double m_credit;
    
    protected SpecialAccount(double credit) {
        super(ACCOUNT_SPECIAL);
        setTypeDescription(ACCOUNT_TYPE_SPECIAL);
        setCredit(credit); 
    }
    
    private SpecialAccount(){
        super();
    }
    
    public final void setCredit(double credit){ m_credit = credit;}
    
    public final double getCredit(){ return m_credit; }
    
    public double getBalanceTotal() {
        double credit = getCredit();
        return credit + super.getBalance();
    }
    
    @Override
    public String toString(){
        return  "Número: " + getNumber() + "\n" +
                "Tipo: " + getTypeDescription() + "\n" +
                "Saldo Total: " + getBalanceTotal() + "\n" +
                "Saldo Parcial:" + getBalance() + "\n" + 
                "Crédito: " + getCredit() + "\n" +
                "Operations: " + getOperationList();
    }
    
}
