package controller.recibo;

import config.Controller;
import controller.Launcher;
import controller.MenuEmpleadoController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import view.MenuEmpleadoView;
import view.recibo.RecibosView;

public class RecibosController extends Controller implements ActionListener{
    public RecibosController(RecibosView view){
        super(view);

        printRecibos();
        view.btnRegresar.addActionListener(this);
        view.btnGenerar.addActionListener(this);
        view.btnModificar.addActionListener(this);
        view.btnConsultar.addActionListener(this);
        view.btnEliminar.addActionListener(this);
    }
    
    private RecibosView getOriginalView(){
        return (RecibosView)this.view;}
    
    private void printRecibos(){
    
    }
    
    private void btnRegresarPushed(){
        Launcher.ac.changeController(new MenuEmpleadoController(new MenuEmpleadoView()));
    }
    
    private void btnGenerarPushed(){}
    
    private void btnConsultarPushed(){}
    
    private void btnModificarPushed(){}
    
    private void btnEliminarPushed(){}
    
    @Override
    public void actionPerformed(ActionEvent e){
    Object source = e.getSource();
        
        if(source == getOriginalView().btnRegresar){
            btnRegresarPushed();}
        
        else if(source == getOriginalView().btnGenerar){
            btnGenerarPushed();}
        
        else if(source == getOriginalView().btnModificar){
            btnModificarPushed();}
        
        else if(source == getOriginalView().btnConsultar){
            btnConsultarPushed();}
        
        else if(source == getOriginalView().btnEliminar){
            btnEliminarPushed();}
    }
}
