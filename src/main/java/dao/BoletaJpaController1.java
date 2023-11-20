/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import dto.Boleta;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import dto.Usuario;
import dto.BoletaDetalle;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author User
 */
public class BoletaJpaController1 implements Serializable {

    public BoletaJpaController1(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Boleta boleta) throws PreexistingEntityException, Exception {
        if (boleta.getBoletaDetalleList() == null) {
            boleta.setBoletaDetalleList(new ArrayList<BoletaDetalle>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario idUsuario = boleta.getIdUsuario();
            if (idUsuario != null) {
                idUsuario = em.getReference(idUsuario.getClass(), idUsuario.getCodiUsua());
                boleta.setIdUsuario(idUsuario);
            }
            List<BoletaDetalle> attachedBoletaDetalleList = new ArrayList<BoletaDetalle>();
            for (BoletaDetalle boletaDetalleListBoletaDetalleToAttach : boleta.getBoletaDetalleList()) {
                boletaDetalleListBoletaDetalleToAttach = em.getReference(boletaDetalleListBoletaDetalleToAttach.getClass(), boletaDetalleListBoletaDetalleToAttach.getIdBoletaDetalle());
                attachedBoletaDetalleList.add(boletaDetalleListBoletaDetalleToAttach);
            }
            boleta.setBoletaDetalleList(attachedBoletaDetalleList);
            em.persist(boleta);
            if (idUsuario != null) {
                idUsuario.getBoletaList().add(boleta);
                idUsuario = em.merge(idUsuario);
            }
            for (BoletaDetalle boletaDetalleListBoletaDetalle : boleta.getBoletaDetalleList()) {
                Boleta oldIdBoletaOfBoletaDetalleListBoletaDetalle = boletaDetalleListBoletaDetalle.getIdBoleta();
                boletaDetalleListBoletaDetalle.setIdBoleta(boleta);
                boletaDetalleListBoletaDetalle = em.merge(boletaDetalleListBoletaDetalle);
                if (oldIdBoletaOfBoletaDetalleListBoletaDetalle != null) {
                    oldIdBoletaOfBoletaDetalleListBoletaDetalle.getBoletaDetalleList().remove(boletaDetalleListBoletaDetalle);
                    oldIdBoletaOfBoletaDetalleListBoletaDetalle = em.merge(oldIdBoletaOfBoletaDetalleListBoletaDetalle);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findBoleta(boleta.getIdBoleta()) != null) {
                throw new PreexistingEntityException("Boleta " + boleta + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Boleta boleta) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Boleta persistentBoleta = em.find(Boleta.class, boleta.getIdBoleta());
            Usuario idUsuarioOld = persistentBoleta.getIdUsuario();
            Usuario idUsuarioNew = boleta.getIdUsuario();
            List<BoletaDetalle> boletaDetalleListOld = persistentBoleta.getBoletaDetalleList();
            List<BoletaDetalle> boletaDetalleListNew = boleta.getBoletaDetalleList();
            List<String> illegalOrphanMessages = null;
            for (BoletaDetalle boletaDetalleListOldBoletaDetalle : boletaDetalleListOld) {
                if (!boletaDetalleListNew.contains(boletaDetalleListOldBoletaDetalle)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain BoletaDetalle " + boletaDetalleListOldBoletaDetalle + " since its idBoleta field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idUsuarioNew != null) {
                idUsuarioNew = em.getReference(idUsuarioNew.getClass(), idUsuarioNew.getCodiUsua());
                boleta.setIdUsuario(idUsuarioNew);
            }
            List<BoletaDetalle> attachedBoletaDetalleListNew = new ArrayList<BoletaDetalle>();
            for (BoletaDetalle boletaDetalleListNewBoletaDetalleToAttach : boletaDetalleListNew) {
                boletaDetalleListNewBoletaDetalleToAttach = em.getReference(boletaDetalleListNewBoletaDetalleToAttach.getClass(), boletaDetalleListNewBoletaDetalleToAttach.getIdBoletaDetalle());
                attachedBoletaDetalleListNew.add(boletaDetalleListNewBoletaDetalleToAttach);
            }
            boletaDetalleListNew = attachedBoletaDetalleListNew;
            boleta.setBoletaDetalleList(boletaDetalleListNew);
            boleta = em.merge(boleta);
            if (idUsuarioOld != null && !idUsuarioOld.equals(idUsuarioNew)) {
                idUsuarioOld.getBoletaList().remove(boleta);
                idUsuarioOld = em.merge(idUsuarioOld);
            }
            if (idUsuarioNew != null && !idUsuarioNew.equals(idUsuarioOld)) {
                idUsuarioNew.getBoletaList().add(boleta);
                idUsuarioNew = em.merge(idUsuarioNew);
            }
            for (BoletaDetalle boletaDetalleListNewBoletaDetalle : boletaDetalleListNew) {
                if (!boletaDetalleListOld.contains(boletaDetalleListNewBoletaDetalle)) {
                    Boleta oldIdBoletaOfBoletaDetalleListNewBoletaDetalle = boletaDetalleListNewBoletaDetalle.getIdBoleta();
                    boletaDetalleListNewBoletaDetalle.setIdBoleta(boleta);
                    boletaDetalleListNewBoletaDetalle = em.merge(boletaDetalleListNewBoletaDetalle);
                    if (oldIdBoletaOfBoletaDetalleListNewBoletaDetalle != null && !oldIdBoletaOfBoletaDetalleListNewBoletaDetalle.equals(boleta)) {
                        oldIdBoletaOfBoletaDetalleListNewBoletaDetalle.getBoletaDetalleList().remove(boletaDetalleListNewBoletaDetalle);
                        oldIdBoletaOfBoletaDetalleListNewBoletaDetalle = em.merge(oldIdBoletaOfBoletaDetalleListNewBoletaDetalle);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = boleta.getIdBoleta();
                if (findBoleta(id) == null) {
                    throw new NonexistentEntityException("The boleta with id " + id + " no longer exists.");
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
            Boleta boleta;
            try {
                boleta = em.getReference(Boleta.class, id);
                boleta.getIdBoleta();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The boleta with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<BoletaDetalle> boletaDetalleListOrphanCheck = boleta.getBoletaDetalleList();
            for (BoletaDetalle boletaDetalleListOrphanCheckBoletaDetalle : boletaDetalleListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Boleta (" + boleta + ") cannot be destroyed since the BoletaDetalle " + boletaDetalleListOrphanCheckBoletaDetalle + " in its boletaDetalleList field has a non-nullable idBoleta field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Usuario idUsuario = boleta.getIdUsuario();
            if (idUsuario != null) {
                idUsuario.getBoletaList().remove(boleta);
                idUsuario = em.merge(idUsuario);
            }
            em.remove(boleta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Boleta> findBoletaEntities() {
        return findBoletaEntities(true, -1, -1);
    }

    public List<Boleta> findBoletaEntities(int maxResults, int firstResult) {
        return findBoletaEntities(false, maxResults, firstResult);
    }

    private List<Boleta> findBoletaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Boleta.class));
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

    public Boleta findBoleta(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Boleta.class, id);
        } finally {
            em.close();
        }
    }

    public int getBoletaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Boleta> rt = cq.from(Boleta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
