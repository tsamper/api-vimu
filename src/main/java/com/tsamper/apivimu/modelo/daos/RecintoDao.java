package com.tsamper.apivimu.modelo.daos;

import com.tsamper.apivimu.modelo.Concierto;
import com.tsamper.apivimu.modelo.Recinto;
import com.tsamper.apivimu.modelo.bbdd.ConexionBBDD;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RecintoDao {
    public static void introducirRecinto(Recinto recinto){
        String querySelect = "SELECT id FROM recintos WHERE nombre = ?";
        String queryRecintos = "INSERT INTO recintos (nombre, direccion, ciudad, telefono, correo, enlace_maps) "+ "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = ConexionBBDD.getConnection().prepareStatement(querySelect)){
            statement.setString(1, recinto.getNombre());
            ResultSet rs = statement.executeQuery();
            if(!rs.next()) {
                try (PreparedStatement statement1 = ConexionBBDD.getConnection().prepareStatement(queryRecintos)) {
                    statement1.setString(1, recinto.getNombre());
                    statement1.setString(2, recinto.getDireccion());
                    statement1.setString(3, recinto.getCiudad());
                    statement1.setString(4, recinto.getTelefono());
                    statement1.setString(5, recinto.getEmail());
                    statement1.setString(6, recinto.getEnlaceMaps());
                    statement1.executeUpdate();
                } catch (SQLException e) {
                    System.out.println("Error al ejecutar la consulta: " + e.getMessage());
                }
            }
        }catch (SQLException e) {
            System.out.println("Error al ejecutar la consulta: " + e.getMessage());
        }
    }

    public static Recinto obtenerRecintoPorId(int id){
        String querySelect = "SELECT * FROM recintos WHERE id = ?";
        try (PreparedStatement statement = ConexionBBDD.getConnection().prepareStatement(querySelect)){
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()){
                Recinto recinto = new Recinto();
                recinto.setId(rs.getInt("id"));
                recinto.setNombre(rs.getString("nombre"));
                recinto.setDireccion(rs.getString("direccion"));
                recinto.setCiudad(rs.getString("ciudad"));
                recinto.setTelefono(rs.getString("telefono"));
                recinto.setEmail(rs.getString("correo"));
                recinto.setEnlaceMaps(rs.getString("enlace_maps"));
                return recinto;
            }
        }catch (SQLException e) {
            System.out.println("Error al ejecutar la consulta: " + e.getMessage());
        }
        return null;
    }

    public static Recinto obtenerRecintoPorNombre(String nombre){
        String querySelect = "SELECT * FROM recintos WHERE nombre = ?";
        try (PreparedStatement statement = ConexionBBDD.getConnection().prepareStatement(querySelect)){
            statement.setString(1, nombre);
            ResultSet rs = statement.executeQuery();
            if (rs.next()){
                Recinto recinto = new Recinto();
                recinto.setId(rs.getInt("id"));
                recinto.setNombre(rs.getString("nombre"));
                recinto.setDireccion(rs.getString("direccion"));
                recinto.setCiudad(rs.getString("ciudad"));
                recinto.setTelefono(rs.getString("telefono"));
                recinto.setEmail(rs.getString("correo"));
                recinto.setEnlaceMaps(rs.getString("enlace_maps"));
                return recinto;
            }
        }catch (SQLException e) {
            System.out.println("Error al ejecutar la consulta: " + e.getMessage());
        }
        return null;
    }
}
