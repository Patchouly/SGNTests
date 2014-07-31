/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entitys;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Pedro
 */
@Entity
@Table(name = "clientes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Clientes.findAll", query = "SELECT c FROM Clientes c"),
    @NamedQuery(name = "Clientes.findById", query = "SELECT c FROM Clientes c WHERE c.id = :id"),
    @NamedQuery(name = "Clientes.findByCnpj", query = "SELECT c FROM Clientes c WHERE c.cnpj = :cnpj"),
    @NamedQuery(name = "Clientes.findByNome", query = "SELECT c FROM Clientes c WHERE c.nome = :nome"),
    @NamedQuery(name = "Clientes.findByDataExp", query = "SELECT c FROM Clientes c WHERE c.dataExp = :dataExp")})
public class Clientes implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "cnpj")
    private String cnpj;
    @Size(max = 50)
    @Column(name = "nome")
    private String nome;
    @Column(name = "data_exp")
    @Temporal(TemporalType.DATE)
    private Date dataExp;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clienteId")
    private List<Licencas> licencasList;
    
    transient private Licencas licencaAtual;

    public Clientes() {
    }

    public Clientes(Integer id) {
        this.id = id;
    }

    public Clientes(Integer id, String cnpj) {
        this.id = id;
        this.cnpj = cnpj;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getDataExp() {
        return dataExp;
    }

    public void setDataExp(Date dataExp) {
        this.dataExp = dataExp;
    }

    @XmlTransient
    public List<Licencas> getLicencasList() {
        return licencasList;
    }

    public void setLicencasList(List<Licencas> licencasList) {
        this.licencasList = licencasList;
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
        if (!(object instanceof Clientes)) {
            return false;
        }
        Clientes other = (Clientes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entitys.Clientes[ id=" + id + " ]";
    }

    public Licencas getLicencaAtual() {
        return licencaAtual;
    }

    public void setLicencaAtual(Licencas licencaAtual) {
        this.licencaAtual = licencaAtual;
    }
    
}
