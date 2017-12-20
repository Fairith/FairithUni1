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

		StartMVC mainRunMVC = new StartMVC();

	}

}