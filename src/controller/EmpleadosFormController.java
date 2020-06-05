package controller;

import config.Controller;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Empleado;
import view.EmpleadosForm;
import view.EmpleadosView;

public class EmpleadosFormController extends Controller implements ActionListener{
    public EmpleadosFormController(EmpleadosForm view){
        super(view);
        
        view.btnCancelar.addActionListener(this);
        view.btnRegistrar.addActionListener(this);
    }
    
    public EmpleadosFormController(EmpleadosForm view,int idEmpleado){
        super(view);
        
        loadEmpleadoOnView(idEmpleado);
        
        view.btnCancelar.addActionListener(this);
        view.btnRegistrar.addActionListener(this);
    }
    
    public EmpleadosForm getOriginalView(){
        return (EmpleadosForm)this.view;}
    
    private boolean emptyFields(){
        if(getOriginalView().txtNombre.getText().equals(""))
            return true;
        else if(getOriginalView().txtPaterno.getText().equals(""))
            return true;
        else if(getOriginalView().txtMaterno.getText().equals(""))
            return true;
        else if(getOriginalView().txtPuesto.getText().equals(""))
            return true;
        else if(getOriginalView().dateFechaAlta.getDate() == null)
            return true;
        else
            return false;
    }
    
    private void blockSection(boolean b){
        
    }
    
    private boolean signUpProcess(){
        boolean success = false;
        
        if(emptyFields()){
            String message = "Existen campos obligatorios sin llenar.";
            JOptionPane.showMessageDialog(this.view, message, "Advertencia #002", JOptionPane.WARNING_MESSAGE);
            return success;
        }
        
        success = signUpEmpleado();
        
        if(!success){
            String message = "Ocurrió un error al registrar el empleado.";
            JOptionPane.showMessageDialog(this.view, message, "Error #005", JOptionPane.ERROR_MESSAGE);
        }else{
            String message = "El empleado se registró exitosamente.";
            JOptionPane.showMessageDialog(this.view, message, "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
        }
        
        return success;
    }
    
    private boolean signUpEmpleado(){
        boolean signedUp;
        
        Empleado newEmpleado = new Empleado(Launcher.session);
        String nombre,paterno,materno,puesto,horario,estado;
        char sexo;
        double salario;
        Date fechaAlta;
        
        nombre = getOriginalView().txtNombre.getText();
        paterno = getOriginalView().txtPaterno.getText();
        materno = getOriginalView().txtMaterno.getText();
        sexo = getOriginalView().radioH.isSelected() ? 'H' : 'M';
        puesto = getOriginalView().txtPuesto.getText();
        horario = getOriginalView().txtHorario.getText();
        salario = (double)getOriginalView().spnSalario.getValue();
        fechaAlta = getOriginalView().dateFechaAlta.getDate();
        estado = getOriginalView().cmbEstado.getItemAt(getOriginalView().cmbEstado.getSelectedIndex());
        
        signedUp = newEmpleado.signUpEmpleado(nombre, paterno, materno, sexo, puesto, horario, salario, fechaAlta, estado);
        
        return signedUp;
    }
    
    private boolean updateProcess(){
        boolean success = updateEmpleado();
        
        if(!success){
            String message = "Ocurrió un error al modificar el empleado.";
            JOptionPane.showMessageDialog(this.view, message, "Error #006", JOptionPane.ERROR_MESSAGE);
        }else{
            String message = "El empleado se modificó exitosamente.";
            JOptionPane.showMessageDialog(this.view, message, "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
        }
        
        return success;
    }
    
    private boolean updateEmpleado(){
        boolean updated;
        Empleado empleado = new Empleado(Launcher.session);
        int idEmpleado;
        String nombre,paterno,materno,puesto,horario,estado;
        char sexo;
        double salario;
        Date fechaAlta;
        
        idEmpleado = Integer.parseInt(getOriginalView().txtIdEmpleado.getText());
        nombre = getOriginalView().txtNombre.getText();
        paterno = getOriginalView().txtPaterno.getText();
        materno = getOriginalView().txtMaterno.getText();
        sexo = getOriginalView().radioH.isSelected() ? 'H' : 'M';
        puesto = getOriginalView().txtPuesto.getText();
        horario = getOriginalView().txtHorario.getText();
        salario = (double)getOriginalView().spnSalario.getValue();
        fechaAlta = getOriginalView().dateFechaAlta.getDate();
        estado = getOriginalView().cmbEstado.getItemAt(getOriginalView().cmbEstado.getSelectedIndex());
        
        updated = empleado.updateEmpleado(idEmpleado, nombre, paterno, materno, sexo, puesto, horario, salario, fechaAlta, estado);
        
        return updated;
    }
    
    private void loadEmpleadoOnView(int idEmpleado){
        try {
            Empleado e = new Empleado(Launcher.session);
            ResultSet result = e.getEmpleado(idEmpleado);
            EmpleadosForm v = getOriginalView();
            
            result.next();
            v.txtIdEmpleado.setText(result.getString("IdEmpleado"));
            v.txtNombre.setText(result.getString("Nombre"));
            v.txtPaterno.setText(result.getString("Paterno"));
            v.txtMaterno.setText(result.getString("Materno"));
            
            if(result.getString("Sexo").equals("H"))
                v.radioH.setSelected(true);
            else 
                v.radioM.setSelected(true);
            
            v.txtPuesto.setText(result.getString("Puesto"));
            v.txtHorario.setText(result.getString("Horario"));
            v.spnSalario.setValue(result.getDouble("Salario"));
            v.dateFechaAlta.setDate(result.getDate("FechaAlta"));
            v.cmbEstado.setSelectedItem(result.getString("Estado"));
            
        } catch (SQLException ex) {
            Logger.getLogger(EmpleadosFormController.class.getName()).log(Level.SEVERE, null, ex);
        }  
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        Object source = e.getSource();
        
        if(source == getOriginalView().btnCancelar){
            Launcher.ac.changeController(new EmpleadosController(new EmpleadosView()));
        }
        
        else if(source == getOriginalView().btnRegistrar){
            if(getOriginalView().type == EmpleadosForm.formType.SIGNUP){
                if(signUpProcess()){
                    Launcher.ac.changeController(new EmpleadosController(new EmpleadosView()));
                }
            }
            
            else if(getOriginalView().type == EmpleadosForm.formType.UPDATE){
                if(updateProcess()){
                    Launcher.ac.changeController(new EmpleadosController(new EmpleadosView()));
                }
            }
        }
    }
}
