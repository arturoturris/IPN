package controller;

import config.Controller;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.Empleado;
import view.EmpleadosForm;
import view.EmpleadosView;
import view.MenuAdministradorView;

public class EmpleadosController extends Controller implements ActionListener{
    Empleado model;
    
    public EmpleadosController(EmpleadosView view){
        super(view);
        model = new Empleado(Launcher.session);
        printEmpleados();
        view.btnRegresar.addActionListener(this);
        view.btnRegistrar.addActionListener(this);
        view.btnModificar.addActionListener(this);
        view.btnConsultar.addActionListener(this);
        view.btnEliminar.addActionListener(this);
    }
    
    private EmpleadosView getOriginalView(){
        return (EmpleadosView)this.view;}
    
    public void printEmpleados(){
        try {
            ResultSet empleados = model.getEmpleados();
            DefaultTableModel tableModel = (DefaultTableModel)getOriginalView().tblEmpleados.getModel();
            tableModel.setRowCount(0);
            Vector<String> row;
            
            while(empleados.next()){
                row = new Vector<>();
                
                row.add(empleados.getString(1)); //ID EMPLEADO
                row.add(empleados.getString(2) + " " + empleados.getString(3) + " " + empleados.getString(4)); //PATERNO MATERNO NOMBRE
                row.add(empleados.getString(5));
                tableModel.addRow(row);
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmpleadosController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void btnRegistrarPushed(){
        Launcher.ac.changeController(new EmpleadosFormController(new EmpleadosForm(EmpleadosForm.formType.SIGNUP)));
    }
    
    private void btnModificarPushed(){
        int selectedRow = getOriginalView().tblEmpleados.getSelectedRow();
        
        if(selectedRow == -1){
            String message = "No se ha seleccionado un empleado a modificar.";
            JOptionPane.showMessageDialog(this.view, message, "Advertencia #004", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int columnIndex = getOriginalView().tblEmpleados.getColumn("Id Empleado").getModelIndex();
        int idEmpleado = Integer.parseInt((String)getOriginalView().tblEmpleados.getValueAt(selectedRow, columnIndex));
        
        Launcher.ac.changeController(new EmpleadosFormController(new EmpleadosForm(EmpleadosForm.formType.UPDATE),idEmpleado));
    }
    
    private void btnConsultarPushed(){
        int selectedRow = getOriginalView().tblEmpleados.getSelectedRow();
        
        if(selectedRow == -1){
            String message = "No se ha seleccionado un empleado para consultar.";
            JOptionPane.showMessageDialog(this.view, message, "Advertencia #005", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int columnIndex = getOriginalView().tblEmpleados.getColumn("Id Empleado").getModelIndex();
        int idEmpleado = Integer.parseInt((String)getOriginalView().tblEmpleados.getValueAt(selectedRow, columnIndex));
        
        Launcher.ac.changeController(new EmpleadosFormController(new EmpleadosForm(EmpleadosForm.formType.UPDATE),idEmpleado));
    }
    
    private void btnEliminarPushed(){
        int selectedRow = getOriginalView().tblEmpleados.getSelectedRow();
        
        if(selectedRow == -1){
            String message = "No se ha seleccionado un empleado a eliminar.";
            JOptionPane.showMessageDialog(this.view, message, "Advertencia #005", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int columnIndex = getOriginalView().tblEmpleados.getColumn("Id Empleado").getModelIndex();
        int idEmpleado = Integer.parseInt((String)getOriginalView().tblEmpleados.getValueAt(selectedRow, columnIndex));
        
        String message = "Eliminar empleado con ID: " + idEmpleado + "?";
        boolean confirmed = (JOptionPane.showConfirmDialog(this.view,message,"Confirmación",JOptionPane.YES_NO_OPTION) == 0);
        
        if(!confirmed)
            return;
        
        Empleado e = new Empleado(Launcher.session);
        if(!e.deleteEmpleado(idEmpleado)){
            String msg = "El empleado no ha podido ser eliminado.";
            JOptionPane.showMessageDialog(this.view,msg,"Error #005",JOptionPane.ERROR_MESSAGE);
        }else{
            String msg = "El empleado se ha eliminado éxitosamente.";
            JOptionPane.showMessageDialog(this.view,msg,"Éxito",JOptionPane.INFORMATION_MESSAGE);
            printEmpleados();
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        Object source = e.getSource();
        
        if(source == getOriginalView().btnRegresar){
            Launcher.ac.changeController(new MenuAdministradorController(new MenuAdministradorView()));}
        
        else if(source == getOriginalView().btnRegistrar){
            btnRegistrarPushed();}
        
        else if(source == getOriginalView().btnModificar){
            btnModificarPushed();}
        
        else if(source == getOriginalView().btnConsultar){
            btnConsultarPushed();}
        
        else if(source == getOriginalView().btnEliminar){
            btnEliminarPushed();}
       
    }
}
