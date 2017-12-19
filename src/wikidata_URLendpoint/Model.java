package wikidata_URLendpoint;

import com.bordercloud.sparql.Endpoint;
import com.bordercloud.sparql.EndpointException;

import java.util.Observable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
//Model.java
//(C) Joseph Mack 2011, jmack (at) wm7d (dot) net, released under GPL v3 (or any later version)

//inspired by Joseph Bergin's MVC gui at http://csis.pace.edu/~bergin/mvc/mvcgui.html

//Model holds an int counter (that's all it is).
//Model is an Observable
//Model doesn't know about View or Controller

public class Model extends Observable {	
	
	private int counter;	//primitive, automatically initialised to 0
	private String[] translation;
	private String[] descriptions;
	
	public Model(){

		System.out.println("Model()");

		/**
		Problem initialising both model and view:

		On a car you set the speedometer (view) to 0 when the car (model) is stationary.
		In some circles, this is called calibrating the readout instrument.
		In this MVC example, you would need two separate pieces of initialisation code,
			in the model and in the view. If you changed the initialisation value in one
			you'd have to remember (or know) to change the initialisation value in the other.
			A recipe for disaster.

		Alternately, when instantiating model, you could run  

		setValue(0);

		as part of the constructor, sending a message to the view. 
		This requires the view to be instantiated before the model,
		otherwise the message will be send to null (the unitialised value for view).
		This isn't a particularly onerous requirement, and is possibly a reasonable approach.

		Alternately, have RunMVC tell the view to intialise the model.
		The requires the view to have a reference to the model.
		This seemed an unneccesary complication.

		I decided instead in RunMVC, to instantiate model, view and controller, make all the connections,
		then since the Controller already has a reference to the model (which it uses to alter the status of the model),
		to initialise the model from the controller and have the model automatically update the view.
		*/

	} //Model()

	//uncomment this if View is using Model Pull to get the counter
	//not needed if getting counter from notifyObservers()
	public int getValue(){return counter;}

	//notifyObservers()
	//model sends notification to view because of RunMVC: myModel.addObserver(myView)
	//myView then runs update()
	//
	//model Push - send counter as part of the message
	
	public void setTranslations(String[] s) {
		this.translation = s;
		for(int i = 0; i < s.length; i++) {
			System.out.println(s[i]);
		}
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
	
	public void setValue(int value) {

		this.counter = value;
		System.out.println("Model init: counter = " + counter);
		setChanged();
		//model Push - send counter as part of the message
		//notifyObservers(counter);
		//if using Model Pull, then can use notifyObservers()
		notifyObservers();

	} //setValue()
	
	public void incrementValue() {

		++counter;
		System.out.println("Model     : counter = " + counter);
		setChanged();
		//model Push - send counter as part of the message
		//notifyObservers(counter);
		//if using Model Pull, then can use notifyObservers()
		notifyObservers();

	} //incrementValue()
	
} //Model