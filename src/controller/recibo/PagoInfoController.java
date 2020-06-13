package controller.recibo;

import config.Controller;
import controller.Launcher;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import model.Pago;
import view.recibo.PagoInfo;
import view.recibo.ReciboInfo;

public class PagoInfoController extends Controller implements ActionListener{
    private final int idRecibo;
    private final int whereAmI;
    
    public PagoInfoController(PagoInfo view,int idRecibo,int whereAmI){
        super(view);
        this.idRecibo = idRecibo;
        this.whereAmI = whereAmI;
        
        printInfo();
        view.btnRegresar.addActionListener(this);
    }
    
    PagoInfo getOriginalView(){
        return (PagoInfo)view;}
    
    private void printInfo(){
        //VIEW
        PagoInfo v = getOriginalView();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        
        //FILLING HEADER
        //IDPAGO, EMPLEADO, RECIBOIDRECIBO, MONTOPAGADO, METODOPAGO, FECHAPAGO, COMENTARIO, ELIMINAR
        JPanel panel = new JPanel(new GridLayout(0,7,20,10));
        
        panel.add(new JLabel("ID Pago"));
        panel.add(new JLabel("Receptor"));
        panel.add(new JLabel("Monto Pagado"));
        panel.add(new JLabel("Metodo Pago"));
        panel.add(new JLabel("Fecha Pago"));
        panel.add(new JLabel("Comentarios"));
        panel.add(new JLabel(""));
        
        v.Pagos.add(panel);
        
        //PAID
        Pago pago = new Pago();
        final int idR = this.idRecibo;
        pago.setIdRecibo(this.idRecibo);
        JButton button;
        
        for(Pago p: pago.readPagos()){
            panel = new JPanel(new GridLayout(0,7,20,10));
            button = new JButton("Eliminiar Pago");
            
            panel.add(new JLabel(Integer.toString(p.getIdPago())));
            panel.add(new JLabel(Integer.toString(p.getIdEmpleado())));
            panel.add(new JLabel(String.format("%.2f",p.getMontoPagado())));
            panel.add(new JLabel(p.getMetodoPago()));
            panel.add(new JLabel(sdf.format(p.getFechaPago())));
            panel.add(new JLabel(p.getComentarios()));
            panel.add(button);

            button.addActionListener(new ActionListener(){
                @Override
            public void actionPerformed(ActionEvent e) {
                //DELETE PAY
                if(deleteProcess(p.getIdPago(),idR)){
                    btnRegresarPushed();
                }
            }
            });
            
            v.Pagos.add(panel);
        }
        
        
    }
    
    private boolean deleteProcess(int idPago, int idRecibo){
        String message = "Eliminar el pago con ID: " + idPago + "?";
        boolean confirmed = (JOptionPane.showConfirmDialog(this.view,message,"Confirmación",JOptionPane.YES_NO_OPTION) == 0);
        
        if(!confirmed)
            return confirmed;
        
        Pago pago = new Pago(idPago);
        pago.setIdRecibo(idRecibo);
        
        if(!pago.deletePago()){
            String msg = "El pago no ha podido ser eliminado.";
            JOptionPane.showMessageDialog(this.view,msg,"Error #005",JOptionPane.ERROR_MESSAGE);
            return false;
        }else{
            String msg = "El pago se ha eliminado éxitosamente.";
            JOptionPane.showMessageDialog(this.view,msg,"Éxito",JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
    }
    
    private void btnRegresarPushed(){
        Launcher.ec.changeController(new ReciboInfoController(new ReciboInfo(),this.idRecibo,whereAmI));
    }
    
    @Override
     public void actionPerformed(ActionEvent e){
         Object source = e.getSource();
         
         if(source == getOriginalView().btnRegresar){
            btnRegresarPushed();}
         
     }
}
