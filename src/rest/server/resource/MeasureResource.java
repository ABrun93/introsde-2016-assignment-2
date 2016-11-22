package rest.server.resource;

import java.io.IOException;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import rest.server.model.Measure;
import rest.server.model.Person;

@Stateless // will work only inside a Java EE application
@LocalBean // will work only inside a Java EE application
public class MeasureResource {
	@Context
    UriInfo uriInfo;
    @Context
    Request request;
    int pId;	//pId from person
    int mId;	//mId from measure
    String type; //type from scopeType

    EntityManager entityManager; // only used if the application is deployed in a Java EE container

    public MeasureResource(UriInfo uriInfo, Request request, int pId, int mId, String type, EntityManager em) {
        this.uriInfo = uriInfo;
        this.request = request;
        this.pId = pId;
        this.mId = mId;
        this.type = type;
        this.entityManager = em;
    }
    
    public MeasureResource(UriInfo uriInfo, Request request, int pId, int mId, String type) {
        this.uriInfo = uriInfo;
        this.request = request;
        this.pId = pId;
        this.mId = mId;
        this.type = type;
    }

    public MeasureResource(UriInfo uriInfo, Request request, int pId, String type) {
        this.uriInfo = uriInfo;
        this.request = request;
        this.pId = pId;
        this.type = type;
    }
     
    @POST
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Measure newMeasure(Measure measure) throws IOException {
        System.out.println("Creating new measure...");            
        
        measure.setPerson(Person.getPersonById(pId));
                
        return Measure.saveMeasure(measure);
    }
    
    @DELETE
    public void deleteMeasure() {
        Measure c = getMeasureById(mId);
        if (c == null)
            throw new RuntimeException("Delete: Person with " + mId
                    + " not found");
        Measure.removeMeasure(c);
    }
    
    public Measure getMeasureById(int measureId) {
        System.out.println("Reading measure from DB with pId: "+measureId);

        // this will work within a Java EE container, where not DAO will be needed
        //Person person = entityManager.find(Person.class, personId); 

        Measure measure = Measure.getMeasureById(measureId);
        System.out.println("Person: "+measure.toString());
        return measure;
    }
    
	@GET
	@Produces({MediaType.TEXT_XML,  MediaType.APPLICATION_JSON , MediaType.APPLICATION_XML })
	public List<Measure> getHistory() {
	    if(mId != 0)
	    {
	    	return Measure.getMeasureByMidAndType(pId, mId, type);
	    }
	    else
	    {
	    	return Measure.getMeasureByPidAndType(pId, type);
	    }
	}
}
