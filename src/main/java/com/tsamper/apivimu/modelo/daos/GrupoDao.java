package com.tsamper.apivimu.modelo.daos;

import com.tsamper.apivimu.modelo.Concierto;
import com.tsamper.apivimu.modelo.Grupo;
import com.tsamper.apivimu.modelo.bbdd.ConexionBBDD;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GrupoDao {
    public static void introducirGrupo(Grupo grupo){
        String querySelect = "SELECT id FROM grupos WHERE nombre = ?";
        String queryGrupos = "INSERT INTO grupos (nombre, descripcion, genero, ciudad, pais, imagen, perfil_spotify) "+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = ConexionBBDD.getConnection().prepareStatement(querySelect)){
            statement.setString(1, grupo.getNombre());
            ResultSet rs = statement.executeQuery();
            if(!rs.next()) {
                try (PreparedStatement statement1 = ConexionBBDD.getConnection().prepareStatement(queryGrupos)) {
                    statement1.setString(1, grupo.getNombre());
                    statement1.setString(2, grupo.getDescripcion());
                    statement1.setString(3, grupo.getGenero());
                    statement1.setString(4, grupo.getCiudad());
                    statement1.setString(5, grupo.getPais());
                    statement1.setString(6, grupo.getImagen());
                    statement1.setString(7, grupo.getPerfilSpotify());
                    statement1.executeUpdate();
                } catch (SQLException e) {
                    System.out.println("Error al ejecutar la consulta: " + e.getMessage());
                }
            }
        }catch (SQLException e) {
            System.out.println("Error al ejecutar la consulta: " + e.getMessage());
        }
    }
    
    public static List<Grupo> obtenerGrupos(){
        String querySelect = "SELECT * FROM grupos";
        try (PreparedStatement statement = ConexionBBDD.getConnection().prepareStatement(querySelect)){
            ResultSet rs =  statement.executeQuery();
            List<Grupo> grupos = new ArrayList<>();
            while(rs.next()) {
            Grupo grupo = new Grupo();
            grupo.setId(rs.getInt("id"));
            grupo.setNombre(rs.getString("nombre"));
            grupo.setDescripcion(rs.getString("descripcion"));
            grupo.setGenero(rs.getString("genero"));
            grupo.setCiudad(rs.getString("ciudad"));
            grupo.setPais(rs.getString("pais"));
            grupo.setImagen(rs.getString("imagen"));
            grupo.setPerfilSpotify(rs.getString("perfil_spotify"));
            grupos.add(grupo);
            }
            return grupos;
        }catch (SQLException e) {
            System.out.println("Error al ejecutar la consulta: " + e.getMessage());
        }
        return null;
    }

    public static Grupo obtenerGrupoPorId(int id){
        String querySelect = "SELECT * FROM grupos WHERE id = ?";
        try (PreparedStatement statement = ConexionBBDD.getConnection().prepareStatement(querySelect)){
            statement.setInt(1, id);
            ResultSet rs =  statement.executeQuery();
            rs.next();
            Grupo grupo = new Grupo();
            grupo.setId(rs.getInt("id"));
            grupo.setNombre(rs.getString("nombre"));
            grupo.setDescripcion(rs.getString("descripcion"));
            grupo.setGenero(rs.getString("genero"));
            grupo.setCiudad(rs.getString("ciudad"));
            grupo.setPais(rs.getString("pais"));
            grupo.setImagen(rs.getString("imagen"));
            grupo.setPerfilSpotify(rs.getString("perfil_spotify"));
            return grupo;
        }catch (SQLException e) {
            System.out.println("Error al ejecutar la consulta: " + e.getMessage());
        }
        return null;
    }

    public static Grupo obtenerGrupoPorNombre(String nombre){
        String querySelect = "SELECT * FROM grupos WHERE nombre = ?";
        try (PreparedStatement statement = ConexionBBDD.getConnection().prepareStatement(querySelect)){
            statement.setString(1, nombre);
            ResultSet rs =  statement.executeQuery();
            rs.next();
            Grupo grupo = new Grupo();
            grupo.setId(rs.getInt("id"));
            grupo.setNombre(rs.getString("nombre"));
            grupo.setDescripcion(rs.getString("descripcion"));
            grupo.setGenero(rs.getString("genero"));
            grupo.setCiudad(rs.getString("ciudad"));
            grupo.setPais(rs.getString("pais"));
            grupo.setImagen(rs.getString("imagen"));
            grupo.setPerfilSpotify(rs.getString("perfil_spotify"));
            return grupo;
        }catch (SQLException e) {
            System.out.println("Error al ejecutar la consulta: " + e.getMessage());
        }
        return null;
    }
}
