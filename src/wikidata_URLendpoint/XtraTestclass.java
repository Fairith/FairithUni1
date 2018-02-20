package wikidata_URLendpoint;
//https://github.com/BorderCloud/SPARQL-JAVA
import com.bordercloud.sparql.Endpoint;
import com.bordercloud.sparql.EndpointException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

public class XtraTestclass {

  public static void main(String[] args) {
	  Scanner input = new Scanner(System.in);
      String toTranslate = "Boxen";
      String targetLanguage = "en";
	  //System.out.println("Enter English Word to translate: ");
	  //toTranslate = input.next();
	  
      try {
          Endpoint sp = new Endpoint("https://query.wikidata.org/sparql", false);
          
          String querySelect = "SELECT ?itemurl ?link ?lang1 ?lang2 ?desc WHERE {\n" +
                  "  ?itemurl rdfs:label ?lang1 ,\n" +
                  "        ?lang2 .\n" +
                  "  OPTIONAL {?itemurl schema:description ?desc. FILTER (LANG(?desc) = \"" + targetLanguage +"\").}\n" +
                  "  OPTIONAL {?link schema:about ?itemurl ; schema:isPartOf <https://"+ targetLanguage +".wikipedia.org/> .}\n" +
                  "  MINUS {?itemurl wdt:P31 wd:Q4167836 } . # no category items\n" +
                  "  VALUES ?lang1 {\""+ toTranslate +"\"@de} .\n" +
                  "  FILTER(LANG(?lang2) = \"" + targetLanguage +"\").\n" +
                  "}";
          
          
          HashMap rs = sp.query(querySelect);
          
          HashMap results = (HashMap) rs.get("result");
          printResult(rs,50);
          viewHashs(rs);
          System.out.println("-------------------------");
          viewHashs(results);
          System.out.println("-------------------------");
          System.out.println(retResults(rs));
          System.out.println("-------------------------");
          
          String[] translations = retLang(rs);
          String[] descriptions = retDesc(rs);
          for(int i = 0; i < translations.length; i++) {
        	  System.out.println(translations[i] + " | " + descriptions[i]);
          }
          System.out.println("--------------------------------------");
          getContent("https://en.wikipedia.org/wiki/B_cell");
          //getContent2(".");
          
          
      }catch(EndpointException eex) {
          System.out.println(eex);
          eex.printStackTrace();
      }
  }



//public static void getContent2(String url) {
//	try {
//		Document doc = Jsoup.connect("http://en.wikipedia.org/wiki/Boston").get();
//		Element contentDiv = doc.select("div[id=content]").first();
//		//contentDiv.toString(); // The result
//		System.out.println(contentDiv.toString());
//	} catch (IOException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
//}
  
public static void getContent(String url) {
	try {
		Document doc = Jsoup.connect(url).get();
		Elements paragraphs = doc.select(".mw-content-ltr p");
		
		Element firstParagraph = paragraphs.first();
		Element lastParagraph = paragraphs.last();
		Element p;
		int i=1;
		p=firstParagraph;
		System.out.println(p.text());
//		while (p!=lastParagraph){
//		    p=paragraphs.get(i);
//		    System.out.println(p.text());
//		    i++;
//		}
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

public static void getContent3(String url) {
	try {
		Response response = Jsoup.connect(url).execute();
		String[] bodyLines = response.body().split("\n");
		String firstLine = bodyLines[0];
		System.out.println(firstLine);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}


  
  public static String[] retLang(HashMap<String, HashMap> rs) {
	  int size = 0;
	  int i = 0;
	  for (HashMap<String, Object> value : (ArrayList<HashMap<String, Object>>) rs.get("result").get("rows")) {
		  size++;
        }
	  String[] toReturn = new String[size];
	  for (HashMap<String, Object> value : (ArrayList<HashMap<String, Object>>) rs.get("result").get("rows")) {
		  //toReturn += value.get("lang2") + " | ";
		  toReturn[i] = (String) value.get("lang2");
		  i++;
        }
	  return toReturn;
  }
  
  public static String[] retDesc(HashMap<String, HashMap> rs) {
	  int size = 0;
	  int i = 0;
	  for (HashMap<String, Object> value : (ArrayList<HashMap<String, Object>>) rs.get("result").get("rows")) {
		  size++;
        }
	  String[] toReturn = new String[size];
	  for (HashMap<String, Object> value : (ArrayList<HashMap<String, Object>>) rs.get("result").get("rows")) {
		  //toReturn += value.get("lang2") + " | ";
		  toReturn[i] = (String) value.get("desc");
		  i++;
        }
	  return toReturn;
  }
  
  //stuff to help me understand START
  public static void viewHashs(HashMap<String, Object> hs) {
	  for (String name: hs.keySet()){

          String key =name.toString();
          String value = hs.get(name).toString();  
          System.out.println("key:"+key + " " +" value:" +value);  
	  	} 
	  
  }
  
  
  public static String retResults(HashMap<String, HashMap> rs) {
	  String toReturn = "";
	  for (HashMap<String, Object> value : (ArrayList<HashMap<String, Object>>) rs.get("result").get("rows")) {
	        for (String variable : (ArrayList<String>) rs.get("result").get("variables")) {
	        	toReturn += value.get(variable);
	        	toReturn += " | ";
	        }
        }
	  return toReturn;
  }  
  
  public static void printResult(HashMap<String, HashMap> rs , int size) {

      //Format für erste Zeile + erste Zeile
	  for (String variable : (ArrayList<String>) rs.get("result").get("variables")) {
        System.out.print(String.format("%-"+size+"."+size+"s", variable ) + " | ");
      }    
      System.out.print("\n");
      
      //alle weiteren Zeilen
      for (HashMap<String, Object> value : (ArrayList<HashMap<String, Object>>) rs.get("result").get("rows")) {
       // (1); Zeilenweises abarbeiten
        for (String variable : (ArrayList<String>) rs.get("result").get("variables")) {            
          //(2); Hier: Einzelne Werte für item, lab, lab2
          System.out.print(String.format("%-"+size+"."+size+"s", value.get(variable)) + " | ");
          
        }
        System.out.print("\n");
      }
      //old comments:
      //(1)
      //System.out.print(value);
      /* for (String key : value.keySet()) {
       System.out.println(value.get(key));            
       }*/
      
      //(2)
      //System.out.println(value.get(variable));
      
    }
  
  //stuff to help me understand END

}
