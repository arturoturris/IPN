package model;

import config.UserSession;
import controller.Launcher;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Usuario {
    private int idUsuario;
    private String usuario;
    private String contrasegna;
    private int empleado_IdEmpleado;
    private Date ultimaEntrada;
    private String tipo;
    
    //CONSTRUCTOR TO CREATE, READ, UPDATE AND DELETE A usuario
    public Usuario(int idEmpleado){
        this.idUsuario = -1;
        usuario = null;
        contrasegna = null;
        empleado_IdEmpleado = idEmpleado;
        ultimaEntrada = new Date(0);
        tipo = null;
    }
    
    public static void main(String []args){
        Usuario newUser = new Usuario(4);
        
        newUser.setUsuario("carmina");
        newUser.setContrasegna("ninguna");
        System.out.println(newUser.createUsuario());
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getNombreUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasegna() {
        return contrasegna;
    }

    public void setContrasegna(String contrasegna) {
        this.contrasegna = contrasegna;
    }

    public int getEmpleado_IdEmpleado() {
        return empleado_IdEmpleado;
    }

    public Date getUltimaEntrada() {
        return ultimaEntrada;
    }

    public void setUltimaEntrada(Date ultimaEntrada) {
        this.ultimaEntrada = ultimaEntrada;
    }
    
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public boolean createUsuario(){
        boolean created = false;
        
        try {    
            String query = "INSERT INTO usuario(Usuario,Contrasegna,IdEmpleado,UltimaEntrada,Tipo) VALUES(?,?,?,?,?)";
            PreparedStatement ps = Launcher.session.getConnection().prepareStatement(query);
            
            ps.setString(1,this.usuario);
            ps.setString(2,this.contrasegna);
            ps.setInt(3,this.empleado_IdEmpleado);
            ps.setDate(4,new java.sql.Date(0));
            ps.setString(5, this.tipo);
            
            created = (ps.executeUpdate() > 0) ? true : false;

        } catch (SQLException ex) {
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return created;
    }
    
    public boolean readUsuario(){
        boolean read = false;
        
        try {
            String query = "SELECT * FROM usuario WHERE IdEmpleado=?";
            PreparedStatement ps = Launcher.session.getConnection().prepareStatement(query);
            ResultSet result;
            
            ps.setInt(1, this.empleado_IdEmpleado);
            ps.execute();
            result = ps.getResultSet();
            
            //IF THERES NO RESULT
            if(!(read = result.next())){
                return read;
            }
            
            this.usuario = result.getString("Usuario");
            this.contrasegna = result.getString("Contrasegna");
            this.empleado_IdEmpleado = result.getInt("IdEmpleado");
            this.ultimaEntrada = result.getDate("UltimaEntrada");
            this.tipo = result.getString("Tipo");
        } catch (SQLException ex) {
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return read;
    }
    
    //PENDING TO IMPLEMENT ---- CONSIDER tipo
    public boolean updateUsuario(){
        boolean updated = false;
        
        try {    
            String query = "UPDATE usuario SET usuario=?, contrasegna=? WHERE IdEmpleado=?";
            PreparedStatement ps = Launcher.session.getConnection().prepareStatement(query);
            
            ps.setString(1, this.usuario);
            ps.setString(2, this.contrasegna);
            ps.setInt(3, this.empleado_IdEmpleado);
            
            updated = (ps.executeUpdate() > 0);
        } catch (SQLException ex) {
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return updated;
    }
    
    //DELETE OPERATION NOT NECESSARY 'CAUSE ON DELETE CASCADE IN empleado TABLE -- MAYBE NECESSARY BUT NOT NOW
    
}
