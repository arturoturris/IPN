package controller.productoservicio;

import config.Controller;
import controller.Launcher;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import model.ProductoServicio;
import view.productoservicio.ProductoServicioInfo;
import view.productoservicio.ProductosServiciosView;

public class ProductoServicioInfoController extends Controller implements ActionListener{
    private final int idProdServ;
    
    public ProductoServicioInfoController(ProductoServicioInfo view,int idProdServ){
        super(view);
        this.idProdServ = idProdServ;
        printInfo();
        view.btnRegresar.addActionListener(this);
    }
    
    ProductoServicioInfo getOriginalView(){
        return (ProductoServicioInfo)view;}
    
    private void printInfo(){
        //VIEW
        ProductoServicioInfo v = getOriginalView();

        ProductoServicio productoServicio = new ProductoServicio(this.idProdServ);
        if(!productoServicio.readProductoServicio())
            return;

        //PRINTING cliente INFO
        v.txtId.setText(Integer.toString(productoServicio.getIdProdServ()));
        v.txtTipo.setText(productoServicio.getTipo());
        v.txtNombre.setText(productoServicio.getNombre());
        v.txtDescripcion.setText(productoServicio.getDescripcion());
        v.txtPrecio.setText("$" + String.format("%.2f", productoServicio.getPrecio()));
    }
    
    private void btnRegresarPushed(){
        Launcher.ac.changeController(new ProductosServiciosController(new ProductosServiciosView()));}
    
    
    @Override
     public void actionPerformed(ActionEvent e){
         Object source = e.getSource();
         
         if(source == getOriginalView().btnRegresar){
            btnRegresarPushed();}
     }
}
