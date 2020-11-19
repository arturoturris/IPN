package controller.productoservicio;

import config.Controller;
import controller.Launcher;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import controller.MenuAdministradorController;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import model.ProductoServicio;
import view.productoservicio.ProductosServiciosView;
import view.MenuAdministradorView;
import view.productoservicio.ProductoServicioInfo;
import view.productoservicio.ProductosServiciosForm;

public class ProductosServiciosController extends Controller implements ActionListener{
    public ProductosServiciosController(ProductosServiciosView view){
        super(view);

        printProductosServicios();
        view.btnRegresar.addActionListener(this);
        view.btnRegistrar.addActionListener(this);
        view.btnModificar.addActionListener(this);
        view.btnConsultar.addActionListener(this);
        view.btnEliminar.addActionListener(this);
    }
    
    private ProductosServiciosView getOriginalView(){
        return (ProductosServiciosView)this.view;}
    
    private void btnRegistrarPushed(){
        Launcher.ac.changeController(new ProductosServiciosFormController(new ProductosServiciosForm(ProductosServiciosForm.formType.SIGNUP)));}
    
    private void btnConsultarPushed(){
        int selectedRow = getOriginalView().tblProductosServicios.getSelectedRow();
        
        if(selectedRow == -1){
            String message = "No se ha seleccionado un producto/servicio para consultar.";
            JOptionPane.showMessageDialog(this.view, message, "Advertencia #005", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int columnIndex = getOriginalView().tblProductosServicios.getColumn("ID").getModelIndex();
        int idProdServ = Integer.parseInt((String)getOriginalView().tblProductosServicios.getValueAt(selectedRow, columnIndex));
        
        Launcher.ac.changeController(new ProductoServicioInfoController(new ProductoServicioInfo(),idProdServ));
    }
    
    private void btnModificarPushed(){
        int selectedRow = getOriginalView().tblProductosServicios.getSelectedRow();
        
        if(selectedRow == -1){
            String message = "No se ha seleccionado un producto/servicio a modificar.";
            JOptionPane.showMessageDialog(this.view, message, "Advertencia #004", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int columnIndex = getOriginalView().tblProductosServicios.getColumn("ID").getModelIndex();
        int idProdServ = Integer.parseInt((String)getOriginalView().tblProductosServicios.getValueAt(selectedRow, columnIndex));
        
        Launcher.ac.changeController(new ProductosServiciosFormController(new ProductosServiciosForm(ProductosServiciosForm.formType.UPDATE),idProdServ));
    }
    
    private void btnEliminarPushed(){
        int selectedRow = getOriginalView().tblProductosServicios.getSelectedRow();
        
        if(selectedRow == -1){
            String message = "No se ha seleccionado un producto/servicio para eliminar.";
            JOptionPane.showMessageDialog(this.view, message, "Advertencia #005", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int columnIndex = getOriginalView().tblProductosServicios.getColumn("ID").getModelIndex();
        int idProdServ = Integer.parseInt((String)getOriginalView().tblProductosServicios.getValueAt(selectedRow, columnIndex));
        
        String message = "Eliminar producto/servicio con ID: " + idProdServ + "?";
        boolean confirmed = (JOptionPane.showConfirmDialog(this.view,message,"Confirmación",JOptionPane.YES_NO_OPTION) == 0);
        
        if(!confirmed)
            return;
        
        ProductoServicio ps = new ProductoServicio(idProdServ);
        if(!ps.deleteProductoServicio()){
            String msg = "El producto/servicio no ha podido ser eliminado.";
            JOptionPane.showMessageDialog(this.view,msg,"Error #005",JOptionPane.ERROR_MESSAGE);
        }else{
            String msg = "El producto/servicio se ha eliminado éxitosamente.";
            JOptionPane.showMessageDialog(this.view,msg,"Éxito",JOptionPane.INFORMATION_MESSAGE);
            printProductosServicios();
        }
    }
    
    private void printProductosServicios(){
        ProductoServicio tmp = new ProductoServicio();
        DefaultTableModel tableModel = (DefaultTableModel)getOriginalView().tblProductosServicios.getModel();

        tableModel.setRowCount(0);
        Vector<String> row;
        
        for(ProductoServicio ps: tmp.readProductosServicios()){
            row = new Vector<>();
            
            row.add(Integer.toString(ps.getIdProdServ()));
            row.add(ps.getTipo());
            row.add(ps.getNombre());
            row.add("$" + String.format("%.2f", ps.getPrecio()));
            tableModel.addRow(row);
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
    Object source = e.getSource();
        
        if(source == getOriginalView().btnRegresar){
            Launcher.ac.changeController(new MenuAdministradorController(new MenuAdministradorView()));}
        
        else if(source == getOriginalView().btnRegistrar){
            btnRegistrarPushed();}
        
        else if(source == getOriginalView().btnModificar){
            btnModificarPushed();}
        
        else if(source == getOriginalView().btnConsultar){
            btnConsultarPushed();}
        
        else if(source == getOriginalView().btnEliminar){
            btnEliminarPushed();}
    }
}
