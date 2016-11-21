package rest.server.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.annotation.JsonFormat;

import rest.server.dao.LifeCoachDao;
import rest.server.model.Person;
import rest.server.model.MeasureType;
import rest.server.aux.XmlDateAdapter;

@Entity  // indicates that this class is an entity to persist in DB
@Table(name="Measure") // to whate table must be persisted
@NamedQuery(name="Measure.findAll", query="SELECT p FROM Measure p")
@XmlRootElement
public class Measure implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id // defines this attributed as the one that identifies the entity
    // @GeneratedValue
    @Column(name="idMeasure") // maps the following attribute to a column
    private int idMeasure;
    @Column(name="value")
    private float value;
    @Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    // @XmlJavaTypeAdapter(XmlDateAdapter.class)
    @Column(name="date")
    private Date date;
      
    @ManyToOne
   	@JoinColumn(name="idPerson", referencedColumnName="idPerson", insertable = true, updatable = true)
   	private Person person;
    @ManyToOne
   	@JoinColumn(name="idMeasureType", referencedColumnName="idMeasureType", insertable = true, updatable = true)
   	private MeasureType measureType;
    

    
    // add below all the getters and setters of all the private attributes   
    public int getIdMeasure() {
		return idMeasure;
	}

	public void setIdMeasure(int id) {
		this.idMeasure = id;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	// Transient for JAXB to avoid and infinite loop on serialization
	@XmlTransient
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}
	
	public MeasureType getMeasureType() {
		return measureType;
	}

	public void setMeasureType(MeasureType measureType) {
		this.measureType = measureType;
	}
	
	public static List<Measure> getLastMeasure(int id)
	{
		EntityManager em = LifeCoachDao.instance.createEntityManager();

		List<Measure> measure = null;
		List<Measure> tmp = null;
		List<MeasureType> types = em.createQuery("SELECT m FROM MeasureType m", MeasureType.class).getResultList();
		
		for(MeasureType type : types)
		{
			int idT = type.getIdMeasureType();
				
			String query = "SELECT mA FROM Measure mA WHERE mA.idPerson = " + id + " AND mA.idMeasureType = " + idT
					+" AND mA.date = (SELECT MAX(mB.date) FROM Measure mB WHERE mB.idPerson = " + id + " AND mB.idMeasureType = " + idT + ")";
		
			System.out.println(query);
			
			tmp = em.createQuery((query), Measure.class).getResultList();
		
			System.out.println("Qui");
			
			for(int i = 0; i < tmp.size(); i++)
			{
				if(measure == null)
				{
					measure = tmp;
				}
				else
				{
					measure.add(tmp.get(i));					
				}
			}
		}
		
		LifeCoachDao.instance.closeConnections(em);
		return measure;
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
    
    // Special methods
	public static List<Measure> getMeasureByPidAndType(int pId, String type) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
				
		System.out.println("SELECT m FROM Measure m WHERE m.idPerson = " + pId 
				+ " AND m.idMeasureType = (SELECT mT.idMeasureType FROM MeasureType mT WHERE mT.type = \"" + type + "\")");
		List<Measure> measure = em.createQuery("SELECT m FROM Measure m WHERE m.idPerson = " + pId 
				+ " AND m.idMeasureType = (SELECT mT.idMeasureType FROM MeasureType mT WHERE mT.type = \"" + type + "\")", Measure.class).getResultList();
		
		LifeCoachDao.instance.closeConnections(em);
		return measure;
	}

	public static List<Measure> getMeasureByMidAndType(int pId, int mId, String type)
	{
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		
		List<Measure> measure = null;
		
//		System.out.println("Qui!");
//		
//		//Query for this request
//		List<Scope> scope = em.createQuery("SELECT l FROM Scope l WHERE l.idPerson = \"" + pId + "\" AND l.idType = (SELECT id FROM ScopeType m WHERE m.type =\"" + type + "\")"
//				, Scope.class).getResultList();
//		
//		System.out.println("SELECT l FROM Scope l WHERE l.idPerson = \"" + pId + "\" AND l.idType = (SELECT id FROM ScopeType m WHERE m.type =\"" + type + "\")");
//			
//		String idScope = scope.get(0).getId();
//		
//		System.out.println("idScope: " + idScope);
//		
//		List<Measure> measure = em.createQuery("SELECT l FROM Measure l WHERE l.idScope = " + idScope + " AND l.id = \"" + mId + "\""
//				, Measure.class).getResultList();
//		
//		System.out.println("SELECT l FROM Measure l WHERE l.idScope = " + idScope + " AND l.id = \"" + mId + "\"");

		LifeCoachDao.instance.closeConnections(em);
		return measure;
	}
    
}
