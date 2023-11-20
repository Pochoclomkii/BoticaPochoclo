/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import dto.BoletaDetalle;
import dto.Producto;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author User
 */
public class ProductoJpaController1 implements Serializable {

    public ProductoJpaController1(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("com.mycompany_BoticaWeb_war_1.0-SNAPSHOTPU");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public ProductoJpaController1() {
    }
    

    public void create(Producto producto) throws PreexistingEntityException, Exception {
        if (producto.getBoletaDetalleList() == null) {
            producto.setBoletaDetalleList(new ArrayList<BoletaDetalle>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<BoletaDetalle> attachedBoletaDetalleList = new ArrayList<BoletaDetalle>();
            for (BoletaDetalle boletaDetalleListBoletaDetalleToAttach : producto.getBoletaDetalleList()) {
                boletaDetalleListBoletaDetalleToAttach = em.getReference(boletaDetalleListBoletaDetalleToAttach.getClass(), boletaDetalleListBoletaDetalleToAttach.getIdBoletaDetalle());
                attachedBoletaDetalleList.add(boletaDetalleListBoletaDetalleToAttach);
            }
            producto.setBoletaDetalleList(attachedBoletaDetalleList);
            em.persist(producto);
            for (BoletaDetalle boletaDetalleListBoletaDetalle : producto.getBoletaDetalleList()) {
                Producto oldIdProductoOfBoletaDetalleListBoletaDetalle = boletaDetalleListBoletaDetalle.getIdProducto();
                boletaDetalleListBoletaDetalle.setIdProducto(producto);
                boletaDetalleListBoletaDetalle = em.merge(boletaDetalleListBoletaDetalle);
                if (oldIdProductoOfBoletaDetalleListBoletaDetalle != null) {
                    oldIdProductoOfBoletaDetalleListBoletaDetalle.getBoletaDetalleList().remove(boletaDetalleListBoletaDetalle);
                    oldIdProductoOfBoletaDetalleListBoletaDetalle = em.merge(oldIdProductoOfBoletaDetalleListBoletaDetalle);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findProducto(producto.getCodiProd()) != null) {
                throw new PreexistingEntityException("Producto " + producto + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Producto producto) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Producto persistentProducto = em.find(Producto.class, producto.getCodiProd());
            List<BoletaDetalle> boletaDetalleListOld = persistentProducto.getBoletaDetalleList();
            List<BoletaDetalle> boletaDetalleListNew = producto.getBoletaDetalleList();
            List<String> illegalOrphanMessages = null;
            for (BoletaDetalle boletaDetalleListOldBoletaDetalle : boletaDetalleListOld) {
                if (!boletaDetalleListNew.contains(boletaDetalleListOldBoletaDetalle)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain BoletaDetalle " + boletaDetalleListOldBoletaDetalle + " since its idProducto field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<BoletaDetalle> attachedBoletaDetalleListNew = new ArrayList<BoletaDetalle>();
            for (BoletaDetalle boletaDetalleListNewBoletaDetalleToAttach : boletaDetalleListNew) {
                boletaDetalleListNewBoletaDetalleToAttach = em.getReference(boletaDetalleListNewBoletaDetalleToAttach.getClass(), boletaDetalleListNewBoletaDetalleToAttach.getIdBoletaDetalle());
                attachedBoletaDetalleListNew.add(boletaDetalleListNewBoletaDetalleToAttach);
            }
            boletaDetalleListNew = attachedBoletaDetalleListNew;
            producto.setBoletaDetalleList(boletaDetalleListNew);
            producto = em.merge(producto);
            for (BoletaDetalle boletaDetalleListNewBoletaDetalle : boletaDetalleListNew) {
                if (!boletaDetalleListOld.contains(boletaDetalleListNewBoletaDetalle)) {
                    Producto oldIdProductoOfBoletaDetalleListNewBoletaDetalle = boletaDetalleListNewBoletaDetalle.getIdProducto();
                    boletaDetalleListNewBoletaDetalle.setIdProducto(producto);
                    boletaDetalleListNewBoletaDetalle = em.merge(boletaDetalleListNewBoletaDetalle);
                    if (oldIdProductoOfBoletaDetalleListNewBoletaDetalle != null && !oldIdProductoOfBoletaDetalleListNewBoletaDetalle.equals(producto)) {
                        oldIdProductoOfBoletaDetalleListNewBoletaDetalle.getBoletaDetalleList().remove(boletaDetalleListNewBoletaDetalle);
                        oldIdProductoOfBoletaDetalleListNewBoletaDetalle = em.merge(oldIdProductoOfBoletaDetalleListNewBoletaDetalle);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = producto.getCodiProd();
                if (findProducto(id) == null) {
                    throw new NonexistentEntityException("The producto with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Producto producto;
            try {
                producto = em.getReference(Producto.class, id);
                producto.getCodiProd();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The producto with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<BoletaDetalle> boletaDetalleListOrphanCheck = producto.getBoletaDetalleList();
            for (BoletaDetalle boletaDetalleListOrphanCheckBoletaDetalle : boletaDetalleListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Producto (" + producto + ") cannot be destroyed since the BoletaDetalle " + boletaDetalleListOrphanCheckBoletaDetalle + " in its boletaDetalleList field has a non-nullable idProducto field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(producto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Producto> findProductoEntities() {
        return findProductoEntities(true, -1, -1);
    }

    public List<Producto> findProductoEntities(int maxResults, int firstResult) {
        return findProductoEntities(false, maxResults, firstResult);
    }

    private List<Producto> findProductoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Producto.class));
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

    public Producto findProducto(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Producto.class, id);
        } finally {
            em.close();
        }
    }

    public int getProductoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Producto> rt = cq.from(Producto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
