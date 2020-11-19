package controller.cliente;

import config.Controller;
import controller.Launcher;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import model.Cliente;
import view.cliente.ClientesForm;
import view.cliente.ClientesView;

public class ClientesFormController extends Controller implements ActionListener{
    public ClientesFormController(ClientesForm view){
        super(view);
        
        view.btnCancelar.addActionListener(this);
        view.btnRegistrar.addActionListener(this);
    }
    
    public ClientesFormController(ClientesForm view,String rfc){
        super(view);
        
        loadClienteOnView(rfc);
        
        view.btnCancelar.addActionListener(this);
        view.btnRegistrar.addActionListener(this);
    }
    
    public ClientesForm getOriginalView(){
        return (ClientesForm)this.view;}
    
    private boolean emptyFields(){
        if(getOriginalView().txtNombre.getText().equals(""))
            return true;
        else if(getOriginalView().txtPaterno.getText().equals(""))
            return true;
        else if(getOriginalView().txtMaterno.getText().equals(""))
            return true;
        else if(getOriginalView().txtRFC.getText().equals(""))
            return true;
        else if(getOriginalView().txtRegimen.getText().equals(""))
            return true;
        else
            return false;
    }
    
    private boolean signUpProcess(){
        boolean success = false;
        
        if(emptyFields()){
            String message = "Existen campos obligatorios sin llenar.";
            JOptionPane.showMessageDialog(this.view, message, "Advertencia #002", JOptionPane.WARNING_MESSAGE);
            return success;
        }
        
        success = signUpCliente();
        
        if(!success){
            String message = "Ocurrió un error al registrar el cliente.";
            JOptionPane.showMessageDialog(this.view, message, "Error #005", JOptionPane.ERROR_MESSAGE);
        }else{
            String message = "El cliente se registró exitosamente.";
            JOptionPane.showMessageDialog(this.view, message, "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
        }
        
        return success;
    }
    
    private boolean signUpCliente(){
        boolean signedUp;
        ClientesForm v = getOriginalView();
        Cliente newCliente = new Cliente();
        
        newCliente.setNombre(v.txtNombre.getText());
        newCliente.setPaterno(v.txtPaterno.getText());
        newCliente.setMaterno(v.txtMaterno.getText());
        newCliente.setSexo((v.radioHombre.isSelected()) ? 'H' : 'M');
        newCliente.setRfc(v.txtRFC.getText());
        newCliente.setRegimen(v.txtRegimen.getText());
        newCliente.setEmail(v.txtEmail.getText());
        newCliente.setTelefono(v.txtTelefono.getText());
        newCliente.setCelular(v.txtCelular.getText());
        newCliente.setContraseganSat(v.txtContraseñaSat.getText());
        newCliente.setComentarios(v.txtComentarios.getText());
        
        signedUp = newCliente.createCliente();
        
        return signedUp;
    }
    
    private boolean updateProcess(){
        boolean success = updateCliente();
        
        if(!success){
            String message = "Ocurrió un error al modificar el cliente.";
            JOptionPane.showMessageDialog(this.view, message, "Error #006", JOptionPane.ERROR_MESSAGE);
        }else{
            String message = "El cliente se modificó exitosamente.";
            JOptionPane.showMessageDialog(this.view, message, "Modificación exitosa", JOptionPane.INFORMATION_MESSAGE);
        }
        
        return success;
    }
    
    private boolean updateCliente(){
        boolean updated;
        ClientesForm v = getOriginalView();
        Cliente cliente = new Cliente(v.txtRFC.getText());
        
        cliente.setNombre(v.txtNombre.getText());
        cliente.setPaterno(v.txtPaterno.getText());
        cliente.setMaterno(v.txtMaterno.getText());
        cliente.setSexo((v.radioHombre.isSelected()) ? 'H' : 'M');
        cliente.setRegimen(v.txtRegimen.getText());
        cliente.setEmail(v.txtEmail.getText());
        cliente.setTelefono(v.txtTelefono.getText());
        cliente.setCelular(v.txtCelular.getText());
        cliente.setContraseganSat(v.txtContraseñaSat.getText());
        cliente.setComentarios(v.txtComentarios.getText());
        
        updated = cliente.updateCliente();
        
        return updated;
    }
    
    private void loadClienteOnView(String rfc){
        Cliente cliente = new Cliente(rfc);
        if(!cliente.readCliente())
            return;

        ClientesForm v = getOriginalView();

        v.txtNombre.setText(cliente.getNombre());
        v.txtPaterno.setText(cliente.getPaterno());
        v.txtMaterno.setText(cliente.getMaterno());
        if(cliente.getSexo() == 'H'){
            v.radioHombre.setSelected(true);}
        else{
            v.radioMujer.setSelected(true);}
        v.txtRFC.setText(cliente.getRfc());
        v.txtRegimen.setText(cliente.getRegimen());
        v.txtEmail.setText(cliente.getEmail());
        v.txtTelefono.setText(cliente.getTelefono());
        v.txtCelular.setText(cliente.getCelular());
        v.txtContraseñaSat.setText(cliente.getContraseganSat());
        v.txtComentarios.setText(cliente.getComentarios());
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        Object source = e.getSource();
        
        if(source == getOriginalView().btnCancelar){
            Launcher.ac.changeController(new ClientesController(new ClientesView()));}
        
        else if(source == getOriginalView().btnRegistrar){
            if(getOriginalView().type == ClientesForm.formType.SIGNUP){
                if(signUpProcess()){
                    Launcher.ac.changeController(new ClientesController(new ClientesView()));
                }
            }
            
            else if(getOriginalView().type == ClientesForm.formType.UPDATE){
                if(updateProcess()){
                    Launcher.ac.changeController(new ClientesController(new ClientesView()));
                }
            }
        }
    }
}
