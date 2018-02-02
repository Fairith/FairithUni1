package wikidata_URLendpoint;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

class Controller implements ActionListener {
	Model model;
	NewView view;

	Controller() {	
//		System.out.println("Controler");
	} 
	
	public void actionPerformed(ActionEvent e){
		System.out.println("Action performed");
		model.translation(view.getTerm());
	}
	public void addModel(Model m){
		this.model = m;
	}

	public void addView(NewView v){
		this.view = v;
	}
} 