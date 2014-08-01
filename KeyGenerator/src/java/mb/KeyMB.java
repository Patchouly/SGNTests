/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mb;

import DAO.ClientesJpaController;
import entities.Clientes;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Pedro
 */
public class KeyMB implements Serializable {

    public void saveCNPJKey(String cnpj, String cliente, String endDate, String cript) {
        ClientesJpaController clientesJpaController = new ClientesJpaController();
        Clientes customer = new Clientes();
        try {
            System.out.println("cnpj: "+cnpj);
            customer = clientesJpaController.findClientesByCNPJ(cnpj);
            if (customer.getId() != null) {
                System.out.println("já existe");
                Date expTime = new SimpleDateFormat("ddMMyyyy").parse(endDate);
                customer.setDataExp(expTime);
                clientesJpaController.edit(customer);
            } else {
                System.out.println("não existe");
                customer.setCnpj(cnpj);
                customer.setNome(cliente);
                Date expTime = new SimpleDateFormat("ddMMyyyy").parse(endDate);
                customer.setDataExp(expTime);
                clientesJpaController.create(customer);
            }
        } catch (Exception e) {
            System.out.println("saveCNPJKey e:> " + e);
        }

    }

    public void saveCPFKey(String cpf, String cliente, String endDate, String cript) {
        ClientesJpaController clientesJpaController = new ClientesJpaController();
        Clientes customer = new Clientes();
        try {
            customer = clientesJpaController.findClientesByCNPJ(cpf);
            if (customer.getId() != null) {
                System.out.println("já existe");
                Date expTime = new SimpleDateFormat("ddMMyyyy").parse(endDate);
                customer.setDataExp(expTime);

                clientesJpaController.edit(customer);
            } else {
                System.out.println("não existe");
                customer.setCnpj(cpf);
                customer.setNome(cliente);
                Date expTime = new SimpleDateFormat("ddMMyyyy").parse(endDate);
                customer.setDataExp(expTime);
                clientesJpaController.create(customer);
            }
        } catch (Exception e) {
            System.out.println("saveCPFKey e:> " + e);
        }
    }
}
