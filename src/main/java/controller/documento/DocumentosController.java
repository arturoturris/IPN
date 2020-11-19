package controller.documento;

import config.Controller;
import controller.Launcher;
import controller.MenuEmpleadoController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.Documento;
import model.TipoDocumento;
import view.MenuEmpleadoView;
import view.documento.DocumentoInfo;
import view.documento.DocumentosForm;
import view.documento.DocumentosView;

public class DocumentosController extends Controller implements ActionListener{
    private final int whereAmI;
    
    public DocumentosController(DocumentosView view, int whereAmI){
        super(view);
        
        this.whereAmI = whereAmI;
        
        if(whereAmI == 2){
            view.btnRegresar.setVisible(false);
            view.btnAgregar.setVisible(false);
        }
        
        printDocumentos();
        view.btnRegresar.addActionListener(this);
        view.btnAgregar.addActionListener(this);
        view.btnModificar.addActionListener(this);
        view.btnConsultar.addActionListener(this);
        view.btnEliminar.addActionListener(this);
    }
    
    private DocumentosView getOriginalView(){
        return (DocumentosView)this.view;}
    
    public void printDocumentos(){
        DefaultTableModel tableModel = (DefaultTableModel)getOriginalView().tblDocumentos.getModel();
        tableModel.setRowCount(0);
        Vector<String> row;
        Format formatter = new SimpleDateFormat("dd-MM-yyyy");
        
        Documento tmpDocumento = new Documento();
        TipoDocumento tmpTipoDocumento = new TipoDocumento();
        ArrayList<TipoDocumento> tipoDocumentos = tmpTipoDocumento.readTipoDocumentos();
        String tipoDocumento = null;
        
        for(Documento d: tmpDocumento.readDocumentos()){
            row = new Vector<>();
            
            for(TipoDocumento td: tipoDocumentos){
                if(td.getIdTipoDocumento() == d.getIdTipoDocumento()){
                    tipoDocumento = td.getNombre();
                    break;
                }
            }
            
            row.add(Integer.toString(d.getIdDocumento()));
            row.add(tipoDocumento);
            row.add(d.getCliente_Rfc());
            row.add(formatter.format(d.getFechaExpiracion()));
            
            tableModel.addRow(row);
        }
    }
    
    private void btnAgregarPushed(){
        if(whereAmI == 1)
            Launcher.ec.changeController(new DocumentosFormController(new DocumentosForm(DocumentosForm.formType.SIGNUP),1));
        else
        {}
    }
    
    private void btnModificarPushed(){
        int selectedRow = getOriginalView().tblDocumentos.getSelectedRow();
        
        if(selectedRow == -1){
            String message = "No se ha seleccionado un documento a modificar.";
            JOptionPane.showMessageDialog(this.view, message, "Advertencia #004", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int columnIndex = getOriginalView().tblDocumentos.getColumn("ID Documento").getModelIndex();
        int idDocumento = Integer.parseInt((String)getOriginalView().tblDocumentos.getValueAt(selectedRow, columnIndex));
        
        Launcher.ec.changeController(new DocumentosFormController(new DocumentosForm(DocumentosForm.formType.UPDATE),idDocumento,this.whereAmI));
    }
    
    private void btnConsultarPushed(){
        int selectedRow = getOriginalView().tblDocumentos.getSelectedRow();
        
        if(selectedRow == -1){
            String message = "No se ha seleccionado un documento para consultar.";
            JOptionPane.showMessageDialog(this.view, message, "Advertencia #005", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int columnIndex = getOriginalView().tblDocumentos.getColumn("ID Documento").getModelIndex();
        int idDocumento = Integer.parseInt((String)getOriginalView().tblDocumentos.getValueAt(selectedRow, columnIndex));
        
       Launcher.ec.changeController(new DocumentoInfoController(new DocumentoInfo(),idDocumento,whereAmI));
    }
    
    private void btnEliminarPushed(){
        int selectedRow = getOriginalView().tblDocumentos.getSelectedRow();
        
        if(selectedRow == -1){
            String message = "No se ha seleccionado un documento a eliminar.";
            JOptionPane.showMessageDialog(this.view, message, "Advertencia #005", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int columnIndex = getOriginalView().tblDocumentos.getColumn("ID Documento").getModelIndex();
        int idDocumento = Integer.parseInt((String)getOriginalView().tblDocumentos.getValueAt(selectedRow, columnIndex));
        
        String message = "Eliminar documento con ID: " + idDocumento + "?";
        boolean confirmed = (JOptionPane.showConfirmDialog(this.view,message,"Confirmación",JOptionPane.YES_NO_OPTION) == 0);
        
        if(!confirmed)
            return;
        
        Documento n = new Documento(idDocumento);
        if(!n.deleteDocumento()){
            String msg = "El documento no ha podido ser eliminado.";
            JOptionPane.showMessageDialog(this.view,msg,"Error #005",JOptionPane.ERROR_MESSAGE);
        }else{
            String msg = "El documento se ha eliminado éxitosamente.";
            JOptionPane.showMessageDialog(this.view,msg,"Éxito",JOptionPane.INFORMATION_MESSAGE);
            printDocumentos();
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        Object source = e.getSource();
        
        if(source == getOriginalView().btnRegresar){
            Launcher.ec.changeController(new MenuEmpleadoController(new MenuEmpleadoView()));}
        
        else if(source == getOriginalView().btnAgregar){
            btnAgregarPushed();}
        
        else if(source == getOriginalView().btnModificar){
            btnModificarPushed();}
        
        else if(source == getOriginalView().btnConsultar){
            btnConsultarPushed();}
        
        else if(source == getOriginalView().btnEliminar){
            btnEliminarPushed();}
       
    }
}
