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
@Table(name = "normalAccount", schema = "MyBank")
/*
@AttributeOverrides(value = {
    @AttributeOverride(name ="accountNumber", column = @Column(name="accountNumber") ),
    @AttributeOverride(name="m_value", column = @Column(name="value")),
    @AttributeOverride(name="m_type", column = @Column(name="type")),
    @AttributeOverride(name="m_typeDescription", column = @Column(name="typeDescription")),
    
    })*/
public class NormalAccount extends Account {
    
    protected NormalAccount() {
        super(ACCOUNT_NORMAL);
        setTypeDescription(ACCOUNT_TYPE_NORMAL);
    }
    
}
