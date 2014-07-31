/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DAO;

import DAO.exceptions.IllegalOrphanException;
import DAO.exceptions.NonexistentEntityException;
import DAO.exceptions.PreexistingEntityException;
import DAO.exceptions.RollbackFailureException;
import entitys.Clientes;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entitys.Licencas;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Pedro
 */
public class ClientesJpaController implements Serializable {

    public ClientesJpaController() {
        this.emf = emf;
    }
    private EntityManagerFactory emf = JPAUtil.getEMF();

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Clientes clientes) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (clientes.getLicencasList() == null) {
            clientes.setLicencasList(new ArrayList<Licencas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            List<Licencas> attachedLicencasList = new ArrayList<Licencas>();
            for (Licencas licencasListLicencasToAttach : clientes.getLicencasList()) {
                licencasListLicencasToAttach = em.getReference(licencasListLicencasToAttach.getClass(), licencasListLicencasToAttach.getId());
                attachedLicencasList.add(licencasListLicencasToAttach);
            }
            clientes.setLicencasList(attachedLicencasList);
            em.persist(clientes);
            for (Licencas licencasListLicencas : clientes.getLicencasList()) {
                Clientes oldClienteIdOfLicencasListLicencas = licencasListLicencas.getClienteId();
                licencasListLicencas.setClienteId(clientes);
                licencasListLicencas = em.merge(licencasListLicencas);
                if (oldClienteIdOfLicencasListLicencas != null) {
                    oldClienteIdOfLicencasListLicencas.getLicencasList().remove(licencasListLicencas);
                    oldClienteIdOfLicencasListLicencas = em.merge(oldClienteIdOfLicencasListLicencas);
                }
            }
        } catch (Exception ex) {
            if (findClientes(clientes.getId()) != null) {
                throw new PreexistingEntityException("Clientes " + clientes + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Clientes clientes) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            Clientes persistentClientes = em.find(Clientes.class, clientes.getId());
            List<Licencas> licencasListOld = persistentClientes.getLicencasList();
            List<Licencas> licencasListNew = clientes.getLicencasList();
            List<String> illegalOrphanMessages = null;
            for (Licencas licencasListOldLicencas : licencasListOld) {
                if (!licencasListNew.contains(licencasListOldLicencas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Licencas " + licencasListOldLicencas + " since its clienteId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Licencas> attachedLicencasListNew = new ArrayList<Licencas>();
            for (Licencas licencasListNewLicencasToAttach : licencasListNew) {
                licencasListNewLicencasToAttach = em.getReference(licencasListNewLicencasToAttach.getClass(), licencasListNewLicencasToAttach.getId());
                attachedLicencasListNew.add(licencasListNewLicencasToAttach);
            }
            licencasListNew = attachedLicencasListNew;
            clientes.setLicencasList(licencasListNew);
            clientes = em.merge(clientes);
            for (Licencas licencasListNewLicencas : licencasListNew) {
                if (!licencasListOld.contains(licencasListNewLicencas)) {
                    Clientes oldClienteIdOfLicencasListNewLicencas = licencasListNewLicencas.getClienteId();
                    licencasListNewLicencas.setClienteId(clientes);
                    licencasListNewLicencas = em.merge(licencasListNewLicencas);
                    if (oldClienteIdOfLicencasListNewLicencas != null && !oldClienteIdOfLicencasListNewLicencas.equals(clientes)) {
                        oldClienteIdOfLicencasListNewLicencas.getLicencasList().remove(licencasListNewLicencas);
                        oldClienteIdOfLicencasListNewLicencas = em.merge(oldClienteIdOfLicencasListNewLicencas);
                    }
                }
            }
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = clientes.getId();
                if (findClientes(id) == null) {
                    throw new NonexistentEntityException("The clientes with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            Clientes clientes;
            try {
                clientes = em.getReference(Clientes.class, id);
                clientes.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The clientes with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Licencas> licencasListOrphanCheck = clientes.getLicencasList();
            for (Licencas licencasListOrphanCheckLicencas : licencasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Clientes (" + clientes + ") cannot be destroyed since the Licencas " + licencasListOrphanCheckLicencas + " in its licencasList field has a non-nullable clienteId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(clientes);
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Clientes> findClientesEntities() {
        return findClientesEntities(true, -1, -1);
    }

    public List<Clientes> findClientesEntities(int maxResults, int firstResult) {
        return findClientesEntities(false, maxResults, firstResult);
    }

    private List<Clientes> findClientesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Clientes.class));
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

    public Clientes findClientes(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Clientes.class, id);
        } finally {
            em.close();
        }
    }

    public int getClientesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Clientes> rt = cq.from(Clientes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
