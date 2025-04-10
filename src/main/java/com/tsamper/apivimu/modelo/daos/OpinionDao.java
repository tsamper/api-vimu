package com.tsamper.apivimu.modelo.daos;

import com.tsamper.apivimu.modelo.*;
import com.tsamper.apivimu.modelo.bbdd.ConexionBBDD;
import com.tsamper.apivimu.modelo.enums.OpcionesOpinion;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class OpinionDao {
    public static void introducirOpinion(Opinion opinion){
        String queryOpinion = "INSERT INTO opiniones (usuario, comentario, fecha, recomendado) "
                + "VALUES (?, ?, ?, ?)";
        String querySelect = "SELECT * FROM opiniones WHERE usuario = ? AND comentario = ?";
        String queryOpinion2 = "INSERT INTO opiniones_conciertos_grupos (grupo, concierto, opinion) "
                + "VALUES (?, ?, ?)";
        try (PreparedStatement statement =  ConexionBBDD.getConnection().prepareStatement(queryOpinion)) {
            statement.setInt(1, opinion.getUsuario().getId());
            statement.setString(2, opinion.getComentario());
            statement.setString(3, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString());
            statement.setString(4, opinion.getRecomendado().toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al ejecutar la consulta: " + e.getMessage());
        }
        try (PreparedStatement statement =  ConexionBBDD.getConnection().prepareStatement(querySelect)) {
            statement.setInt(1, opinion.getUsuario().getId());
            statement.setString(2, opinion.getComentario());
            ResultSet rs = statement.executeQuery();
            rs.next();
            try (PreparedStatement statement1 =  ConexionBBDD.getConnection().prepareStatement(queryOpinion2)) {
                statement1.setInt(1, opinion.getGrupo().getId());
                statement1.setInt(2, opinion.getConcierto().getId());
                statement1.setInt(3, rs.getInt("id"));
                statement1.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Error al ejecutar la consulta: " + e.getMessage());
        }
    }

    public static List<Opinion> buscarOpinionesPorGrupo(Grupo grupo){
        String query = "SELECT * FROM opiniones INNER JOIN opiniones_conciertos_grupos ON opiniones.id = opiniones_conciertos_grupos.opinion WHERE opiniones_conciertos_grupos.grupo = ?";
        List<Opinion> opiniones = new ArrayList<>();
        try (PreparedStatement statement = ConexionBBDD.getConnection().prepareStatement(query)) {
            statement.setInt(1, grupo.getId());
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Opinion opinion = new Opinion();
                opinion.setId(rs.getInt("id"));
                opinion.setComentario(rs.getString("comentario"));
                opinion.setUsuario(UsuarioDao.obtenerUsuarioPorId(rs.getInt("usuario")));
                opinion.setFecha(LocalDate.parse(rs.getString("fecha"), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                opinion.setGrupo(grupo);
                opinion.setConcierto(ConciertoDao.buscarConciertoPorId(rs.getInt("concierto")));
                opinion.setRecomendado(OpcionesOpinion.valueOf(rs.getString("recomendado")));
                opiniones.add(opinion);
            }
            return opiniones;
        } catch (SQLException e) {
            System.out.println("Error al ejecutar la consulta: " + e.getMessage());
        }
        return null;
    }

    public static boolean buscarOpinionesPorUsuarioYConcierto(Usuario usuario, Concierto concierto){
        String query = "SELECT * FROM opiniones INNER JOIN opiniones_conciertos_grupos ON opiniones.id = opiniones_conciertos_grupos.opinion" +
                " WHERE opiniones_conciertos_grupos.concierto = ? AND opiniones.usuario = ?";
        List<Opinion> opiniones = new ArrayList<>();
        try (PreparedStatement statement = ConexionBBDD.getConnection().prepareStatement(query)) {
            statement.setInt(1, concierto.getId());
            statement.setInt(2, usuario.getId());
            ResultSet rs = statement.executeQuery();
            if(rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error al ejecutar la consulta: " + e.getMessage());
        }
        return false;
    }
}
