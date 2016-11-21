package rest.server.resource;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.PersistenceUnit;
import javax.ws.rs.GET;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import rest.server.model.Measure;

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
