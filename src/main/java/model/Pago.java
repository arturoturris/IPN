package model;

import controller.Launcher;
import java.sql.PreparedStatement;
import java.text.DecimalFormat;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Pago {
    private int idPago;
    private int idEmpleado;
    private int idRecibo;
    private double montoPagado;
    private String metodoPago;
    private Date fechaPago;
    private String comentarios;
    
    //CONSTRUTOR TO CREATE AND READ pagos -> FOR READING pagos SET idRecibo
    public Pago(){
        this.idPago = -1;
        this.idEmpleado = -1;
        this.idRecibo = -1;
        this.montoPagado = -1;
        this.metodoPago = null;
        this.fechaPago = null;
        this.comentarios = null;
    }
    
    //CONSTRUCTOR TO UPDATE, READ AND DELETE A pago
    public Pago(int idPago){
        this.idPago = idPago;
        this.idEmpleado = -1;
        this.idRecibo = -1;
        this.montoPagado = -1;
        this.metodoPago = null;
        this.fechaPago = null;
        this.comentarios = null;
    }

    public int getIdPago() {
        return idPago;
    }

    public void setIdPago(int idPago) {
        this.idPago = idPago;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public int getIdRecibo() {
        return idRecibo;
    }

    public void setIdRecibo(int idRecibo) {
        this.idRecibo = idRecibo;
    }

    public double getMontoPagado() {
        return montoPagado;
    }

    public void setMontoPagado(double montoPagado) {
        this.montoPagado = montoPagado;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }
    
    public boolean createPago(){
        boolean created = false;
        
        try {    
            String query = "INSERT INTO pago(Empleado_IdEmpleado,Recibo_IdRecibo,MontoPagado,MetodoPago,FechaPago,Comentario) VALUES(?,?,?,?,?,?)";
            PreparedStatement ps = Launcher.session.getConnection().prepareStatement(query);
            DecimalFormat f = new DecimalFormat("##.00");
            
            ps.setInt(1,Launcher.session.getIdEmpleado());
            ps.setInt(2,this.idRecibo);
            ps.setDouble(3,this.montoPagado);
            ps.setString(4,this.metodoPago);
            ps.setDate(5,new java.sql.Date(this.fechaPago.getTime()));
            ps.setString(6,this.comentarios);
            
            created = (ps.executeUpdate() > 0);
            
            //CHECK IF recibo HAS BEEN PAID
            isReciboPaid();
        } catch (SQLException ex) {
            Logger.getLogger(Pago.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return created;
    }
    
    public boolean readPago(){
        boolean read = false;
        
        try {
            String query = "SELECT * FROM pago WHERE IdPago=?";
            PreparedStatement ps = Launcher.session.getConnection().prepareStatement(query);
            ResultSet result;
            
            ps.setInt(1, this.idPago);
            ps.execute();
            result = ps.getResultSet();
            
            //IF THERES NO RESULT
            if(!(read = result.next())){
                return read;
            }
            
            this.idRecibo = result.getInt("Recibo_IdRecibo");
            this.idEmpleado = result.getInt("Empleado_IdEmpleado");
            this.montoPagado = result.getDouble("MontoPagado");
            this.metodoPago = result.getString("MetodoPago");
            this.fechaPago = result.getDate("FechaPago");
            this.comentarios = result.getString("Comentario");
        } catch (SQLException ex) {
            Logger.getLogger(Pago.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return read;
    }
    
    public ArrayList<Pago> readPagos(){
        ArrayList<Pago> pagos = new ArrayList<>();
        
        try {
            String query = "SELECT * FROM pago WHERE Recibo_IdRecibo=?";
            ResultSet result;
            PreparedStatement ps = Launcher.session.getConnection().prepareStatement(query);
            ps.setInt(1, this.idRecibo);
            Pago tmpPago;
            
            result = ps.executeQuery();
            
            while(result.next()){
                tmpPago = new Pago();
                
                tmpPago.idRecibo = this.idRecibo;
                tmpPago.idEmpleado = result.getInt("Empleado_IdEmpleado");
                tmpPago.idPago = result.getInt("IdPago");
                tmpPago.montoPagado = result.getDouble("MontoPagado");
                tmpPago.metodoPago = result.getString("MetodoPago");
                tmpPago.fechaPago = result.getDate("FechaPago");
                tmpPago.comentarios = result.getString("Comentario");
                
                pagos.add(tmpPago);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Pago.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return pagos;
    }
    
    public boolean updatePago(){
        boolean updated = false;
        
        try {    
            String query = "UPDATE pago SET Empleado_IdEmpleado=?,MontoPagado=?,MetodoPago=?,FechaPago=?,Comentario=? WHERE IdPago=?";
            PreparedStatement ps = Launcher.session.getConnection().prepareStatement(query);
            
            ps.setInt(1, Launcher.session.getIdEmpleado());
            ps.setDouble(2, this.montoPagado);
            ps.setString(3,this.metodoPago);
            ps.setDate(4, new java.sql.Date(this.fechaPago.getTime()));
            ps.setString(5,this.comentarios);
            
            updated = (ps.executeUpdate() > 0);
            
            //CHECK IF recibo HAS BEEN PAID
            isReciboPaid();
        } catch (SQLException ex) {
            Logger.getLogger(Pago.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return updated;
    }
    
    public boolean deletePago(){
        boolean deleted = false;
        
        try {
            String query = "DELETE FROM pago WHERE IdPago=?";
            PreparedStatement ps = Launcher.session.getConnection().prepareStatement(query);
            
            ps.setInt(1, this.idPago);
            
            deleted = (ps.executeUpdate() > 0);
            
            //CHECK IF recibo HAS BEEN PAID
            isReciboPaid();
        } catch (SQLException ex) {
            Logger.getLogger(Pago.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return deleted;
    }
    
    
    
    //NEEDS TO IMPLEMENT A METHOD TO VALIDATE IF recibo HAS BEEN PAID
    private boolean isReciboPaid(){
        boolean paid = false;
        
        try {
            //RETURN ALL pays MADE
            double totalPaid = 0;
            Pago tmpPago = new Pago();
            
            tmpPago.setIdRecibo(this.idRecibo);
            
            for(Pago p: tmpPago.readPagos()){
                totalPaid += p.getMontoPagado();
            }
            
            //RETURN TOTAL TO PAY
            Recibo recibo = new Recibo(this.idRecibo);
            double totalOfRecibo;
            
            recibo.readRecibo();
            totalOfRecibo = recibo.getTotal();
            
            boolean allIsPaid = (totalPaid >= totalOfRecibo);
            
            String query = "UPDATE recibo SET Pagado=? WHERE IdRecibo=?";
            PreparedStatement ps = Launcher.session.getConnection().prepareStatement(query);
            
            ps.setInt(1,(allIsPaid) ? 1 : 0);
            ps.setInt(2, this.idRecibo);
            
            paid = (ps.executeUpdate() > 0);
        } catch (SQLException ex) {
            Logger.getLogger(Pago.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return paid;
    }
}
