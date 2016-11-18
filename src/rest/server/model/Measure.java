package rest.server.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import rest.server.dao.LifeCoachDao;
import rest.server.model.Scope;

@Entity  // indicates that this class is an entity to persist in DB
@Table(name="Measure") // to whate table must be persisted
@NamedQuery(name="Measure.findAll", query="SELECT p FROM Measure p")
@XmlRootElement
public class Measure implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id // defines this attributed as the one that identifies the entity
    @GeneratedValue
    @Column(name="id") // maps the following attribute to a column
    private int id;
    @Column(name="value")
    private float value;
    @Column(name="date")
    private String date;
      
    @ManyToOne
   	@JoinColumn(name="idScope", referencedColumnName="id", insertable = true, updatable = true)
   	private Scope scope;
    
    // add below all the getters and setters of all the private attributes   
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	// Transient for JAXB to avoid and infinite loop on serialization
	@XmlTransient
	public Scope getScope() {
		return scope;
	}

	public void setScope(Scope scope) {
		this.scope = scope;
	}

	// Database operations
	public static Measure getMeasureById(int personId) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        Measure p = em.find(Measure.class, personId);
        LifeCoachDao.instance.closeConnections(em);
        return p;
    }

	public static List<Measure> getAll() {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        List<Measure> list = em.createNamedQuery("Measure.findAll", Measure.class)
            .getResultList();
        LifeCoachDao.instance.closeConnections(em);
        return list;
    }

    public static Measure saveMeasure(Measure p) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(p);
        tx.commit();
        LifeCoachDao.instance.closeConnections(em);
        return p;
    } 

    public static Measure updateMeasure(Measure p) {
        EntityManager em = LifeCoachDao.instance.createEntityManager(); 
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        p=em.merge(p);
        tx.commit();
        LifeCoachDao.instance.closeConnections(em);
        return p;
    }

    public static void removeMeasure(Measure p) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        p=em.merge(p);
        em.remove(p);
        tx.commit();
        LifeCoachDao.instance.closeConnections(em);
    }
    
}