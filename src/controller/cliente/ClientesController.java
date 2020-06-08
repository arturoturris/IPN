package controller.cliente;

import config.Controller;
import controller.Launcher;
import controller.MenuAdministradorController;
import controller.MenuEmpleadoController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.Cliente;
import view.MenuAdministradorView;
import view.MenuEmpleadoView;
import view.cliente.ClienteInfo;
import view.cliente.ClientesForm;
import view.cliente.ClientesView;

public class ClientesController extends Controller implements ActionListener{
    public ClientesController(ClientesView view){
        super(view);

        if(Launcher.session.getTipoSesion().equals("Empleado")){
            view.btnRegistrar.setVisible(false);
            view.btnModificar.setVisible(false);
            view.btnEliminar.setVisible(false);
        }
        
        printClientes();
        view.btnRegresar.addActionListener(this);
        view.btnRegistrar.addActionListener(this);
        view.btnModificar.addActionListener(this);
        view.btnConsultar.addActionListener(this);
        view.btnEliminar.addActionListener(this);
    }
    
    private ClientesView getOriginalView(){
        return (ClientesView)this.view;}
    
    private void printClientes(){
        Cliente tmp = new Cliente();
        DefaultTableModel tableModel = (DefaultTableModel)getOriginalView().tblClientes.getModel();
        tableModel.setRowCount(0);
        Vector<String> row;
        
        for(Cliente c: tmp.readClientes()){
            row = new Vector<>();
            row.add(c.getRfc());
            row.add(c.getPaterno() + " " + c.getMaterno() + " " + c.getNombre());
            row.add(c.getRegimen());
            tableModel.addRow(row);
        }
    }
    
    private void btnRegistrarPushed(){
        Launcher.ac.changeController(new ClientesFormController(new ClientesForm(ClientesForm.formType.SIGNUP)));}
    
    private void btnModificarPushed(){
        int selectedRow = getOriginalView().tblClientes.getSelectedRow();
        
        if(selectedRow == -1){
            String message = "No se ha seleccionado un cliente a modificar.";
            JOptionPane.showMessageDialog(this.view, message, "Advertencia #004", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int columnIndex = getOriginalView().tblClientes.getColumn("RFC").getModelIndex();
        String rfcCliente = (String)getOriginalView().tblClientes.getValueAt(selectedRow, columnIndex);
        
        Launcher.ac.changeController(new ClientesFormController(new ClientesForm(ClientesForm.formType.UPDATE),rfcCliente));
    }
    
    private void btnConsultarPushed(){
        int selectedRow = getOriginalView().tblClientes.getSelectedRow();
        
        if(selectedRow == -1){
            String message = "No se ha seleccionado un cliente para consultar.";
            JOptionPane.showMessageDialog(this.view, message, "Advertencia #005", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int columnIndex = getOriginalView().tblClientes.getColumn("RFC").getModelIndex();
        String rfc = (String)getOriginalView().tblClientes.getValueAt(selectedRow, columnIndex);
        
        if(Launcher.session.getTipoSesion().equals("Administrador"))
            Launcher.ac.changeController(new ClienteInfoController(new ClienteInfo(),rfc));
        else
            Launcher.ec.changeController(new ClienteInfoController(new ClienteInfo(),rfc));
    }
    
    private void btnEliminarPushed(){
        int selectedRow = getOriginalView().tblClientes.getSelectedRow();
        
        if(selectedRow == -1){
            String message = "No se ha seleccionado un cliente para eliminar.";
            JOptionPane.showMessageDialog(this.view, message, "Advertencia #005", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int columnIndex = getOriginalView().tblClientes.getColumn("RFC").getModelIndex();
        String rfcCliente = (String)getOriginalView().tblClientes.getValueAt(selectedRow, columnIndex);
        
        String message = "Eliminar cliente con RFC: " + rfcCliente + "?";
        boolean confirmed = (JOptionPane.showConfirmDialog(this.view,message,"Confirmación",JOptionPane.YES_NO_OPTION) == 0);
        
        if(!confirmed)
            return;
        
        Cliente c = new Cliente(rfcCliente);
        if(!c.deleteCliente()){
            String msg = "El cliente no ha podido ser eliminado.";
            JOptionPane.showMessageDialog(this.view,msg,"Error #005",JOptionPane.ERROR_MESSAGE);
        }else{
            String msg = "El cliente se ha eliminado éxitosamente.";
            JOptionPane.showMessageDialog(this.view,msg,"Éxito",JOptionPane.INFORMATION_MESSAGE);
            printClientes();
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
    Object source = e.getSource();
        
        if(source == getOriginalView().btnRegresar){
            if(Launcher.session.getTipoSesion().equals("Administrador"))
                Launcher.ac.changeController(new MenuAdministradorController(new MenuAdministradorView()));
            else
                Launcher.ec.changeController(new MenuEmpleadoController(new MenuEmpleadoView()));
        }
        
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
