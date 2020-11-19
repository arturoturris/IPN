package controller.recibo;

import config.Controller;
import controller.Launcher;
import controller.MenuEmpleadoController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.Recibo;
import view.MenuEmpleadoView;
import view.recibo.ReciboInfo;
import view.recibo.RecibosForm;
import view.recibo.RecibosView;

public class RecibosController extends Controller implements ActionListener{
    private final int whereAmI;
    
    public RecibosController(RecibosView view){
        super(view);

        this.whereAmI = 1;
        
        printRecibos();
        view.btnRegresar.addActionListener(this);
        view.btnGenerar.addActionListener(this);
        view.btnModificar.addActionListener(this);
        view.btnConsultar.addActionListener(this);
        view.btnEliminar.addActionListener(this);
    }
    
    public RecibosController(RecibosView view, String rfcCliente,int whereAmI){
        super(view);

        this.whereAmI = whereAmI;
        printRecibosCliente(rfcCliente);
        
        view.btnGenerar.setVisible(false);
        view.btnModificar.setVisible(false);
        view.btnRegresar.setVisible(false);
        
        view.btnRegresar.addActionListener(this);
        view.btnGenerar.addActionListener(this);
        view.btnModificar.addActionListener(this);
        view.btnConsultar.addActionListener(this);
        view.btnEliminar.addActionListener(this);
    }
    
    private RecibosView getOriginalView(){
        return (RecibosView)this.view;}
    
    private void printRecibos(){
       Recibo tmp = new Recibo();
       DefaultTableModel tableModel = (DefaultTableModel)getOriginalView().tblRecibos.getModel();
       Vector<String> row;
       DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
       
       for(Recibo r: tmp.readRecibos()){
           row = new Vector<>();
           
           row.add(Integer.toString(r.getIdRecibo()));
           row.add(r.getCliente_rfc());
           row.add(dateFormat.format(r.getFechaEmision()));
           row.add((r.isPagado()) ? "Pagado" : "Pendiente");
           
           tableModel.addRow(row);
       }
       
    }
    
    private void printRecibosCliente(String rfcCliente){
       DefaultTableModel tableModel = (DefaultTableModel)getOriginalView().tblRecibos.getModel();
       Vector<String> row;
       DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
       
       for(Recibo r: Recibo.readRecibosCliente(rfcCliente)){
           row = new Vector<>();
           
           row.add(Integer.toString(r.getIdRecibo()));
           row.add(r.getCliente_rfc());
           row.add(dateFormat.format(r.getFechaEmision()));
           row.add((r.isPagado()) ? "Pagado" : "Pendiente");
           
           tableModel.addRow(row);
       }
       
    }
    
    private void btnRegresarPushed(){
        Launcher.ec.changeController(new MenuEmpleadoController(new MenuEmpleadoView()));
    }
    
    private void btnGenerarPushed(){
        Launcher.ec.changeController(new ReciboFormController(new RecibosForm(RecibosForm.formType.SIGNUP),this.whereAmI));
    }
    
    private void btnConsultarPushed(){
        int selectedRow = getOriginalView().tblRecibos.getSelectedRow();
        
        if(selectedRow == -1){
            String message = "No se ha seleccionado un recibo para consultar.";
            JOptionPane.showMessageDialog(this.view, message, "Advertencia #005", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int columnIndex = getOriginalView().tblRecibos.getColumn("ID Recibo").getModelIndex();
        int idRecibo = Integer.parseInt((String)getOriginalView().tblRecibos.getValueAt(selectedRow, columnIndex));
        
        if(this.whereAmI == 1)
            Launcher.ac.changeController(new ReciboInfoController(new ReciboInfo(),idRecibo,this.whereAmI));
        else
            Launcher.ec.changeController(new ReciboInfoController(new ReciboInfo(),idRecibo,this.whereAmI));
    }
    
    private void btnModificarPushed(){
        int selectedRow = getOriginalView().tblRecibos.getSelectedRow();
        
        if(selectedRow == -1){
            String message = "No se ha seleccionado un recibo a modificar.";
            JOptionPane.showMessageDialog(this.view, message, "Advertencia #004", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int columnIndex = getOriginalView().tblRecibos.getColumn("ID Recibo").getModelIndex();
        int idRecibo = Integer.parseInt((String)getOriginalView().tblRecibos.getValueAt(selectedRow, columnIndex));
        
        Launcher.ec.changeController(new ReciboFormController(new RecibosForm(RecibosForm.formType.UPDATE),idRecibo));
    }
    
    private void btnEliminarPushed(){
        int selectedRow = getOriginalView().tblRecibos.getSelectedRow();
        
        if(selectedRow == -1){
            String message = "No se ha seleccionado un recibo para eliminar.";
            JOptionPane.showMessageDialog(this.view, message, "Advertencia #005", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int columnIndex = getOriginalView().tblRecibos.getColumn("ID Recibo").getModelIndex();
        int idRecibo = Integer.parseInt((String)getOriginalView().tblRecibos.getValueAt(selectedRow, columnIndex));
        
        String message = "Eliminar recibo con ID: " + idRecibo + "?";
        boolean confirmed = (JOptionPane.showConfirmDialog(this.view,message,"Confirmación",JOptionPane.YES_NO_OPTION) == 0);
        
        if(!confirmed)
            return;
        
        Recibo r = new Recibo(idRecibo);
        if(!r.deleteRecibo()){
            String msg = "El recibo no ha podido ser eliminado.";
            JOptionPane.showMessageDialog(this.view,msg,"Error #005",JOptionPane.ERROR_MESSAGE);
        }else{
            String msg = "El recibo se ha eliminado éxitosamente.";
            JOptionPane.showMessageDialog(this.view,msg,"Éxito",JOptionPane.INFORMATION_MESSAGE);
            printRecibos();
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
    Object source = e.getSource();
        
        if(source == getOriginalView().btnRegresar){
            btnRegresarPushed();}
        
        else if(source == getOriginalView().btnGenerar){
            btnGenerarPushed();}
        
        else if(source == getOriginalView().btnModificar){
            btnModificarPushed();}
        
        else if(source == getOriginalView().btnConsultar){
            btnConsultarPushed();}
        
        else if(source == getOriginalView().btnEliminar){
            btnEliminarPushed();}
    }
}
