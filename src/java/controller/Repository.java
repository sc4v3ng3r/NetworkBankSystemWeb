/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.List;
import model.Account;
import model.Client;
import model.operation.builder.Operation;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import web.bank.system.util.NewHibernateUtil;

/**
 *
 * @author scavenger
 */

public class Repository {
    
    private static Repository m_instance = null;
    private List<Client> m_list;
    
    private Repository(){
        m_list = getUpdatedList();
    }
    
    public static synchronized Repository getInstance(){
        if (m_instance == null)
            m_instance = new Repository();
        
        return m_instance;
    }
    
    public synchronized void add(Client client){
        Client c = salvar(client);
        m_list.add( c );
    
    }
    
    public synchronized void delete(Client c) {
       Session session = NewHibernateUtil.getSessionFactory().openSession();
       Transaction transaction = session.getTransaction();
       transaction.begin();
       session.delete(c);
       m_list.remove(c);
       session.flush();
       transaction.commit();
       session.close();
    }
    
    public synchronized void registerOperation(Operation operation){
        Session s = NewHibernateUtil.getSessionFactory().openSession();
        s.getTransaction().begin();
        //s.update(m_currentClient);
        s.save(operation);
        s.flush();
        s.getTransaction().commit();
        s.close();
    }
    
    public synchronized List<Client> getList(){ return m_list;}
    
    private Client salvar(Client c){
        Session session = NewHibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.getTransaction();
        transaction.begin();
        session.save(c);
        session.flush();
        //session.update(c);
        transaction.commit();
        session.close();
        return c;
    }
    
    private List<Client> getUpdatedList(){
        List<Client> list = null;
        Session session = NewHibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.getTransaction();
        transaction.begin();
        list = session.createCriteria(Client.class).
                setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
        //transaction.commit();
        session.close();
        return list;
        //System.out.println("ClientDAO::updateList() | LIST SIZE " + m_clientList.size());
    }
    
    public synchronized List<Client> getClientListFromDB(){
        //updateList();
        return getUpdatedList();
    }
    
    public synchronized void removeFirst(){
        if (m_list.isEmpty())
            return;
        
        Client c = m_list.remove(0);
        delete(c);
    }
    
    public synchronized Client getById(long id){
        Client c = null;
        try{
            Session session = NewHibernateUtil.getSessionFactory().openSession();
             c = (Client) session.get(Client.class, id);
             session.close();
        
        } catch(HibernateException ex){
            System.out.println(ex);
        }
        
        return c;
    }
    
    public synchronized Account getAccountById( long id){
        Account acc = null;
        try{
            Session session = NewHibernateUtil.getSessionFactory().openSession();
            session.getTransaction().begin();
            acc = (Account) session.get(Account.class, id);
        } catch(HibernateException ex){
            System.out.println(ex);
        }
        return acc;
    }
    
    
}
