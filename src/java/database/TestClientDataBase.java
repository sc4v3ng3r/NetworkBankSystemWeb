/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import model.Client;

/**
 *
 * @author scavenger
 */
@ManagedBean(name="TestClientDataBase")
@ApplicationScoped
public class TestClientDataBase {
    //private static TestClientDataBase m_instance = null;
    private List<Client> m_list = new ArrayList<>();
    
    public TestClientDataBase(){
    }
    
    public void add(Client c){
        System.out.println("TestClientDataBase::add -> " + c + " LIst size: " + m_list.size());
        m_list.add(c);
    }
    
    public List<Client> getClientList(){ return m_list;}
    
    public Client getClientAt(int i){
        return m_list.get(i);
    }
}   
