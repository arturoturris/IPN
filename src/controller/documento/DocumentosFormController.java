package controller.documento;

import config.Controller;
import controller.Launcher;
import controller.cliente.ClienteInfoController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import model.Cliente;
import model.Documento;
import model.TipoDocumento;
import view.cliente.ClienteInfo;
import view.documento.DocumentosForm;
import view.documento.DocumentosView;

public class DocumentosFormController extends Controller implements ActionListener{
    private final int idDocumento;
    private final int whereAmI;
    
    //CONTROLLER TO CREATE FORM
    public DocumentosFormController(DocumentosForm view,int whereAmI){
        super(view);
        
        this.idDocumento = -1;
        this.whereAmI = whereAmI;
        
        loadTipoDocumentos();
        view.btnFileChooser.addActionListener(this);
        view.btnCancelar.addActionListener(this);
        view.btnAgregar.addActionListener(this);
    }
    
    //CONTROLLER TO MODIFY FORM
    public DocumentosFormController(DocumentosForm view,int idDocumento,int whereAmI){
        super(view);
        
        this.idDocumento = idDocumento;
        this.whereAmI = whereAmI;
        
        loadTipoDocumentos();
        loadDocumentoOnView();
        view.btnFileChooser.addActionListener(this);
        view.btnCancelar.addActionListener(this);
        view.btnAgregar.addActionListener(this);
    }
    
    public DocumentosForm getOriginalView(){
        return (DocumentosForm)this.view;}
    
    private boolean emptyFields(){
        if(getOriginalView().type == DocumentosForm.formType.SIGNUP){
            if(getOriginalView().txtFilePath.getText().equals(""))
                return true;
        }
        if(getOriginalView().txtCliente.getText().equals(""))
            return true;
        if(getOriginalView().txtFechaExpedicion.getDate() == null)
            return true;
        if(getOriginalView().txtFechaExpiracion.getDate() == null)
            return true;
        else
            return false;
    }
    
    private void loadTipoDocumentos(){
        DocumentosForm v = getOriginalView();
        v.cmbTipoDocumento.removeAllItems();
        TipoDocumento tmpTipoDocumento = new TipoDocumento();
        
        for(TipoDocumento td: tmpTipoDocumento.readTipoDocumentos()){
            v.cmbTipoDocumento.addItem(td.getNombre());
        }
    }
    
    private int getIdTipoDocumento(String name){
        int id = -1;
        DocumentosForm v = getOriginalView();
        v.cmbTipoDocumento.removeAllItems();
        TipoDocumento tmpTipoDocumento = new TipoDocumento();
        
        for(TipoDocumento td: tmpTipoDocumento.readTipoDocumentos()){
            if(td.getNombre().contains(name)){
                id = td.getIdTipoDocumento();
                break;
            }
        }
        
        return id;
    }
    
    private void loadDocumentoOnView(){
        DocumentosForm v = getOriginalView();
        Documento d = new Documento(this.idDocumento);
        String tipoDocumento = null;
        TipoDocumento tmpTipoDocumento = new TipoDocumento();

        d.readDocumento();
        
        for(TipoDocumento tp: tmpTipoDocumento.readTipoDocumentos()){
            if(tp.getIdTipoDocumento() == d.getIdTipoDocumento()){
                tipoDocumento = tp.getNombre();
                break;
            }
        }

        v.cmbTipoDocumento.setSelectedItem(tipoDocumento);
        v.txtCliente.setText(d.getCliente_Rfc());
        v.txtFechaExpedicion.setDate(d.getFechaExpedicion());
        v.txtFechaExpiracion.setDate(d.getFechaExpiracion());
        v.txtContrasegna.setText(d.getContrasegna());
    }

    private boolean addProcess(){
        boolean success = false;
        
        if(emptyFields()){
            String message = "Existen campos obligatorios sin llenar.";
            JOptionPane.showMessageDialog(this.view, message, "Advertencia #002", JOptionPane.WARNING_MESSAGE);
            return success;
        }
        
        if(!clienteExists()){
            String message = "El cliente especificado no existe.";
            JOptionPane.showMessageDialog(this.view, message, "Advertencia #009", JOptionPane.WARNING_MESSAGE);
            return success;
        }
        
        success = addDocumento();
        
        if(!success){
            String message = "Ocurrió un error al agregar el documento.";
            JOptionPane.showMessageDialog(this.view, message, "Error #005", JOptionPane.ERROR_MESSAGE);
        }else{
            String message = "El documento se agregó exitosamente.";
            JOptionPane.showMessageDialog(this.view, message, "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
        }
        
        return success;
    }
    
    private boolean addDocumento(){
        boolean added;
        DocumentosForm v = getOriginalView();
        Documento newDocumento = new Documento();
        
        newDocumento.setCliente_Rfc(v.txtCliente.getText());
        newDocumento.setIdTipoDocumento(getIdTipoDocumento((String)v.cmbTipoDocumento.getSelectedItem()));
        newDocumento.setFechaExpedicion(v.txtFechaExpedicion.getDate());
        newDocumento.setFechaExpiracion(v.txtFechaExpiracion.getDate());
        newDocumento.setArchivo(v.txtFilePath.getText());
        newDocumento.setContrasegna(v.txtContrasegna.getText());
        
        added = newDocumento.createDocumento();
        
        return added;
    }
    
    private boolean updateProcess(){
        boolean success = false;
        
        if(emptyFields()){
            String message = "Existen campos obligatorios sin llenar.";
            JOptionPane.showMessageDialog(this.view, message, "Advertencia #002", JOptionPane.WARNING_MESSAGE);
            return success;
        }
        
        if(!clienteExists()){
            String message = "El cliente especificado no existe.";
            JOptionPane.showMessageDialog(this.view, message, "Advertencia #009", JOptionPane.WARNING_MESSAGE);
            return success;
        }
        
        success = updateDocumento();
        
        if(!success){
            String message = "Ocurrió un error al modificar el documento.";
            JOptionPane.showMessageDialog(this.view, message, "Error #006", JOptionPane.ERROR_MESSAGE);
        }else{
            String message = "El documento se modificó exitosamente.";
            JOptionPane.showMessageDialog(this.view, message, "Modificación exitosa", JOptionPane.INFORMATION_MESSAGE);
        }
        
        return success;
    }
    
    private boolean updateDocumento(){
        boolean updated;
        DocumentosForm v = getOriginalView();
        Documento documento = new Documento(this.idDocumento);
        
        documento.setCliente_Rfc(v.txtCliente.getText());
        documento.setIdTipoDocumento(getIdTipoDocumento((String)v.cmbTipoDocumento.getSelectedItem()));
        documento.setFechaExpedicion(v.txtFechaExpedicion.getDate());
        documento.setFechaExpiracion(v.txtFechaExpiracion.getDate());
        documento.setArchivo(v.txtFilePath.getText());
        documento.setContrasegna(v.txtContrasegna.getText());
        
        updated = documento.updateDocumento();
        
        return updated;
    }

    private void btnSelectFilePushed(){
        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showDialog(view,"Subir");
        
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            getOriginalView().txtFilePath.setText(file.getPath());
        }
    }
    
    private boolean clienteExists(){
        boolean exists = false;
        Cliente c = new Cliente(getOriginalView().txtCliente.getText());
        
        c.readCliente();
        
        //CLIENT EXISTS
        if(c.getPersona_id() > 0){
            exists = true;
        }
        
        return exists;
    }
    
    private void btnCancelarPushed(){
        Documento tmp = new Documento(this.idDocumento);
        tmp.readDocumento();
        
        if(this.whereAmI == 1)
            Launcher.ec.changeController(new DocumentosController(new DocumentosView(),whereAmI));
        else
            Launcher.ec.changeController(new ClienteInfoController(new ClienteInfo(),tmp.getCliente_Rfc()));
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        Object source = e.getSource();
        
        if(source == getOriginalView().btnFileChooser){
            btnSelectFilePushed();
        }
        
        else if(source == getOriginalView().btnCancelar){
            btnCancelarPushed();
        }
        
        else if(source == getOriginalView().btnAgregar){
            if(getOriginalView().type == DocumentosForm.formType.SIGNUP){
                if(addProcess()){
                    if(whereAmI == 1)
                        Launcher.ec.changeController(new DocumentosController(new DocumentosView(),whereAmI));
                    else
                    {}
                }
            }
            
            else if(getOriginalView().type == DocumentosForm.formType.UPDATE){
                if(updateProcess()){
                    Documento tmp = new Documento(this.idDocumento);
                    tmp.readDocumento();
                    
                    if(whereAmI == 1)
                        Launcher.ec.changeController(new DocumentosController(new DocumentosView(),whereAmI));
                    else
                        Launcher.ec.changeController(new ClienteInfoController(new ClienteInfo(),tmp.getCliente_Rfc()));
                }
            }
        }
    }
}
