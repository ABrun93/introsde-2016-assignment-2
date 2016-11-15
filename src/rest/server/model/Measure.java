package rest.server.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import rest.server.dao.LifeCoachDao;
import rest.server.model.HealthProfile;

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
    @Column(name="type")
    private String type;
    // MappedBy must be equal to the name of the attribute in HealthProfile that maps this relation
    @OneToMany(mappedBy="measure")
    private List<HealthProfile> healthProfile;
       
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
    public List<HealthProfile> getHealthProfile() {
        return healthProfile;
    }
    public void setHealthProfile(List<HealthProfile> param) {
        this.healthProfile = param;
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
