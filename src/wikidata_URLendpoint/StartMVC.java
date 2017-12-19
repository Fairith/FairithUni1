package wikidata_URLendpoint;

public class StartMVC {

	//The order of instantiating the objects below will be important for some pairs of commands.
	//I haven't explored this in any detail, beyond that the order below works.

	private int start_value = 10;	//initialise model, which in turn initialises view

	public StartMVC() {

		//create Model and View
		Model myModel 	= new Model();
		View myView 	= new View();

		//tell Model about View. 
		myModel.addObserver(myView);
		/*	
			init model after view is instantiated and can show the status of the model
			(I later decided that only the controller should talk to the model
			and moved initialisation to the controller (see below).)
		*/
		//uncomment to directly initialise Model
		//myModel.setValue(start_value);	

		//create Controller. tell it about Model and View, initialise model
		Controller myController = new Controller();
		myController.addModel(myModel);
		myController.addView(myView);
		//myController.initModel(start_value);

		//tell View about Controller 
		myView.addController(myController);
		//and Model, 
		//this was only needed when the view inits the model
		myView.addModel(myModel);
		//myController.initModel(start_value);

	} //RunMVC()
	
	public static void main(String[] args){

		StartMVC mainRunMVC = new StartMVC();

	} //main()

}