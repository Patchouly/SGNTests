/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package JPA;

import JPA.exceptions.IllegalOrphanException;
import JPA.exceptions.NonexistentEntityException;
import JPA.exceptions.RollbackFailureException;
import entities.Clientes;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entities.Licencas;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.transaction.UserTransaction;

/**
 *
 * @author Pedro
 */
public class ClientesJpaController implements Serializable {

    public ClientesJpaController() {
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = JPAUtil.getEMF();

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Clientes clientes) throws RollbackFailureException, Exception {
        if (clientes.getLicencasList() == null) {
            clientes.setLicencasList(new ArrayList<Licencas>());
        }
        EntityManager em = null;
        try {
            utx.begin();
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

    public void edit(Clientes clientes) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
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
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
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
            utx.begin();
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
    
    public Clientes findClientesByCNPJ(String cnpj){
        EntityManager em = getEntityManager();
        try {
            Clientes cliente = (Clientes) em.createNativeQuery("select * from clientes "
                    + "where cnpj = '" + cnpj, Clientes.class).getSingleResult();
            return cliente;
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
    
    public Clientes findClientesByCPF(String cpf){
        EntityManager em = getEntityManager();
        try {
            Clientes cliente = (Clientes) em.createNativeQuery("select * from clientes "
                    + "where cpf = '" + cpf, Clientes.class).getSingleResult();
            return cliente;
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
    
}
