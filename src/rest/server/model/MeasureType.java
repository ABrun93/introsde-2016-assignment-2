package rest.server.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import rest.server.dao.LifeCoachDao;
import rest.server.model.Measure;

@Entity  // indicates that this class is an entity to persist in DB
@Table(name="MeasureType") // to whate table must be persisted
@NamedQuery(name="MeasureType.findAll", query="SELECT p FROM MeasureType p")
@XmlRootElement
public class MeasureType implements Serializable 
{
    private static final long serialVersionUID = 1L;
    
    @Id // defines this attributed as the one that identifies the entity
    @GeneratedValue(generator="sqlite_measuretype")
	@TableGenerator(name="sqlite_measuretype", table="sqlite_sequence",
				    pkColumnName="name", valueColumnName="seq",
				    pkColumnValue="MeasureType")
    
    @Column(name="idMeasureType") // maps the following attribute to a column
    private int idMeasureType;
    
    @Column(name="type")
    private String type;
    
    // MappedBy must be equal to the name of the attribute in Measure that maps this relation
//    @OneToMany(mappedBy="measureType")
//    private List<Measure> measure;
       
    public int getIdMeasureType() {
		return idMeasureType;
	}

	public void setIdMeasureType(int id) {
		this.idMeasureType = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	// Transient for JAXB to avoid and infinite loop on serialization
//	@XmlTransient
//    public List<Measure> getMeasure() {
//        return measure;
//    }
//    public void setMeasure(List<Measure> measure) {
//        this.measure = measure;
//    }
	
	// Database operations
	public static MeasureType getMeasureTypeById(int personId) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        MeasureType p = em.find(MeasureType.class, personId);
        LifeCoachDao.instance.closeConnections(em);
        return p;
    }

	public static List<MeasureType> getAll() {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        List<MeasureType> list = em.createNamedQuery("MeasureType.findAll", MeasureType.class)
            .getResultList();
        LifeCoachDao.instance.closeConnections(em);
        return list;
    }

    public static MeasureType saveMeasureType(MeasureType p) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(p);
        tx.commit();
        LifeCoachDao.instance.closeConnections(em);
        return p;
    } 

    public static MeasureType updateMeasureType(MeasureType p) {
        EntityManager em = LifeCoachDao.instance.createEntityManager(); 
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        p=em.merge(p);
        tx.commit();
        LifeCoachDao.instance.closeConnections(em);
        return p;
    }

    public static void removeMeasureType(MeasureType p) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        p=em.merge(p);
        em.remove(p);
        tx.commit();
        LifeCoachDao.instance.closeConnections(em);
    }
    
}
