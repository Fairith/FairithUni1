package wikidata_URLendpoint;

import com.bordercloud.sparql.Endpoint;
import com.bordercloud.sparql.EndpointException;

import java.util.Observable;

import javax.naming.TimeLimitExceededException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Connection.Response;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;


public class Model extends Observable {	
	private String[] translation;
	private String[] descriptions;
	private String[] wikiContent;
	private String[] links;
	private String originLanguage = "en";
	private String targetLanguage = "de";
	private String urlErrorMessage = "<em>An incorrect or missing Wikidata url was encountered.</em>";
	private String linkErrorMessage = "<em>No Wikipedia Link on Wikidata was found</em>";
	
	public Model(){
//		System.out.println("Model");
	}
	
	// ------------------------------------------------------- \\
	
	public void init() {
		translation(""); // workaround, not a fix
	}
	
	// ------------------------------------------------------- \\
	
	public void setLinks(String[] s) {
		this.links = s;
	}
	
	public void setWikiContent(String[] s) {
		this.wikiContent = s;
	}
	
	public void setTranslations(String[] s) {
		this.translation = s;
	}
	
	public void setDescriptions(String[] s) {
		this.descriptions = s;
	}
	public void setOriginLanguage(String orgLng) {
		originLanguage = orgLng; //test ob 'this.' einen Unterschied macht
	}
	
	public void setTargetLanguage(String trgLng) {
		targetLanguage = trgLng; //test ob 'this.' einen Unterschied macht
	}
// ------------------------------------------------------- \\
	public String[] getTranslations() {
		return this.translation;
	}
	
	public String[] getDescriptions() {
		return this.descriptions;
	}
	
	public String[] getLinks() {
		return this.links;
	}
	
	public String[] getWikiContent() {
		return this.wikiContent;
	}
	
	public String getUrlErrorMessage() {
		return this.urlErrorMessage;
	}
	
	public String getLinkErrorMessage() {
		return this.linkErrorMessage;
	}
	
	public void translation(String term) {
		String toTranslateUC = "";
		String toTranslateLC = "";
		
		if(term.length() < 1) { //because nothing can be easy; first letter upper case
			toTranslateUC += term;
		}else {
			toTranslateUC += term.substring(0,1).toUpperCase() + term.substring(1);
		}
		
		if(term.length() < 1) { //because nothing can be easy; first letter lower case
			toTranslateLC += term;
		}else {
			toTranslateLC += term.substring(0,1).toLowerCase() + term.substring(1);
		}
		//System.out.println(toTranslateUC + " | " + toTranslateLC);
		int rlength = 0;
		
	    String querySelect = "SELECT ?itemurl ?link ?lang1 ?lang2 ?desc WHERE {\n" +
	            "  ?itemurl rdfs:label ?lang1 ,\n" +
	            "        ?lang2 .\n" +
	            "  OPTIONAL {?itemurl schema:description ?desc. FILTER (LANG(?desc) = \"" + targetLanguage +"\").}\n" +
	            "  OPTIONAL {?link schema:about ?itemurl ; schema:isPartOf <https://"+ targetLanguage +".wikipedia.org/> .}\n" +
	            "  MINUS {?itemurl wdt:P31 wd:Q4167836 } .\n" + //no category items
	            "  VALUES (?lang1) {(\""+ toTranslateUC +"\"@"+originLanguage +") (\""+ toTranslateLC +"\"@"+originLanguage +")} .\n" +
	            "  FILTER(LANG(?lang2) = \"" + targetLanguage +"\").\n" +
	            "  MINUS{?itemurl wdt:P31 wd:Q4167410 } .\n" + //removes disambiguations
	            "}";
		//"  FILTER NOT EXISTS{?itemurl wdt:P31 wd:Q4167410 } .\n" + //removes disambiguations
		try {
			Endpoint ep = new Endpoint("https://query.wikidata.org/sparql", true); //ReadOnly (true/false)
		    HashMap result = ep.query(querySelect);
		   
		    rlength = resultLength(result);
	        
	        setTranslations(extractTranslations(result, rlength));
	        setDescriptions(extractDescriptions(result, rlength));
	        setLinks(extractLinks(result, rlength));
	        setWikiContent(extractWikiContent(getLinks()));
			
	        
	        setChanged();
	        notifyObservers();
	  		
	      } catch(EndpointException eex) {
	          System.out.println(eex);
	          eex.printStackTrace();
	      }
	}
	
	private int resultLength(HashMap<String, HashMap> hs) {
		int toReturn = 0;
		for (HashMap<String, Object> value : (ArrayList<HashMap<String, Object>>) hs.get("result").get("rows")) {
			  toReturn++;
		    }
		return toReturn;
	}
	
	private String[] extractLinks(HashMap<String, HashMap> hs, int size) {
		int i = 0;
		String[] toReturn = new String[size];
		for (HashMap<String, Object> value : (ArrayList<HashMap<String, Object>>) hs.get("result").get("rows")) {
		  toReturn[i] = (String) value.get("link");
		  i++;
	    }
		return toReturn;
	}
	
	private String[] extractTranslations(HashMap<String, HashMap> hs, int size) {
		int i = 0;
		String[] toReturn = new String[size];
		for (HashMap<String, Object> value : (ArrayList<HashMap<String, Object>>) hs.get("result").get("rows")) {
		  toReturn[i] = (String) value.get("lang2");
		  i++;
	    }
		return toReturn;
	}
	
	private String[] extractDescriptions(HashMap<String, HashMap> hs, int size) {
		int i = 0;
		String[] toReturn = new String[size];
		for (HashMap<String, Object> value : (ArrayList<HashMap<String, Object>>) hs.get("result").get("rows")) {
		  toReturn[i] = (String) value.get("desc");
		  i++;
	    }
		return toReturn;
	}
	
	private String[] extractWikiContent(String[] urls) {
		String[] toReturn = new String[urls.length];
		int i = 0;
		try {
			for(i = 0; i < urls.length; i++) {
				if(urls[i] == null) {
					toReturn[i] = linkErrorMessage;
				} else {
					Document doc = Jsoup.connect(urls[i]).get();
					Elements paragraphs = doc.select(".mw-content-ltr p");	
					Element firstParagraph = paragraphs.first();
					toReturn[i] = firstParagraph.text();
					
					/**
					System.out.println(p.text());
					while (p!=lastParagraph){
					    p=paragraphs.get(i);
					    System.out.println(p.text());
					    i++;
					}
					 **/
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException iae) {
			toReturn[i] = urlErrorMessage;
		}
		return toReturn;
	}
}