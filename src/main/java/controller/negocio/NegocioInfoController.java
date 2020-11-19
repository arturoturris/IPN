package controller.negocio;

import config.Controller;
import controller.Launcher;
import controller.cliente.ClienteInfoController;
import controller.cliente.ClientesController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import model.Negocio;
import view.cliente.ClienteInfo;
import view.cliente.ClientesView;
import view.negocio.NegocioInfo;

public class NegocioInfoController extends Controller implements ActionListener{
    private int idNegocio;
    
    public NegocioInfoController(NegocioInfo view,int idNegocio){
        super(view);
        this.idNegocio = idNegocio;
        
        printInfo();
        view.btnRegresar.addActionListener(this);
    }
    
    NegocioInfo getOriginalView(){
        return (NegocioInfo)view;}
    
    private void printInfo(){
        //VIEW
        NegocioInfo v = getOriginalView();
        Negocio negocio = new Negocio(this.idNegocio);
        negocio.readNegocio();
        
        v.txtIdNegocio.setText(Integer.toString(negocio.getIdNegocio()));
        v.txtNombre.setText(negocio.getNombre());
        v.txtGiroComercial.setText(negocio.getGiroComercial());
        v.txtCalle.setText(negocio.getCalle());
        v.txtNumero.setText(Integer.toString(negocio.getNumero()));
        v.txtInterior.setText(negocio.getInterior());
        v.txtColonia.setText(negocio.getColonia());
        v.txtMunicipio.setText(negocio.getMunicipio());
        v.txtEstado.setText(negocio.getEstado());
        v.txtCodigoPostal.setText(Integer.toString(negocio.getCodigoPostal()));
        v.txtSuperficie.setText(Double.toString(negocio.getSuperficie()) + " m2");
        v.txtHorario.setText(negocio.getHorario());
        v.txtCoordenadas.setText(negocio.getCoordenadas());
    }
    
    private void btnRegresarPushed(){
        Negocio tmp = new Negocio(idNegocio);
        tmp.readNegocio();
        
        if(Launcher.session.getTipoSesion().equals("Administrador"))
            Launcher.ac.changeController(new ClienteInfoController(new ClienteInfo(),tmp.getCliente_rfc()));
        else
            Launcher.ec.changeController(new ClienteInfoController(new ClienteInfo(),tmp.getCliente_rfc()));
    }
    
     @Override
     public void actionPerformed(ActionEvent e){
         Object source = e.getSource();
         
         if(source == getOriginalView().btnRegresar){
            btnRegresarPushed();}
     }
}
