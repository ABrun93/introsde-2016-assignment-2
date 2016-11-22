package rest.server.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonFormat;

import rest.server.dao.LifeCoachDao;
import rest.server.model.Measure;

@Entity  // indicates that this class is an entity to persist in DB
@Table(name="Person") // to whate table must be persisted
@NamedQuery(name="Person.findAll", query="SELECT p FROM Person p")
@XmlRootElement
public class Person implements Serializable 
{
    private static final long serialVersionUID = 1L;
    
    @Id // defines this attributed as the one that identifies the entity
    @GeneratedValue(generator="sqlite_person")
    @TableGenerator(name="sqlite_person", table="sqlite_sequence",
			        pkColumnName="name", valueColumnName="seq",
			        pkColumnValue="Person")
    
    @Column(name="idPerson") // maps the following attribute to a column
    private int idPerson;
    
    @Column(name="lastname")
    private String lastname;
    
    @Column(name="firstname")
    private String firstname;
    
    // TODO: Use type Date
    // @Temporal(TemporalType.DATE)
    // @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name="birthdate")
    //private Date birthdate; 
    private String birthdate;
    
    // MappedBy must be equal to the name of the attribute in Measure that maps this relation
    @OneToMany(mappedBy="person", cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    private List<Measure> measure;
       
    public int getIdPerson() {
		return idPerson;
	}

	public void setIdPerson(int id) {
		this.idPerson = id;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

//	public String getBirthdate() {
//		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//		return df.format(this.birthdate);
//	}
	
	public String getBirthdate() {
		return birthdate;
	}

//	public void setBirthdate(String birthdate) throws ParseException {
//        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
//        this.birthdate = format.parse(birthdate);
//	}
	
	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}
	
	// Inherit elements
	@XmlElementWrapper(name = "healthProfile")
    public List<Measure> getMeasure() {
        return Measure.getLastMeasure(this.idPerson);
		//return measure;
    }
	
    public void setMeasure(List<Measure> measure) {
        this.measure = measure;
    }
    
    @XmlTransient
    public List<Measure> getAllMeasure() {
    	return measure;
    }
	
	// Database operations
	public static Person getPersonById(int personId) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        Person p = em.find(Person.class, personId);
        LifeCoachDao.instance.closeConnections(em);
        return p;
    }

	public static List<Person> getAll() {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        List<Person> list = em.createNamedQuery("Person.findAll", Person.class)
            .getResultList();
        LifeCoachDao.instance.closeConnections(em);
        return list;
    }

    public static Person savePerson(Person p) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(p);
        tx.commit();
        LifeCoachDao.instance.closeConnections(em);
        return p;
    } 

    public static Person updatePerson(Person p) {
        EntityManager em = LifeCoachDao.instance.createEntityManager(); 
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        p=em.merge(p);
        tx.commit();
        LifeCoachDao.instance.closeConnections(em);
        return p;
    }

    public static void removePerson(Person p) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        p=em.merge(p);
        em.remove(p);
        tx.commit();
        LifeCoachDao.instance.closeConnections(em);
    }
    
}
