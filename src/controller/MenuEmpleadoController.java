package controller;

import controller.empleado.EmpleadosController;
import config.Controller;
import controller.cliente.ClientesController;
import controller.productoservicio.ProductosServiciosController;
import controller.recibo.RecibosController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import view.empleado.EmpleadosView;
import view.MenuEmpleadoView;
import view.cliente.ClientesView;
import view.productoservicio.ProductosServiciosView;
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
            
        }
        
        else if(source == getOriginalView().btnRecibos){
            btnRecibosPushed();
        }
    }
    
}
