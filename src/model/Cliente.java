package model;

import controller.Launcher;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cliente extends Persona{
    private String rfc;
    private int persona_id;
    private String regimen;
    private String email;
    private String telefono;
    private String celular;
    private String contraseganSat;
    private Date fechaActualizacion;
    private String comentarios;
    
    //CONSTRUCTOR TO CREATE AND READ clientes
    public Cliente(){
        super();
        this.rfc = null;
        this.persona_id = -1;
        this.regimen = null;
        this.email = null;
        this.telefono = null;
        this.celular = null;
        this.contraseganSat = null;
        this.fechaActualizacion = null;
        this.comentarios = null;
    }
    
    //CONSTRUCTOR TO READ, UPDATE AND DELETE
    public Cliente(String rfc){
        this.rfc = rfc;
        this.persona_id = -1;
        this.regimen = null;
        this.email = null;
        this.telefono = null;
        this.celular = null;
        this.contraseganSat = null;
        this.fechaActualizacion = null;
        this.comentarios = null;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public int getPersona_id() {
        return persona_id;
    }

    public void setPersona_id(int persona_id) {
        this.persona_id = persona_id;
    }

    public String getRegimen() {
        return regimen;
    }

    public void setRegimen(String regimen) {
        this.regimen = regimen;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getContraseganSat() {
        return contraseganSat;
    }

    public void setContraseganSat(String contraseganSat) {
        this.contraseganSat = contraseganSat;
    }

    public Date getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(Date fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }
    
    public int getPersonaId(String rfc){
        int personaId = -1;
        
        try {
            String query = "SELECT Persona_Id FROM cliente WHERE Rfc=?";
            PreparedStatement ps = Launcher.session.getConnection().prepareStatement(query);
            ResultSet result;
            
            ps.setString(1, this.rfc);
            result = ps.executeQuery();
            result.next();
            personaId = result.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(Empleado.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return personaId;
    }
    
    public boolean createCliente(){
        boolean created = false;
        
        try {
            //CREATE persona
            Persona newPersona = new Persona(Launcher.session);
            int idPersona = newPersona.signUpPersona(this.nombre, this.paterno, this.materno, this.sexo);
            
            String query = "INSERT INTO cliente(Rfc,Persona_Id,Regimen,Email,Telefono,Celular,ContrasegnaSAT,FechaActualizacionDatos,Comentarios) VALUES(?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = Launcher.session.getConnection().prepareStatement(query);
            
            ps.setString(1,this.rfc);
            ps.setInt(2,idPersona);
            ps.setString(3,this.regimen);
            ps.setString(4,this.email);
            ps.setString(5, this.telefono);
            ps.setString(6,this.celular);
            ps.setString(7,this.contraseganSat);
            ps.setDate(8, new java.sql.Date(Calendar.getInstance().getTimeInMillis()));
            ps.setString(9,this.comentarios);
            
            created = (ps.executeUpdate() > 0);

        } catch (SQLException ex) {
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return created;
    }
    
    public boolean readCliente(){
        boolean read = false;
        
        try {
            String query = "SELECT p.*,c.* FROM persona as p INNER JOIN cliente as c ON p.Id=c.Persona_Id WHERE c.Rfc=?";
            PreparedStatement ps = Launcher.session.getConnection().prepareStatement(query);
            ResultSet result;
            
            ps.setString(1, this.rfc);
            ps.execute();
            result = ps.getResultSet();
            
            //IF THERES NO RESULT
            if(!(read = result.next())){
                return read;
            }
            
            this.nombre = result.getString("Nombre");
            this.paterno = result.getString("Paterno");
            this.materno = result.getString("Materno");
            this.sexo = result.getString("Sexo").charAt(0);
            this.regimen = result.getString("Regimen");
            this.email = result.getString("Email");
            this.telefono = result.getString("Telefono");
            this.celular = result.getString("Celular");
            this.contraseganSat = result.getString("ContrasegnaSat");
            this.fechaActualizacion = result.getDate("FechaActualizacionDatos");
            this.comentarios = result.getString("Comentarios");
        } catch (SQLException ex) {
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return read;
    }
    
    public ArrayList<Cliente> readClientes(){
        ArrayList<Cliente> clientes = new ArrayList<>();
        
        try {
            String query = "SELECT p.Nombre,p.Paterno,p.Materno,c.Rfc,c.Regimen FROM persona AS p INNER JOIN cliente AS c ON p.Id=c.Persona_Id ";
            ResultSet result;
            PreparedStatement ps = Launcher.session.getConnection().prepareStatement(query);
            Cliente tmpCliente;
            
            result = ps.executeQuery();
            
            while(result.next()){
                tmpCliente = new Cliente();
                
                tmpCliente.nombre = result.getString("Nombre");
                tmpCliente.paterno = result.getString("Paterno");
                tmpCliente.materno = result.getString("Materno");
                tmpCliente.rfc = result.getString("Rfc");
                tmpCliente.regimen = result.getString("Regimen");
                
                clientes.add(tmpCliente);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Empleado.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return clientes;
    }
    
    public boolean updateCliente(){
        boolean updated;
        
        //FIRST UPDATE PERSONA
        Persona persona = new Persona(Launcher.session);
        int personaId = getPersonaId(this.rfc);
        
        updated = persona.updatePersona(personaId, this.nombre, this.paterno, this.materno, this.sexo);
        
        if(!updated)
            return updated;
        
        try {
            //********rfc or idpersona
            String query = "UPDATE cliente SET Regimen=?,Email=?,Telefono=?,Celular=?,ContrasegnaSat=?,FechaActualizacionDatos=?,Comentarios=? WHERE Rfc=?";
            PreparedStatement ps = Launcher.session.getConnection().prepareStatement(query);
            
            ps.setString(1, this.regimen);
            ps.setString(2, this.email);
            ps.setString(3, this.telefono);
            ps.setString(4,this.celular);
            ps.setString(5, this.contraseganSat);
            ps.setDate(6, new java.sql.Date(Calendar.getInstance().getTimeInMillis()));
            ps.setString(7,this.comentarios);
            ps.setString(8, this.rfc);
            
            updated = (ps.executeUpdate() > 0);
        } catch (SQLException ex) {
            Logger.getLogger(Empleado.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return updated;
    }
    
    public boolean deleteCliente(){
        int personaId = getPersonaId(this.rfc);
        Persona persona = new Persona(Launcher.session);
        boolean deleted;
        
        //THIS WORKS THANKS TO ON DELETE CASCADE
        deleted = persona.deletePersona(personaId);

        return deleted;
    }
    
}
