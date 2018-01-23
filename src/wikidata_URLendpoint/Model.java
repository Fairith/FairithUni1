package wikidata_URLendpoint;

import com.bordercloud.sparql.Endpoint;
import com.bordercloud.sparql.EndpointException;

import java.util.Observable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Connection.Response;
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
	
	public Model(){

	}
	
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
		this.originLanguage = orgLng;
	}
	
	public void setTargetLanguage(String trgLng) {
		this.targetLanguage = trgLng;
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
	
	public void translation(String term) {
		String toTranslate = term;
		//TODO: für's schnellere Testen; Später löschen.
		if(toTranslate=="") {
			toTranslate = "Isomorphism";
		}
	    //String targetLanguage = "de";
		
	    String querySelect = "SELECT ?itemurl ?link ?lang1 ?lang2 ?desc WHERE {\n" +
	            "  ?itemurl rdfs:label ?lang1 ,\n" +
	            "        ?lang2 .\n" +
	            "  OPTIONAL {?itemurl schema:description ?desc. FILTER (LANG(?desc) = \"" + targetLanguage +"\").}\n" +
	            "  OPTIONAL {?link schema:about ?itemurl ; schema:isPartOf <https://"+ targetLanguage +".wikipedia.org/> .}\n" +
	            "  MINUS {?itemurl wdt:P31 wd:Q4167836 } . # no category items\n" +
	            "  VALUES ?lang1 {\""+ toTranslate +"\"@"+originLanguage +"} .\n" +
	            "  FILTER(LANG(?lang2) = \"" + targetLanguage +"\").\n" +
	            "}";
		
		try {
	        Endpoint ep = new Endpoint("https://query.wikidata.org/sparql", true);
	        HashMap result = ep.query(querySelect);
	  		
	        setTranslations(extractTranslations(result));
	        setDescriptions(extractDescriptions(result));
	        setLinks(extractLinks(result));
	        setWikiContent(extractWikiContent(getLinks()));
	        
	        setChanged();
	        notifyObservers();
	          
	      }catch(EndpointException eex) {
	          System.out.println(eex);
	          eex.printStackTrace();
	      }
	}
	
	private String[] extractLinks(HashMap<String, HashMap> hs) {
		int size = 0;
		int i = 0;
		for (HashMap<String, Object> value : (ArrayList<HashMap<String, Object>>) hs.get("result").get("rows")) {
		  size++;
	    }
		String[] toReturn = new String[size];
		for (HashMap<String, Object> value : (ArrayList<HashMap<String, Object>>) hs.get("result").get("rows")) {
		  toReturn[i] = (String) value.get("link");
		  i++;
	    }
		return toReturn;
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
	
	private String[] extractWikiContent(String[] urls) {
		String[] toReturn = new String[urls.length];
		try {
			for(int i = 0; i < urls.length; i++) {
				Document doc = Jsoup.connect(urls[i]).get();
				Elements paragraphs = doc.select(".mw-content-ltr p");	
				Element firstParagraph = paragraphs.first();
				//Element lastParagraph = paragraphs.last();
				Element p;
				int j=1;
				p=firstParagraph;
				toReturn[i] = p.text();
//				System.out.println(p.text());
//				while (p!=lastParagraph){
//				    p=paragraphs.get(i);
//				    System.out.println(p.text());
//				    i++;
//				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return toReturn;
	}
}