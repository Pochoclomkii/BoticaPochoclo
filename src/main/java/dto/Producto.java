/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import com.google.gson.annotations.Expose;
import java.io.Serializable;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author User
 */
@Entity
@Table(name = "producto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Producto.findAll", query = "SELECT p FROM Producto p"),
    @NamedQuery(name = "Producto.findByCodiProd", query = "SELECT p FROM Producto p WHERE p.codiProd = :codiProd"),
    @NamedQuery(name = "Producto.findByNombProd", query = "SELECT p FROM Producto p WHERE p.nombProd = :nombProd"),
    @NamedQuery(name = "Producto.findByMarcProd", query = "SELECT p FROM Producto p WHERE p.marcProd = :marcProd"),
    @NamedQuery(name = "Producto.findByCateProd", query = "SELECT p FROM Producto p WHERE p.cateProd = :cateProd"),
    @NamedQuery(name = "Producto.findByFormProd", query = "SELECT p FROM Producto p WHERE p.formProd = :formProd"),
    @NamedQuery(name = "Producto.findByPrecProd", query = "SELECT p FROM Producto p WHERE p.precProd = :precProd"),
    @NamedQuery(name = "Producto.findByStocProd", query = "SELECT p FROM Producto p WHERE p.stocProd = :stocProd")})
public class Producto implements Serializable {
    @Expose(serialize = false, deserialize = false)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idProducto")
    transient private List<BoletaDetalle> boletaDetalleList;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "codiProd")
    private Integer codiProd;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "nombProd")
    private String nombProd;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "marcProd")
    private String marcProd;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "cateProd")
    private String cateProd;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "formProd")
    private String formProd;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "precProd")
    private String precProd;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "stocProd")
    private String stocProd;

    public Producto() {
    }

    public Producto(Integer codiProd) {
        this.codiProd = codiProd;
    }

    public Producto(Integer codiProd, String nombProd, String marcProd, String cateProd, String formProd, String precProd, String stocProd) {
        this.codiProd = codiProd;
        this.nombProd = nombProd;
        this.marcProd = marcProd;
        this.cateProd = cateProd;
        this.formProd = formProd;
        this.precProd = precProd;
        this.stocProd = stocProd;
    }

    public Integer getCodiProd() {
        return codiProd;
    }

    public void setCodiProd(Integer codiProd) {
        this.codiProd = codiProd;
    }

    public String getNombProd() {
        return nombProd;
    }

    public void setNombProd(String nombProd) {
        this.nombProd = nombProd;
    }

    public String getMarcProd() {
        return marcProd;
    }

    public void setMarcProd(String marcProd) {
        this.marcProd = marcProd;
    }

    public String getCateProd() {
        return cateProd;
    }

    public void setCateProd(String cateProd) {
        this.cateProd = cateProd;
    }

    public String getFormProd() {
        return formProd;
    }

    public void setFormProd(String formProd) {
        this.formProd = formProd;
    }

    public String getPrecProd() {
        return precProd;
    }

    public void setPrecProd(String precProd) {
        this.precProd = precProd;
    }

    public String getStocProd() {
        return stocProd;
    }

    public void setStocProd(String stocProd) {
        this.stocProd = stocProd;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codiProd != null ? codiProd.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Producto)) {
            return false;
        }
        Producto other = (Producto) object;
        if ((this.codiProd == null && other.codiProd != null) || (this.codiProd != null && !this.codiProd.equals(other.codiProd))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.Producto[ codiProd=" + codiProd + " ]";
    }
    
    @XmlTransient
    public List<BoletaDetalle> getBoletaDetalleList() {
        return boletaDetalleList;
    }

    public void setBoletaDetalleList(List<BoletaDetalle> boletaDetalleList) {
        this.boletaDetalleList = boletaDetalleList;
    }
    
}
