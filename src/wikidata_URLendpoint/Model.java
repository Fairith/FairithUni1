package wikidata_URLendpoint;

import com.bordercloud.sparql.Endpoint;
import com.bordercloud.sparql.EndpointException;

import java.util.Observable;
import java.util.ArrayList;
import java.util.HashMap;


public class Model extends Observable {	
	private String[] translation;
	private String[] descriptions;
	
	public Model(){

	}
	
	public void setTranslations(String[] s) {
		this.translation = s;
	}
	
	public void setDescriptions(String[] s) {
		this.descriptions = s;
	}
	
	public String[] getTranslations() {
		return this.translation;
	}
	
	public String[] getDescriptions() {
		return this.descriptions;
	}
	
	
	public void translation(String term) {
		String toTranslate = term;
		//TODO: für's schnellere Testen; Später löschen.
		if(toTranslate=="") {
			toTranslate = "Isomorphism";
		}
	    String targetLanguage = "de";
		
	    String querySelect = "SELECT ?itemurl ?lang1 ?lang2 ?desc WHERE {\n" +
	            "  ?itemurl rdfs:label ?lang1 ,\n" +
	            "        ?lang2 .\n" +
	            "  OPTIONAL {?itemurl schema:description ?desc. FILTER (LANG(?desc) = \"" + targetLanguage +"\").}\n" +
	            "  MINUS {?itemurl wdt:P31 wd:Q4167836 } . # no category items\n" +
	            "  VALUES ?lang1 {\""+ toTranslate +"\"@en} .\n" +
	            "  FILTER(LANG(?lang2) = \"" + targetLanguage +"\").\n" +
	            "}";
		
		try {
	        Endpoint ep = new Endpoint("https://query.wikidata.org/sparql", true);
	        HashMap result = ep.query(querySelect);
	  		
	        setTranslations(extractTranslations(result));
	        setDescriptions(extractDescriptions(result));
	        
	        setChanged();
	        notifyObservers();
	          
	      }catch(EndpointException eex) {
	          System.out.println(eex);
	          eex.printStackTrace();
	      }
	}
	
	private String[] extractTranslations(HashMap<String, HashMap> hs) {
		int size = 0;
		int i = 0;
		for (HashMap<String, Object> value : (ArrayList<HashMap<String, Object>>) hs.get("result").get("rows")) {
		  size++;
	    }
		String[] toReturn = new String[size];
		for (HashMap<String, Object> value : (ArrayList<HashMap<String, Object>>) hs.get("result").get("rows")) {
		  toReturn[i] = (String) value.get("lang2");
		  i++;
	    }
		return toReturn;
	}
	
	private String[] extractDescriptions(HashMap<String, HashMap> hs) {
		int size = 0;
		int i = 0;
		for (HashMap<String, Object> value : (ArrayList<HashMap<String, Object>>) hs.get("result").get("rows")) {
		  size++;
	    }
		String[] toReturn = new String[size];
		for (HashMap<String, Object> value : (ArrayList<HashMap<String, Object>>) hs.get("result").get("rows")) {
		  toReturn[i] = (String) value.get("desc");
		  i++;
	    }
		return toReturn;
	}
}