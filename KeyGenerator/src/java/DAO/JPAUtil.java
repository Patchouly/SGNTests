/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.io.Serializable;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;



/**
 *
 * @author PRCC
 */
public class JPAUtil implements Serializable{
    
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("KeyGeneratorPU");
    
    public static EntityManagerFactory getEMF() {
        return emf;
    }
}
