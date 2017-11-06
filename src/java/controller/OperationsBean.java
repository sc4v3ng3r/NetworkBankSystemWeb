/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.view.ViewScoped;

/**
 *
 * @author scavenger
 */
@ManagedBean(name = "OperationsBean")
@SessionScoped
public class OperationsBean {
    
    public String carryOn(){
        return "operationScreen.xhtml";
    }
}
