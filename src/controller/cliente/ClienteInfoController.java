package controller.cliente;

import config.Controller;
import controller.Launcher;
import controller.negocio.NegociosController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import model.Cliente;
import view.cliente.ClienteInfo;
import view.cliente.ClientesView;
import view.negocio.NegociosView;

public class ClienteInfoController extends Controller implements ActionListener{
    private final String rfcCliente;
    
    public ClienteInfoController(ClienteInfo view,String rfcCliente){
        super(view);
        this.rfcCliente = rfcCliente;
        
        printInfo();
        view.btnRegresar.addActionListener(this);
    }
    
    ClienteInfo getOriginalView(){
        return (ClienteInfo)view;}
    
    private void printInfo(){
        //VIEW
        ClienteInfo v = getOriginalView();
        //DATE FORMAT
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

        Cliente cliente = new Cliente(this.rfcCliente);
        if(!cliente.readCliente())
            return;

        //PRINTING cliente INFO
        v.txtNombre.setText(cliente.getPaterno() + " " + cliente.getMaterno() + " " + cliente.getNombre());
        v.txtSexo.setText(cliente.getSexo() == 'H' ? "Hombre" : "Mujer");
        v.txtRFC.setText(cliente.getRfc());
        v.txtRegimen.setText(cliente.getRegimen());
        v.txtEmail.setText(cliente.getEmail());
        v.txtTelefono.setText(cliente.getTelefono());
        v.txtCelular.setText(cliente.getCelular());
        v.txtContraseña.setText(cliente.getContraseganSat());
        v.txtFechaActualizacion.setText(sdf.format(cliente.getFechaActualizacion()));
        v.txtComentarios.setText(cliente.getComentarios());
        
        //PRINTING negocio *******
        NegociosController nc = new NegociosController(new NegociosView(),this.rfcCliente);
        v.Negocios.add(nc.getView());
    }
    
    private void btnRegresarPushed(){
        if(Launcher.session.getTipoSesion().equals("Administrador"))
            Launcher.ac.changeController(new ClientesController(new ClientesView()));
        else
            Launcher.ec.changeController(new ClientesController(new ClientesView()));
    }
    
    
    @Override
     public void actionPerformed(ActionEvent e){
         Object source = e.getSource();
         
         if(source == getOriginalView().btnRegresar){
            btnRegresarPushed();}
     }
}
