package controller;

import config.Controller;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import view.EmpleadosView;
import view.MenuAdministradorView;

public class MenuAdministradorController extends Controller implements ActionListener{
    
    public MenuAdministradorController(MenuAdministradorView view){
        super(view);
        
        view.btnEmpleados.addActionListener(this);
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
    }
    
}
