/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author bryan
 */
@Entity
@Table(name = "boleta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Boleta.findAll", query = "SELECT b FROM Boleta b"),
    @NamedQuery(name = "Boleta.findByIdBoleta", query = "SELECT b FROM Boleta b WHERE b.idBoleta = :idBoleta"),
    @NamedQuery(name = "Boleta.findByFechaBoleta", query = "SELECT b FROM Boleta b WHERE b.fechaBoleta = :fechaBoleta"),
    @NamedQuery(name = "Boleta.findByDireBoleta", query = "SELECT b FROM Boleta b WHERE b.direBoleta = :direBoleta"),
    @NamedQuery(name = "Boleta.findByRucBoleta", query = "SELECT b FROM Boleta b WHERE b.rucBoleta = :rucBoleta")})
public class Boleta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idBoleta")
    private Integer idBoleta;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "fechaBoleta")
    private String fechaBoleta;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "direBoleta")
    private String direBoleta;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 11)
    @Column(name = "rucBoleta")
    private String rucBoleta;
    @JoinColumn(name = "idUsuario", referencedColumnName = "codiUsua")
    @ManyToOne(optional = false)
    private Usuario idUsuario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idBoleta")
    private List<BoletaDetalle> boletaDetalleList;

    public Boleta() {
    }

    public Boleta(Integer idBoleta) {
        this.idBoleta = idBoleta;
    }

    public Boleta(Integer idBoleta, String fechaBoleta, String direBoleta, String rucBoleta) {
        this.idBoleta = idBoleta;
        this.fechaBoleta = fechaBoleta;
        this.direBoleta = direBoleta;
        this.rucBoleta = rucBoleta;
    }

    public Integer getIdBoleta() {
        return idBoleta;
    }

    public void setIdBoleta(Integer idBoleta) {
        this.idBoleta = idBoleta;
    }

    public String getFechaBoleta() {
        return fechaBoleta;
    }

    public void setFechaBoleta(String fechaBoleta) {
        this.fechaBoleta = fechaBoleta;
    }

    public String getDireBoleta() {
        return direBoleta;
    }

    public void setDireBoleta(String direBoleta) {
        this.direBoleta = direBoleta;
    }

    public String getRucBoleta() {
        return rucBoleta;
    }

    public void setRucBoleta(String rucBoleta) {
        this.rucBoleta = rucBoleta;
    }

    public Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuario idUsuario) {
        this.idUsuario = idUsuario;
    }

    @XmlTransient
    public List<BoletaDetalle> getBoletaDetalleList() {
        return boletaDetalleList;
    }

    public void setBoletaDetalleList(List<BoletaDetalle> boletaDetalleList) {
        this.boletaDetalleList = boletaDetalleList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idBoleta != null ? idBoleta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Boleta)) {
            return false;
        }
        Boleta other = (Boleta) object;
        if ((this.idBoleta == null && other.idBoleta != null) || (this.idBoleta != null && !this.idBoleta.equals(other.idBoleta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.Boleta[ idBoleta=" + idBoleta + " ]";
    }
    
}
