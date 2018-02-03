package wikidata_URLendpoint;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import javax.swing.border.TitledBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.UIManager;
import java.awt.Color;
import java.awt.Desktop;

import javax.swing.JTextArea;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JEditorPane;

public class View extends JFrame implements Observer {

	private JPanel contentPane;
	private JTextField txf_input;
	private JEditorPane jep_output;
	private JButton btn_translate;
	private JComboBox comboBox_targetLanguage;
	private JComboBox comboBox_originLanguage;
	private String toPrint;
	
	private Model model;

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					NewView frame = new NewView();
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the frame.
	 */
	public View() {
		setTitle("Mighty Translator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblEnterATerm = new JLabel("Enter a Term to translate!");
		
		txf_input = new JTextField();
		txf_input.setColumns(10);
		
		comboBox_originLanguage = new JComboBox();
		comboBox_originLanguage.setModel(new DefaultComboBoxModel(new String[] {"DE", "EN", "FR"}));
		
		btn_translate = new JButton("Translate");
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Results", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(404)
					.addComponent(lblEnterATerm, GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)
					.addGap(336))
				.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
					.addComponent(comboBox_originLanguage, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(txf_input, GroupLayout.DEFAULT_SIZE, 826, Short.MAX_VALUE)
					.addContainerGap())
				.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
					.addComponent(btn_translate)
					.addContainerGap())
				.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
					.addComponent(panel, GroupLayout.DEFAULT_SIZE, 864, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblEnterATerm)
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(txf_input, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(comboBox_originLanguage, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addComponent(btn_translate)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(panel, GroupLayout.DEFAULT_SIZE, 426, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		comboBox_targetLanguage = new JComboBox();
		comboBox_targetLanguage.setModel(new DefaultComboBoxModel(new String[] {"DE", "EN", "FR"}));
		comboBox_targetLanguage.setSelectedIndex(1);
		
		jep_output = new JEditorPane();
		jep_output.setOpaque(false);
		jep_output.setEditable(false);
		jep_output.setContentType("text/html");
		jep_output.addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent hle) {
                if (HyperlinkEvent.EventType.ACTIVATED.equals(hle.getEventType())) {
                    System.out.println(hle.getURL());
                    Desktop desktop = Desktop.getDesktop();
                    try {
                        desktop.browse(hle.getURL().toURI());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
		
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addComponent(comboBox_targetLanguage, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(736, Short.MAX_VALUE))
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(10)
					.addComponent(jep_output, GroupLayout.DEFAULT_SIZE, 842, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addComponent(comboBox_targetLanguage, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(jep_output, GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
		contentPane.setLayout(gl_contentPane);
		
		//setVisible(true);
	}
	
	public void addModel(Model m) {
		this.model = m;
	}
	
	public void addController(ActionListener c) {
		btn_translate.addActionListener(c);
	}
	
	public String getTerm() {
		return this.txf_input.getText();
	}
	
	public void update(Observable obs, Object obj) {
    	model.setOriginLanguage(comboBox_originLanguage.getSelectedItem().toString().toLowerCase());
    	model.setTargetLanguage(comboBox_targetLanguage.getSelectedItem().toString().toLowerCase());
    	String[] translations = model.getTranslations();
    	String[] descriptions = model.getDescriptions();
    	String[] links = model.getLinks();
    	String[] wikiContent = model.getWikiContent();
    	jep_output.setText("");
    	//jep_output.updateUI(); //test if text doesn't update
    	String bugtest = "";
    	toPrint = "";
    	for(int i = 0; i < translations.length; i++) {
    		toPrint += "\" " + translations[i] + "\"" + ": ";
    		if(wikiContent[i].length() < 90) { //because nothing can be easy
    			toPrint += wikiContent[i];
    		}else {
    			toPrint += wikiContent[i].substring(0,90);
    		}
    		toPrint += "...";
    		toPrint +="<a href='";
    		toPrint += links[i];
    		toPrint +="'>[continue reading]</a>" + "<br>";
    		//text: <Translation>: + ~100 Zeichen Wiki Description + "..." + link "read more"
    		bugtest += translations[i] + " | " + descriptions[i] + " | " + wikiContent[i] + "\n";
    	}
    	jep_output.setText(toPrint);
    	System.out.println("update() called");
    	System.out.println("bugtest: " + bugtest);
    }
}
