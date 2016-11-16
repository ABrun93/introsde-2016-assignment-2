package rest.server.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import rest.server.dao.LifeCoachDao;
import rest.server.model.Scope;

@Entity  // indicates that this class is an entity to persist in DB
@Table(name="ScopeType") // to whate table must be persisted
@NamedQuery(name="ScopeType.findAll", query="SELECT p FROM ScopeType p")
@XmlRootElement
public class ScopeType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id // defines this attributed as the one that identifies the entity
    @GeneratedValue
    @Column(name="id") // maps the following attribute to a column
    private int id;
    @Column(name="type")
    private String type;
    // MappedBy must be equal to the name of the attribute in Measure that maps this relation
    @OneToMany(mappedBy="scopeType")
    private List<Scope> scope;
       
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	// Transient for JAXB to avoid and infinite loop on serialization
	@XmlTransient
    public List<Scope> getScope() {
        return scope;
    }
    public void setScope(List<Scope> scope) {
        this.scope = scope;
    }
	
	// Database operations
	public static ScopeType getScopeTypeById(int personId) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        ScopeType p = em.find(ScopeType.class, personId);
        LifeCoachDao.instance.closeConnections(em);
        return p;
    }

	public static List<ScopeType> getAll() {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        List<ScopeType> list = em.createNamedQuery("ScopeType.findAll", ScopeType.class)
            .getResultList();
        LifeCoachDao.instance.closeConnections(em);
        return list;
    }

    public static ScopeType saveScopeType(ScopeType p) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(p);
        tx.commit();
        LifeCoachDao.instance.closeConnections(em);
        return p;
    } 

    public static ScopeType updateScopeType(ScopeType p) {
        EntityManager em = LifeCoachDao.instance.createEntityManager(); 
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        p=em.merge(p);
        tx.commit();
        LifeCoachDao.instance.closeConnections(em);
        return p;
    }

    public static void removeScopeType(ScopeType p) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        p=em.merge(p);
        em.remove(p);
        tx.commit();
        LifeCoachDao.instance.closeConnections(em);
    }
    
}
