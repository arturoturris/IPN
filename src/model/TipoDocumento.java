package model;

import controller.Launcher;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TipoDocumento {
    private int idTipoDocumento;
    private String nombre;
    private String descripcion;
    private String requisitos;
    
    //CONSTRUCTOR TO CREATE AND READ tipodocumentos
    public TipoDocumento(){
        this.idTipoDocumento = -1;
        this.nombre = null;
        this.descripcion = null;
        this.requisitos = null;
    }

    public int getIdTipoDocumento() {
        return idTipoDocumento;
    }

    public void setIdTipoDocumento(int idTipoDocumento) {
        this.idTipoDocumento = idTipoDocumento;
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

    public String getRequisitos() {
        return requisitos;
    }

    public void setRequisitos(String requisitos) {
        this.requisitos = requisitos;
    }
    
    public ArrayList<TipoDocumento> readTipoDocumentos(){
        ArrayList<TipoDocumento> tipoDocumentos = new ArrayList<>();
        
        try {
            String query = "SELECT * FROM tipodocumento";
            ResultSet result;
            PreparedStatement ps = Launcher.session.getConnection().prepareStatement(query);
            TipoDocumento tmpTipoDocumento;
            
            result = ps.executeQuery();
            
            while(result.next()){
                tmpTipoDocumento = new TipoDocumento();
                
                //IdTipoDocumento, Nombre, Descripcion, Requisitos
                tmpTipoDocumento.idTipoDocumento = result.getInt("IdTipoDocumento");
                tmpTipoDocumento.nombre = result.getString("Nombre");
                tmpTipoDocumento.descripcion = result.getString("Descripcion");
                tmpTipoDocumento.requisitos = result.getString("Requisitos");
                
                tipoDocumentos.add(tmpTipoDocumento);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Documento.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return tipoDocumentos;
    }
}
