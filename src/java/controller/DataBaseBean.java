/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import model.Client;

/**
 *
 * @author scavenger
 */

@ManagedBean(name="DataBaseBean")

public class DataBaseBean implements Serializable {
    
    private final Repository m_repository = Repository.getInstance();
    public DataBaseBean(){}
    
    public List<Client> getClientListFromDB(){
        return m_repository.getClientListFromDB();
    }
    
    public void removeFirst(){
        m_repository.removeFirst();
    }
    
    
}
