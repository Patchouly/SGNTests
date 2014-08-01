/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import mb.KeyMB;
import mb.keyator;
import utils.DesEncrypter;
import utils.Base64Crypt;

/**
 *
 * @author Pedro
 */
@SessionScoped
@ManagedBean(name = "keyBean")
public class KeyBean implements Serializable {

    private Integer software; // 0 = Genérico; 1 = PontoWeb; 2 = procuradoria;
    private String cliente;
    private String cnpj;
    private String cpf;
    private String code;

    private Integer meses;
    private String date;
    private boolean success;
    private Base64Crypt td;
    
    //chave de cryptografia e descryptografia
    private String key = "deadwood8986deadwood8986";

    public void init() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            td = new Base64Crypt("deadwood8986deadwood8986");
            success = false;
            software = 0;
            cliente = "";
            cnpj = "";
            cpf = "";
            code = "";
            date = "";
        }
    }

    public void submit() {
        Date sysDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
        String iniDate = sdf.format(sysDate);
        String endDate = "null";
        if (date != null) {
            endDate = date.replace("/", "");
        }
        String fullCode = "";
        String fullCodeSeparator = "";
        Boolean fisica;
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        if ((req.getParameter("pessoa") != null) && (req.getParameter("pessoa").equals("on"))) {
            cpf = req.getParameter("cpf");
            cpf = cpf.replace(".", "").replace("-", "");
            fullCode = software + cpf + iniDate + endDate;
            fullCodeSeparator = software + " - " + cpf + " - " + iniDate + " - " + endDate;
            fisica = true;
        } else {
            cnpj = req.getParameter("cnpj");
            cnpj = cnpj.replace(".", "").replace("-", "").replace("/", "");
            fullCode = software + cnpj + iniDate + endDate;
            fullCodeSeparator = software + " - " + cnpj + " - " + iniDate + " - " + endDate;
            fisica = false;
        }
        System.out.println("fullcode: " + fullCode);
        System.out.println("With separator: " + fullCodeSeparator);
        System.out.println("key: "+key);
        code = encriptonator(fullCode);
        System.out.println("code: " + code);
        if (fullCode.equals(DesEncriptonator(code))) {
            timeLeft(code);
            success = true;
            KeyMB keyMB = new KeyMB();
            if (fisica) {
                keyMB.saveCPFKey(cpf, cliente, endDate, code);
            } else {
                keyMB.saveCNPJKey(cnpj, cliente, endDate, code);
            }
        } else {
            code = "Chave criptografada não pode ser descriptografada!";
        }
    }

    public void timeLeft(String criptografy) {
        String originalCode = DesEncriptonator(criptografy);
        //System.out.println("original: "+originalCode);
    }

    public String encriptonator(String chave) {
        /*
         DesEncrypter encrypter = new DesEncrypter("aabbccaa");
         return encrypter.encrypt(chave);
         */
        
        return td.encrypt(chave);
                
    }

    public String DesEncriptonator(String chave) {
        /*
         DesEncrypter encrypter = new DesEncrypter("aabbccaa");
         return encrypter.decrypt(chave);
         */
        return td.decrypt(chave);
    }

    public Integer getSoftware() {
        return software;
    }

    public void setSoftware(Integer software) {
        this.software = software;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getMeses() {
        return meses;
    }

    public void setMeses(Integer meses) {
        this.meses = meses;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

}
