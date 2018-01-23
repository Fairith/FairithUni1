package wikidata_URLendpoint;
import java.awt.Button;
import java.awt.Choice;
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
	private Choice originLanguage;
	private Choice targetLanguage;

	private Model model;		
	
	View() {
		//System.out.println("View()");	
		mainFrame = new Frame("Mighty Translator");
	    mainFrame.setSize(800,400);
	    mainFrame.setLocation(200, 200);
	    mainFrame.setLayout(new GridLayout(2, 1));
	    
	    headerLabel = new Label("Enter an english term to translate");
	    headerLabel.setAlignment(Label.CENTER);
	    tmp1 = new Label("temp1");
	    tmp2 = new Label("temp2");
	    
	    originLanguage = new Choice();
	    originLanguage.add("de");
	    originLanguage.add("en");
	    originLanguage.add("fr");
	    
	    targetLanguage = new Choice();
	    targetLanguage.add("de");
	    targetLanguage.add("en");
	    targetLanguage.add("fr");
	    targetLanguage.select(1); //selected at init
	    
	    
	    headPanel = new Panel();
	    headPanel.setLayout(new GridLayout(1,3));
	    
	    controlPanel = new Panel();
	    controlPanel.setLayout(new GridLayout(1,3));
	    
	    btn_translate = new Button("Translate");
	    txf_input = new TextField("");
	    txa_output = new TextArea ("");
	    
	    headPanel.add(originLanguage);
	    headPanel.add(headerLabel);
	    headPanel.add(targetLanguage);
	    
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
    	model.setOriginLanguage(originLanguage.getSelectedItem());
    	model.setTargetLanguage(targetLanguage.getSelectedItem());
    	String[] translations = model.getTranslations();
    	String[] descriptions = model.getDescriptions();
    	String[] wikiContent = model.getWikiContent();
    	
    	for(int i = 0; i < translations.length; i++) {
    		txa_output.append(translations[i] + " | " + descriptions[i] + " | " + wikiContent[i] + "\n");
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