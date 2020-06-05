package model;

import config.UserSession;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Empleado {
    UserSession session;
    
    
    public Empleado(UserSession session){
        this.session = session;
    }
    
    private int getPersonaId(int idEmpleado){
        int personaId = -1;
        
        try {
            String query = "SELECT Persona_Id FROM empleado WHERE IdEmpleado=?";
            PreparedStatement ps = session.getConnection().prepareStatement(query);
            ResultSet result;
            
            ps.setInt(1, idEmpleado);
            result = ps.executeQuery();
            result.next(); //POSIBLEMENTE QUITAR
            personaId = result.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(Empleado.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return personaId;
    }
    
    public ResultSet getEmpleado(int idEmpleado){
        ResultSet result = null;
        
        try {
            String query = "SELECT p.*,e.* FROM `empleado` as e LEFT JOIN persona as p ON e.Persona_Id=p.Id WHERE e.IdEmpleado=?";
            PreparedStatement ps = session.getConnection().prepareStatement(query);
            
            ps.setInt(1, idEmpleado);
            result = ps.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(Empleado.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return result;
    }
    
    public ResultSet getEmpleados(){
        ResultSet result = null;
        try {
            String query = "SELECT e.IdEmpleado,p.Paterno,p.Materno,p.Nombre,e.Estado FROM empleado as e LEFT JOIN persona as p ON e.Persona_Id = p.Id";
            PreparedStatement ps = session.getConnection().prepareStatement(query);
            result = ps.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(Empleado.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return result;
    }
    
    public boolean signUpEmpleado(String nombre, String paterno, String materno, char sexo, String puesto, String horario, double salario, Date fechaAlta, String estado){
        boolean signedUp = false;
        
        //FIRST SIGN UP PERSONA
        Persona newPersona = new Persona(this.session);
        int generatedId = newPersona.signUpPersona(nombre, paterno, materno, sexo);
        
        if(generatedId == -1)
            return signedUp;
        
        try {
            String query = "INSERT INTO EMPLEADO(Persona_Id,Puesto,Horario,Salario,FechaAlta,Estado) VALUES (?,?,?,?,?,?)";
            PreparedStatement ps = session.getConnection().prepareStatement(query);
            
            ps.setInt(1, generatedId);
            ps.setString(2, puesto);
            ps.setString(3, horario);
            ps.setDouble(4, salario);
            ps.setDate(5,new java.sql.Date(fechaAlta.getTime()));
            ps.setString(6, estado);
            
            signedUp = (ps.executeUpdate() > 0);
        } catch (SQLException ex) {
            Logger.getLogger(Empleado.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return signedUp;
    }

    public boolean updateEmpleado(int idEmpleado, String nombre, String paterno, String materno, char sexo, String puesto, String horario, double salario, Date fechaAlta, String estado){
        boolean updated;
        
        //FIRST UPDATE PERSONA
        Persona persona = new Persona(this.session);
        int personaId = getPersonaId(idEmpleado);
        
        updated = persona.updatePersona(personaId, nombre, paterno, materno, sexo);
        
        if(!updated)
            return updated;
        
        try {
            String query = "UPDATE empleado SET Puesto=?,Horario=?,Salario=?,FechaAlta=?,Estado=? WHERE IdEmpleado=?";
            PreparedStatement ps = session.getConnection().prepareStatement(query);
            
            ps.setString(1, puesto);
            ps.setString(2, horario);
            ps.setDouble(3, salario);
            ps.setDate(4,new java.sql.Date(fechaAlta.getTime()));
            ps.setString(5, estado);
            ps.setInt(6, idEmpleado);
            
            updated = (ps.executeUpdate() > 0);
        } catch (SQLException ex) {
            Logger.getLogger(Empleado.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return updated;
    }
    
    public boolean deleteEmpleado(int idEmpleado){
        int personaId = getPersonaId(idEmpleado);
        Persona persona = new Persona(session);
        boolean deleted;
        
        //THIS WORKS THANKS TO ON DELETE CASCADE
        deleted = persona.deletePersona(personaId);

        return deleted;
    }
}
