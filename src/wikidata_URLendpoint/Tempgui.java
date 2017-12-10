package wikidata_URLendpoint;
import java.awt.*;
import java.awt.event.*;


public class Tempgui {
	private Frame mainFrame;
	   private Label headerLabel;
	   private Label statusLabel;
	   private Panel controlPanel;

	   public Tempgui(){
	      prepareGUI();
	   }

	   public static void main(String[] args){
		   Tempgui testgui = new Tempgui();
		   testgui.showButtonDemo();
	   }

	   private void prepareGUI(){
	      mainFrame = new Frame("Mighty Translator");
	      mainFrame.setSize(400,200);
	      mainFrame.setLayout(new GridLayout(2, 1));
	      mainFrame.addWindowListener(new WindowAdapter() {
	         public void windowClosing(WindowEvent windowEvent){
	            System.exit(0);
	         }        
	      });    
	      headerLabel = new Label();
	      headerLabel.setAlignment(Label.CENTER);

	      controlPanel = new Panel();
	      controlPanel.setLayout(new GridLayout(1,2));

	      mainFrame.add(headerLabel);
	      mainFrame.add(controlPanel);
	      //mainFrame.add(statusLabel);
	      mainFrame.setVisible(true);  
	   }

	   private void showButtonDemo(){
	      headerLabel.setText("Enter something to translate"); 

	      Button btn_translate = new Button("Translate");
	      TextField txf_input = new TextField("");
	      Label lbl_output = new Label("");
	      

	      btn_translate.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	            lbl_output.setText("Button clicked.");
	         }
	      });
	      
	      controlPanel.add(txf_input);
	      controlPanel.add(btn_translate);
	      controlPanel.add(lbl_output);

	      mainFrame.setVisible(true);  
	   }
}
