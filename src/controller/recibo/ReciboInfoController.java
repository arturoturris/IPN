package controller.recibo;

import config.Controller;
import controller.Launcher;
import controller.cliente.ClienteInfoController;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JPanel;
import model.Concepto;
import model.Pago;
import model.Recibo;
import view.cliente.ClienteInfo;
import view.recibo.PagoInfo;
import view.recibo.PagoReciboForm;
import view.recibo.ReciboInfo;
import view.recibo.RecibosView;

public class ReciboInfoController extends Controller implements ActionListener{
    private final int idRecibo;
    private final int whereAmI;
    
    public ReciboInfoController(ReciboInfo view,int idRecibo,int whereAmI){
        super(view);
        this.idRecibo = idRecibo;
        this.whereAmI = whereAmI;
        printInfo();
        view.btnRegresar.addActionListener(this);
        view.btnPagar.addActionListener(this);
        view.btnInfoPagos.addActionListener(this);
    }
    
    ReciboInfo getOriginalView(){
        return (ReciboInfo)view;}
    
    private void printInfo(){
        //VIEW
        ReciboInfo v = getOriginalView();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        
        Recibo recibo = new Recibo(this.idRecibo);
        if(!recibo.readRecibo())
            return;

        //PRINTING cliente INFO
        v.txtIdRecibo.setText(Integer.toString(recibo.getIdRecibo()));
        v.txtCliente.setText(recibo.getCliente_rfc());
        v.txtFechaEmision.setText(sdf.format(recibo.getFechaEmision()));
        v.txtEstado.setText((recibo.isPagado()) ? "PAGADO" : "PENDIENTE");
        
        //PAID
        Pago pago = new Pago();
        double totalPaid = 0;
        pago.setIdRecibo(this.idRecibo);
        
        for(Pago p: pago.readPagos()){
            totalPaid += p.getMontoPagado();
        }
        v.txtImportePagado.setText("$" + String.format("%.2f",totalPaid));
        
        //FILL conceptos
        JPanel panel;
        
        //FILL HEADER
        //IDCONCEPTO, CANTIDAD, NOMBRE, PRECIO UNITARIO, SUBTOTAL
        panel = new JPanel(new GridLayout(0,7,10,5));

        panel.add(new JLabel("ID Concepto"));
        panel.add(new JLabel("Cantidad"));
        panel.add(new JLabel("Descripcion"));
        panel.add(new JLabel("P.Unitario"));
        panel.add(new JLabel("SubTotal"));

        v.Conceptos.add(panel);
        
        for(Concepto c: recibo.getConceptos()){
            panel = new JPanel(new GridLayout(0,7,10,5));
            
            panel.add(new JLabel(Integer.toString(c.getIdConcepto())));
            panel.add(new JLabel(Integer.toString(c.getCantidad())));
            panel.add(new JLabel(c.getConcepto().getNombre()));
            panel.add(new JLabel(String.format("%.2f", c.getPrecioUnitario())));
            panel.add(new JLabel(String.format("%.2f", c.getSubTotal())));
            
            v.Conceptos.add(panel);
        }
        
        v.txtTotal.setText(String.format("%.2f", recibo.getTotal()));
        
        //BLOCK btnPagar IF IT IS ALL PAID
        v.btnPagar.setEnabled(!(totalPaid >= recibo.getTotal()));
        
        //BLOCK btnInfoPagos IF IT IS NO PAIDS
        v.btnInfoPagos.setEnabled(!(pago.readPagos().isEmpty()));
    }
    
    private void btnRegresarPushed(){
        if(whereAmI == 1)
            Launcher.ec.changeController(new RecibosController(new RecibosView()));
        else{
            Recibo recibo = new Recibo(this.idRecibo);
            recibo.readRecibo();
            
            Launcher.ec.changeController(new ClienteInfoController(new ClienteInfo(),recibo.getCliente_rfc()));
        }
    }
    
    private void btnPagarPushed(){
        Launcher.ec.changeController(new PagoReciboFormController(new PagoReciboForm(PagoReciboForm.formType.SIGNUP),this.idRecibo,this.whereAmI));
    }
    
    private void btnInfoPagosPushed(){
        Launcher.ec.changeController(new PagoInfoController(new PagoInfo(),this.idRecibo,this.whereAmI));
    }
    
    @Override
     public void actionPerformed(ActionEvent e){
         Object source = e.getSource();
         
         if(source == getOriginalView().btnRegresar){
            btnRegresarPushed();}
         
         else if(source == getOriginalView().btnPagar){
            btnPagarPushed();}
         
         else if(source == getOriginalView().btnInfoPagos){
            btnInfoPagosPushed();}
     }
}
