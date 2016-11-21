package rest.client;

import java.net.URI;    
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.ClientConfig;

public class App 
{
	// URI of server
	private static final String URI = "https://introsde-2016-assignment-2.herokuapp.com/sdelab/";
	
	// Files of log
	static String LOG_XML = "client-server-xml.log";
	static String LOG_JSON = "client-server-json.log";
	
    public static void main(String[] args) 
    {
        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget service = client.target(getBaseURI());

        System.out.println("Url server: " +App.URI);
        
        

    }

    private static URI getBaseURI() {
        return UriBuilder.fromUri(URI).build();
    }
    
    private String execRequest1()
    {
    	String req = "person";
		
    	return "TODO";
    }
    
    private String execRequest2()
    {
    	String req = "person/1";
		
    	return "TODO";
    }
    
    private String execRequest3()
    {
    	String req = "person/1";
    	
    	String req_body_json = "{"
				+"\"idPerson\": 1,"
				+"\"firstname\": \"Vecchio\","
				+"\"lastname\": \"Norris\","
				+"\"birthdate\": \"1945-02-25\""
				+"}";

		String req_body_xml = "<person>"
				+"<idPerson>1</idPerson>"
				+"<firstname>Vecchio</firstname>"	
				+"<lastname>Norris</lastname>"
				+"<birthdate>1945-02-25</birthdate>"
				+"</person>";
		   	
    	return "TODO";
    }
    
    private String execRequest4()
    {
    	String req = "person";
    	
    	String r4_body_json = "{"
    			+"\"id\": 7,"
    			+"\"firstname\": \"Chuck\","
    			+"\"lastname\": \"Norris\","
    			+"\"birthdate\": \"1945-01-01\","
    			+"\"scope\":" 
    			+"["
    			+"		{"
    			+"				\"id\": \"7\","
    			+"				\"scopeType\": "
    			+" 				{"
				+"						\"id\": \"1\","
				+"			    		\"type\": \"height\""
				+"				},"
				+"				\"measure\": " 
				+"				{"
				+"						\"id\": \"7\","
				+" 						\"value\": 1.72,"
				+"						\"date\": \"1978-09-02\""
				+"				}"
				+"		},"
				+"		{"
    			+"				\"id\": \"8\","
    			+"				\"scopeType\": "
    			+" 				{"
				+"						\"id\": \"2\","
				+"			    		\"type\": \"weight\""
				+"				},"
				+"				\"measure\": " 
				+"				{"
				+"						\"id\": \"8\","
				+" 						\"value\": 78.9,"
				+"						\"date\": \"1978-09-02\""
				+"				}"
				+"		}"
				+"]"
    			+"}";

    			String r4_body_xml ="<person>"
    			+"<id>7</id>"
    			+"<firstname>Chuck</firstname>"
    			+"<lastname>Norris</lastname>"
    			+"<birthdate>1945-01-01</birthdate>"
    			+"<healthProfile>"
    			+"		<scope>"
    			+"				<id>7</id>"
    			+"				<measure>"
    			+"						<id>7</id>"
    			+"						<value>1.72</value>"
    			+"						<date>1978-09-02</date>"
    			+"				</measure>"
    			+"				<scopeType>"
    			+"						<id>1</id>"
    			+"						<type>height</type>"
    			+"				</scopeType>"
    			+"		</scope>"
    			+"		<scope>"
    			+"				<id>8</id>"
    			+"				<measure>"
    			+"						<id>8</id>"
    			+"						<value>75.0</value>"
    			+"						<date>1978-09-02</date>"
    			+"				</measure>"
    			+"				<scopeType>"
    			+"						<id>2</id>"
    			+"						<type>weight</type>"
    			+"				</scopeType>"   
    			+"		</scope>"
    			+"</healthProfile>"
    			+"</person>";
		
    	return "TODO";
    }
    
    private String execRequest5()
    {
    	String req = "person/7";//Look at r4_body_json for 555
		
    	return "TODO";
    }
    
    private String execRequest6()
    {
    	
		String req = "person/1/height";
		
    	return "TODO";
    }
    
    private String execRequest7()
    {
    	String req = "person/1/height/4";
    	
    	return "TODO";
    }
    
    private String execRequest8()
    {
    	String req = "person/1/height";
    	
    	return "TODO";
    }
    
    private String execRequest9()
    {
    	String req = "measureTypes";
    	
    	return "TODO";
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
}