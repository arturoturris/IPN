package controller;

import config.Controller;
import controller.cliente.ClientesController;
import controller.documento.DocumentosController;
import controller.recibo.RecibosController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import view.MenuEmpleadoView;
import view.cliente.ClientesView;
import view.documento.DocumentosView;
import view.recibo.RecibosView;

public class MenuEmpleadoController extends Controller implements ActionListener{
    
    public MenuEmpleadoController(MenuEmpleadoView view){
        super(view);
        
        view.btnClientes.addActionListener(this);
        view.btnDocumentacion.addActionListener(this);
        view.btnRecibos.addActionListener(this);
    }
    
    private MenuEmpleadoView getOriginalView(){
        return (MenuEmpleadoView)this.view;
    }
    
    private void btnClientesPushed(){
        Launcher.ec.changeController(new ClientesController(new ClientesView()));
    }
    
    private void btnDocumentacionPushed(){
        Launcher.ec.changeController(new DocumentosController(new DocumentosView(),1));
    }
    
    private void btnRecibosPushed(){
        Launcher.ec.changeController(new RecibosController(new RecibosView()));
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        Object source = e.getSource();
        
        if(source == getOriginalView().btnClientes){
            btnClientesPushed();
        }
        
        else if(source == getOriginalView().btnDocumentacion){
            btnDocumentacionPushed();
        }
        
        else if(source == getOriginalView().btnRecibos){
            btnRecibosPushed();
        }
    }
    
}
