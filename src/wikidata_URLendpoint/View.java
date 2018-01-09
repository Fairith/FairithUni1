package wikidata_URLendpoint;
import java.awt.Button;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.Label;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.util.Observable;
import java.awt.event.ActionListener;
import java.util.Observer;


class View implements Observer {

	private Frame mainFrame;
	private Label headerLabel;
	private Label tmp1;
	private Label tmp2;
	private Panel controlPanel;
	private Panel headPanel;
	private Button btn_translate;
	private TextField txf_input;
	private TextArea txa_output;

	private Model model;		
	
	View() {
<<<<<<< HEAD
		//System.out.println("View()");	
		
=======
>>>>>>> 8ec7e71a8a02a77307ce5ed9b91455467bf305ec
		mainFrame = new Frame("Mighty Translator");
	    mainFrame.setSize(800,400);
	    mainFrame.setLocation(200, 200);
	    mainFrame.setLayout(new GridLayout(2, 1));
	    
	    headerLabel = new Label("Enter an english term to translate");
	    headerLabel.setAlignment(Label.CENTER);
	    tmp1 = new Label("temp1");
	    tmp2 = new Label("temp2");
	    
	    headPanel = new Panel();
	    headPanel.setLayout(new GridLayout(1,3));
	    
	    controlPanel = new Panel();
	    controlPanel.setLayout(new GridLayout(1,3));
	    
	    btn_translate = new Button("Translate");
	    txf_input = new TextField("");
	    txa_output = new TextArea ("");
	    
	    headPanel.add(tmp1);
	    headPanel.add(headerLabel);
	    headPanel.add(tmp2);
	    
	    controlPanel.add(txf_input);
	    controlPanel.add(btn_translate);
	    controlPanel.add(txa_output);
	    

	    mainFrame.add(headPanel);
	    mainFrame.add(controlPanel);
	    mainFrame.addWindowListener(new CloseListener());
	    mainFrame.setVisible(true);
	}
    
	public void update(Observable obs, Object obj) {
		
    	txa_output.setText("");
    	
    	String[] translations = model.getTranslations();
    	String[] descriptions = model.getDescriptions();
    	
    	for(int i = 0; i < translations.length; i++) {
    		txa_output.append(translations[i] + " | " + descriptions[i] + "\n");
    	}
    }
	
    public String getTerm() {
    	return this.txf_input.getText();
    }
    	
	public void addController(ActionListener controller){
		btn_translate.addActionListener(controller);
	}

	public void addModel(Model m){
		this.model = m;
	}
	
	public static class CloseListener extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			e.getWindow().setVisible(false);
			System.exit(0);
		}
	}

}