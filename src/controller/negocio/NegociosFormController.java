package controller.negocio;

import config.Controller;
import controller.Launcher;
import controller.cliente.ClienteInfoController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import model.Negocio;
import view.cliente.ClienteInfo;
import view.negocio.NegociosForm;

public class NegociosFormController extends Controller implements ActionListener{
    private String rfcCliente;

    //CONTROLLER TO CREATE FORM
    public NegociosFormController(NegociosForm view,String rfcCliente){
        super(view);
        this.rfcCliente = rfcCliente;
        
        view.btnCancelar.addActionListener(this);
        view.btnRegistrar.addActionListener(this);
    }
    
    //CONTROLLER TO MODIFY FORM
    public NegociosFormController(NegociosForm view,int idNegocio,String rfcCliente){
        super(view);
        this.rfcCliente = rfcCliente;
        
        loadNegocioOnView(idNegocio);
        
        view.btnCancelar.addActionListener(this);
        view.btnRegistrar.addActionListener(this);
    }
    
    public NegociosForm getOriginalView(){
        return (NegociosForm)this.view;}
    
    private boolean emptyFields(){
        if(getOriginalView().txtNombre.getText().equals(""))
            return true;
        else if(getOriginalView().txtGiroComercial.getText().equals(""))
            return true;
        else if(getOriginalView().txtCalle.getText().equals(""))
            return true;
        else if(getOriginalView().txtNumero.getText().equals(""))
            return true;
        else if(getOriginalView().txtColonia.getText() == null)
            return true;
        else if(getOriginalView().txtMunicipio.getText() == null)
            return true;
        else if(getOriginalView().txtEstado.getText() == null)
            return true;
        else if(getOriginalView().txtCP.getText() == null)
            return true;
        else
            return false;
    }
    
    private void loadNegocioOnView(int idNegocio){
        Negocio n = new Negocio(idNegocio);
        n.readNegocio();
        NegociosForm v = getOriginalView();

        v.txtIdNegocio.setText(Integer.toString(n.getIdNegocio()));
        v.txtNombre.setText(n.getNombre());
        v.txtGiroComercial.setText(n.getGiroComercial());
        v.txtCalle.setText(n.getCalle());
        v.txtNumero.setText(Integer.toString(n.getNumero()));
        v.txtInterior.setText(n.getInterior());
        v.txtColonia.setText(n.getColonia());
        v.txtMunicipio.setText(n.getMunicipio());
        v.txtEstado.setText(n.getEstado());
        v.txtCP.setText(Integer.toString(n.getCodigoPostal()));
        v.txtSuperficie.setText(Double.toString(n.getSuperficie()));
        v.txtHorario.setText(n.getHorario());
        v.txtCoordenadas.setText(n.getCoordenadas());
    }
    
    private boolean addNegocioProcess(){
        boolean success = false;
        
        if(emptyFields()){
            String message = "Existen campos obligatorios sin llenar.";
            JOptionPane.showMessageDialog(this.view, message, "Advertencia #002", JOptionPane.WARNING_MESSAGE);
            return success;
        }
        
        success = addNegocio();
        
        if(!success){
            String message = "Ocurrió un error al registrar el negocio.";
            JOptionPane.showMessageDialog(this.view, message, "Error #005", JOptionPane.ERROR_MESSAGE);
        }else{
            String message = "El negocio se registró exitosamente.";
            JOptionPane.showMessageDialog(this.view, message, "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
        }
        
        return success;
    }
    
    private boolean addNegocio(){
        boolean added;
        NegociosForm v = getOriginalView();
        Negocio newNegocio = new Negocio(this.rfcCliente);
        
        newNegocio.setNombre(v.txtNombre.getText());
        newNegocio.setGiroComercial(v.txtGiroComercial.getText());
        newNegocio.setCalle(v.txtCalle.getText());
        newNegocio.setNumero(Integer.parseInt(v.txtNumero.getText()));
        newNegocio.setInterior(v.txtInterior.getText());
        newNegocio.setColonia(v.txtColonia.getText());
        newNegocio.setMunicipio(v.txtMunicipio.getText());
        newNegocio.setEstado(v.txtEstado.getText());
        newNegocio.setCodigoPostal(Integer.parseInt(v.txtCP.getText()));
        newNegocio.setSuperficie(Double.parseDouble(v.txtSuperficie.getText()));
        newNegocio.setHorario(v.txtHorario.getText());
        newNegocio.setCoordenadas(v.txtCoordenadas.getText());
        
        added = newNegocio.createNegocio();
        
        return added;
    }
    
    private boolean updateProcess(){
        boolean success = updateNegocio();
        
        if(!success){
            String message = "Ocurrió un error al modificar el negocio.";
            JOptionPane.showMessageDialog(this.view, message, "Error #006", JOptionPane.ERROR_MESSAGE);
        }else{
            String message = "El negocio se modificó exitosamente.";
            JOptionPane.showMessageDialog(this.view, message, "Modificación exitosa", JOptionPane.INFORMATION_MESSAGE);
        }
        
        return success;
    }
    
    private boolean updateNegocio(){
        boolean updated;
        NegociosForm v = getOriginalView();
        Negocio negocio = new Negocio(Integer.parseInt(v.txtIdNegocio.getText()));
        
        negocio.setNombre(v.txtNombre.getText());
        negocio.setGiroComercial(v.txtGiroComercial.getText());
        negocio.setCalle(v.txtCalle.getText());
        negocio.setNumero(Integer.parseInt(v.txtNumero.getText()));
        negocio.setInterior(v.txtInterior.getText());
        negocio.setColonia(v.txtColonia.getText());
        negocio.setMunicipio(v.txtMunicipio.getText());
        negocio.setEstado(v.txtEstado.getText());
        negocio.setCodigoPostal(Integer.parseInt(v.txtCP.getText()));
        negocio.setSuperficie(Double.parseDouble(v.txtSuperficie.getText()));
        negocio.setHorario(v.txtHorario.getText());
        negocio.setCoordenadas(v.txtCoordenadas.getText());
        
        updated = negocio.updateNegocio();
        
        return updated;
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        Object source = e.getSource();
        
        if(source == getOriginalView().btnCancelar){
            Launcher.ac.changeController(new ClienteInfoController(new ClienteInfo(),this.rfcCliente));
        }
        
        else if(source == getOriginalView().btnRegistrar){
            if(getOriginalView().type == NegociosForm.formType.SIGNUP){
                if(addNegocioProcess()){
                    Launcher.ac.changeController(new ClienteInfoController(new ClienteInfo(),this.rfcCliente));
                }
            }
            
            else if(getOriginalView().type == NegociosForm.formType.UPDATE){
                if(updateProcess()){
                    Launcher.ac.changeController(new ClienteInfoController(new ClienteInfo(),this.rfcCliente));
                }
            }
        }
    }
}
