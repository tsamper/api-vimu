package com.tsamper.apivimu.modelo.bbdd;

import com.tsamper.apivimu.modelo.constantes.Constantes;

import java.sql.*;

public class ConexionBBDD {
    private static Connection connection;
    private static boolean yaCreado = false;


    public static void crearConexion(){
        String user = "root";
        String password = "";
        String url = Constantes.ENLACE +  Constantes.PUERTO;
        try{
            connection = DriverManager.getConnection(url, user, password);
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void conectar(){
        String user = "vimu";
        String password = "vimu1234";
        String url =  Constantes.ENLACE +  Constantes.PUERTO+ "/vimu";
        try{
            connection = DriverManager.getConnection(url, user, password);
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void desconectar(){
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean crearUsuario()  {
        String queryCrearUsuario = "CREATE USER 'vimu'@'localhost' IDENTIFIED BY 'vimu1234'";
        String queryDarPrivilegios = "GRANT INSERT, SELECT, UPDATE, DELETE ON vimu.* TO 'vimu'@'localhost'";
        try (PreparedStatement statement =  connection.prepareStatement(queryCrearUsuario)) {
            statement.execute();
            try (PreparedStatement statement1 =  connection.prepareStatement(queryDarPrivilegios)) {
                statement1.execute();
            }catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }catch (SQLException e) {
            System.out.println("Usuario ya creado");
            yaCreado = true;
            return yaCreado;
        }
        return yaCreado;
    }

    public static void crearTablas(){
        if (!yaCreado){
            String query1 = "DROP DATABASE IF EXISTS vimu";
            String query2 = "CREATE DATABASE IF NOT EXISTS vimu";
            String query3 = "USE vimu";
            String query4 = "CREATE TABLE grupo_usuarios (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "tipo VARCHAR(10) NOT NULL)";
            String query5 = "CREATE TABLE usuarios (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "nombre VARCHAR(50), " +
                    "apellidos VARCHAR(100)," +
                    "email VARCHAR(50)," +
                    "nomusuario VARCHAR(50) NOT NULL, " +
                    "contrasenya VARCHAR(50) NOT NULL, " +
                    "privilegios INT, " +
                    "CONSTRAINT fk1_gruposusuarios_usuarios FOREIGN KEY(privilegios) REFERENCES grupo_usuarios(id) ON UPDATE CASCADE ON DELETE NO ACTION)";
            String query6 = "CREATE TABLE grupos (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "nombre VARCHAR(200) NOT NULL, " +
                    "descripcion TEXT," +
                    "genero VARCHAR(50), " +
                    "ciudad VARCHAR(50), " +
                    "pais VARCHAR(50), " +
                    "imagen VARCHAR(50), " +
                    "perfil_spotify VARCHAR(200))";
            String query7 = "CREATE TABLE canciones (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "titulo VARCHAR(255) NOT NULL, " +
                    "grupo INT NOT NULL," +
                    "enlace_youtube VARCHAR(200)," +
                    "CONSTRAINT fk2_canciones_grupos FOREIGN KEY(grupo) REFERENCES grupos(id) ON UPDATE CASCADE ON DELETE NO ACTION)";
            String query8 = "CREATE TABLE opiniones (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "comentario TEXT," +
                    "usuario INT, " +
                    "fecha DATE," +
                    "recomendado VARCHAR(30), " +
                    "CONSTRAINT fk3_opiniones_usuarios FOREIGN KEY(usuario) REFERENCES usuarios(id) ON UPDATE CASCADE ON DELETE NO ACTION)";
            String query9 = "CREATE TABLE recintos (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "nombre VARCHAR(100)," +
                    "direccion VARCHAR(200), " +
                    "ciudad VARCHAR(100), " +
                    "telefono VARCHAR(10)," +
                    "correo VARCHAR(50), " +
                    "enlace_maps VARCHAR(300))";
            String query10 = "CREATE TABLE conciertos (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "nombre VARCHAR(50) NOT NULL, " +
                    "recinto INT," +
                    "fecha DATE," +
                    "hora TIME," +
                    "grupo INT NOT NULL, " +
                    "cantidad_entradas INT, " +
                    "cantidad_entradas_vendidas INT DEFAULT 0, " +
                    "precio_entradas DECIMAL(10,2), " +
                    "cantidad_entradas_vip INT, " +
                    "cantidad_entradas_vip_vendidas INT DEFAULT 0, " +
                    "precio_entradas_vip DECIMAL(10,2), " +
                    "imagen VARCHAR(50), " +
                    "promotor INT, " +
                    "CONSTRAINT fk3_conciertos_recintos FOREIGN KEY(recinto) REFERENCES recintos(id) ON UPDATE CASCADE ON DELETE NO ACTION, " +
                    "CONSTRAINT fk4_conciertos_usuarios FOREIGN KEY(promotor) REFERENCES usuarios(id) ON UPDATE CASCADE ON DELETE NO ACTION, " +
                    "CONSTRAINT fk5_conciertos_grupos FOREIGN KEY(grupo) REFERENCES grupos(id) ON UPDATE CASCADE ON DELETE NO ACTION)";
            String query11 = "CREATE TABLE festivales (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "nombre VARCHAR(50) NOT NULL, " +
                    "recinto INT," +
                    "fecha DATE," +
                    "hora TIME," +
                    "cantidad_entradas INT, " +
                    "cantidad_entradas_vendidas INT DEFAULT 0, " +
                    "precio_entradas DECIMAL(10,2), " +
                    "cantidad_entradas_vip INT, " +
                    "cantidad_entradas_vip_vendidas INT DEFAULT 0, " +
                    "precio_entradas_vip DECIMAL(10,2), " +
                    "imagen VARCHAR(50), " +
                    "CONSTRAINT fk5_festivales_recintos FOREIGN KEY(recinto) REFERENCES recintos(id) ON UPDATE CASCADE ON DELETE NO ACTION) ";
            String query12 = "CREATE TABLE opiniones_conciertos_grupos (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "grupo INT," +
                    "concierto INT," +
                    "opinion INT," +
                    "FOREIGN KEY (grupo) REFERENCES grupos(id)," +
                    "FOREIGN KEY (concierto) REFERENCES conciertos(id)," +
                    "FOREIGN KEY (opinion) REFERENCES opiniones(id)," +
                    "UNIQUE (grupo, concierto, opinion))";
            String query13 = "CREATE TABLE festivales_grupos (" +
                    "festival INT, " +
                    "grupo INT, "+
                    "PRIMARY KEY(festival, grupo), " +
                    "CONSTRAINT fk6_festivales_grupos FOREIGN KEY (festival) REFERENCES festivales(id) ON DELETE CASCADE, " +
                    "CONSTRAINT fk7_festivales_grupos FOREIGN KEY (grupo) REFERENCES grupos(id) ON DELETE CASCADE) ";
            String query14 = "CREATE TABLE entradas (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "precio DECIMAL, " +
                    "usuario INT," +
                    "fecha DATETIME," +
                    "concierto INT, " +
                    "festival INT, " +
                    "tipo VARCHAR(10), " +
                    "CONSTRAINT fk8_entradas_usuarios FOREIGN KEY(usuario) REFERENCES usuarios(id) ON UPDATE CASCADE ON DELETE NO ACTION, " +
                    "CONSTRAINT fk9_entradas_conciertos FOREIGN KEY(concierto) REFERENCES conciertos(id) ON UPDATE CASCADE ON DELETE NO ACTION, " +
                    "CONSTRAINT fk10_entradas_festivales FOREIGN KEY(festival) REFERENCES festivales(id) ON UPDATE CASCADE ON DELETE NO ACTION) ";
            String query15 = "CREATE TABLE guardados (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "usuario INT," +
                    "concierto INT, " +
                    "festival INT, " +
                    "CONSTRAINT fk11_guardados_usuarios FOREIGN KEY(usuario) REFERENCES usuarios(id) ON UPDATE CASCADE ON DELETE NO ACTION, " +
                    "CONSTRAINT fk12_guardados_conciertos FOREIGN KEY(concierto) REFERENCES conciertos(id) ON UPDATE CASCADE ON DELETE NO ACTION, " +
                    "CONSTRAINT fk13_guardados_festivales FOREIGN KEY(festival) REFERENCES festivales(id) ON UPDATE CASCADE ON DELETE NO ACTION) ";
            try{
                PreparedStatement statement =  connection.prepareStatement(query1);
                statement.execute();
                PreparedStatement statement1 =  connection.prepareStatement(query2);
                statement1.execute();
                PreparedStatement statement2 =  connection.prepareStatement(query3);
                statement2.execute();
                PreparedStatement statement3 =  connection.prepareStatement(query4);
                statement3.execute();
                PreparedStatement statement4 =  connection.prepareStatement(query5);
                statement4.execute();
                PreparedStatement statement5 =  connection.prepareStatement(query6);
                statement5.execute();
                PreparedStatement statement6 =  connection.prepareStatement(query7);
                statement6.execute();
                PreparedStatement statement7 =  connection.prepareStatement(query8);
                statement7.execute();
                PreparedStatement statement8 =  connection.prepareStatement(query9);
                statement8.execute();
                PreparedStatement statement9 =  connection.prepareStatement(query10);
                statement9.execute();
                PreparedStatement statement10 =  connection.prepareStatement(query11);
                statement10.execute();
                PreparedStatement statement11 =  connection.prepareStatement(query12);
                statement11.execute();
                PreparedStatement statement12 =  connection.prepareStatement(query13);
                statement12.execute();
                PreparedStatement statement13 =  connection.prepareStatement(query14);
                statement13.execute();
                PreparedStatement statement14 =  connection.prepareStatement(query15);
                statement14.execute();
            }catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}
