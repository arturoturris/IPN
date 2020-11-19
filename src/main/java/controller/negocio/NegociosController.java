package controller.negocio;

import config.Controller;
import controller.Launcher;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.Negocio;
import view.negocio.NegocioInfo;
import view.negocio.NegociosForm;
import view.negocio.NegociosView;

public class NegociosController extends Controller implements ActionListener{
    String rfcCliente;
    
    public NegociosController(NegociosView view,String rfcCliente){
        super(view);
        this.rfcCliente = rfcCliente;
        
        if(Launcher.session.getTipoSesion().equals("Empleado")){
            view.btnRegistrar.setVisible(false);
            view.btnModificar.setVisible(false);
            view.btnEliminar.setVisible(false);
        }
        
        printNegocios();
        view.btnRegistrar.addActionListener(this);
        view.btnModificar.addActionListener(this);
        view.btnConsultar.addActionListener(this);
        view.btnEliminar.addActionListener(this);
    }
    
    private NegociosView getOriginalView(){
        return (NegociosView)this.view;}
    
    private void printNegocios(){
        Negocio tmpNegocio = new Negocio(this.rfcCliente);
        DefaultTableModel tableModel = (DefaultTableModel)getOriginalView().tblNegocios.getModel();
        tableModel.setRowCount(0);
        Vector<String> row;
        
        for(Negocio n: tmpNegocio.readNegocios()){
            row = new Vector<>();
            
            row.add(Integer.toString(n.getIdNegocio()));
            row.add(n.getNombre());
            tableModel.addRow(row);
        }
    }
    
    private void btnRegistrarPushed(){
        Launcher.ac.changeController(new NegociosFormController(new NegociosForm(NegociosForm.formType.SIGNUP),this.rfcCliente));}
    
    private void btnModificarPushed(){
        int selectedRow = getOriginalView().tblNegocios.getSelectedRow();
        
        if(selectedRow == -1){
            String message = "No se ha seleccionado un negocio a modificar.";
            JOptionPane.showMessageDialog(this.view, message, "Advertencia #004", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int columnIndex = getOriginalView().tblNegocios.getColumn("ID Negocio").getModelIndex();
        int idNegocio = Integer.parseInt((String)getOriginalView().tblNegocios.getValueAt(selectedRow, columnIndex));
        
        Launcher.ac.changeController(new NegociosFormController(new NegociosForm(NegociosForm.formType.UPDATE),idNegocio,this.rfcCliente));
    }
    
    private void btnConsultarPushed(){
        int selectedRow = getOriginalView().tblNegocios.getSelectedRow();
        
        if(selectedRow == -1){
            String message = "No se ha seleccionado un negocio para consultar.";
            JOptionPane.showMessageDialog(this.view, message, "Advertencia #005", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int columnIndex = getOriginalView().tblNegocios.getColumn("ID Negocio").getModelIndex();
        int idNegocio = Integer.parseInt((String)getOriginalView().tblNegocios.getValueAt(selectedRow, columnIndex));
        
        if(Launcher.session.getTipoSesion().equals("Administrador"))
            Launcher.ac.changeController(new NegocioInfoController(new NegocioInfo(),idNegocio));
        else
            Launcher.ec.changeController(new NegocioInfoController(new NegocioInfo(),idNegocio));
    }
    
    private void btnEliminarPushed(){
        int selectedRow = getOriginalView().tblNegocios.getSelectedRow();
        
        if(selectedRow == -1){
            String message = "No se ha seleccionado un negocio a eliminar.";
            JOptionPane.showMessageDialog(this.view, message, "Advertencia #005", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int columnIndex = getOriginalView().tblNegocios.getColumn("ID Negocio").getModelIndex();
        int idNegocio = Integer.parseInt((String)getOriginalView().tblNegocios.getValueAt(selectedRow, columnIndex));
        
        String message = "Eliminar negocio con ID: " + idNegocio + "?";
        boolean confirmed = (JOptionPane.showConfirmDialog(this.view,message,"Confirmación",JOptionPane.YES_NO_OPTION) == 0);
        
        if(!confirmed)
            return;
        
        Negocio n = new Negocio(idNegocio);
        if(!n.deleteNegocio()){
            String msg = "El negocio no ha podido ser eliminado.";
            JOptionPane.showMessageDialog(this.view,msg,"Error #005",JOptionPane.ERROR_MESSAGE);
        }else{
            String msg = "El negocio se ha eliminado éxitosamente.";
            JOptionPane.showMessageDialog(this.view,msg,"Éxito",JOptionPane.INFORMATION_MESSAGE);
            printNegocios();
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
    Object source = e.getSource();
        
        if(source == getOriginalView().btnRegistrar){
            btnRegistrarPushed();}
        
        else if(source == getOriginalView().btnModificar){
            btnModificarPushed();}
        
        else if(source == getOriginalView().btnConsultar){
            btnConsultarPushed();}
        
        else if(source == getOriginalView().btnEliminar){
            btnEliminarPushed();}
    }
}
