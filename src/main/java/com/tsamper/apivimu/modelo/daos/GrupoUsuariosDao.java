package com.tsamper.apivimu.modelo.daos;

import com.tsamper.apivimu.modelo.GrupoUsuarios;
import com.tsamper.apivimu.modelo.Usuario;
import com.tsamper.apivimu.modelo.bbdd.ConexionBBDD;
import com.tsamper.apivimu.modelo.enums.Privilegios;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GrupoUsuariosDao {
    public static void introducirGrupoUsuarios(Usuario usuario){
        String querySelect = "SELECT id FROM grupo_usuarios WHERE tipo = ?";
        String queryGrupos = "INSERT INTO grupo_usuarios (tipo) "+ "VALUES (?)";
        try (PreparedStatement statement = ConexionBBDD.getConnection().prepareStatement(querySelect)){
            statement.setString(1, usuario.getGrupoUsuarios().getTipo().name());
            ResultSet rs = statement.executeQuery();
            if(!rs.next()) {
                try (PreparedStatement statement1 = ConexionBBDD.getConnection().prepareStatement(queryGrupos)) {
                    statement1.setString(1, usuario.getGrupoUsuarios().getTipo().name());
                    statement1.executeUpdate();
                } catch (SQLException e) {
                    System.out.println("Error al ejecutar la consulta: " + e.getMessage());
                }
            }
        }catch (SQLException e) {
            System.out.println("Error al ejecutar la consulta: " + e.getMessage());
        }
    }

    public static GrupoUsuarios obtenerGrupoPorId(int id){
        GrupoUsuarios grupo = null;
        String querySelect = "SELECT * FROM grupo_usuarios WHERE id = ?";
        try (PreparedStatement statement = ConexionBBDD.getConnection().prepareStatement(querySelect)){
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if(rs.next()) {
               GrupoUsuarios grupoUsuarios = new GrupoUsuarios();
               grupoUsuarios.setTipo(Privilegios.valueOf(rs.getString("tipo")));
               return grupoUsuarios;
            }
        }catch (SQLException e){
            System.out.println("Error al ejecutar la consulta: " + e.getMessage());
        }
        return null;
    }
}
