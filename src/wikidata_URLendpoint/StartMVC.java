package wikidata_URLendpoint;

public class StartMVC {

	public StartMVC() {

		Model myModel 	= new Model();
		OldView myView 	= new OldView();
		myModel.addObserver(myView);
		
		Controller myController = new Controller();
		myController.addModel(myModel);
		myController.addView(myView);
		
		myView.addController(myController);
		myView.addModel(myModel);
		
		myModel.init(); //not a fix, only a workaround

	}
	
	public static void main(String[] args){
		//TODO: Zeitplan / zeiten aufschreiben
		//TODO: Zukunfts-Ausblick: Fehler in Wikidata finden / Korrigieren -> Endpoint POST-request true/false :D 
		StartMVC mainRunMVC = new StartMVC();
	}

}