/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entitys;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Pedro
 */
@Entity
@Table(name = "licencas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Licencas.findAll", query = "SELECT l FROM Licencas l"),
    @NamedQuery(name = "Licencas.findById", query = "SELECT l FROM Licencas l WHERE l.id = :id"),
    @NamedQuery(name = "Licencas.findByLicenca", query = "SELECT l FROM Licencas l WHERE l.licenca = :licenca"),
    @NamedQuery(name = "Licencas.findByDataExp", query = "SELECT l FROM Licencas l WHERE l.dataExp = :dataExp"),
    @NamedQuery(name = "Licencas.findByDataIni", query = "SELECT l FROM Licencas l WHERE l.dataIni = :dataIni")})
public class Licencas implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "licenca")
    private String licenca;
    @Basic(optional = false)
    @NotNull
    @Column(name = "data_exp")
    @Temporal(TemporalType.DATE)
    private Date dataExp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "data_ini")
    @Temporal(TemporalType.DATE)
    private Date dataIni;
    @JoinColumn(name = "cliente_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Clientes clienteId;

    public Licencas() {
    }

    public Licencas(Integer id) {
        this.id = id;
    }

    public Licencas(Integer id, String licenca, Date dataExp, Date dataIni) {
        this.id = id;
        this.licenca = licenca;
        this.dataExp = dataExp;
        this.dataIni = dataIni;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLicenca() {
        return licenca;
    }

    public void setLicenca(String licenca) {
        this.licenca = licenca;
    }

    public Date getDataExp() {
        return dataExp;
    }

    public void setDataExp(Date dataExp) {
        this.dataExp = dataExp;
    }

    public Date getDataIni() {
        return dataIni;
    }

    public void setDataIni(Date dataIni) {
        this.dataIni = dataIni;
    }

    public Clientes getClienteId() {
        return clienteId;
    }

    public void setClienteId(Clientes clienteId) {
        this.clienteId = clienteId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Licencas)) {
            return false;
        }
        Licencas other = (Licencas) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entitys.Licencas[ id=" + id + " ]";
    }
    
}
