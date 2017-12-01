/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import model.Client;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import web.bank.system.util.NewHibernateUtil;

/**
 *
 * @author scavenger
 */
@ManagedBean
public class ClientDAO implements Serializable{
    private List<Client> m_clientList = new ArrayList<>();
    
    public ClientDAO(){
        updateList();
        
        /*System.out.println("####################### DATA DAO DEBUG ####################### ");
        System.out.println("SHOW ALL DATA DEBUG!");
        for(Client c: m_clientList)
            System.out.println(c);
        System.out.println("############################################################## ");*/
    }
    public void add(Client client){
        Client c = salvar(client);
        m_clientList.add( c );
    
    }
    
    
    private Client salvar(Client c){
        Session session = NewHibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.getTransaction();
        transaction.begin();
        session.save(c);
        session.flush();
        session.update(c);
        transaction.commit();
        session.close();
        m_clientList.add(c);
        return c;
    }
    
    public void delete(Client c){
        Session session = NewHibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.getTransaction();
        transaction.begin();
        session.delete(c);
        m_clientList.remove(c);
        session.flush();
        transaction.commit();
        session.close();
    }
    
    public void update(Client c){
        Session session = NewHibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.getTransaction();
        transaction.begin();
        session.save(c);
        session.flush();
        transaction.commit();
        session.close();
    }
    
    public List<Client> getList(){ return m_clientList;}
    
    public void updateList(){
        Session session = NewHibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.getTransaction();
        transaction.begin();
        m_clientList = session.createCriteria(Client.class).
                setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
        //transaction.commit();
        session.close();
        System.out.println("ClientDAO::updateList() | LIST SIZE " + m_clientList.size());
    }
    
    public List<Client> getClientListFromDB(){
        updateList();
        return getList();
    }
    
    public Client getById(long id){
        Client c = null;
        try{
            Session session = NewHibernateUtil.getSessionFactory().openSession();
             c = (Client) session.get(Client.class, id);
             session.close();
        } catch(Exception ex){
            System.out.println(ex);
        }
        
        return c;
    }
    public void removeFirst(){
        if (m_clientList.size() == 0)
            return;
        Client c = m_clientList.remove(0);
        delete(c);
    }
    
}
