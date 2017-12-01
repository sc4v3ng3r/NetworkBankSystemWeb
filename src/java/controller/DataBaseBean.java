/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import model.Client;

/**
 *
 * @author scavenger
 */

@ManagedBean(name="DataBaseBean")
public class DataBaseBean implements Serializable {
    
    private ClientDAO m_dao = new ClientDAO();
    
    public DataBaseBean(){}
    
    public void add(Client client) { m_dao.add(client); }
    
    public List<Client> getClients(){ return m_dao.getList();}
    
    public void removeFirst(){
        m_dao.removeFirst();
    }
    
}
