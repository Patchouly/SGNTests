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
import mb.keyator;
import utils.DesEncrypter;

/**
 *
 * @author Pedro
 */
@SessionScoped
@ManagedBean(name = "keyBean")
public class KeyBean implements Serializable {

    private String key1;
    private String code1;

    private Integer meses;
    private String date;
    private boolean success;

    public void init() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            success = false;
            key1 = "";
            code1 = "";
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
        String fullCode = key1 + "SGN" + iniDate + "SGN" + endDate;
        code1 = encriptonator(fullCode);
        timeLeft(code1);
        success = true;
    }

    public void timeLeft(String criptografy) {
        String originalCode = DesEncriptonator(criptografy);
    }

    public String encriptonator(String chave) {
        DesEncrypter encrypter = new DesEncrypter("aabcca");
        return encrypter.encrypt(chave);
    }

    public String DesEncriptonator(String chave) {
        DesEncrypter encrypter = new DesEncrypter("aabcca");
        return encrypter.decrypt(chave);
    }

    public String getKey1() {
        return key1;
    }

    public void setKey1(String key1) {
        this.key1 = key1;
    }

    public String getCode1() {
        return code1;
    }

    public void setCode1(String code1) {
        this.code1 = code1;
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

}
