package com.tsamper.apivimu.modelo.daos;

import com.tsamper.apivimu.modelo.Concierto;
import com.tsamper.apivimu.modelo.Usuario;
import com.tsamper.apivimu.modelo.bbdd.ConexionBBDD;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ConciertoDao {

    public static void introducirConcierto(Concierto concierto, Usuario usuario){
        String querySelectConcierto = "SELECT * FROM conciertos WHERE nombre = ? and fecha = ?";
        boolean yaCreado = false;
        String queryConciertos = "INSERT INTO conciertos (nombre, fecha, hora, cantidad_entradas_vip, precio_entradas_vip, cantidad_entradas, precio_entradas, imagen, grupo, recinto, promotor) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String querySelectGrupo = "SELECT * FROM grupos WHERE nombre = ?";
        String querySelectRecinto = "SELECT * FROM recintos WHERE nombre = ?";
        try (PreparedStatement statement =  ConexionBBDD.getConnection().prepareStatement(querySelectConcierto)) {
            statement.setString(1, concierto.getNombre());
            statement.setString(2, concierto.getFecha().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            try (ResultSet resultSeto = statement.executeQuery()) {
                if (resultSeto.next()) {
                    yaCreado = true;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al ejecutar la consulta: " + e.getMessage());
        }
        if (!yaCreado){
            int idRecinto = -1;
                try (PreparedStatement statement1 =  ConexionBBDD.getConnection().prepareStatement(querySelectRecinto)){
                    statement1.setString(1, concierto.getRecinto().getNombre());
                    try (ResultSet resultSeto = statement1.executeQuery()) {
                        while (resultSeto.next()) {
                            idRecinto = resultSeto.getInt("id");
                        }
                    }
            } catch (SQLException e) {
                System.out.println("Error al ejecutar la consulta: " + e.getMessage());
            }
            try (PreparedStatement statement =  ConexionBBDD.getConnection().prepareStatement(queryConciertos)) {
                int idGrupo = -1;
                try (PreparedStatement statement1 =  ConexionBBDD.getConnection().prepareStatement(querySelectGrupo)){
                    statement1.setString(1, concierto.getGrupo().getNombre());
                    try (ResultSet resultSeto = statement1.executeQuery()) {
                        while (resultSeto.next()) {
                            idGrupo = resultSeto.getInt("id");
                        }
                    }
                }
                statement.setString(1, concierto.getNombre());
                statement.setString(2, concierto.getFecha().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                statement.setString(3, concierto.getHora().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                statement.setInt(4, concierto.getCantidadEntradasVip());
                statement.setDouble(5, concierto.getPrecioEntradasVip());
                statement.setInt(6, concierto.getCantidadEntradas());
                statement.setDouble(7, concierto.getPrecioEntradas());
                statement.setString(8, concierto.getImagen());
                statement.setInt(9, idGrupo);
                statement.setInt(10, idRecinto);
                statement.setInt(11, usuario.getId());
                statement.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error al ejecutar la consulta: " + e.getMessage());
            }
        }
    }

    public static ResultSet buscarTodosConciertos(){
        String queryConciertos = "SELECT * FROM conciertos";
        try (PreparedStatement statement = ConexionBBDD.getConnection().prepareStatement(queryConciertos)) {
            return statement.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error al ejecutar la consulta: " + e.getMessage());
        }
        return null;
    }

    public static ResultSet buscarConciertosPorFecha(){
        String queryConciertos = "SELECT * FROM conciertos WHERE fecha > ?";
        try (PreparedStatement statement = ConexionBBDD.getConnection().prepareStatement(queryConciertos)) {
            statement.setString(1, LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            return statement.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error al ejecutar la consulta: " + e.getMessage());
        }
        return null;
    }

    public static Concierto buscarConciertosPorNombre(String nombre){
        String queryConciertos = "SELECT * FROM conciertos WHERE nombre LIKE ?";
        try (PreparedStatement statement = ConexionBBDD.getConnection().prepareStatement(queryConciertos)) {
            statement.setString(1, nombre);
            ResultSet rs = statement.executeQuery();
            if(rs.next()) {
                Concierto concierto = new Concierto();
                concierto.setId(rs.getInt("id"));
                concierto.setNombre(rs.getString("nombre"));
                concierto.setImagen(rs.getString("imagen"));
                concierto.setRecinto(RecintoDao.obtenerRecintoPorId(rs.getInt("recinto")));
                concierto.setFecha(LocalDate.parse(rs.getString("fecha")));
                concierto.setHora(LocalTime.parse(rs.getString("hora")));
                concierto.setCantidadEntradas(rs.getInt("cantidad_entradas"));
                concierto.setCantidadEntradasVendidas(rs.getInt("cantidad_entradas_vendidas"));
                concierto.setCantidadEntradasVip(rs.getInt("cantidad_entradas_vip"));
                concierto.setCantidadEntradasVipVendidas(rs.getInt("cantidad_entradas_vip_vendidas"));
                concierto.setPrecioEntradas(rs.getDouble("precio_entradas"));
                concierto.setPrecioEntradasVip(rs.getInt("precio_entradas_vip"));
                concierto.setGrupo(GrupoDao.obtenerGrupoPorId(rs.getInt("grupo")));
                concierto.setPromotor(UsuarioDao.obtenerUsuarioPorId(rs.getInt("promotor")));
                return concierto;
            }
        } catch (SQLException e) {
            System.out.println("Error al ejecutar la consulta: " + e.getMessage());
        }
        return null;
    }


    public static List<Concierto> buscarConciertosPorGrupoYFecha(String grupo){
        String queryConciertos = "SELECT * FROM conciertos INNER JOIN grupos ON conciertos.grupo=grupos.id WHERE conciertos.fecha > ? AND grupos.nombre LIKE ?";
        List<Concierto> conciertos = new ArrayList<>();
        try (PreparedStatement statement = ConexionBBDD.getConnection().prepareStatement(queryConciertos)) {
            statement.setString(1, LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            statement.setString(2, "%"+grupo+"%");
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                Concierto concierto = new Concierto();
                concierto.setId(rs.getInt("id"));
                concierto.setNombre(rs.getString("nombre"));
                concierto.setImagen(rs.getString("imagen"));
                concierto.setRecinto(RecintoDao.obtenerRecintoPorId(rs.getInt("recinto")));
                concierto.setFecha(LocalDate.parse(rs.getString("fecha")));
                concierto.setHora(LocalTime.parse(rs.getString("hora")));
                concierto.setCantidadEntradas(rs.getInt("cantidad_entradas"));
                concierto.setCantidadEntradasVendidas(rs.getInt("cantidad_entradas_vendidas"));
                concierto.setCantidadEntradasVip(rs.getInt("cantidad_entradas_vip"));
                concierto.setCantidadEntradasVipVendidas(rs.getInt("cantidad_entradas_vip_vendidas"));
                concierto.setPrecioEntradas(rs.getDouble("precio_entradas"));
                concierto.setPrecioEntradasVip(rs.getInt("precio_entradas_vip"));
                concierto.setGrupo(GrupoDao.obtenerGrupoPorId(rs.getInt("grupo")));
                concierto.setPromotor(UsuarioDao.obtenerUsuarioPorId(rs.getInt("promotor")));
                conciertos.add(concierto);
            }
            return conciertos;
        } catch (SQLException e) {
            System.out.println("Error al ejecutar la consulta: " + e.getMessage());
        }
        return null;
    }

    public static List<Concierto> buscarConciertosPorCiudadYFecha(String ciudad){
        String queryConciertos = "SELECT * FROM conciertos INNER JOIN recintos ON conciertos.recinto=recintos.id WHERE conciertos.fecha > ? AND recintos.ciudad LIKE ?";
        List<Concierto> conciertos = new ArrayList<>();
        try (PreparedStatement statement = ConexionBBDD.getConnection().prepareStatement(queryConciertos)) {
            statement.setString(1, LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            statement.setString(2, "%"+ciudad+"%");
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                Concierto concierto = new Concierto();
                concierto.setId(rs.getInt("id"));
                concierto.setNombre(rs.getString("nombre"));
                concierto.setImagen(rs.getString("imagen"));
                concierto.setRecinto(RecintoDao.obtenerRecintoPorId(rs.getInt("recinto")));
                concierto.setFecha(LocalDate.parse(rs.getString("fecha")));
                concierto.setHora(LocalTime.parse(rs.getString("hora")));
                concierto.setCantidadEntradas(rs.getInt("cantidad_entradas"));
                concierto.setCantidadEntradasVendidas(rs.getInt("cantidad_entradas_vendidas"));
                concierto.setCantidadEntradasVip(rs.getInt("cantidad_entradas_vip"));
                concierto.setCantidadEntradasVipVendidas(rs.getInt("cantidad_entradas_vip_vendidas"));
                concierto.setPrecioEntradas(rs.getDouble("precio_entradas"));
                concierto.setPrecioEntradasVip(rs.getInt("precio_entradas_vip"));
                concierto.setGrupo(GrupoDao.obtenerGrupoPorId(rs.getInt("grupo")));
                concierto.setPromotor(UsuarioDao.obtenerUsuarioPorId(rs.getInt("promotor")));
                conciertos.add(concierto);
            }
            return conciertos;
        } catch (SQLException e) {
            System.out.println("Error al ejecutar la consulta: " + e.getMessage());
        }
        return null;
    }

    public static Concierto buscarConciertoPorId(int id){
        String queryConciertos = "SELECT * FROM conciertos WHERE id = ?";
        try (PreparedStatement statement = ConexionBBDD.getConnection().prepareStatement(queryConciertos)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if(rs.next()) {
                Concierto concierto = new Concierto();
                concierto.setId(rs.getInt("id"));
                concierto.setNombre(rs.getString("nombre"));
                concierto.setImagen(rs.getString("imagen"));
                concierto.setRecinto(RecintoDao.obtenerRecintoPorId(rs.getInt("recinto")));
                concierto.setFecha(LocalDate.parse(rs.getString("fecha")));
                concierto.setHora(LocalTime.parse(rs.getString("hora")));
                concierto.setCantidadEntradas(rs.getInt("cantidad_entradas"));
                concierto.setCantidadEntradasVendidas(rs.getInt("cantidad_entradas_vendidas"));
                concierto.setCantidadEntradasVip(rs.getInt("cantidad_entradas_vip"));
                concierto.setCantidadEntradasVipVendidas(rs.getInt("cantidad_entradas_vip_vendidas"));
                concierto.setPrecioEntradas(rs.getDouble("precio_entradas"));
                concierto.setPrecioEntradasVip(rs.getInt("precio_entradas_vip"));
                concierto.setGrupo(GrupoDao.obtenerGrupoPorId(rs.getInt("grupo")));
                concierto.setPromotor(UsuarioDao.obtenerUsuarioPorId(rs.getInt("promotor")));
                return concierto;
            }
        } catch (SQLException e) {
            System.out.println("Error al ejecutar la consulta: " + e.getMessage());
        }
        return null;
    }

    public static List<Concierto> buscarConciertoPorPromotor(int id){
        String queryConciertos = "SELECT * FROM conciertos WHERE promotor = ?";
        List<Concierto> conciertos = new ArrayList<>();
        try (PreparedStatement statement = ConexionBBDD.getConnection().prepareStatement(queryConciertos)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                Concierto concierto = new Concierto();
                concierto.setId(rs.getInt("id"));
                concierto.setNombre(rs.getString("nombre"));
                concierto.setImagen(rs.getString("imagen"));
                concierto.setRecinto(RecintoDao.obtenerRecintoPorId(rs.getInt("recinto")));
                concierto.setFecha(LocalDate.parse(rs.getString("fecha")));
                concierto.setHora(LocalTime.parse(rs.getString("hora")));
                concierto.setCantidadEntradas(rs.getInt("cantidad_entradas"));
                concierto.setCantidadEntradasVendidas(rs.getInt("cantidad_entradas_vendidas"));
                concierto.setCantidadEntradasVip(rs.getInt("cantidad_entradas_vip"));
                concierto.setCantidadEntradasVipVendidas(rs.getInt("cantidad_entradas_vip_vendidas"));
                concierto.setPrecioEntradas(rs.getDouble("precio_entradas"));
                concierto.setPrecioEntradasVip(rs.getInt("precio_entradas_vip"));
                concierto.setGrupo(GrupoDao.obtenerGrupoPorId(rs.getInt("grupo")));
                concierto.setPromotor(UsuarioDao.obtenerUsuarioPorId(rs.getInt("promotor")));
                conciertos.add(concierto);
            }
            return conciertos;
        } catch (SQLException e) {
            System.out.println("Error al ejecutar la consulta: " + e.getMessage());
        }
        return null;
    }

    public static List<Concierto> buscarConciertosPorUsuarioYFechaAnterior(Usuario usuario){
        String queryConciertos = "SELECT * FROM conciertos INNER JOIN entradas ON entradas.concierto=conciertos.id WHERE entradas.usuario = ? AND conciertos.fecha < CURRENT_DATE";
        List<Concierto> conciertos = new ArrayList<>();
        try (PreparedStatement statement = ConexionBBDD.getConnection().prepareStatement(queryConciertos)) {
            statement.setInt(1, usuario.getId());
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                Concierto concierto = new Concierto();
                concierto.setId(rs.getInt("id"));
                concierto.setNombre(rs.getString("nombre"));
                concierto.setImagen(rs.getString("imagen"));
                concierto.setRecinto(RecintoDao.obtenerRecintoPorId(rs.getInt("recinto")));
                concierto.setFecha(LocalDate.parse(rs.getString("fecha")));
                concierto.setHora(LocalTime.parse(rs.getString("hora")));
                concierto.setCantidadEntradas(rs.getInt("cantidad_entradas"));
                concierto.setCantidadEntradasVendidas(rs.getInt("cantidad_entradas_vendidas"));
                concierto.setCantidadEntradasVip(rs.getInt("cantidad_entradas_vip"));
                concierto.setCantidadEntradasVipVendidas(rs.getInt("cantidad_entradas_vip_vendidas"));
                concierto.setPrecioEntradas(rs.getDouble("precio_entradas"));
                concierto.setPrecioEntradasVip(rs.getInt("precio_entradas_vip"));
                concierto.setGrupo(GrupoDao.obtenerGrupoPorId(rs.getInt("grupo")));
                concierto.setPromotor(UsuarioDao.obtenerUsuarioPorId(rs.getInt("promotor")));
                conciertos.add(concierto);
            }
            return conciertos;
        } catch (SQLException e) {
            System.out.println("Error al ejecutar la consulta: " + e.getMessage());
        }
        return null;
    }

    public static void actualizarCantidadEntradasConciertos(Concierto concierto) {
        String queryConciertos = "UPDATE conciertos SET cantidad_entradas_vendidas = ? WHERE id = ?";
        try (PreparedStatement statement = ConexionBBDD.getConnection().prepareStatement(queryConciertos)) {
            statement.setInt(1, concierto.getCantidadEntradasVendidas());
            statement.setInt(2, concierto.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al ejecutar la consulta: " + e.getMessage());
        }
    }

    public static void actualizarCantidadEntradasVipConciertos(Concierto concierto){
        String queryConciertos = "UPDATE conciertos SET cantidad_entradas_vip_vendidas = ? WHERE id = ?";
        try (PreparedStatement statement = ConexionBBDD.getConnection().prepareStatement(queryConciertos)) {
            statement.setInt(1, concierto.getCantidadEntradasVipVendidas());
            statement.setInt(2, concierto.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al ejecutar la consulta: " + e.getMessage());
        }
    }

    public static void eliminarConciertoPorId(int id){
        String queryConciertos = "DELETE FROM conciertos WHERE id = ?";
        try (PreparedStatement statement = ConexionBBDD.getConnection().prepareStatement(queryConciertos)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al ejecutar la consulta: " + e.getMessage());
        }
    }


}
