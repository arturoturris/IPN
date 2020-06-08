package controller.empleado;

import config.Controller;
import controller.Launcher;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import model.Empleado;
import model.Usuario;
import view.empleado.EmpleadoInfo;
import view.empleado.EmpleadosView;

public class EmpleadosInfoController extends Controller implements ActionListener{
    private final int idEmpleado;
    
    public EmpleadosInfoController(EmpleadoInfo view,int idEmpleado){
        super(view);
        this.idEmpleado = idEmpleado;
        printInfo();
        view.btnCrearInicio.addActionListener(this);
        view.btnRegresar.addActionListener(this);
    }
    
    EmpleadoInfo getOriginalView(){
        return (EmpleadoInfo)view;}
    
    private void printInfo(){
        try {
            //VIEW
            EmpleadoInfo v = getOriginalView();
            //DATE FORMAT
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            
            //PRINTING usuario INFO
            Usuario usuario = new Usuario(idEmpleado);
            usuario.readUsuario();
            v.txtUsuario.setText(usuario.getNombreUsuario());
            v.txtContraseña.setText(usuario.getContrasegna());
            v.txtUltimoInicio.setText(sdf.format(usuario.getUltimaEntrada()));
            v.txtTipo.setText(usuario.getTipo());
            
            //PRINTG empleado INFO
            Empleado empleado = new Empleado(Launcher.session);
            ResultSet result = empleado.getEmpleado(idEmpleado);
            result.next();
            
            v.txtId.setText(result.getString("IdEmpleado"));
            v.txtPuesto.setText(result.getString("Puesto"));
            v.txtHorario.setText(result.getString("Horario"));
            v.txtSalario.setText("$" + Double.toString(result.getDouble("Salario")));
            v.txtFechaAlta.setText(sdf.format(result.getDate("FechaAlta")));
            v.txtEstado.setText(result.getString("Estado"));
            
            //PRINTING persona INFO
            v.txtNombre.setText(result.getString("Nombre"));
            v.txtPaterno.setText(result.getString("Paterno"));
            v.txtMaterno.setText(result.getString("Materno"));
            v.txtSexo.setText(result.getString("Sexo"));
        } catch (SQLException ex) {
            Logger.getLogger(EmpleadosInfoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void btnCrearInicioPushed(){
        Usuario empleado = new Usuario(idEmpleado);
        String nuevoUsuario, nuevaContrasegna, nuevoTipo;
        String mensaje, titulo;
        int tipoMensaje;
        boolean inicioCreado;
        
        nuevoUsuario = JOptionPane.showInputDialog(view, "Ingrese el usuario", "Inicio de sesión", JOptionPane.INFORMATION_MESSAGE);
        nuevaContrasegna = JOptionPane.showInputDialog(view, "Ingrese la contraseña", "Inicio de sesión", JOptionPane.INFORMATION_MESSAGE);
        nuevoTipo = JOptionPane.showInputDialog(view, "Ingrese el tipo de sesión", "Inicio de sesión", JOptionPane.INFORMATION_MESSAGE);
        
        empleado.setUsuario(nuevoUsuario);
        empleado.setContrasegna(nuevaContrasegna);
        empleado.setTipo(nuevoTipo);
        inicioCreado = empleado.createUsuario();
        
        if(!inicioCreado){
            mensaje = "Ha existido un error al crear el incio de sesión.";
            titulo = "Error #007";
            tipoMensaje = JOptionPane.ERROR_MESSAGE;
        }
        else{
            mensaje = "Se ha creado el inicio de sesión éxitosamente";
            titulo = "Éxito";
            tipoMensaje = JOptionPane.INFORMATION_MESSAGE;
        }
        
        JOptionPane.showMessageDialog(view, mensaje, titulo, tipoMensaje);
        
        if(inicioCreado){
            printInfo();
        }
    }
    
    private void btnRegresarPushed(){
        Launcher.ac.changeController(new EmpleadosController(new EmpleadosView()));}
    
     @Override
     public void actionPerformed(ActionEvent e){
         Object source = e.getSource();
         
         if(source == getOriginalView().btnCrearInicio){
             btnCrearInicioPushed();}
         
         else if(source == getOriginalView().btnRegresar){
            btnRegresarPushed();}
     }
}
