package config;

import javax.swing.JPanel;

public class Controller {
    protected final JPanel view;
    
    public Controller(JPanel view){
        this.view = view;
    }
    
    public JPanel getView(){
        return this.view;}
}
