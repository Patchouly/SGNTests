/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mb;

import DAO.ClientesJpaController;
import java.io.Serializable;

/**
 *
 * @author Pedro
 */
public class KeyMB implements Serializable{
    
    
    
    public void saveCNPJKey(String cnpj, String cliente, String endDate, String cript){
        ClientesJpaController clientesJpaController = new ClientesJpaController();
        
    }
    
    public void saveCPFKey(String cpf, String cliente, String endDate, String cript){
        ClientesJpaController clientesJpaController = new ClientesJpaController();
        
    }
    
}
