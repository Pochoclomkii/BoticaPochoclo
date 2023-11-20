/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author bryan
 */
@Entity
@Table(name = "boleta_detalle")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BoletaDetalle.findAll", query = "SELECT b FROM BoletaDetalle b"),
    @NamedQuery(name = "BoletaDetalle.findByIdBoletaDetalle", query = "SELECT b FROM BoletaDetalle b WHERE b.idBoletaDetalle = :idBoletaDetalle"),
    @NamedQuery(name = "BoletaDetalle.findByCantidadProducto", query = "SELECT b FROM BoletaDetalle b WHERE b.cantidadProducto = :cantidadProducto")})
public class BoletaDetalle implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idBoletaDetalle")
    private Integer idBoletaDetalle;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cantidadProducto")
    private int cantidadProducto;
    @JoinColumn(name = "idProducto", referencedColumnName = "codiProd")
    @ManyToOne(optional = false)
    private Producto idProducto;
    @JoinColumn(name = "idBoleta", referencedColumnName = "idBoleta")
    @ManyToOne(optional = false)
    private Boleta idBoleta;

    public BoletaDetalle() {
    }

    public BoletaDetalle(Integer idBoletaDetalle) {
        this.idBoletaDetalle = idBoletaDetalle;
    }

    public BoletaDetalle(Integer idBoletaDetalle, int cantidadProducto) {
        this.idBoletaDetalle = idBoletaDetalle;
        this.cantidadProducto = cantidadProducto;
    }

    public Integer getIdBoletaDetalle() {
        return idBoletaDetalle;
    }

    public void setIdBoletaDetalle(Integer idBoletaDetalle) {
        this.idBoletaDetalle = idBoletaDetalle;
    }

    public int getCantidadProducto() {
        return cantidadProducto;
    }

    public void setCantidadProducto(int cantidadProducto) {
        this.cantidadProducto = cantidadProducto;
    }

    public Producto getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Producto idProducto) {
        this.idProducto = idProducto;
    }

    public Boleta getIdBoleta() {
        return idBoleta;
    }

    public void setIdBoleta(Boleta idBoleta) {
        this.idBoleta = idBoleta;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idBoletaDetalle != null ? idBoletaDetalle.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BoletaDetalle)) {
            return false;
        }
        BoletaDetalle other = (BoletaDetalle) object;
        if ((this.idBoletaDetalle == null && other.idBoletaDetalle != null) || (this.idBoletaDetalle != null && !this.idBoletaDetalle.equals(other.idBoletaDetalle))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.BoletaDetalle[ idBoletaDetalle=" + idBoletaDetalle + " ]";
    }
    
}
