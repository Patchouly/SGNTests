/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beans;

import entities.Clientes;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Pedro
 */
@SessionScoped
@ManagedBean(name = "customerBean")
public class CustomerBean implements Serializable{
    
    private Clientes cliente;
    
    public void init() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            
        }
    }

    public Clientes getCliente() {
        return cliente;
    }

    public void setCliente(Clientes cliente) {
        this.cliente = cliente;
    }
    
    
}
