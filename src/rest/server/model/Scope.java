package rest.server.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import rest.server.dao.LifeCoachDao;
import rest.server.model.Person;
import rest.server.model.Measure;
import rest.server.model.ScopeType;

@Entity  // indicates that this class is an entity to persist in DB
@Table(name="Scope") // to whate table must be persisted
@NamedQuery(name="Scope.findAll", query="SELECT p FROM Scope p")
@XmlRootElement
public class Scope implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id // defines this attributed as the one that identifies the entity
    @GeneratedValue
    @Column(name="id") // maps the following attribute to a column
    private int id;
        
    @ManyToOne
	@JoinColumn(name="idPerson", referencedColumnName="id")
	private Person person;
    
    @ManyToOne
   	@JoinColumn(name="idType", referencedColumnName="id", insertable = true, updatable = true)
   	private ScopeType scopeType;
    
    /*  Insert the history of measures in the rappresentation
     
	    @OneToMany(mappedBy="scope",cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	    private List<Measure> measure;
	*/
    
    @OneToOne
    @JoinColumn(name="idMeasure", referencedColumnName="id")
    private Measure measure;
    
    // add below all the getters and setters of all the private attributes   
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	// Transient for JAXB to avoid and infinite loop on serialization
	@XmlTransient
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}
	
	public ScopeType getScopeType() {
		return scopeType;
	}

	public void setScopeType(ScopeType scopeType) {
		this.scopeType = scopeType;
	}
	
	/* Insert the history of measures in the rappresentation

		@XmlElementWrapper(name = "history")
	    public List<Measure> getMeasure() {
	        return measure;
	    }
	    public void setMeasure(List<Measure> measure) {
	        this.measure = measure;
	    }
	*/
	
	public Measure getMeasure() {
        return measure;
    }
    public void setMeasure(Measure measure) {
        this.measure = measure;
    }

	// Database operations
	public static Scope getScopeById(int personId) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        Scope p = em.find(Scope.class, personId);
        LifeCoachDao.instance.closeConnections(em);
        return p;
    }

	public static List<Scope> getAll() {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        List<Scope> list = em.createNamedQuery("Scope.findAll", Scope.class)
            .getResultList();
        LifeCoachDao.instance.closeConnections(em);
        return list;
    }

    public static Scope saveScope(Scope p) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(p);
        tx.commit();
        LifeCoachDao.instance.closeConnections(em);
        return p;
    } 

    public static Scope updateScope(Scope p) {
        EntityManager em = LifeCoachDao.instance.createEntityManager(); 
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        p=em.merge(p);
        tx.commit();
        LifeCoachDao.instance.closeConnections(em);
        return p;
    }

    public static void removeScope(Scope p) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        p=em.merge(p);
        em.remove(p);
        tx.commit();
        LifeCoachDao.instance.closeConnections(em);
    }
    
}