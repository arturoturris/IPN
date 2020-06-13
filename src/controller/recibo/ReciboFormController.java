package controller.recibo;

import config.Controller;
import controller.Launcher;
import javax.swing.Timer;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.JOptionPane;
import model.Cliente;
import model.Concepto;
import model.Recibo;
import view.recibo.ConceptoForm;
import view.recibo.ReciboInfo;
import view.recibo.RecibosForm;
import view.recibo.RecibosView;

public class ReciboFormController extends Controller implements ActionListener{
    private Recibo recibo;
    private final int whereAmI;
    
    //CONSTRUCTOR TO CREATE recibo
    public ReciboFormController(RecibosForm view, int whereAmI){
        super(view);
        
        recibo = new Recibo();
        this.whereAmI = whereAmI;
        
        timerCalculateTotal();
        view.btnAgregarConcepto.addActionListener(this);
        view.btnGenerar.addActionListener(this);
        view.btnCancelar.addActionListener(this);
    }
    
    //CONSTRUCTOR TO MODIFY recibo
    public ReciboFormController(RecibosForm view,int idRecibo,int whereAmI){
        super(view);
        
        this.whereAmI = whereAmI;
        recibo = new Recibo(idRecibo);
        recibo.readRecibo();
        loadReciboOnView();
        
        timerCalculateTotal();
        view.btnAgregarConcepto.addActionListener(this);
        view.btnGenerar.addActionListener(this);
        view.btnCancelar.addActionListener(this);
    }
    
    public RecibosForm getOriginalView(){
        return (RecibosForm)this.view;}
    
    private boolean emptyFields(){
        if(getOriginalView().txtCliente.getText().equals(""))
            return true;
        else if(getOriginalView().txtFechaEmision.getDate() == null)
            return true;
        else if(recibo.getConceptos().size() == 0)
            return true;
        else
            return false;
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
    
    private void timerCalculateTotal(){
        Timer timer = new Timer (500, new ActionListener ()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                printTotal();
            }
        });
        timer.start();
    }
    
    private void printTotal(){
        getOriginalView().txtTotal.setText("$" + String.format("%.2f",recibo.getTotal()));
    }
    
    private void btnAgregarConceptoPushed(){
        ConceptoForm cf = new ConceptoForm();
        ConceptoController cc = new ConceptoController(cf,recibo.getIdConcepto());
        
        cf.btnEliminar.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                //eliminar de recibo
                int idConcepto = cc.getConcepto().getIdConcepto();
                recibo.removeConcepto(idConcepto);
                
                getOriginalView().Conceptos.remove(cf);
                getOriginalView().Scroll.revalidate();
                getOriginalView().Scroll.repaint();
                getOriginalView().Conceptos.revalidate();
                getOriginalView().Conceptos.repaint();
            }
        });
        
        this.recibo.addConcepto(cc.getConcepto());
        
        getOriginalView().Conceptos.add(cf);
        getOriginalView().Scroll.revalidate();
        getOriginalView().Scroll.repaint();
        getOriginalView().Conceptos.revalidate();
        getOriginalView().Conceptos.repaint();
        
        Dimension minSize = new Dimension(5, 5);
        Dimension prefSize = new Dimension(5, 5);
        Dimension maxSize = new Dimension(Short.MAX_VALUE, 5);
        getOriginalView().Conceptos.add(new Box.Filler(minSize, prefSize, maxSize));
    }
    
    private void loadReciboOnView(){
        RecibosForm v = getOriginalView();
        
        v.txtIdRecibo.setText(Integer.toString(recibo.getIdRecibo()));
        v.txtCliente.setText(recibo.getCliente_rfc());
        v.txtFechaEmision.setDate(recibo.getFechaEmision());
        
        //FILLING conceptos
        for(Concepto c: recibo.getConceptos()){
            ConceptoForm cf = new ConceptoForm();
            ConceptoController cc = new ConceptoController(cf,c);
            
            
            v.Conceptos.add(cc.getOriginalView());
            //AGREGAR EVENTO BTN ELIMINAR
            cf.btnEliminar.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                //eliminar de recibo
                int idConcepto = cc.getConcepto().getIdConcepto();
                recibo.removeConcepto(idConcepto);
                
                getOriginalView().Conceptos.remove(cf);
                getOriginalView().Scroll.revalidate();
                getOriginalView().Scroll.repaint();
                getOriginalView().Conceptos.revalidate();
                getOriginalView().Conceptos.repaint();
            }
        });
        }
    }
    
    private boolean generateProcess(){
        boolean generated = false;
        
        if(emptyFields()){
            String message = "Existen campos obligatorios sin llenar.";
            JOptionPane.showMessageDialog(this.view, message, "Advertencia #008", JOptionPane.WARNING_MESSAGE);
            return generated;
        }
        
        if(!clienteExists()){
            String message = "El cliente especificado no existe.";
            JOptionPane.showMessageDialog(this.view, message, "Advertencia #009", JOptionPane.WARNING_MESSAGE);
            return generated;
        }
        
        generated = generateRecibo();
        
        return generated;
    }
    
    private boolean generateRecibo(){
        this.recibo.setCliente_rfc(getOriginalView().txtCliente.getText());
        this.recibo.setFechaEmision(getOriginalView().txtFechaEmision.getDate());
        boolean success = this.recibo.createRecibo();
        
        if(!success){
            String message = "Ha existido un error al crear el recibo.";
            JOptionPane.showMessageDialog(this.view, message, "Advertencia #010", JOptionPane.WARNING_MESSAGE);
        }
        else{
            String message = "El recibo se ha generado con éxito.";
            JOptionPane.showMessageDialog(this.view, message, "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
        
        return success;
    }
    
    private boolean updateProcess(){
        boolean success = false;
        
        if(emptyFields()){
            String message = "Existen campos obligatorios sin llenar.";
            JOptionPane.showMessageDialog(this.view, message, "Advertencia #008", JOptionPane.WARNING_MESSAGE);
            return success;
        }
        
        if(!updateRecibo()){
            String message = "Ocurrió un error al modificar el recibo.";
            JOptionPane.showMessageDialog(this.view, message, "Error #006", JOptionPane.ERROR_MESSAGE);
        }else{
            String message = "El recibo se modificó exitosamente.";
            JOptionPane.showMessageDialog(this.view, message, "Modificación exitosa", JOptionPane.INFORMATION_MESSAGE);
            success = true;
        }
        
        return success;
    }
    
    private boolean updateRecibo(){
        boolean updated;
        RecibosForm v = getOriginalView();
        
        updated = this.recibo.updateRecibo();
        
        //MAKE CHANGES TO CONCEPTOS
        Concepto tmp = new Concepto();
        
        //DELETING OLD conceptos
        tmp.setRecibo_IdRecibo(this.recibo.getIdRecibo());
        if(!tmp.deleteConceptos())
            return false;
        
        //ADDING NEW CONCEPTOS
        for(Concepto c: recibo.getConceptos()){
            c.createConcepto();
        }
        
        return updated;
    }
    
    private void btnCancelarPushed(){
        if(whereAmI == 1)
            Launcher.ec.changeController(new RecibosController(new RecibosView()));
        else
            Launcher.ec.changeController(new ReciboInfoController(new ReciboInfo(),this.recibo.getIdRecibo(),this.whereAmI));
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        Object source = e.getSource();
        
        if(source == getOriginalView().btnCancelar){
            btnCancelarPushed();
        }
        
        else if(source == getOriginalView().btnAgregarConcepto){
            btnAgregarConceptoPushed();
        }
        
        else if(source == getOriginalView().btnGenerar){
            if(getOriginalView().type == RecibosForm.formType.SIGNUP){
                if(generateProcess()){
                    if(whereAmI == 1)
                        Launcher.ec.changeController(new RecibosController(new RecibosView()));
                    else
                        Launcher.ec.changeController(new ReciboInfoController(new ReciboInfo(),this.recibo.getIdRecibo(),2));
                }
            }
            
            else if(getOriginalView().type == RecibosForm.formType.UPDATE){
                if(whereAmI == 1)
                        Launcher.ec.changeController(new RecibosController(new RecibosView()));
                    else
                        Launcher.ec.changeController(new ReciboInfoController(new ReciboInfo(),this.recibo.getIdRecibo(),2));
            }
        }
    }
}
