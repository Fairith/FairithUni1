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
		
		myView.setVisible(true);
		
		myView.setOutputText("initializing ... ");
		myModel.init(); //not a fix, only a workaround
		myView.setOutputText("initialization complete!");

	}
	
	public static void main(String[] args){
		//TODO: Report schreiben: Zukunfts-Ausblick - Fehler in Wikidata finden / Korrigieren -> Endpoint POST-request true/false :D 
		StartMVC mainRunMVC = new StartMVC();
	}

}