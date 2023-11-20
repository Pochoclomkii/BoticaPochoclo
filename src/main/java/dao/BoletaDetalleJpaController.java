/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import dto.Producto;
import dto.Boleta;
import dto.BoletaDetalle;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author User
 */
public class BoletaDetalleJpaController implements Serializable {

    public BoletaDetalleJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(BoletaDetalle boletaDetalle) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Producto idProducto = boletaDetalle.getIdProducto();
            if (idProducto != null) {
                idProducto = em.getReference(idProducto.getClass(), idProducto.getCodiProd());
                boletaDetalle.setIdProducto(idProducto);
            }
            Boleta idBoleta = boletaDetalle.getIdBoleta();
            if (idBoleta != null) {
                idBoleta = em.getReference(idBoleta.getClass(), idBoleta.getIdBoleta());
                boletaDetalle.setIdBoleta(idBoleta);
            }
            em.persist(boletaDetalle);
            if (idProducto != null) {
                idProducto.getBoletaDetalleList().add(boletaDetalle);
                idProducto = em.merge(idProducto);
            }
            if (idBoleta != null) {
                idBoleta.getBoletaDetalleList().add(boletaDetalle);
                idBoleta = em.merge(idBoleta);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findBoletaDetalle(boletaDetalle.getIdBoletaDetalle()) != null) {
                throw new PreexistingEntityException("BoletaDetalle " + boletaDetalle + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(BoletaDetalle boletaDetalle) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BoletaDetalle persistentBoletaDetalle = em.find(BoletaDetalle.class, boletaDetalle.getIdBoletaDetalle());
            Producto idProductoOld = persistentBoletaDetalle.getIdProducto();
            Producto idProductoNew = boletaDetalle.getIdProducto();
            Boleta idBoletaOld = persistentBoletaDetalle.getIdBoleta();
            Boleta idBoletaNew = boletaDetalle.getIdBoleta();
            if (idProductoNew != null) {
                idProductoNew = em.getReference(idProductoNew.getClass(), idProductoNew.getCodiProd());
                boletaDetalle.setIdProducto(idProductoNew);
            }
            if (idBoletaNew != null) {
                idBoletaNew = em.getReference(idBoletaNew.getClass(), idBoletaNew.getIdBoleta());
                boletaDetalle.setIdBoleta(idBoletaNew);
            }
            boletaDetalle = em.merge(boletaDetalle);
            if (idProductoOld != null && !idProductoOld.equals(idProductoNew)) {
                idProductoOld.getBoletaDetalleList().remove(boletaDetalle);
                idProductoOld = em.merge(idProductoOld);
            }
            if (idProductoNew != null && !idProductoNew.equals(idProductoOld)) {
                idProductoNew.getBoletaDetalleList().add(boletaDetalle);
                idProductoNew = em.merge(idProductoNew);
            }
            if (idBoletaOld != null && !idBoletaOld.equals(idBoletaNew)) {
                idBoletaOld.getBoletaDetalleList().remove(boletaDetalle);
                idBoletaOld = em.merge(idBoletaOld);
            }
            if (idBoletaNew != null && !idBoletaNew.equals(idBoletaOld)) {
                idBoletaNew.getBoletaDetalleList().add(boletaDetalle);
                idBoletaNew = em.merge(idBoletaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = boletaDetalle.getIdBoletaDetalle();
                if (findBoletaDetalle(id) == null) {
                    throw new NonexistentEntityException("The boletaDetalle with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BoletaDetalle boletaDetalle;
            try {
                boletaDetalle = em.getReference(BoletaDetalle.class, id);
                boletaDetalle.getIdBoletaDetalle();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The boletaDetalle with id " + id + " no longer exists.", enfe);
            }
            Producto idProducto = boletaDetalle.getIdProducto();
            if (idProducto != null) {
                idProducto.getBoletaDetalleList().remove(boletaDetalle);
                idProducto = em.merge(idProducto);
            }
            Boleta idBoleta = boletaDetalle.getIdBoleta();
            if (idBoleta != null) {
                idBoleta.getBoletaDetalleList().remove(boletaDetalle);
                idBoleta = em.merge(idBoleta);
            }
            em.remove(boletaDetalle);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<BoletaDetalle> findBoletaDetalleEntities() {
        return findBoletaDetalleEntities(true, -1, -1);
    }

    public List<BoletaDetalle> findBoletaDetalleEntities(int maxResults, int firstResult) {
        return findBoletaDetalleEntities(false, maxResults, firstResult);
    }

    private List<BoletaDetalle> findBoletaDetalleEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(BoletaDetalle.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public BoletaDetalle findBoletaDetalle(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(BoletaDetalle.class, id);
        } finally {
            em.close();
        }
    }

    public int getBoletaDetalleCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<BoletaDetalle> rt = cq.from(BoletaDetalle.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
