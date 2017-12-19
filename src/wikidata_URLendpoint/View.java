package wikidata_URLendpoint;
import java.awt.Button;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.Label;
import java.awt.event.WindowEvent;	//for CloseListener()
import java.awt.event.WindowAdapter;	//for CloseListener()
import java.lang.Integer;		//int from Model is passed as an Integer
import java.util.Observable;		//for update();
import java.awt.event.ActionListener;	//for addController()
import java.util.Observer;


class View implements Observer {

	//attributes as must be visible within class

	private Frame mainFrame;
	private Label headerLabel;
	private Panel controlPanel;
	private Button btn_translate;
	private TextField txf_input;
	private TextArea txa_output;

	private Model model;		//Joe: Model is hardwired in, 
					//needed only if view initialises model (which we aren't doing)
	
	View() {
		System.out.println("View()");	
		
		mainFrame = new Frame("Mighty Translator");
	    mainFrame.setSize(800,400);
	    mainFrame.setLocation(200, 200);
	    mainFrame.setLayout(new GridLayout(2, 1));
	    
	    headerLabel = new Label("Enter an english term to translate");
	    headerLabel.setAlignment(Label.CENTER);

	    controlPanel = new Panel();
	    controlPanel.setLayout(new GridLayout(1,2));
	    
	    btn_translate = new Button("Translate");
	    txf_input = new TextField("");
	    txa_output = new TextArea ("");
	    
	    controlPanel.add(txf_input);
	    controlPanel.add(btn_translate);
	    controlPanel.add(txa_output);
	    

	    mainFrame.add(headerLabel);
	    mainFrame.add(controlPanel);
	    mainFrame.addWindowListener(new CloseListener());
	    mainFrame.setVisible(true);
		
		/**
		//frame in constructor and not an attribute as doesn't need to be visible to whole class
		Frame frame 		= new Frame("Prototype Translator");
		frame.add("North", new Label("counter"));

		myTextField 		= new TextField();
		frame.add("Center", myTextField);

		//panel in constructor and not an attribute as doesn't need to be visible to whole class
		Panel panel 		= new Panel();
		button	 		= new Button("PressMe");
		panel.add(button);
		frame.add("South", panel);		

		frame.addWindowListener(new CloseListener());	
		frame.setSize(200,200);
		frame.setLocation(100,100);
		frame.setVisible(true);
		**/
	} //View()

	// Called from the Model
    	public void update(Observable obs, Object obj) {
    		
		//who called us and what did they send?
		//System.out.println ("View      : Observable is " + obs.getClass() + ", object passed is " + obj.getClass());

		//model Pull 
		//ignore obj and ask model for value, 
		//to do this, the view has to know about the model (which I decided I didn't want to do)
		//uncomment next line to do Model Pull
    	//txa_output.setText("" + model.getValue());
    	txa_output.setText("");
    	
    	String[] translations = model.getTranslations();
    	String[] descriptions = model.getDescriptions();
    	
    	for(int i = 0; i < translations.length; i++) {
    		txa_output.append(translations[i] + " | " + descriptions[i] + "\n");
    	}
		
		//model Push 
		//parse obj
    		
    		//System.out.println("Observable is " + obs.getClass() + " | Object passed is " + obj.getClass() + " | Hashcode: " + obj.hashCode());
    		//lbl_output.setText("" + ((Integer)obj).intValue());	//obj is an Object, need to cast to an Integer
    		

    	} //update()

	//to initialise TextField
    public String getTerm() {
    	return this.txf_input.getText();
    }
	public void setValue(int v){
    		txf_input.setText("" + v);
	} //setValue()
    	
	public void addController(ActionListener controller){
		System.out.println("View      : adding controller");
		//button.addActionListener(controller);	//need instance of controller before can add it as a listener 
		btn_translate.addActionListener(controller);
	} //addController()

	//uncomment to allow controller to use view to initialise model	
	public void addModel(Model m){
		System.out.println("View      : adding model");
		this.model = m;
	} //addModel()
	
	public static class CloseListener extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			e.getWindow().setVisible(false);
			System.exit(0);
		} //windowClosing()
	} //CloseListener

} //View