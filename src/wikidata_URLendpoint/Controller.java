package wikidata_URLendpoint;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

class Controller implements ActionListener {
	Model model;
	View view;

	Controller() {	
		
	} 
	
	public void actionPerformed(ActionEvent e){
		model.translation(view.getTerm());
	}
	public void addModel(Model m){
		this.model = m;
	}

	public void addView(View v){
		this.view = v;
	}
} 