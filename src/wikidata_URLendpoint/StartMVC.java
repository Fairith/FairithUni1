package wikidata_URLendpoint;

public class StartMVC {

	public StartMVC() {

		Model myModel 	= new Model();
		View myView 	= new View();
		myModel.addObserver(myView);
		
		Controller myController = new Controller();
		myController.addModel(myModel);
		myController.addView(myView);
		
		myView.addController(myController);
		myView.addModel(myModel);

	}
	
	public static void main(String[] args){
		//TODO: Zeitplan / zeiten aufschreiben
		//TODO: Zukunfts-Ausblick: Fehler finden / Korrigieren -> Endpoint POST-request true/false :D 
		StartMVC mainRunMVC = new StartMVC();
	}

}