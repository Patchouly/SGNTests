/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package JPA;

import JPA.exceptions.NonexistentEntityException;
import JPA.exceptions.PreexistingEntityException;
import JPA.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entities.Clientes;
import entities.Licencas;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Pedro
 */
public class LicencasJpaController implements Serializable {

    public LicencasJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Licencas licencas) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Clientes clienteId = licencas.getClienteId();
            if (clienteId != null) {
                clienteId = em.getReference(clienteId.getClass(), clienteId.getId());
                licencas.setClienteId(clienteId);
            }
            em.persist(licencas);
            if (clienteId != null) {
                clienteId.getLicencasList().add(licencas);
                clienteId = em.merge(clienteId);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findLicencas(licencas.getId()) != null) {
                throw new PreexistingEntityException("Licencas " + licencas + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Licencas licencas) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Licencas persistentLicencas = em.find(Licencas.class, licencas.getId());
            Clientes clienteIdOld = persistentLicencas.getClienteId();
            Clientes clienteIdNew = licencas.getClienteId();
            if (clienteIdNew != null) {
                clienteIdNew = em.getReference(clienteIdNew.getClass(), clienteIdNew.getId());
                licencas.setClienteId(clienteIdNew);
            }
            licencas = em.merge(licencas);
            if (clienteIdOld != null && !clienteIdOld.equals(clienteIdNew)) {
                clienteIdOld.getLicencasList().remove(licencas);
                clienteIdOld = em.merge(clienteIdOld);
            }
            if (clienteIdNew != null && !clienteIdNew.equals(clienteIdOld)) {
                clienteIdNew.getLicencasList().add(licencas);
                clienteIdNew = em.merge(clienteIdNew);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = licencas.getId();
                if (findLicencas(id) == null) {
                    throw new NonexistentEntityException("The licencas with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Licencas licencas;
            try {
                licencas = em.getReference(Licencas.class, id);
                licencas.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The licencas with id " + id + " no longer exists.", enfe);
            }
            Clientes clienteId = licencas.getClienteId();
            if (clienteId != null) {
                clienteId.getLicencasList().remove(licencas);
                clienteId = em.merge(clienteId);
            }
            em.remove(licencas);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Licencas> findLicencasEntities() {
        return findLicencasEntities(true, -1, -1);
    }

    public List<Licencas> findLicencasEntities(int maxResults, int firstResult) {
        return findLicencasEntities(false, maxResults, firstResult);
    }

    private List<Licencas> findLicencasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Licencas.class));
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

    public Licencas findLicencas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Licencas.class, id);
        } finally {
            em.close();
        }
    }

    public int getLicencasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Licencas> rt = cq.from(Licencas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
