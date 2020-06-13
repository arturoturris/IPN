package controller.documento;

import config.Controller;
import controller.Launcher;
import controller.cliente.ClienteInfoController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import model.Documento;
import model.TipoDocumento;
import view.cliente.ClienteInfo;
import view.documento.DocumentoInfo;
import view.documento.DocumentosView;

public class DocumentoInfoController extends Controller implements ActionListener{
    final private int whereAmI;
    final private int idDocumento;
    
    public DocumentoInfoController(DocumentoInfo view,int idDocumento, int whereAmI){
        super(view);
        
        this.whereAmI = whereAmI;
        this.idDocumento = idDocumento;
        
        printInfo();
        view.btnRegresar.addActionListener(this);
        view.btnDescargar.addActionListener(this);
    }
    
    DocumentoInfo getOriginalView(){
        return (DocumentoInfo)view;}
    
    private void printInfo(){
        //VIEW
        DocumentoInfo v = getOriginalView();
        Documento documento = new Documento(this.idDocumento);
        documento.readDocumento();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        //tipoDocumento
        String tipoDocumento = null;
        TipoDocumento tmpTD = new TipoDocumento();
        
        for(TipoDocumento td: tmpTD.readTipoDocumentos()){
            if(td.getIdTipoDocumento() == documento.getIdTipoDocumento()){
                tipoDocumento = td.getNombre();
                break;
            }
        }
        
        v.txtIdDocumento.setText(Integer.toString(documento.getIdDocumento()));
        v.txtCliente.setText(documento.getCliente_Rfc());
        v.txtTipoDocumento.setText(tipoDocumento);
        v.txtFechaExpedicion.setText(sdf.format(documento.getFechaExpedicion()));
        v.txtFechaExpiracion.setText(sdf.format(documento.getFechaExpiracion()));
        v.txtNombreArchivo.setText(documento.getArchivo());
        v.txtContrasegna.setText(documento.getContrasegna());
    }
    
    private void btnRegresarPushed(){
        Documento tmp = new Documento(this.idDocumento);
        tmp.readDocumento();
        
        if(whereAmI == 1)
            Launcher.ec.changeController(new DocumentosController(new DocumentosView(),whereAmI));
        else
            Launcher.ec.changeController(new ClienteInfoController(new ClienteInfo(),tmp.getCliente_Rfc()));
    }
    
    private void btnDescargarPushed(){
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setAcceptAllFileFilterUsed(false);
        
        int returnVal = fc.showSaveDialog(view);
        
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            File destinationFolder = fc.getSelectedFile();
            File fileToMove, destinationFile;
            Path finalPath = null;
            Documento documento = new Documento(this.idDocumento);
            
            documento.readDocumento();
            fileToMove = new File(documento.getClienteFilesFolder()+documento.getArchivo());
            destinationFile = new File(destinationFolder + "/" + documento.getArchivo());
            
            try {
                finalPath = Files.copy(fileToMove.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ex) {
                Logger.getLogger(DocumentoInfoController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            if(finalPath == null){
                String message = "El documento no pudo ser descargado.";
                JOptionPane.showMessageDialog(this.view, message, "Advertencia #010", JOptionPane.WARNING_MESSAGE);
            }
            else{
                String message = "El documento se descargó con éxito.";
                JOptionPane.showMessageDialog(this.view, message, "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    @Override
     public void actionPerformed(ActionEvent e){
         Object source = e.getSource();
         
         if(source == getOriginalView().btnRegresar){
            btnRegresarPushed();}
         
         else if(source == getOriginalView().btnDescargar){
             btnDescargarPushed();}
     }
}
