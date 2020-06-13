package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import controller.Launcher;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Recibo {
    private int idConcepto;
    private int idRecibo;
    private String cliente_rfc;
    private Date fechaEmision;
    private boolean pagado;
    private ArrayList<Concepto> conceptos;
    
    //CONSTRUCTOR TO CREATE AND READ recibos
    public Recibo(){
        this.idConcepto = 1;
        this.idRecibo = -1;
        this.cliente_rfc = null;
        this.fechaEmision = null;
        this.pagado = false;
        this.conceptos = new ArrayList<>();
    }
    
    //CONSTRUCTOR TO READ, UPDATE AND DELETE recibo
    public Recibo(int idRecibo){
        this.idConcepto = -1;
        this.idRecibo = idRecibo;
        this.cliente_rfc = null;
        this.fechaEmision = null;
        this.pagado = false;
        this.conceptos = new ArrayList<>();
    }

    public int getIdConcepto() {
        return idConcepto;
    }

    public void setIdConcepto(int idConcepto) {
        this.idConcepto = idConcepto;
    }

    public int getIdRecibo() {
        return idRecibo;
    }

    public void setIdRecibo(int idRecibo) {
        this.idRecibo = idRecibo;
    }

    public String getCliente_rfc() {
        return cliente_rfc;
    }

    public void setCliente_rfc(String cliente_rfc) {
        this.cliente_rfc = cliente_rfc;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public boolean isPagado() {
        return pagado;
    }

    public void setPagado(boolean pagado) {
        this.pagado = pagado;
    }

    public ArrayList<Concepto> getConceptos() {
        return conceptos;
    }

    public void addConcepto(Concepto concepto) {
        concepto.setIdConcepto(idConcepto++);
        this.conceptos.add(concepto);
    }
    
    public void removeConcepto(int idConcepto){
        for(Concepto c: conceptos){
            if(c.getIdConcepto() == idConcepto){
                conceptos.remove(c);
                break;
            }
        }
    }
    
    public double getTotal(){
        double total = 0;
        
        for(Concepto c: conceptos){
            total += c.getSubTotal();
        }
        
        return total;
    }
    
    public boolean createRecibo(){
        boolean created = false;
        
        try {    
            String query = "INSERT INTO recibo(Cliente_Rfc,FechaDeEmision,Pagado) VALUES(?,?,?)";
            PreparedStatement ps = Launcher.session.getConnection().prepareStatement(query,PreparedStatement.RETURN_GENERATED_KEYS);
            ResultSet result;
            int generatedId;
            
            ps.setString(1,this.cliente_rfc);
            ps.setDate(2,new java.sql.Date(this.fechaEmision.getTime()));
            ps.setBoolean(3,this.pagado);
            
            ps.executeUpdate();
            
            result = ps.getGeneratedKeys();
            result.next();
            generatedId = result.getInt(1);
            
            //PARA CADA CONCEPTO -> CREAR CONCEPTO EN BASE DE DATOS
            for(Concepto c: conceptos){
                c.setRecibo_IdRecibo(generatedId);
                created = c.createConcepto();
            }
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(Recibo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        return created;
    }
    
    public boolean readRecibo(){
        boolean read = false;
        
        try {    
            String query = "SELECT * FROM recibo WHERE IdRecibo=?";
            PreparedStatement ps = Launcher.session.getConnection().prepareStatement(query);
            ResultSet result;
            
            ps.setInt(1, this.idRecibo);
            ps.execute();
            result = ps.getResultSet();
            
            //IF THERES NO RESULT
            if(!(read = result.next())){
                return read;
            }
            
            //READ recibo
            this.cliente_rfc = result.getString("Cliente_rfc");
            this.fechaEmision = result.getDate("FechaDeEmision");
            this.pagado = result.getBoolean("Pagado");
            
            //READ ALL conceptos FROM recibo
            Concepto c = new Concepto();
            c.setRecibo_IdRecibo(this.idRecibo);
            
            this.conceptos = c.readConceptos();
            this.idConcepto = this.conceptos.get(this.conceptos.size()-1).getIdConcepto() + 1;
        } catch (SQLException ex) {
            Logger.getLogger(Recibo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return read;
    }
    
//    //TO READ ALL recibos PUT rfcCliente AND estado NULL, OTHERWISE SET rfcCliente OR/AND estado
//    public ArrayList<Recibo> readRecibos(String rfcCliente,String estado){
//        ArrayList<Recibo> recibos = new ArrayList<>();
//        
//        try {
//            String w,a;
//            w = (rfcCliente != null) ? " WHERE Cliente_Rfc=" + rfcCliente : "";
//            a = (estado != null) ? (rfcCliente != null) ? " AND Pagado=" + estado : " WHERE Pagado=" + estado : "";
//            String query = "SELECT * FROM recibo" + w + a;
//            PreparedStatement ps = Launcher.session.getConnection().prepareStatement(query);
//            ResultSet result;
//            Recibo tmpR;
//            
//            ps.execute();
//            result = ps.getResultSet();
//            
//            while(result.next()){
//                tmpR = new Recibo();
//                
//                //READ recibo
//                tmpR.idRecibo = result.getInt("IdRecibo");
//                tmpR.cliente_rfc = result.getString("Cliente_rfc");
//                tmpR.fechaEmision = result.getDate("FechaDeEmision");
//                tmpR.pagado = result.getBoolean("Pagado");
//
//                //READ ALL conceptos FROM recibo
//                Concepto c = new Concepto();
//                c.setRecibo_IdRecibo(tmpR.idRecibo);
//
//                tmpR.conceptos = c.readConceptos();
//                System.out.println("Conceptos leidos: " + conceptos.size());
//                tmpR.idConcepto = this.conceptos.get(this.conceptos.size()-1).getIdConcepto() + 1;
//                
//                recibos.add(tmpR);
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(Recibo.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//        return recibos;
//    }
    
    public ArrayList<Recibo> readRecibos(){
        ArrayList<Recibo> recibos = new ArrayList<>();
        
        try {
            String query = "SELECT * FROM recibo";
            PreparedStatement ps = Launcher.session.getConnection().prepareStatement(query);
            ResultSet result;
            Recibo tmpR;
            
            ps.execute();
            result = ps.getResultSet();
            
            while(result.next()){
                tmpR = new Recibo();
                
                //READ recibo
                tmpR.idRecibo = result.getInt("IdRecibo");
                tmpR.cliente_rfc = result.getString("Cliente_rfc");
                tmpR.fechaEmision = result.getDate("FechaDeEmision");
                tmpR.pagado = result.getBoolean("Pagado");
                
                recibos.add(tmpR);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Recibo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return recibos;
    }
    
    public ArrayList<Recibo> readRecibosCliente(){
        ArrayList<Recibo> recibos = new ArrayList<>();
        
        try {
            String query = "SELECT * FROM recibo WHERE Cliente_Rfc=?";
            PreparedStatement ps = Launcher.session.getConnection().prepareStatement(query);
            
            ps.setString(1,this.cliente_rfc);
            
            ResultSet result;
            Recibo tmpR;
           
            ps.execute();
            result = ps.getResultSet();
            
            while(result.next()){
                tmpR = new Recibo();
                
                //READ recibo
                tmpR.idRecibo = result.getInt("IdRecibo");
                tmpR.cliente_rfc = result.getString("Cliente_rfc");
                tmpR.fechaEmision = result.getDate("FechaDeEmision");
                tmpR.pagado = result.getBoolean("Pagado");
                
                recibos.add(tmpR);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Recibo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return recibos;
    }
    
    public boolean updateRecibo(){
        boolean updated = false;
        
        try {    
            String query = "UPDATE recibo SET Cliente_Rfc=?, FechaDeEmision=?, Pagado=? WHERE IdRecibo=?";
            PreparedStatement ps = Launcher.session.getConnection().prepareStatement(query);
            
            ps.setString(1, this.cliente_rfc);
            ps.setDate(2, new java.sql.Date(this.fechaEmision.getTime()));
            ps.setBoolean(3, this.pagado);
            ps.setInt(4, this.idRecibo);
            
            updated = (ps.executeUpdate() > 0);
        } catch (SQLException ex) {
            Logger.getLogger(Recibo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return updated;
    }
    
    //conceptos ARE DELETED THANKS TO ON DELETE CASCADE
    public boolean deleteRecibo(){
        boolean deleted = false;
        
        try {    
            String query = "DELETE FROM recibo WHERE IdRecibo=?";
            PreparedStatement ps = Launcher.session.getConnection().prepareStatement(query);
            
            ps.setInt(1, this.idRecibo);
            
            deleted = (ps.executeUpdate() > 0);
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(Recibo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return deleted;
    }
}
