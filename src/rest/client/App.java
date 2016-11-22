package rest.client;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;    
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.glassfish.jersey.client.ClientConfig;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class App 
{
	// URI of server
	private static final String URI = "https://introsde-2016-assignment-2.herokuapp.com/sdelab/";
	// private static final String URI = "http://localhost:5900/sdelab/";
	
	// Files of log
	static final String LOG_XML = "client-server-xml.log";
	static final String LOG_JSON = "client-server-json.log";
	static String logXml = "";
	static String logJson = "";
	
	// TODO Dynamic
	static int first_person_id = 1;
	static int last_person_id = 3;
	
	// TODO Dynamic
	static int measure_id = 1;
	static String measureType = "height";
		
    public static void main(String[] args) throws IOException 
    {    	
    	ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget service = client.target(getBaseURI());

        System.out.println("Url server: " + App.URI);
        logXml += "Url server: " + App.URI;
        logJson += "Url server: " + App.URI;
        
        App a = new App();
        
        a.execRequest1(service);
        
        a.execRequest2(service, first_person_id);
        
        a.execRequest3(service, first_person_id);
        
        a.execRequest4(service);

        a.execRequest5(service);
        
        a.execRequest6(service);
        
        a.execRequest7(service, first_person_id, last_person_id);
        
        a.execRequest8(service, measure_id, measureType);
        
        a.execRequest9(service, measureType);
        
        writeLogXml(logXml);
        writeLogJson(logJson);
    }

    private static URI getBaseURI() {
        return UriBuilder.fromUri(URI).build();
    }
    
    private static void writeLogXml(String text) throws IOException
    {
    	FileWriter logxml;
    	
    	logxml = new FileWriter(LOG_XML);
		    	
    	BufferedWriter b;
		b = new BufferedWriter(logxml);
		b.write(text);
		b.flush();
    }
    
    private static void writeLogJson(String text) throws IOException
    {
    	FileWriter logjson;
    	
    	logjson = new FileWriter(LOG_JSON);
		    	
    	BufferedWriter b;
		b=new BufferedWriter (logjson);
		b.write(text);
		b.flush();
    }
    
    private int countOccourence(String text, String token)
	{
		int counter = 0;
		int lastIndex = 0;

		while(lastIndex != -1){

			lastIndex = text.indexOf(token,lastIndex);

			if(lastIndex != -1){
				counter ++;
				lastIndex += token.length();
			}
		}

		return counter;
	}
    
    private String countOcc(String body, String token, int cond) 
	{
		//Count Person in the response, if there are much than 2 people the result is OK, else the result is ERROR
		String result = "";

		int counter = countOccourence(body, token);
		
		if(counter > cond)
			result = "OK";
		else
			result = "ERROR";
		
		return result;
	}
           
    private void execRequest1(WebTarget service) throws IOException
    {
    	int status;
    	Response response;
		String req, head, tmp, body, result;
    	
		req = "person";
		head = "Request 1: GET /" + req;
		
		body = "";
		tmp = head + " Accept:Application/xml Content-type:Application/xml";
    	
    	System.out.println("===============================================");
    	logXml += "===============================================\n";
    	logJson += "===============================================\n";
    	System.out.println(tmp);    	
    	logXml += tmp + "\n";
    	
		response = service.path(req).request().accept(MediaType.APPLICATION_XML).get();
		status = response.getStatus();
		body = response.readEntity(String.class);

		result = countOcc(body, "firstname", 2);
		
		System.out.println("=> Result: " + result);
		logXml += "=> Result: " + result + "\n";
		System.out.println("=> HTTP Status: " + status);
		logXml += "=> HTTP Status: " + status + "\n";
		System.out.println(prettyFormat(body));
		logXml += prettyFormat(body) + "\n";
		
		/* ----- */
		
		tmp = "\n" + head + " Accept:Application/json Content-type:Application/json";
		
		System.out.println(tmp);
    	logJson += tmp + "\n";
    	
		response = service.path(req).request().accept(MediaType.APPLICATION_JSON).get();
		status = response.getStatus();
		body = response.readEntity(String.class);

		result = countOcc(body, "firstname", 2);
		
		System.out.println("=> Result: " + result);
    	logJson += "=> Result: " + result + "\n";
		System.out.println("=> HTTP Status: " + status);
    	logJson += "=> HTTP Status: " + status + "\n";
		System.out.println(prettyJson(body));
    	logJson += prettyJson(body) + "\n";
    	
		System.out.println("===============================================");
		logXml += "===============================================\n";
    	logJson += "===============================================\n";
    }
    
    private void execRequest2(WebTarget service, int first_person_id) throws IOException
    {
    	int status;
    	Response response;
		String req, head, tmp, body, result;
    	
		req = "person/" + first_person_id;
    	head = "Request 2: GET /" + req;
		
		body = "";
		tmp = head + " Accept:Application/xml Content-type:Application/xml";
    	
    	System.out.println("===============================================");
    	logXml += "===============================================\n";
    	logJson += "===============================================\n";
    	System.out.println(tmp);
    	logXml += tmp + "\n";
    	
		response = service.path(req).request().accept(MediaType.APPLICATION_XML).get();
		status = response.getStatus();
		body = response.readEntity(String.class);

		result = (status == 200 || status == 202 ? "OK" : "ERROR");
		
		System.out.println("=> Result: " + result);
		logXml += "=> Result: " + result + "\n";
		System.out.println("=> HTTP Status: " + status);
		logXml += "=> HTTP Status: " + status + "\n";
		System.out.println(prettyFormat(body));
		logXml += prettyFormat(body) + "\n";
		
		/* ----- */
		
		tmp = "\n" + head + " Accept:Application/json Content-type:Application/json";
		
		System.out.println(tmp);
    	logJson += tmp + "\n";
    	
		response = service.path(req).request().accept(MediaType.APPLICATION_JSON).get();
		status = response.getStatus();
		body = response.readEntity(String.class);

		result = (status == 200 || status == 202 ? "OK" : "ERROR");
		
		System.out.println("=> Result: " + result);
    	logJson += "=> Result: " + result + "\n";
		System.out.println("=> HTTP Status: " + status);
    	logJson += "=> HTTP Status: " + status + "\n";
		System.out.println(prettyJson(body));
    	logJson += prettyJson(body) + "\n";
    	
		System.out.println("===============================================");
		logXml += "===============================================\n";
    	logJson += "===============================================\n";
    }
    
    private void execRequest3(WebTarget service, int first_person_id) throws IOException
    {
    	int status;
    	Response response;
		String req, head, tmp, body, result;
		
    	req = "person/" + first_person_id;
    	head = "Request 3: PUT /" + req;
    	
    	String req_body_xml = "<person>"
				+"<idPerson>1</idPerson>"
				+"<firstname>Nuovo</firstname>"	
				+"<lastname>Fiorese</lastname>"
				+"<birthdate>1993-06-15</birthdate>"
				+"</person>";
    	
    	String req_body_json = "{"
				+"\"idPerson\": 1,"
				+"\"firstname\": \"Nuovo\","
				+"\"lastname\": \"Fiorese\","
				+"\"birthdate\": \"1993-06-15\""
				+"}";

		body = "";
		tmp = head + " Accept:Application/xml Content-type:Application/xml";
    	
    	System.out.println("===============================================");
    	logXml += "===============================================\n";
    	logJson += "===============================================\n";
    	System.out.println(tmp);
    	logXml += tmp + "\n";
    	
		response = service.path(req).request().accept(MediaType.APPLICATION_XML).put(Entity.xml(req_body_xml));
		status = response.getStatus();
		body = response.readEntity(String.class);

		result = (status == 200 || status == 201 || status == 202 ? "OK" : "ERROR");
		
		System.out.println("=> Result: " + result);
		logXml += "=> Result: " + result + "\n";
		System.out.println("=> HTTP Status: " + status);
		logXml += "=> HTTP Status: " + status + "\n";
		
		/* ----- */		

		tmp = "\n" + head + " Accept:Application/json Content-type:Application/json";
		
		System.out.println(tmp);
    	logJson += tmp + "\n";
		
		response = service.path(req).request().accept(MediaType.APPLICATION_JSON).put(Entity.json(req_body_json));
		status = response.getStatus();
		body = response.readEntity(String.class);
		
		result = (status == 200 || status == 201 || status == 202 ? "OK" : "ERROR");
		
		System.out.println("=> Result: " + result);
    	logJson += "=> Result: " + result + "\n";
		System.out.println("=> HTTP Status: " + status);
    	logJson += "=> HTTP Status: " + status + "\n";
		    	
		System.out.println("===============================================");
		logXml += "===============================================\n";
    	logJson += "===============================================\n";
    }
    
    private void execRequest4(WebTarget service) throws IOException
    {
    	int status;
    	Response response;
		String req, head, tmp, body, result;
		
    	req = "person/";
    	head = "Request 4: POST /" + req;
    	
    	String req_body_xml ="<person>"
    			+"<idPerson>7</idPerson>"
    			+"<firstname>Chuck</firstname>"
    			+"<lastname>Norris</lastname>"
    			+"<birthdate>1945-01-01</birthdate>"
    			+"<healthProfile>"
    			+"		<measure>"
    			+"				<idMeasure>7</idMeasure>"
    			+"				<value>1.72</value>"
    			+"				<date>1978-09-02</date>"
    			+"				<measureType>"
    			+"						<idMeasureType>1</idMeasureType>"
    			+"						<type>height</type>"
    			+"				</measureType>"
    			+"		</measure>"
    			+"		<measure>"
    			+"				<idMeasure>8</idMeasure>"
    			+"				<value>75.0</value>"
    			+"				<date>1978-09-02</date>"
    			+"				<measureType>"
    			+"						<idMeasureType>2</idMeasureType>"
    			+"						<type>weight</type>"
    			+"				</measureType>"   
    			+"		</measure>"
    			+"</healthProfile>"
    			+"</person>";
    	
    	String req_body_json = "{"
    			+"\"idPerson\": 8,"
    			+"\"firstname\": \"Chuck\","
    			+"\"lastname\": \"Norris\","
    			+"\"birthdate\": \"1945-01-01\","
    			+"\"measure\":" 
    			+"["
    			+"		{"
    			+"				\"idMeasure\": 9,"
    			+"				\"measureType\": "
    			+" 				{"
				+"						\"idMeasureType\": 1,"
				+"			    		\"type\": \"height\""
				+"				},"
				+"				\"value\": 1.72,"
				+"				\"date\": \"1978-09-02\""
				+"		},"
				+"		{"
    			+"				\"idMeasure\": 10,"
    			+"				\"measureType\": "
    			+" 				{"
				+"						\"idMeasureType\": 2,"
				+"			    		\"type\": \"weight\""
				+"				},"
				+"				\"value\": 78.9,"
				+"				\"date\": \"1978-09-02\""
				+"		}"
				+"]"
    			+"}";
		
    			body = "";
    			tmp = head + " Accept:Application/xml Content-type:Application/xml";
    	    	
    	    	System.out.println("===============================================");
    	    	logXml += "===============================================\n";
    	    	logJson += "===============================================\n";
    	    	System.out.println(tmp);
    	    	logXml += tmp + "\n";
    	    	    	    	
    			response = service.path(req).request().accept(MediaType.APPLICATION_XML).post(Entity.xml(req_body_xml));
    			status = response.getStatus();
    			body = response.readEntity(String.class);

    			result = (status == 200 || status == 201 || status == 202 ? "OK" : "ERROR");
    			
    			System.out.println("=> Result: " + result);
    			logXml += "=> Result: " + result + "\n";
    			System.out.println("=> HTTP Status: " + status);
    			logXml += "=> HTTP Status: " + status + "\n";
    			System.out.println(prettyFormat(body));
    			logXml += prettyFormat(body) + "\n";
    			
    			/* ----- */		

    			tmp = "\n" + head + " Accept:Application/json Content-type:Application/json";
    			
    			System.out.println(tmp);
    	    	logJson += tmp + "\n";
    			
    			response = service.path(req).request().accept(MediaType.APPLICATION_JSON).post(Entity.json(req_body_json));
    			status = response.getStatus();
    			body = response.readEntity(String.class);
    			
    			result = (status == 200 || status == 201 || status == 202 ? "OK" : "ERROR");
    			
    			System.out.println("=> Result: " + result);
    	    	logJson += "=> Result: " + result + "\n";
    			System.out.println("=> HTTP Status: " + status);
    	    	logJson += "=> HTTP Status: " + status + "\n";
    			System.out.println(prettyJson(body));
    	    	logJson += prettyJson(body) + "\n";
    	    	
    			System.out.println("===============================================");
    			logXml += "===============================================\n";
    	    	logJson += "===============================================\n";
    }
    
    private void execRequest5(WebTarget service) throws IOException
    {
    	int status;
    	Response response;
		String req, reqId, head, tmp, body, result;
		
    	req = "person/";
    	reqId = "7";
    	head = "Request 5: DELETE /" + req;
    	
    	body = "";
		tmp = head + reqId +" Accept:Application/xml Content-type:Application/xml";
    	
    	System.out.println("===============================================");
    	logXml += "===============================================\n";
    	logJson += "===============================================\n";
    	System.out.println(tmp);
    	logXml += tmp + "\n";
    	logJson += tmp + "\n";
    	
		response = service.path(req+reqId).request().accept(MediaType.APPLICATION_XML).delete();
		status = response.getStatus();
		body = response.readEntity(String.class);

		result = (status == 200 || status == 204 || status == 404 ? "OK" : "ERROR");
		
		System.out.println("=> Result: " + result);
		logXml += "=> Result: " + result + "\n";
		System.out.println("=> HTTP Status: " + status);
		logXml += "=> HTTP Status: " + status + "\n";
    			
		/* ----- */		
		
		reqId = "8";
		
		tmp = "\n" + head + reqId + " Accept:Application/json Content-type:Application/json";
		
		System.out.println(tmp);
    	logJson += tmp + "\n";
		
		response = service.path(req+reqId).request().accept(MediaType.APPLICATION_JSON).delete();
		status = response.getStatus();
		body = response.readEntity(String.class);
		
		result = (status == 200 || status == 204 || status == 404 ? "OK" : "ERROR");
		
		System.out.println("=> Result: " + result);
    	logJson += "=> Result: " + result + "\n";
		System.out.println("=> HTTP Status: " + status);
    	logJson += "=> HTTP Status: " + status + "\n";
    	
		System.out.println("===============================================");
		logXml += "===============================================\n";
    	logJson += "===============================================\n";
    }
    
    private void execRequest6(WebTarget service) throws IOException
    {
    	int status;
    	Response response;
		String req, head, tmp, body, result;
		
    	req = "measureTypes";
    	head = "Request 6: GET /" + req;
    	
    	body = "";
		tmp = head +" Accept:Application/xml Content-type:Application/xml";
    	
    	System.out.println("===============================================");
    	logXml += "===============================================\n";
    	logJson += "===============================================\n";
    	System.out.println(tmp);
    	logXml += tmp + "\n";
    	
		response = service.path(req).request().accept(MediaType.APPLICATION_XML).get();
		status = response.getStatus();
		body = response.readEntity(String.class);

		result = countOcc(body, "type", 2);
		
		System.out.println("=> Result: " + result);
		logXml += "=> Result: " + result + "\n";
		System.out.println("=> HTTP Status: " + status);
		logXml += "=> HTTP Status: " + status + "\n";
		System.out.println(prettyFormat(body));
		logXml += prettyFormat(body) + "\n";
		
		/* ----- */		
			
		tmp = "\n" + head + " Accept:Application/json Content-type:Application/json";
		
		System.out.println(tmp);
    	logJson += tmp + "\n";
		
		response = service.path(req).request().accept(MediaType.APPLICATION_JSON).get();
		status = response.getStatus();
		body = response.readEntity(String.class);
		
		result = countOcc(body, "type", 2);
		
		System.out.println("=> Result: " + result);
    	logJson += "=> Result: " + result + "\n";
		System.out.println("=> HTTP Status: " + status);
    	logJson += "=> HTTP Status: " + status + "\n";
		System.out.println(prettyJson(body));
    	logJson += prettyJson(body) + "\n";
    	
		System.out.println("===============================================");
		logXml += "===============================================\n";
    	logJson += "===============================================\n";
    }
    
    private void execRequest7(WebTarget service, int first_person_id, int last_person_id) throws IOException
    {
    	int status;
    	Response response;
		String req, reqF, reqL, head, headTmp, body, result;
		String[] types = {"height", "weight", "steps", "bloodpressure"};	// TODO Dynamic
		
		reqF = "person/" + first_person_id;
    	reqL = "person/" + last_person_id;
		head = "Request 7: GET /person/{idPerson}/{measureType}";
    	
		status = 500;
    	body = "";    	
    	headTmp = head +" Accept:Application/xml Content-type:Application/xml";
    	
    	System.out.println("===============================================");
    	logXml += "===============================================\n";
    	logJson += "===============================================\n";
    	System.out.println(headTmp);
    	logXml += headTmp + "\n";
    	
    	for(String type : types)
    	{    		   		
    		req = reqF + "/" + type;
    		
    		response = service.path(reqF).request().accept(MediaType.APPLICATION_XML).get();
			status = response.getStatus();
			
			body += response.readEntity(String.class);
    	}
    	
    	result = countOcc(body, "value", 1);
		    	
		System.out.println("=> Result: " + result);
		logXml += "=> Result: " + result + "\n";
		System.out.println("=> HTTP Status: " + status);
		logXml += "=> HTTP Status: " + status + "\n";
		System.out.println(body);
		logXml += body + "\n";
		
		/* ----- */
		
		body = "";	
		headTmp = "\n" + head + " Accept:Application/json Content-type:Application/json";
		
		System.out.println(headTmp);
    	logJson += headTmp + "\n";
		
		for(String type : types)
    	{    			
			req = reqL + "/" + type;
			
			response = service.path(reqL).request().accept(MediaType.APPLICATION_JSON).get();
			status = response.getStatus();
			
			body += response.readEntity(String.class);
    	}
		
		result = countOcc(body, "value", 1);
		
		System.out.println("=> Result: " + result);
    	logJson += "=> Result: " + result + "\n";
		System.out.println("=> HTTP Status: " + status);
    	logJson += "=> HTTP Status: " + status + "\n";
		System.out.println(prettyJson(body));
    	logJson += prettyJson(body) + "\n";
    	
		System.out.println("===============================================");
		logXml += "===============================================\n";
    	logJson += "===============================================\n";
    }
    
    private void execRequest8(WebTarget service, int measure_id, String measureType) throws IOException
    {
    	int status;
    	Response response;
		String req, head, tmp, body, result;
		
		req = "person/1/" + measureType + "/" + measure_id;
    	head = "Request 8: GET /" + req;
    	
    	body = "";
		tmp = head +" Accept:Application/xml Content-type:Application/xml";
    	
    	System.out.println("===============================================");
    	logXml += "===============================================\n";
    	logJson += "===============================================\n";
    	System.out.println(tmp);
    	logXml += tmp + "\n";
    	
		response = service.path(req).request().accept(MediaType.APPLICATION_XML).get();
		status = response.getStatus();
		body = response.readEntity(String.class);

		result = (status == 200 ? "OK" : "ERROR");
		
		System.out.println("=> Result: " + result);
		logXml += "=> Result: " + result + "\n";
		System.out.println("=> HTTP Status: " + status);
		logXml += "=> HTTP Status: " + status + "\n";
		System.out.println(prettyFormat(body));
		logXml += prettyFormat(body) + "\n";
		
		/* ----- */		
			
		tmp = "\n" + head + " Accept:Application/json Content-type:Application/json";
		
		System.out.println(tmp);
    	logJson += tmp + "\n";
		
		response = service.path(req).request().accept(MediaType.APPLICATION_JSON).get();
		status = response.getStatus();
		body = response.readEntity(String.class);
		
		result = (status == 200 ? "OK" : "ERROR");
		
		System.out.println("=> Result: " + result);
    	logJson += "=> Result: " + result + "\n";
		System.out.println("=> HTTP Status: " + status);
    	logJson += "=> HTTP Status: " + status + "\n";
		System.out.println(prettyJson(body));
    	logJson += prettyJson(body) + "\n";
    	
		System.out.println("===============================================");
		logXml += "===============================================\n";
    	logJson += "===============================================\n";
    	
    }
    
    private void execRequest9(WebTarget service, String measureType) throws IOException
    {
    	int status, count;
    	Response response;
		String req, head, tmpHead, body, tmpBody, result;
		
		req = "person/1/" + measureType;
    	head = "Request 9: POST /" + req;
    	
    	String req_body_xml="<measure>"
    			+"<date>2011-12-09</date>"
    			+"<idMeasure>17</idMeasure>"
    			+"		<measureType>"
    			+"				<idMeasureType>1</idMeasureType>"
    			+"				<type>height</type>"
    			+"		</measureType>"
    			+"<value>72</value>"
    			+"</measure>";
    	
    	String req_body_json="{"
    			+"\"idMeasure\": 18,"
    			+"\"value\": 72,"
    			+"\"date\": \"2011-12-09\","
    			+"\"measureType\":" 
    			+"{"
    			+"		\"idMeasureType\": 1,"
    			+"		\"type\": \"height\""
    			+"}"
    			+"}";
    	
    	body = "";
		tmpHead = head +" Accept:Application/xml Content-type:Application/xml";
    	
    	System.out.println("===============================================");
    	logXml += "===============================================\n";
    	logJson += "===============================================\n";
    	System.out.println(tmpHead);
    	logXml += tmpHead + "\n";
    	
		response = service.path(req).request().accept(MediaType.APPLICATION_XML).get();
		status = response.getStatus();
		body = response.readEntity(String.class);
		
		count = countOccourence(body, "value");
		
		response = service.path(req).request().accept(MediaType.APPLICATION_XML).post(Entity.xml(req_body_xml));
		status = response.getStatus();
		tmpBody = response.readEntity(String.class);
		
		response = service.path(req).request().accept(MediaType.APPLICATION_JSON).get();
		status = response.getStatus();
		body = response.readEntity(String.class);
		
		result = (countOccourence(body, "value") == count + 1 ? "OK" : "ERROR");
		
		System.out.println("=> Result: " + result);
		logXml += "=> Result: " + result + "\n";
		System.out.println("=> HTTP Status: " + status);
		logXml += "=> HTTP Status: " + status + "\n";
		System.out.println(prettyFormat(tmpBody));
		logXml += prettyFormat(tmpBody) + "\n";

		/* ----- */		
			
		tmpHead = "\n" + head + " Accept:Application/json Content-type:Application/json";
		
		System.out.println(tmpHead);
    	logJson += tmpHead + "\n";
		
		response = service.path(req).request().accept(MediaType.APPLICATION_JSON).get();
		status = response.getStatus();
		body = response.readEntity(String.class);
		
		count = countOccourence(body, "value");
		
		response = service.path(req).request().accept(MediaType.APPLICATION_JSON).post(Entity.json(req_body_json));
		status = response.getStatus();
		tmpBody = response.readEntity(String.class);
		
		response = service.path(req).request().accept(MediaType.APPLICATION_JSON).get();
		status = response.getStatus();
		body = response.readEntity(String.class);
		
		result = (countOccourence(body, "value") == count + 1 ? "OK" : "ERROR");
		
		System.out.println("=> Result: " + result);
    	logJson += "=> Result: " + result + "\n";
		System.out.println("=> HTTP Status: " + status);
    	logJson += "=> HTTP Status: " + status + "\n";
		System.out.println(prettyJson(tmpBody));
    	logJson += prettyJson(tmpBody) + "\n";
    	
		System.out.println("===============================================");
		logXml += "===============================================\n";
    	logJson += "===============================================\n";
    	
    	response = service.path("person/1/hight/17").request().accept(MediaType.APPLICATION_XML).delete();
    	response = service.path("person/1/hight/18").request().accept(MediaType.APPLICATION_JSON).delete();
    }
    
    private String execRequest10()
    {
    	return "Extra: TODO";
    }
    
    private String execRequest11()
    {
    	return "Extra: TODO";
    }
    
    private String execRequest12()
    {
    	return " Extra: TODO";
    }
    
    // Format for body
    public static String prettyXml(String input, int indent) 
    {
		try 
		{
			Source xmlInput = new StreamSource(new StringReader(input));
			StringWriter stringWriter = new StringWriter();
			StreamResult xmlOutput = new StreamResult(stringWriter);
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			transformerFactory.setAttribute("indent-number", indent);
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(xmlInput, xmlOutput);
			
			return xmlOutput.getWriter().toString();
		} 
		catch (Exception e) 
		{
			throw new RuntimeException(e); 
		}
	}

	public static String prettyFormat(String input) 
	{
		return prettyXml(input, 2);
	}
	
	public static String prettyJson(String input) throws JsonParseException, JsonMappingException, IOException 
	{
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);

		Object json = mapper.readValue(input, Object.class);
		String indented = mapper.writerWithDefaultPrettyPrinter()
				.writeValueAsString(json);
		return indented;
	}
}