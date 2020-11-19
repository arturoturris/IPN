package model;

import controller.Launcher;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;

public class Concepto {
    private int idConcepto;
    private int recibo_IdRecibo;
    private ProductoServicio concepto;
    private int cantidad;
    private double precioUnitario;
    
    //CONSTRUCTOR TO CREATE, DELETE AND READ conceptos - FOR READING AND DELETING CONCEPTOS SET recibo_IdRecibo
    public Concepto(){
        this.idConcepto = -1;
        this.recibo_IdRecibo = -1;
        this.concepto = null;
        this.cantidad = 1;
        this.precioUnitario = 0;
    }
    
    //CONSTRUCTOR TO READ, UPDATE AND DELETE concepto
    public Concepto(int recibo_IdRecibo, int concepto){
        this.idConcepto = -1;
        this.recibo_IdRecibo = -1;
        this.concepto = null;
        this.cantidad = 1;
        this.precioUnitario = 0;
    }

    public int getIdConcepto() {
        return idConcepto;
    }

    public void setIdConcepto(int idConcepto) {
        this.idConcepto = idConcepto;
    }

    public int getRecibo_IdRecibo() {
        return recibo_IdRecibo;
    }

    public void setRecibo_IdRecibo(int recibo_IdRecibo) {
        this.recibo_IdRecibo = recibo_IdRecibo;
    }

    public ProductoServicio getConcepto() {
        return concepto;
    }

    public void setConcepto(ProductoServicio concepto) {
        this.concepto = concepto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public double getSubTotal() {
        return (this.cantidad*this.precioUnitario);
    }
    
    public boolean createConcepto(){
        boolean created = false;
        
        try {    
            String query = "INSERT INTO concepto(IdConcepto,Recibo_IdRecibo,ProductoServicio_IdProdServ,Cantidad,PrecioUnitario) VALUES(?,?,?,?,?)";
            PreparedStatement ps = Launcher.session.getConnection().prepareStatement(query);
            
            ps.setInt(1,this.idConcepto);
            ps.setInt(2,this.recibo_IdRecibo);
            ps.setInt(3,this.concepto.getIdProdServ());
            ps.setInt(4, this.cantidad);
            ps.setDouble(5, this.precioUnitario);
            
            created = (ps.executeUpdate() > 0);
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(Recibo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        return created;
    }
    
    public boolean readConcepto(){
        boolean read = false;
        
        try {    
            String query = "SELECT * FROM negocio WHERE IdConcepto=? AND Recibo_IdRecibo=?";
            PreparedStatement ps = Launcher.session.getConnection().prepareStatement(query);
            ResultSet result;
            
            ps.setInt(1, this.idConcepto);
            ps.setInt(2, this.recibo_IdRecibo);
            ps.execute();
            result = ps.getResultSet();
            
            //IF THERES NO RESULT
            if(!(read = result.next())){
                return read;
            }
            
            //READ productoServicio
            ProductoServicio c = new ProductoServicio(result.getInt("ProductoServicio_IdProdServ"));
            
            if(!(read = c.readProductoServicio())){
                return read;
            }
            
            this.concepto = c;
            this.cantidad = result.getInt("Cantidad");
            this.precioUnitario = result.getInt("PrecioUnitario");
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(Concepto.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return read;
    }
    
    public ArrayList<Concepto> readConceptos(){
        ArrayList<Concepto> conceptos = new ArrayList<>();
        
        try {    
            String query = "SELECT * FROM concepto WHERE Recibo_IdRecibo=?";
            PreparedStatement ps = Launcher.session.getConnection().prepareStatement(query);
            ResultSet result;
            Concepto tmpC;
            ProductoServicio tmpPS;
            
            ps.setInt(1, this.recibo_IdRecibo);
            ps.execute();
            result = ps.getResultSet();
            
            while(result.next()){
                tmpC = new Concepto();
                tmpPS = new ProductoServicio(result.getInt("ProductoServicio_IdProdServ"));
                tmpPS.readProductoServicio();
                
                tmpC.setRecibo_IdRecibo(result.getInt("Recibo_IdRecibo"));
                tmpC.setIdConcepto(result.getInt("IdConcepto"));
                tmpC.setConcepto(tmpPS);
                tmpC.setCantidad(result.getInt("Cantidad"));
                tmpC.setPrecioUnitario(result.getDouble("PrecioUnitario"));
                
                conceptos.add(tmpC);
            }
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(Concepto.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return conceptos;
    }
    
    public boolean updateConcepto(){
        boolean updated = false;
        
        try {    
            String query = "UPDATE concepto SET ProductoServicio_IdProdServ=?, Cantidad=?, PrecioUnitario=? VALUES(?,?,?,?,?,?) WHERE IdConcepto=? AND Recibo_IdRecibo=?";
            PreparedStatement ps = Launcher.session.getConnection().prepareStatement(query);
            
            ps.setInt(1,this.concepto.getIdProdServ());
            ps.setInt(2, this.cantidad);
            ps.setDouble(3, this.precioUnitario);
            ps.setInt(4,this.idConcepto);
            ps.setInt(5,this.recibo_IdRecibo);
            
            updated = (ps.executeUpdate() > 0);
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(Recibo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        return updated;
    }
    
    public boolean deleteConcepto(){
        boolean deleted = false;
        
        try {    
            String query = "DELETE FROM concepto WHERE IdConcepto=? AND Recibo_IdRecibo=?";
            PreparedStatement ps = Launcher.session.getConnection().prepareStatement(query);
            
            ps.setInt(1, this.idConcepto);
            ps.setInt(2, this.recibo_IdRecibo);
            
            deleted = (ps.executeUpdate() > 0);
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(Concepto.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return deleted;
    }
    
    //DELETE ALL conceptos FROM A recibo
     public boolean deleteConceptos(){
        boolean deleted = false;
        
        try {    
            String query = "DELETE FROM concepto WHERE Recibo_IdRecibo=?";
            PreparedStatement ps = Launcher.session.getConnection().prepareStatement(query);
            
            ps.setInt(1, this.recibo_IdRecibo);
            
            deleted = (ps.executeUpdate() > 0);
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(Concepto.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return deleted;
    }
}
