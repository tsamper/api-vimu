package com.tsamper.apivimu.modelo.daos;

import com.tsamper.apivimu.modelo.Cancion;
import com.tsamper.apivimu.modelo.Concierto;
import com.tsamper.apivimu.modelo.Entrada;
import com.tsamper.apivimu.modelo.bbdd.ConexionBBDD;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class CancionDao {
    public static void introducirCancion(Cancion cancion){
        String querySelect = "SELECT id FROM canciones WHERE titulo = ?";
        String queryGrupos = "INSERT INTO canciones (titulo, grupo, enlace_youtube) "+ "VALUES (?, ?, ?)";
        try (PreparedStatement statement = ConexionBBDD.getConnection().prepareStatement(querySelect)){
            statement.setString(1, cancion.getTitulo());
            ResultSet rs = statement.executeQuery();
            if(!rs.next()) {
                try (PreparedStatement statement1 = ConexionBBDD.getConnection().prepareStatement(queryGrupos)) {
                    statement1.setString(1, cancion.getTitulo());
                    statement1.setInt(2, cancion.getGrupo().getId());
                    statement1.setString(3, cancion.getEnlaceYoutube());
                    statement1.executeUpdate();
                } catch (SQLException e) {
                    System.out.println("Error al ejecutar la consulta: " + e.getMessage());
                }
            }
        }catch (SQLException e) {
            System.out.println("Error al ejecutar la consulta: " + e.getMessage());
        }
    }

    public static List<Cancion> buscarCancionPorGrupo(int id){
        String queryConciertos = "SELECT * FROM canciones WHERE grupo = ?";
        List<Cancion> canciones = new ArrayList<>();
        try (PreparedStatement statement = ConexionBBDD.getConnection().prepareStatement(queryConciertos)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {

                Cancion cancion = new Cancion();
                cancion.setId(rs.getInt("id"));
                cancion.setTitulo(rs.getString("titulo"));
                cancion.setGrupo(GrupoDao.obtenerGrupoPorId(id));
                cancion.setEnlaceYoutube(rs.getString("enlace_youtube"));
                canciones.add(cancion);
            }
        } catch (SQLException e) {
            System.out.println("Error al ejecutar la consulta: " + e.getMessage());
        }
        return canciones;
    }

}
