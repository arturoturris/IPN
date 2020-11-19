package config;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Usuario;

public class UserSession{
    private final String url;
    private final String user;
    private final String password;
    private int idEmpleado;
    private String tipoSesion;
    private Connection connection;
    
    public UserSession(String url, String user, String password){
        this.url = url;
        this.user= user;
        this.password = password;
        this.idEmpleado = -1;
        this.tipoSesion = null;
    }
    
    public static boolean authorizeLogin(UserSession session,String user, String password){
        boolean authorized = false;
        
        try {
            String query = "SELECT EXISTS(SELECT * FROM usuario WHERE usuario=? AND contrasegna=?)";
            PreparedStatement ps = session.getConnection().prepareStatement(query);
            ResultSet result;
            
            ps.setString(1, user);
            ps.setString(2, password);
            result = ps.executeQuery();
            result.next();
            authorized = result.getBoolean(1);
        } catch (SQLException ex) {
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return authorized;
    }
    
    public boolean connect(){
        boolean connected = false;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(this.url,this.user,this.password);
            connected = this.connection.isValid(50000);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UserSession.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(UserSession.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return connected;
    }
    
    public boolean isConnected(){
        boolean connected = false;
        
        try {
            connected =  this.connection.isValid(50000);
        } catch (SQLException ex) {
            Logger.getLogger(UserSession.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return connected;
}
    
    public Connection getConnection(){
        return this.connection;}
    
    public String[] getIdEmpleadoTipoEmpleado(String user){
        String[] userAttributes = new String[2];
        try {
            String query = "SELECT IdEmpleado,Tipo FROM usuario WHERE usuario=? ";
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet result;
            
            ps.setString(1, user);
            result = ps.executeQuery();
            result.next();
            
            userAttributes[0] = result.getString(1);
            userAttributes[1] = result.getString(2);
        } catch (SQLException ex) {
            Logger.getLogger(UserSession.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return userAttributes;
    }
    
    public void setIdEmpleado(int idEmpleado){
        this.idEmpleado = idEmpleado;
    }
    
    public int getIdEmpleado(){
        return this.idEmpleado;
    }

    public String getTipoSesion() {
        return tipoSesion;
    }

    public void setTipoSesion(String tipoSesion) {
        this.tipoSesion = tipoSesion;
    }
}
