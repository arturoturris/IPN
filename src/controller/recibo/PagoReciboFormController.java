package controller.recibo;

import config.Controller;
import controller.Launcher;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.SpinnerNumberModel;
import model.Pago;
import model.Recibo;
import view.recibo.PagoReciboForm;
import view.recibo.ReciboInfo;

//WHERE AM I ---> 1 -> pago Recibo Form, 2 -> Client view

public class PagoReciboFormController extends Controller implements ActionListener{
    private int idRecibo;
    private int idPago;
    private final int whereAmI;
    
    //CONSTRUCTOR TO CREATE recibo
    public PagoReciboFormController(PagoReciboForm view,int idRecibo, int whereAmI){
        super(view);
        
        this.idRecibo = idRecibo;
        this.idPago = -1;
        this.whereAmI = whereAmI;
        
        getMaxToPay();
        view.txtIdRecibo.setText(Integer.toString(this.idRecibo));
        
        view.btnPagar.addActionListener(this);
        view.btnCancelar.addActionListener(this);
    }
    
    //CONSTRUCTOR TO MODIFY recibo
    public PagoReciboFormController(PagoReciboForm view,int idRecibo, int idPago, int whereAmI){
        super(view);
        
        this.idRecibo = idRecibo;
        this.idPago = idPago;
        this.whereAmI = whereAmI;
        //CARGAR EN VISTA EL PAGO
        getMaxToPay();
        view.txtIdRecibo.setText(Integer.toString(this.idRecibo));
        view.btnPagar.addActionListener(this);
        view.btnCancelar.addActionListener(this);
    }
    
    public PagoReciboForm getOriginalView(){
        return (PagoReciboForm)this.view;}
    
    private boolean emptyFields(){
        if(getOriginalView().fechaPago.getDate() == null)
            return true;
        else if((double)getOriginalView().spnMontoPagar.getValue() == 0)
            return true;
        else
            return false;
    }
    
    private void getMaxToPay(){
        Recibo recibo = new Recibo(this.idRecibo);
        Pago pago = new Pago();
        double totalPaid = 0;
        
        pago.setIdRecibo(this.idRecibo);
        
        recibo.readRecibo();
        
        for(Pago p: pago.readPagos()){
            totalPaid += p.getMontoPagado();
        }
        
        getOriginalView().spnMontoPagar.setModel(new SpinnerNumberModel(0,0,Math.ceil(recibo.getTotal()),1.5));
        getOriginalView().txtMontoPagado.setText("$" + String.format("%.2f",totalPaid));
    }
    
    private boolean payProcess(){
        boolean paid = false;
        
        if(emptyFields()){
            String message = "Existen campos obligatorios sin llenar.";
            JOptionPane.showMessageDialog(this.view, message, "Advertencia #008", JOptionPane.WARNING_MESSAGE);
            return paid;
        }
        
        paid = payRecibo();
        
        return paid;
    }
    
    private boolean payRecibo(){
        boolean paid;
        PagoReciboForm v = getOriginalView();
        Pago newPago = new Pago();
        
        newPago.setIdRecibo(this.idRecibo);
        newPago.setMontoPagado((double)v.spnMontoPagar.getValue());
        newPago.setMetodoPago((String)v.cmbMetodoPago.getSelectedItem());
        newPago.setFechaPago(v.fechaPago.getDate());
        newPago.setComentarios(v.comentarios.getText());
        
        paid = newPago.createPago();
        
        if(!paid){
            String message = "Ocurrió un error al efectuar el pago.";
            JOptionPane.showMessageDialog(this.view, message, "Error #006", JOptionPane.ERROR_MESSAGE);
        }else{
            String message = "El pago se efectuó exitosamente.";
            JOptionPane.showMessageDialog(this.view, message, "Modificación exitosa", JOptionPane.INFORMATION_MESSAGE);
        }
        
        return paid;
    }
    
    private boolean updateProcess(){
        boolean updated = false;
        
        if(emptyFields()){
            String message = "Existen campos obligatorios sin llenar.";
            JOptionPane.showMessageDialog(this.view, message, "Advertencia #008", JOptionPane.WARNING_MESSAGE);
            return updated;
        }
        
        updated = updatePay();
        
        if(!updated){
            String message = "Ocurrió un error al modificar el pago.";
            JOptionPane.showMessageDialog(this.view, message, "Error #006", JOptionPane.ERROR_MESSAGE);
        }else{
            String message = "El pago se modificó exitosamente.";
            JOptionPane.showMessageDialog(this.view, message, "Modificación exitosa", JOptionPane.INFORMATION_MESSAGE);
        }
        
        return updated;
    }
    
    private boolean updatePay(){
        boolean updated;
        PagoReciboForm v = getOriginalView();
        Pago pago = new Pago(Integer.parseInt(v.txtIdRecibo.getText()));
        
        pago.setIdRecibo(this.idRecibo);
        pago.setMontoPagado((double)v.spnMontoPagar.getValue());
        pago.setMetodoPago((String)v.cmbMetodoPago.getSelectedItem());
        pago.setFechaPago(v.fechaPago.getDate());
        pago.setComentarios(v.comentarios.getText());
        
        updated = pago.updatePago();
        
        return updated;
    }
    
    private void btnPagarPushed(){
        if(getOriginalView().type == PagoReciboForm.formType.SIGNUP){
                if(payProcess()){
                    //RETURN TO WHERE AM I
                        Launcher.ec.changeController(new ReciboInfoController(new ReciboInfo(),this.idRecibo,this.whereAmI));
                }
            }
            
            else if(getOriginalView().type == PagoReciboForm.formType.UPDATE){
                if(updateProcess()){
                        Launcher.ec.changeController(new ReciboInfoController(new ReciboInfo(),this.idRecibo,this.whereAmI));
                }
            }
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        Object source = e.getSource();
        
        //BACK DEPENDING ON WHERE I AM
        if(source == getOriginalView().btnCancelar){
                Launcher.ec.changeController(new ReciboInfoController(new ReciboInfo(),this.idRecibo,this.whereAmI));
        }
        
        else if(source == getOriginalView().btnPagar){
            btnPagarPushed();
        }
    }
}
