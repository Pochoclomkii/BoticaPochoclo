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
import dto.Boleta;
import dto.Usuario;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author User
 */
public class UsuarioJpaController1 implements Serializable {

    public UsuarioJpaController1(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) throws PreexistingEntityException, Exception {
        if (usuario.getBoletaList() == null) {
            usuario.setBoletaList(new ArrayList<Boleta>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Boleta> attachedBoletaList = new ArrayList<Boleta>();
            for (Boleta boletaListBoletaToAttach : usuario.getBoletaList()) {
                boletaListBoletaToAttach = em.getReference(boletaListBoletaToAttach.getClass(), boletaListBoletaToAttach.getIdBoleta());
                attachedBoletaList.add(boletaListBoletaToAttach);
            }
            usuario.setBoletaList(attachedBoletaList);
            em.persist(usuario);
            for (Boleta boletaListBoleta : usuario.getBoletaList()) {
                Usuario oldIdUsuarioOfBoletaListBoleta = boletaListBoleta.getIdUsuario();
                boletaListBoleta.setIdUsuario(usuario);
                boletaListBoleta = em.merge(boletaListBoleta);
                if (oldIdUsuarioOfBoletaListBoleta != null) {
                    oldIdUsuarioOfBoletaListBoleta.getBoletaList().remove(boletaListBoleta);
                    oldIdUsuarioOfBoletaListBoleta = em.merge(oldIdUsuarioOfBoletaListBoleta);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUsuario(usuario.getCodiUsua()) != null) {
                throw new PreexistingEntityException("Usuario " + usuario + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getCodiUsua());
            List<Boleta> boletaListOld = persistentUsuario.getBoletaList();
            List<Boleta> boletaListNew = usuario.getBoletaList();
            List<String> illegalOrphanMessages = null;
            for (Boleta boletaListOldBoleta : boletaListOld) {
                if (!boletaListNew.contains(boletaListOldBoleta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Boleta " + boletaListOldBoleta + " since its idUsuario field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Boleta> attachedBoletaListNew = new ArrayList<Boleta>();
            for (Boleta boletaListNewBoletaToAttach : boletaListNew) {
                boletaListNewBoletaToAttach = em.getReference(boletaListNewBoletaToAttach.getClass(), boletaListNewBoletaToAttach.getIdBoleta());
                attachedBoletaListNew.add(boletaListNewBoletaToAttach);
            }
            boletaListNew = attachedBoletaListNew;
            usuario.setBoletaList(boletaListNew);
            usuario = em.merge(usuario);
            for (Boleta boletaListNewBoleta : boletaListNew) {
                if (!boletaListOld.contains(boletaListNewBoleta)) {
                    Usuario oldIdUsuarioOfBoletaListNewBoleta = boletaListNewBoleta.getIdUsuario();
                    boletaListNewBoleta.setIdUsuario(usuario);
                    boletaListNewBoleta = em.merge(boletaListNewBoleta);
                    if (oldIdUsuarioOfBoletaListNewBoleta != null && !oldIdUsuarioOfBoletaListNewBoleta.equals(usuario)) {
                        oldIdUsuarioOfBoletaListNewBoleta.getBoletaList().remove(boletaListNewBoleta);
                        oldIdUsuarioOfBoletaListNewBoleta = em.merge(oldIdUsuarioOfBoletaListNewBoleta);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usuario.getCodiUsua();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
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
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getCodiUsua();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Boleta> boletaListOrphanCheck = usuario.getBoletaList();
            for (Boleta boletaListOrphanCheckBoleta : boletaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Boleta " + boletaListOrphanCheckBoleta + " in its boletaList field has a non-nullable idUsuario field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
