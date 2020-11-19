package controller;

import config.Controller;
import java.awt.GridLayout;
import javax.swing.JPanel;
import view.MainLayout;
import view.MenuEmpleadoView;

public class EmpleadoController {
    private final MainLayout mainView;
    private Controller controller;
    
    public EmpleadoController(){
        mainView = new MainLayout();
        mainView.setTitle("Impulsando Tu Negocio (EMPLEADO)");
        mainView.setSize(820, 480);
        mainView.setLocationRelativeTo(null);
        changeController(new MenuEmpleadoController(new MenuEmpleadoView()));
        mainView.setVisible(true);
    }
    
    public void changeController(Controller controller){
        this.controller = controller;
        changeView(this.controller.getView());
    }
    
    private void changeView(JPanel view){
        clearLayout();
        mainView.add(view);
        mainView.revalidate();
        mainView.repaint();
    }
    
    private void clearLayout(){
        mainView.getContentPane().removeAll();
        mainView.revalidate();
        mainView.repaint();
        mainView.setLayout(new GridLayout(1,1));
    }
}
