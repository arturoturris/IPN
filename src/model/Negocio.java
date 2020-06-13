package model;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import controller.Launcher;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Negocio {
    private int idNegocio;
    private String cliente_rfc;
    private String nombre;
    private String giroComercial;
    private String calle;
    private int numero;
    private String interior;
    private String colonia;
    private String municipio;
    private String estado;
    private int codigoPostal;
    private double superficie;
    private String horario;
    private String coordenadas;
    
    //CONSTRUTOR TO CREATE AND READ negocios
    public Negocio(String cliente_rfc){
        this.idNegocio = -1;
        this.cliente_rfc = cliente_rfc;
        this.nombre = null;
        this.giroComercial = null;
        this.calle = null;
        this.numero = -1;
        this.interior = null;
        this.colonia = null;
        this.municipio = null;
        this.estado = null;
        this.codigoPostal = -1;
        this.superficie = -1;
        this.horario = null;
        this.coordenadas = null;
    }
    
    //CONSTRUTOR TO READ,UPDATE, DELETE negocio
    public Negocio(int idNegocio){
        this.idNegocio = idNegocio;
        this.cliente_rfc = null;
        this.nombre = null;
        this.giroComercial = null;
        this.calle = null;
        this.numero = -1;
        this.interior = null;
        this.colonia = null;
        this.municipio = null;
        this.estado = null;
        this.codigoPostal = -1;
        this.superficie = -1;
        this.horario = null;
        this.coordenadas = null;
    }

    public int getIdNegocio() {
        return idNegocio;
    }

    public void setIdNegocio(int idNegocio) {
        this.idNegocio = idNegocio;
    }
    
    public String getCliente_rfc() {
        return cliente_rfc;
    }

    public void setCliente_rfc(String cliente_rfc) {
        this.cliente_rfc = cliente_rfc;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getGiroComercial() {
        return giroComercial;
    }

    public void setGiroComercial(String giroComercial) {
        this.giroComercial = giroComercial;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getInterior() {
        return interior;
    }

    public void setInterior(String interior) {
        this.interior = interior;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(int codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public double getSuperficie() {
        return superficie;
    }

    public void setSuperficie(double superficie) {
        this.superficie = superficie;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(String coordenadas) {
        this.coordenadas = coordenadas;
    }
    
    public boolean createNegocio(){
        boolean created = false;
        
        try {    
            String query = "INSERT INTO negocio(Cliente_Rfc,Nombre,GiroComercial,Calle,Numero,Interior,Colonia,Municipio,Estado,CodigoPostal,Superficie,Horario,Coordenadas) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = Launcher.session.getConnection().prepareStatement(query);
            DecimalFormat f = new DecimalFormat("##.00");
            
            ps.setString(1,this.cliente_rfc);
            ps.setString(2,this.nombre);
            ps.setString(3,this.giroComercial);
            ps.setString(4,this.calle);
            ps.setInt(5,this.numero);
            ps.setString(6,this.interior);
            ps.setString(7,this.colonia);
            ps.setString(8,this.municipio);
            ps.setString(9,this.estado);
            ps.setInt(10,this.codigoPostal);
            ps.setDouble(11,Double.valueOf(f.format(this.superficie)));
            ps.setString(12,this.horario);
            ps.setString(13,this.coordenadas);
            
            
            created = (ps.executeUpdate() > 0);

        } catch (SQLException ex) {
            Logger.getLogger(Negocio.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return created;
    }
    
    public boolean readNegocio(){
        boolean read = false;
        
        try {
            String query = "SELECT * FROM negocio WHERE IdNegocio=?";
            PreparedStatement ps = Launcher.session.getConnection().prepareStatement(query);
            ResultSet result;
            
            ps.setInt(1, this.idNegocio);
            ps.execute();
            result = ps.getResultSet();
            
            //IF THERES NO RESULT
            if(!(read = result.next())){
                return read;
            }
            
            this.cliente_rfc = result.getString("Cliente_rfc");
            this.nombre = result.getString("Nombre");
            this.giroComercial = result.getString("GiroComercial");
            this.calle = result.getString("Calle");
            this.numero = result.getInt("Numero");
            this.interior = result.getString("Interior");
            this.colonia = result.getString("Colonia");
            this.municipio = result.getString("Municipio");
            this.estado = result.getString("Estado");
            this.codigoPostal = result.getInt("CodigoPostal");
            this.superficie = result.getDouble("Superficie");
            this.horario = result.getString("Horario");
            this.coordenadas = result.getString("Coordenadas");
        } catch (SQLException ex) {
            Logger.getLogger(Negocio.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return read;
    }
    
    public ArrayList<Negocio> readNegocios(){
        ArrayList<Negocio> negocios = new ArrayList<>();
        
        try {
            String query = "SELECT * FROM negocio WHERE Cliente_rfc=?";
            ResultSet result;
            PreparedStatement ps = Launcher.session.getConnection().prepareStatement(query);
            ps.setString(1, this.cliente_rfc);
            Negocio tmpNegocio;
            
            result = ps.executeQuery();
            
            while(result.next()){
                tmpNegocio = new Negocio(this.cliente_rfc);
                
                tmpNegocio.idNegocio = result.getInt("IdNegocio");
                tmpNegocio.cliente_rfc = result.getString("Cliente_rfc");
                tmpNegocio.nombre = result.getString("Nombre");
                tmpNegocio.giroComercial = result.getString("GiroComercial");
                tmpNegocio.calle = result.getString("Calle");
                tmpNegocio.numero = result.getInt("Numero");
                tmpNegocio.interior = result.getString("Interior");
                tmpNegocio.colonia = result.getString("Colonia");
                tmpNegocio.municipio = result.getString("Municipio");
                tmpNegocio.estado = result.getString("Estado");
                tmpNegocio.codigoPostal = result.getInt("CodigoPostal");
                tmpNegocio.superficie = result.getDouble("Superficie");
                tmpNegocio.horario = result.getString("Horario");
                tmpNegocio.coordenadas = result.getString("Coordenadas");
                
                negocios.add(tmpNegocio);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Negocio.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return negocios;
    }
    
    public boolean updateNegocio(){
        boolean updated = false;
        
        try {    
            String query = "UPDATE negocio SET Nombre=?,GiroComercial=?,Calle=?,Numero=?,Interior=?,Colonia=?,Municipio=?,Estado=?,CodigoPostal=?,Superficie=?,Horario=?,Coordenadas=? WHERE IdNegocio=?";
            PreparedStatement ps = Launcher.session.getConnection().prepareStatement(query);
            DecimalFormat f = new DecimalFormat("##.00");
            
            ps.setString(1, this.nombre);
            ps.setString(2, this.giroComercial);
            ps.setString(3, this.calle);
            ps.setInt(4, this.numero);
            ps.setString(5, this.interior);
            ps.setString(6, this.colonia);
            ps.setString(7, this.municipio);
            ps.setString(8, this.estado);
            ps.setInt(9, this.codigoPostal);
            ps.setDouble(10, Double.valueOf(f.format(this.superficie)));
            ps.setString(11, this.horario);
            ps.setString(12, this.coordenadas);
            ps.setInt(13, this.idNegocio);
            
            updated = (ps.executeUpdate() > 0);
        } catch (SQLException ex) {
            Logger.getLogger(Negocio.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return updated;
    }
    
    public boolean deleteNegocio(){
        boolean deleted = false;
        
        try {
            String query = "DELETE FROM negocio WHERE IdNegocio=?";
            PreparedStatement ps = Launcher.session.getConnection().prepareStatement(query);
            
            ps.setInt(1, this.idNegocio);
            
            deleted = (ps.executeUpdate() > 0);
        } catch (SQLException ex) {
            Logger.getLogger(Negocio.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return deleted;
    }
}
