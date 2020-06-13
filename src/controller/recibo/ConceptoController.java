package controller.recibo;

import config.Controller;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import model.Concepto;
import model.ProductoServicio;
import view.recibo.ConceptoForm;

public class ConceptoController extends Controller implements ActionListener{
    private Concepto concepto;
    int idConcepto;
    
    //CONSTRUCTOR TO CREATE
    public ConceptoController(ConceptoForm view,int idConcepto){
        super(view);
        this.concepto = new Concepto();
        this.concepto.setIdConcepto(idConcepto);
        readProductosServicios();
        loadProductoServicioInfo();
        printInfoConcepto();
        view.btnEliminar.addActionListener(this);
        view.spnCantidad.addChangeListener(new ChangeListener()
            {
                @Override
                public void stateChanged(ChangeEvent e) {
                    changeCantidadConcepto();
                    recalculateSubTotal();
                }
            }
        );
        view.cmbProductoServicio.addActionListener(this);
        view.spnNuevoPrecio.addChangeListener(new ChangeListener()
            {
                @Override
                public void stateChanged(ChangeEvent e) {
                    changeNuevoPrecioConcepto();
                    recalculateSubTotal();
                }
            }
        );
        view.chkNuevoPrecio.addActionListener(this);
    }
    
    //CONSTRUCTOR FOR MODIFYING
    public ConceptoController(ConceptoForm view, Concepto concepto){
        super(view);
        this.concepto = concepto;
        readProductosServicios();
        loadCurrentInfo();
        printInfoConcepto();
        view.btnEliminar.addActionListener(this);
        view.spnCantidad.addChangeListener(new ChangeListener()
            {
                @Override
                public void stateChanged(ChangeEvent e) {
                    changeCantidadConcepto();
                    recalculateSubTotal();
                }
            }
        );
        view.cmbProductoServicio.addActionListener(this);
        view.spnNuevoPrecio.addChangeListener(new ChangeListener()
            {
                @Override
                public void stateChanged(ChangeEvent e) {
                    changeNuevoPrecioConcepto();
                    recalculateSubTotal();
                }
            }
        );
        view.chkNuevoPrecio.addActionListener(this);
    }
    
    public ConceptoForm getOriginalView(){
        return (ConceptoForm)this.view;}
    
    public Concepto getConcepto(){
        return this.concepto;}
    
    //DISPLAY A LIST OF productosServicios
    public void readProductosServicios(){
        ConceptoForm v = getOriginalView();
        ProductoServicio tmp = new ProductoServicio();
        
        v.cmbProductoServicio.removeAllItems();
        
        for(ProductoServicio ps : tmp.readProductosServicios()){
            v.cmbProductoServicio.addItem("#" + ps.getIdProdServ() + "-" + ps.getNombre());
        }
        
        if(concepto.getConcepto() != null)
            v.cmbProductoServicio.setSelectedItem("#" + concepto.getConcepto().getIdProdServ() + "-" + concepto.getConcepto().getNombre());
    }
    
    //FILLS ATTRIBUTES LOCAL VARIABLE concepto BASED ON SELECTED ITEM ON COMBOBOX
    private void loadProductoServicioInfo(){
        ConceptoForm v = getOriginalView();
        String selection = (String)v.cmbProductoServicio.getSelectedItem();
        selection = selection.split("-")[0];
        selection = selection.split("#")[1];
        int idProdServ = Integer.parseInt(selection);
        ProductoServicio ps = new ProductoServicio(idProdServ);
        double precio;
        
        ps.readProductoServicio();
        precio = (v.chkNuevoPrecio.isSelected()) ? (double)v.spnNuevoPrecio.getValue() : ps.getPrecio();
        
        concepto.setCantidad((int)v.spnCantidad.getValue());
        concepto.setConcepto(ps);
        concepto.setPrecioUnitario(precio);
    }
    
    //PAINTS INFO LOADED ON LOCAL VARIABLE concepto
    private void printInfoConcepto(){
        ConceptoForm v = getOriginalView();
        
        v.txtIdConcepto.setText(Integer.toString(concepto.getIdConcepto()));
        
        //LOAD TXT precio WHEN CHK IS NOT SELECTED
        
        v.txtPrecioUnitario.setText("$" + String.format("%.2f", concepto.getConcepto().getPrecio()));
        //CALCULATE SUBTOTAL
        recalculateSubTotal();
    }
    
    private void changeCantidadConcepto(){
        concepto.setCantidad((int)getOriginalView().spnCantidad.getValue());
    }
    
    private void changeNuevoPrecioConcepto(){
        concepto.setPrecioUnitario((double)getOriginalView().spnNuevoPrecio.getValue());
    }
    
    private void chkNuevoPrecioPushed(){
        if(getOriginalView().chkNuevoPrecio.isSelected()){
            concepto.setPrecioUnitario((double)getOriginalView().spnNuevoPrecio.getValue());
        }
        else{
            concepto.setPrecioUnitario(concepto.getConcepto().getPrecio());
        }
        
        recalculateSubTotal();
    }
    
    private void recalculateSubTotal(){
        getOriginalView().txtSubtotal.setText("$" + String.format("%.2f", concepto.getSubTotal()));
    }
    
    private void loadCurrentInfo(){
        getOriginalView().spnCantidad.setValue(concepto.getCantidad());
        
        if(!getOriginalView().txtPrecioUnitario.getText().equals(String.format("%.2f", concepto.getPrecioUnitario()))){
            getOriginalView().chkNuevoPrecio.setSelected(true);
            getOriginalView().spnNuevoPrecio.setValue(concepto.getPrecioUnitario());
            getOriginalView().changeCard3();
        }
        else{
            getOriginalView().chkNuevoPrecio.setSelected(false);
        }
    }
    
    //THIS WHEN UPDATE
    private void cmbProductoServicioChanged(){
        loadProductoServicioInfo();
        printInfoConcepto();
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        Object source = e.getSource();
        
        if(source == getOriginalView().btnEliminar){
            //QUIZA AGARRAR EL EVENTO EN LA CLASE RECIBOFORM
        }
        
        else if(source == getOriginalView().cmbProductoServicio){
            cmbProductoServicioChanged();
        }
        
        else if(source == getOriginalView().chkNuevoPrecio){
            chkNuevoPrecioPushed();
        }
    }
}
