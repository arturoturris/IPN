package controller.productoservicio;

import config.Controller;
import controller.Launcher;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import model.ProductoServicio;
import view.productoservicio.ProductosServiciosForm;
import view.productoservicio.ProductosServiciosView;

public class ProductosServiciosFormController extends Controller implements ActionListener{
    public ProductosServiciosFormController(ProductosServiciosForm view){
        super(view);
        
        view.btnCancelar.addActionListener(this);
        view.btnRegistrar.addActionListener(this);
    }
    
    public ProductosServiciosFormController(ProductosServiciosForm view,int idProdServ){
        super(view);
        
        loadProductoServicioOnView(idProdServ);
        
        view.btnCancelar.addActionListener(this);
        view.btnRegistrar.addActionListener(this);
    }
    
    public ProductosServiciosForm getOriginalView(){
        return (ProductosServiciosForm)this.view;}
    
    private boolean emptyFields(){
        if(getOriginalView().txtNombre.getText().equals(""))
            return true;
        else if(getOriginalView().txtDescripcion.getText().equals(""))
            return true;
        else
            return false;
    }
    
    private boolean addProcess(){
        boolean success = false;
        
        if(emptyFields()){
            String message = "Existen campos obligatorios sin llenar.";
            JOptionPane.showMessageDialog(this.view, message, "Advertencia #002", JOptionPane.WARNING_MESSAGE);
            return success;
        }
        
        success = addProductoServicio();
        
        if(!success){
            String message = "Ocurrió un error al registrar el producto/servicio.";
            JOptionPane.showMessageDialog(this.view, message, "Error #005", JOptionPane.ERROR_MESSAGE);
        }else{
            String message = "El producto/servicio se registró exitosamente.";
            JOptionPane.showMessageDialog(this.view, message, "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
        }
        
        return success;
    }
    
    private boolean addProductoServicio(){
        boolean added;
        ProductosServiciosForm v = getOriginalView();
        ProductoServicio newProductoServicio = new ProductoServicio();
        
        newProductoServicio.setTipo(v.cmbTipo.getItemAt(v.cmbTipo.getSelectedIndex()));
        newProductoServicio.setNombre(v.txtNombre.getText());
        newProductoServicio.setDescripcion(v.txtDescripcion.getText());
        newProductoServicio.setPrecio((double)v.spnPrecio.getValue());
        
        
        added = newProductoServicio.createProductoServicio();
        
        return added;
    }
    
    private boolean updateProcess(){
        boolean success = updateProductoServicio();
        
        if(!success){
            String message = "Ocurrió un error al modificar el producto/servicio.";
            JOptionPane.showMessageDialog(this.view, message, "Error #006", JOptionPane.ERROR_MESSAGE);
        }else{
            String message = "El producto/servicio se modificó exitosamente.";
            JOptionPane.showMessageDialog(this.view, message, "Modificación exitosa", JOptionPane.INFORMATION_MESSAGE);
        }
        
        return success;
    }
    
    private boolean updateProductoServicio(){
        boolean updated;
        ProductosServiciosForm v = getOriginalView();
        ProductoServicio productoServicio = new ProductoServicio(Integer.parseInt(v.txtId.getText()));
        
        productoServicio.setTipo(v.cmbTipo.getItemAt(v.cmbTipo.getSelectedIndex()));
        productoServicio.setNombre(v.txtNombre.getText());
        productoServicio.setDescripcion(v.txtDescripcion.getText());
        productoServicio.setPrecio((double)v.spnPrecio.getValue());
        
        updated = productoServicio.updateProductoServicio();
        
        return updated;
    }
    
    private void loadProductoServicioOnView(int idProdServ){
        ProductoServicio productoServicio = new ProductoServicio(idProdServ);
        if(!productoServicio.readProductoServicio())
            return;

        ProductosServiciosForm v = getOriginalView();

        v.txtId.setText(Integer.toString(productoServicio.getIdProdServ()));
        v.cmbTipo.setSelectedItem(productoServicio.getTipo());
        v.txtNombre.setText(productoServicio.getNombre());
        v.txtDescripcion.setText(productoServicio.getDescripcion());
        v.spnPrecio.setValue(productoServicio.getPrecio());
    }
    
    public void actionPerformed(ActionEvent e){
        Object source = e.getSource();
        
        if(source == getOriginalView().btnCancelar){
            Launcher.ac.changeController(new ProductosServiciosController(new ProductosServiciosView()));}
        
        else if(source == getOriginalView().btnRegistrar){
            if(getOriginalView().type == ProductosServiciosForm.formType.SIGNUP){
                if(addProcess()){
                    Launcher.ac.changeController(new ProductosServiciosController(new ProductosServiciosView()));
                }
            }
            
            else if(getOriginalView().type == ProductosServiciosForm.formType.UPDATE){
                if(updateProcess()){
                    Launcher.ac.changeController(new ProductosServiciosController(new ProductosServiciosView()));
                }
            }
        }
    }
}
