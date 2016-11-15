package rest.server.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import rest.server.dao.LifeCoachDao;
import rest.server.model.Person;

@Entity  // indicates that this class is an entity to persist in DB
@Table(name="HealthProfile") // to whate table must be persisted
@NamedQuery(name="HealthProfile.findAll", query="SELECT p FROM HealthProfile p")
@XmlRootElement
public class HealthProfile implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id // defines this attributed as the one that identifies the entity
    @GeneratedValue
    @Column(name="id") // maps the following attribute to a column
    private int id;
    @Column(name="value")
    private int value;
    @Column(name="date")
    private String date;
    
    @ManyToOne
	@JoinColumn(name="idPerson", referencedColumnName="id")
	private Person person;
    
    // add below all the getters and setters of all the private attributes   
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String birthdate) {
		this.date = birthdate;
	}
	
	// Transient for JAXB to avoid and infinite loop on serialization
	@XmlTransient
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	// Database operations
	public static HealthProfile getHealthProfileById(int personId) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        HealthProfile p = em.find(HealthProfile.class, personId);
        LifeCoachDao.instance.closeConnections(em);
        return p;
    }

	public static List<HealthProfile> getAll() {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        List<HealthProfile> list = em.createNamedQuery("HealthProfile.findAll", HealthProfile.class)
            .getResultList();
        LifeCoachDao.instance.closeConnections(em);
        return list;
    }

    public static HealthProfile saveHealthProfile(HealthProfile p) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(p);
        tx.commit();
        LifeCoachDao.instance.closeConnections(em);
        return p;
    } 

    public static HealthProfile updateHealthProfile(HealthProfile p) {
        EntityManager em = LifeCoachDao.instance.createEntityManager(); 
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        p=em.merge(p);
        tx.commit();
        LifeCoachDao.instance.closeConnections(em);
        return p;
    }

    public static void removeHealthProfile(HealthProfile p) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        p=em.merge(p);
        em.remove(p);
        tx.commit();
        LifeCoachDao.instance.closeConnections(em);
    }
    
}
