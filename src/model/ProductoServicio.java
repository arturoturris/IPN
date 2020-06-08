package model;

import controller.Launcher;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductoServicio {
    private int idProdServ;
    private String tipo;
    private String nombre;
    private String descripcion;
    private double precio;
    
    //CONSTRUCTOR TO CREATE AND READ productosServicios
    public ProductoServicio(){
        this.idProdServ = -1;
        this.tipo = null;
        this.nombre = null;
        this.descripcion = null;
        this.precio = -1;
    }
    
    //CONSTRUCTOR TO READ, UPDATE AND DELETE
    public ProductoServicio(int idProdServ){
        this.idProdServ = idProdServ;
        this.tipo = null;
        this.nombre = null;
        this.descripcion = null;
        this.precio = -1;
    }

    public int getIdProdServ() {
        return idProdServ;
    }

    public void setIdProdServ(int idProdServ) {
        this.idProdServ = idProdServ;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
    
    public boolean createProductoServicio(){
        boolean created = false;
        
        try {
            //CREATE productoServicio
            String query = "INSERT INTO productoservicio(Tipo,Nombre,Descripcion,Precio) VALUES(?,?,?,?)";
            PreparedStatement ps = Launcher.session.getConnection().prepareStatement(query);
            
            ps.setString(1,this.tipo);
            ps.setString(2,this.nombre);
            ps.setString(3,this.descripcion);
            ps.setDouble(4,this.precio);
            
            created = (ps.executeUpdate() > 0);

        } catch (SQLException ex) {
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return created;
    }
    
    public boolean readProductoServicio(){
        boolean read = false;
        
        try {
            String query = "SELECT * FROM productoservicio WHERE IdProdServ=?";
            PreparedStatement ps = Launcher.session.getConnection().prepareStatement(query);
            ResultSet result;
            
            ps.setInt(1, this.idProdServ);
            ps.execute();
            result = ps.getResultSet();
            
            //IF THERES NO RESULT
            if(!(read = result.next())){
                return read;
            }
            
            this.tipo = result.getString("Tipo");
            this.nombre = result.getString("Nombre");
            this.descripcion = result.getString("Descripcion");
            this.precio = result.getDouble("Precio");
        } catch (SQLException ex) {
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return read;
    }
    
    public ArrayList<ProductoServicio> readProductosServicios(){
        ArrayList<ProductoServicio> productosServicios = new ArrayList<>();
        
        try {
            String query = "SELECT * FROM productoservicio";
            ResultSet result;
            PreparedStatement ps = Launcher.session.getConnection().prepareStatement(query);
            ProductoServicio tmpProductoServicio;
            
            result = ps.executeQuery();
            
            while(result.next()){
                tmpProductoServicio = new ProductoServicio();
                
                tmpProductoServicio.setIdProdServ(result.getInt("IdProdServ"));
                tmpProductoServicio.setTipo(result.getString("Tipo"));
                tmpProductoServicio.setNombre(result.getString("Nombre"));
                tmpProductoServicio.setDescripcion(result.getString("Descripcion"));
                tmpProductoServicio.setPrecio(result.getDouble("Precio"));
                
                productosServicios.add(tmpProductoServicio);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Empleado.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return productosServicios;
    }
    
    public boolean updateProductoServicio(){
        boolean updated = false;
        ProductoServicio productoServicio = new ProductoServicio(this.idProdServ);
        
        try {
            String query = "UPDATE productoservicio SET Tipo=?,Nombre=?,Descripcion=?,Precio=? WHERE IdProdServ=?";
            PreparedStatement ps = Launcher.session.getConnection().prepareStatement(query);
            
            ps.setString(1, this.tipo);
            ps.setString(2, this.nombre);
            ps.setString(3, this.descripcion);
            ps.setDouble(4,this.precio);
            ps.setInt(5, this.idProdServ);
            
            updated = (ps.executeUpdate() > 0);
        } catch (SQLException ex) {
            Logger.getLogger(Empleado.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return updated;
    }
    
    public boolean deleteProductoServicio(){
        boolean deleted = false;
        
        try {
            String query = "DELETE FROM productoservicio WHERE IdProdServ=?";
            PreparedStatement ps = Launcher.session.getConnection().prepareStatement(query);
            
            ps.setInt(1, this.idProdServ);
            
            deleted = (ps.executeUpdate() > 0);
        } catch (SQLException ex) {
            Logger.getLogger(Persona.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return deleted;
    }
}
