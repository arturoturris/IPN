package model;

import controller.Launcher;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.PreparedStatement;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Documento {
    private int idDocumento;
    private String cliente_Rfc;
    private int idTipoDocumento;
    private Date fechaExpedicion;
    private Date fechaExpiracion;
    private String archivo;
    private String contrasegna;
    
    //CONSTRUCTOR TO CREATE AND AND READ documentos -> FOR READING documentos SET clienteRFC
    public Documento(){
        this.idDocumento = -1;
        this.cliente_Rfc = null;
        this.idTipoDocumento = -1;
        this.fechaExpedicion = null;
        this.fechaExpiracion = null;
        this.archivo = null;
        this.contrasegna = null;
    }
    
    //CONSTRUCTOR TO MODIFY, READ AND DELETE DOCUMENTO
    public Documento(int idDocumento){
        this.idDocumento = idDocumento;
        this.cliente_Rfc = null;
        this.idTipoDocumento = -1;
        this.fechaExpedicion = null;
        this.fechaExpiracion = null;
        this.archivo = null;
        this.contrasegna = null;
    }

    public int getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(int idDocumento) {
        this.idDocumento = idDocumento;
    }

    public String getCliente_Rfc() {
        return cliente_Rfc;
    }

    public void setCliente_Rfc(String cliente_Rfc) {
        this.cliente_Rfc = cliente_Rfc;
    }

    public int getIdTipoDocumento() {
        return idTipoDocumento;
    }

    public void setIdTipoDocumento(int idTipoDocumento) {
        this.idTipoDocumento = idTipoDocumento;
    }

    public Date getFechaExpedicion() {
        return fechaExpedicion;
    }

    public void setFechaExpedicion(Date fechaExpedicion) {
        this.fechaExpedicion = fechaExpedicion;
    }

    public Date getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(Date fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public String getContrasegna() {
        return contrasegna;
    }

    public void setContrasegna(String contrasegna) {
        this.contrasegna = contrasegna;
    }
    
    public boolean createDocumento(){
        boolean created = false;
        
        try {
            String query = "INSERT INTO documento(Cliente_Rfc,TipoDocumento_idDocumento,FechaExpedicion,FechaExpiracion,Contrasegna) VALUES(?,?,?,?,?)";
            PreparedStatement ps = Launcher.session.getConnection().prepareStatement(query,PreparedStatement.RETURN_GENERATED_KEYS);
            ResultSet result;
            int idCreated;
            
            ps.setString(1,this.cliente_Rfc);
            ps.setInt(2,this.idTipoDocumento);
            ps.setDate(3,new java.sql.Date(this.fechaExpedicion.getTime()));
            ps.setDate(4,new java.sql.Date(this.fechaExpiracion.getTime()));
            ps.setString(5,this.contrasegna);
            
            created = (ps.executeUpdate() > 0);
            result = ps.getGeneratedKeys();
            result.next();
            idCreated = result.getInt(1);
            this.setIdDocumento(idCreated);
            
            //MOVE FILE TO FOLDER
            String fileName = moveFile();
            
            //IF FAILED WHILE MOVING FILE
            if(fileName == null){
                return false;
            }
            
            //UPDATE FILE PATH
            query = "UPDATE documento SET Archivo=? WHERE idDocumento=?";
            ps = Launcher.session.getConnection().prepareStatement(query);
            
            ps.setString(1, fileName);
            ps.setInt(2, idCreated);
            
            created = (ps.executeUpdate() > 0);
        } catch (SQLException ex) {
            Logger.getLogger(Documento.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return created;
    }
    
    public boolean readDocumento(){
        boolean read = false;
        
        try {
            String query = "SELECT * FROM documento WHERE IdDocumento=?";
            PreparedStatement ps = Launcher.session.getConnection().prepareStatement(query);
            ResultSet result;
            
            ps.setInt(1, this.idDocumento);
            ps.execute();
            result = ps.getResultSet();
            
            //IF THERES NO RESULT
            if(!(read = result.next())){
                return read;
            }
            
            this.cliente_Rfc = result.getString("Cliente_rfc");
            this.idTipoDocumento = result.getInt("TipoDocumento_idDocumento");
            this.fechaExpedicion = result.getDate("FechaExpedicion");
            this.fechaExpiracion = result.getDate("FechaExpiracion");
            this.archivo = result.getString("Archivo");
            this.contrasegna = result.getString("Contrasegna");
        } catch (SQLException ex) {
            Logger.getLogger(Negocio.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return read;
    }
    
    public ArrayList<Documento> readDocumentos(){
        ArrayList<Documento> documentos = new ArrayList<>();
        
        try {
            String query = "SELECT * FROM documento";
            ResultSet result;
            PreparedStatement ps = Launcher.session.getConnection().prepareStatement(query);
            Documento tmpDocumento;
            
            result = ps.executeQuery();
            
            while(result.next()){
                tmpDocumento = new Documento();
                
                //cliente, tipo, fe,fe,archivo,contrasegna
                tmpDocumento.idDocumento = result.getInt("idDocumento");
                tmpDocumento.cliente_Rfc = result.getString("Cliente_Rfc");
                tmpDocumento.idTipoDocumento = result.getInt("TipoDocumento_idDocumento");
                tmpDocumento.fechaExpedicion = result.getDate("FechaExpedicion");
                tmpDocumento.fechaExpiracion = result.getDate("FechaExpiracion");
                tmpDocumento.archivo = result.getString("Archivo");
                tmpDocumento.contrasegna = result.getString("Contrasegna");
                
                documentos.add(tmpDocumento);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Documento.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return documentos;
    }
    
    public ArrayList<Documento> readDocumentosCliente(){
        ArrayList<Documento> documentos = new ArrayList<>();
        
        try {
            String query = "SELECT * FROM documento WHERE Cliente_Rfc=?";
            ResultSet result;
            PreparedStatement ps = Launcher.session.getConnection().prepareStatement(query);
            ps.setString(1, this.cliente_Rfc);
            Documento tmpDocumento;
            
            result = ps.executeQuery();
            
            while(result.next()){
                tmpDocumento = new Documento();
                
                //cliente, tipo, fe,fe,archivo,contrasegna
                tmpDocumento.idDocumento = result.getInt("idDocumento");
                tmpDocumento.idTipoDocumento = result.getInt("TipoDocumento_idDocumento");
                tmpDocumento.fechaExpedicion = result.getDate("FechaExpedicion");
                tmpDocumento.fechaExpiracion = result.getDate("FechaExpiracion");
                tmpDocumento.archivo = result.getString("Archivo");
                tmpDocumento.contrasegna = result.getString("Contrasegna");
                
                documentos.add(tmpDocumento);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Documento.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return documentos;
    }
    
    public boolean updateDocumento(){
        boolean updated = false;
        
        try {    
            String query = "UPDATE documento SET Cliente_Rfc=?,TipoDocumento_idDocumento=?,FechaExpedicion=?,FechaExpiracion=?,Contrasegna=? WHERE IdDocumento=?";
            PreparedStatement ps = Launcher.session.getConnection().prepareStatement(query);
            
            ps.setString(1, this.cliente_Rfc);
            ps.setInt(2, this.idTipoDocumento);
            ps.setDate(3, new java.sql.Date(this.fechaExpedicion.getTime()));
            ps.setDate(4, new java.sql.Date(this.fechaExpiracion.getTime()));
            ps.setString(5, this.contrasegna);
            ps.setInt(6, this.idDocumento);
            
            updated = (ps.executeUpdate() > 0);
            
            if(this.archivo == null || this.archivo.equals(""))
                return updated;
            
            //GETTING archivo FROM DB
            ResultSet result;
            String actualFile,newFilePath;
            query = "SELECT archivo FROM documento WHERE idDocumento=?";
            ps = Launcher.session.getConnection().prepareStatement(query);
            
            ps.setInt(1, this.idDocumento);
            ps.execute();
            result = ps.getResultSet();
            result.next();
            actualFile = result.getString(1);
            newFilePath = this.archivo;
            
            //REPLACE EXISTING FILE -> DELETE OLD - MOVE NEW this.archivo
            this.archivo = actualFile;
            boolean oldFileDeleted = this.deleteDocumento();
            this.archivo = newFilePath;
            String fileName = this.moveFile();
            
            System.out.println("Viejo archivo eliminado: " + oldFileDeleted);
            System.out.println("Nombre nuevo archivo: " + fileName);
            System.out.println("IdDocumento a actualizar: " + this.idDocumento);
            
            //UPDATE FILE PATH -> HAS A CRITICAL ERROR, FIX INMEDIATLY
            query = "UPDATE documento SET Archivo=? WHERE idDocumento=?";
            ps = Launcher.session.getConnection().prepareStatement(query);
            
            ps.setString(1, fileName);
            ps.setInt(2, this.idDocumento);
            
            updated = (ps.executeUpdate() > 0);
        } catch (SQLException ex) {
            Logger.getLogger(Negocio.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return updated;
    }
    
    public boolean deleteDocumento(){
        boolean deleted = false;
        
        try {
            String query = "SELECT Cliente_Rfc, Archivo FROM documento WHERE idDocumento=?";
            PreparedStatement ps = Launcher.session.getConnection().prepareStatement(query);
            ResultSet result;
            
            ps.setInt(1, this.idDocumento);
            ps.execute();
            result = ps.getResultSet();
            result.next();
            this.cliente_Rfc = result.getString(1);
            this.archivo = result.getString(2);
            
            query = "DELETE FROM documento WHERE idDocumento=?";
            ps = Launcher.session.getConnection().prepareStatement(query);
            
            ps.setInt(1, this.idDocumento);
            
            deleted = (ps.executeUpdate() > 0);
            
            if(!deleted)
                return deleted;
            
            //DELETE FILE FROM FOLDER
            deleted = deleteFile();
        } catch (SQLException ex) {
            Logger.getLogger(Pago.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return deleted;
    }
    
    private String moveFile(){
        Path path = null;
        String fileName = null;
        
        try {
            File originalFile = new File(this.archivo);
            File newFile = new File(this.getClienteFilesFolder() + originalFile.getName());
            File folder = new File(this.getClienteFilesFolder());
            
            //IF FOLDERS DONT EXISTS
            if(!Files.exists(folder.toPath(), LinkOption.NOFOLLOW_LINKS)){
                folder.mkdirs();
            }
            
            path = Files.copy(originalFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            
            if(path != null)
                fileName = originalFile.getName();
        } catch (IOException ex) {
            Logger.getLogger(Documento.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return fileName;
    }
    
    private boolean deleteFile(){
        boolean deleted;
        
        File file = new File(this.getClienteFilesFolder()+this.archivo);
        
        if(file.exists()){
            deleted = file.delete();
        }
        else{
            deleted = true;
        }
        
        return deleted;
    }
    
    public String getClienteFilesFolder(){
        return (this.getRootPath() + this.getClienteFolder() + "/Documentos/");
    }
    
    private String getClienteFolder(){
        boolean gotten;
        String folderName = null;
        
        try {
            String query = "SELECT p.Paterno,p.Materno,p.Nombre FROM persona as p INNER JOIN cliente as c ON p.Id=c.Persona_Id WHERE c.Rfc=?";
            PreparedStatement ps = Launcher.session.getConnection().prepareStatement(query);
            ResultSet result;
            
            ps.setString(1,this.cliente_Rfc);
            gotten = ps.execute();
            
            if(gotten){
                result = ps.getResultSet();
                result.next();
                folderName = result.getString("Paterno") + " " + result.getString("Materno") + " " + result.getString("Nombre");
                folderName = folderName.toUpperCase();
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(Documento.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return folderName;
    }
    
    private String getRootPath(){
        String path = null;
        
        try {
            File configFile = new File(System.getProperty("user.dir")+"/config.txt");
            File filesPath;
            Scanner sc = new Scanner(configFile);
            
            sc.hasNextLine();
            path = sc.nextLine();
            
            if(!(filesPath = new File(path)).exists()){
                filesPath.mkdir();
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Documento.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return path;
    }
}
