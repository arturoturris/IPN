package controller;

import java.sql.*;
import config.UserSession;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import model.Usuario;
import view.LoginView;

public class Launcher implements ActionListener {
    private final LoginView view;
    public static AdministradorController ac;
    public static EmpleadoController ec;
    public static UserSession session;
    
    public Launcher(){
        this.view = new LoginView();
        
        //ACTION LISTENERS
        view.btnIngresar.addActionListener(this);
    }
    
    public static void main(String[] args){
        Launcher launcher = new Launcher();
        launcher.initLoginView();
    }
    
    public void initLoginView(){
        view.setVisible(true);
    }
    
    public void validateEntry(){
        if(emptyFields()){
            final JPanel panel = new JPanel();
            String message = "No se han rellenado todos los campos.";
            JOptionPane.showMessageDialog(panel, message, "Error #001", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        UserSession tmpSession = new UserSession("jdbc:mysql://localhost:3306/ipn","user","user");
        
        //ERROR WHILE CONNECTING
        if(!tmpSession.connect()){
            final JPanel panel = new JPanel();
            String message = "No se ha podido conectar con la base de datos.";
            JOptionPane.showMessageDialog(panel, message, "Error #002", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        //CHECK PERMISION
        boolean authorized = UserSession.authorizeLogin(tmpSession, view.txtUsuario.getText(), String.valueOf(view.txtContraseña.getPassword()));

        //TEST MESSAGES
        final JPanel panel = new JPanel();
        String message;

        if(!authorized){
            message = "El usuario o contraseña es incorrecto.";
            JOptionPane.showMessageDialog(panel, message, "Advertencia #001", JOptionPane.WARNING_MESSAGE);
        } else{
            message = "Datos correctos.";
            JOptionPane.showMessageDialog(panel, message, "Sesión Aprovada", JOptionPane.INFORMATION_MESSAGE);
            
            //CREATING NEW SESSION FOR EMPLEADO
            String []userAttributes = tmpSession.getIdEmpleadoTipoEmpleado(view.txtUsuario.getText());
            int idEmpelado = Integer.parseInt(userAttributes[0]);
            String tipoEmpleado = userAttributes[1];
            
            session = new UserSession("jdbc:mysql://localhost:3306/ipn","root","");
            if(!session.connect()){
                message = "No se pudo establecer la conexión con la base de datos.";
                JOptionPane.showMessageDialog(panel, message, "Error #003", JOptionPane.ERROR_MESSAGE);
            }
            session.setIdEmpleado(idEmpelado);
            session.setTipoSesion(tipoEmpleado);
            
            if(tipoEmpleado.equals("Administrador")){
                ac = new AdministradorController();
            }else if(tipoEmpleado.equals("Empleado")){
                ec = new EmpleadoController();
            }
            
            view.dispose();
        }
    }
    
    private boolean emptyFields(){
        if(view.txtUsuario.getText().trim().isEmpty())
                return true;
        else if(view.txtContraseña.getPassword().length == 0)
                return true;
        else
            return false;
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        Object source = e.getSource();
        
        if(source == view.btnIngresar){
            validateEntry();
        }
    }
}
