package model;

import config.UserSession;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Persona {
    UserSession session;
    String nombre;
    String paterno;
    String materno;
    char sexo;
    
    public Persona(UserSession session){
        this.session = session;
    }

    public Persona(){
        this.nombre = null;
        this.paterno = null;
        this.materno = null;
        this.sexo = 'H';
    }
    
    public UserSession getSession() {
        return session;
    }

    public void setSession(UserSession session) {
        this.session = session;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPaterno() {
        return paterno;
    }

    public void setPaterno(String paterno) {
        this.paterno = paterno;
    }

    public String getMaterno() {
        return materno;
    }

    public void setMaterno(String materno) {
        this.materno = materno;
    }

    public char getSexo() {
        return sexo;
    }

    public void setSexo(char sexo) {
        this.sexo = sexo;
    }
    
    
    
    public static void main(String []args){
        UserSession tmpSession = new UserSession("jdbc:mysql://localhost:3306/ipn","root","");
        
        if(!tmpSession.connect()){
            System.out.println("ERROR AL CONECTAR");
            System.exit(0);
        }
        
        Persona p = new Persona(tmpSession);
        if(p.signUpPersona("Perla", "Hernandez", "Juares", 'M') != -1){
            System.out.println("SE REGISTRÓ");
        }
    }
    
    public int signUpPersona(String nombre, String paterno, String materno, char sexo){
        int generatedId = -1;
        
        try {
            String query = "INSERT INTO persona(nombre, paterno, materno, sexo) VALUES (?,?,?,?)";
            PreparedStatement ps = session.getConnection().prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
            ResultSet result;
            
            ps.setString(1, nombre);
            ps.setString(2, paterno);
            ps.setString(3, materno);
            ps.setString(4, String.valueOf(sexo));
            
            ps.executeUpdate();
            result = ps.getGeneratedKeys();
            result.next();
            generatedId = result.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(Persona.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return generatedId;
    }
    
    public boolean updatePersona(int idPersona,String nombre, String paterno, String materno, char sexo){
        boolean updated = false;
        
        try {
            String query = "UPDATE persona SET Nombre=?,Paterno=?,Materno=?,Sexo=? WHERE Id=?";
            PreparedStatement ps = session.getConnection().prepareStatement(query);
            
            ps.setString(1, nombre);
            ps.setString(2, paterno);
            ps.setString(3, materno);
            ps.setString(4, String.valueOf(sexo));
            ps.setInt(5, idPersona);
            
            updated = (ps.executeUpdate() > 0);
        } catch (SQLException ex) {
            Logger.getLogger(Persona.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return updated;
    }
    
    public boolean deletePersona(int idPersona){
        boolean deleted = false;
        
        try {
            String query = "DELETE FROM persona WHERE Id=?";
            PreparedStatement ps = session.getConnection().prepareStatement(query);
            
            ps.setInt(1, idPersona);
            
            deleted = (ps.executeUpdate() > 0);
        } catch (SQLException ex) {
            Logger.getLogger(Persona.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return deleted;
    }
}
