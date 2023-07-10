package it.polito.tdp.extflightdelays;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.extflightdelays.model.Airport;
import it.polito.tdp.extflightdelays.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML // fx:id="compagnieMinimo"
    private TextField compagnieMinimo; // Value injected by FXMLLoader

    @FXML // fx:id="cmbBoxAeroportoPartenza"
    private ComboBox<Airport> cmbBoxAeroportoPartenza; // Value injected by FXMLLoader

    @FXML // fx:id="cmbBoxAeroportoDestinazione"
    private ComboBox<Airport> cmbBoxAeroportoDestinazione; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalizza"
    private Button btnAnalizza; // Value injected by FXMLLoader

    @FXML // fx:id="btnConnessione"
    private Button btnConnessione; // Value injected by FXMLLoader

    @FXML
    void doAnalizzaAeroporti(ActionEvent event) {
    	
    	this.cmbBoxAeroportoDestinazione.getItems().clear();
    	this.cmbBoxAeroportoPartenza.getItems().clear();
    	
    	int nAirlines = 0;
    	try {
    		nAirlines = Integer.parseInt(this.compagnieMinimo.getText());
    	} catch (NumberFormatException e) {
    		this.txtResult.setText("Inserire un numero!");
    	}
    	model.creaGrafo(nAirlines);
    	this.cmbBoxAeroportoPartenza.getItems().setAll(model.getVertici());
    	this.cmbBoxAeroportoDestinazione.getItems().setAll(model.getVertici());
    }

    @FXML
    void doTestConnessione(ActionEvent event) {
    	
    	
    	Airport origin = this.cmbBoxAeroportoPartenza.getValue();
    	Airport destination = this.cmbBoxAeroportoDestinazione.getValue();
    	if(origin != null && destination != null && !origin.equals(destination)) { //in questo caso ha senso cercare un percorso
    		List<Airport> percorso = this.model.trovaPercorso(origin, destination);
    		if(percorso.isEmpty()) {
    			this.txtResult.setText("Percorso tra " + origin.getAirportName() + " e " + destination.getAirportName() + " non trovato!");
    		} else {
    			txtResult.setText("Percorso tra " + origin.getAirportName() + " e " + destination.getAirportName() + "\n\n");
    			for(Airport a : percorso) 
    				txtResult.appendText(a.getAirportName() +"\n");
    		}
    	} else {
    		txtResult.appendText("Devi selezionare due aeroporti diverse tra loro!");
    	}
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        assert compagnieMinimo != null : "fx:id=\"compagnieMinimo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbBoxAeroportoPartenza != null : "fx:id=\"cmbBoxAeroportoPartenza\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbBoxAeroportoDestinazione != null : "fx:id=\"cmbBoxAeroportoDestinazione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnAnalizza != null : "fx:id=\"btnAnalizza\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnConnessione != null : "fx:id=\"btnConnessione\" was not injected: check your FXML file 'Scene.fxml'.";

        
    }

    public void setModel(Model model) {
    	this.model = model;
    }
}