package controller;

import controller.empleado.EmpleadosController;
import config.Controller;
import controller.cliente.ClientesController;
import controller.productoservicio.ProductosServiciosController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import view.empleado.EmpleadosView;
import view.MenuAdministradorView;
import view.cliente.ClientesView;
import view.productoservicio.ProductosServiciosView;

public class MenuAdministradorController extends Controller implements ActionListener{
    
    public MenuAdministradorController(MenuAdministradorView view){
        super(view);
        
        view.btnEmpleados.addActionListener(this);
        view.btnClientes.addActionListener(this);
        view.btnProductosServicios.addActionListener(this);
    }
    
    private MenuAdministradorView getOriginalView(){
        return (MenuAdministradorView)this.view;
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        Object source = e.getSource();
        
        if(source == getOriginalView().btnEmpleados){
            Launcher.ac.changeController(new EmpleadosController(new EmpleadosView()));
        }
        
        else if(source == getOriginalView().btnClientes){
            Launcher.ac.changeController(new ClientesController(new ClientesView()));
        }
        
        else if(source == getOriginalView().btnProductosServicios){
            Launcher.ac.changeController(new ProductosServiciosController(new ProductosServiciosView()));
        }
    }
    
}
