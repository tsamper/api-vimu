package com.tsamper.apivimu.controlador;


import org.json.JSONArray;
import org.json.JSONObject;
import com.tsamper.apivimu.modelo.*;
import com.tsamper.apivimu.modelo.bbdd.ConexionBBDD;
import com.tsamper.apivimu.modelo.constantes.Constantes;
import com.tsamper.apivimu.modelo.daos.*;
import com.tsamper.apivimu.modelo.enums.OpcionesOpinion;
import com.tsamper.apivimu.modelo.enums.Privilegios;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestController
public class VimuController {

    public void conectarBBDD(){
        ConexionBBDD.crearConexion();
        boolean yaCreado = ConexionBBDD.crearUsuario();
        ConexionBBDD.crearTablas();
        ConexionBBDD.conectar();
        if (!yaCreado){
            importar();
        }
        obtenerConciertos();
    }

    
    public List<Concierto> guardarConciertos(File archivoConciertos){
        List<Concierto> conciertos = new ArrayList<>();
        try (FileReader reader = new FileReader(archivoConciertos)) {
            char[] buffer = new char[(int) archivoConciertos.length()];
            reader.read(buffer);
            String jsonString = new String(buffer);
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("conciertos");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonConcierto = jsonArray.getJSONObject(i);
                Concierto concierto = new Concierto();
                concierto.setNombre(jsonConcierto.getString("nombre"));
                concierto.setImagen(jsonConcierto.getString("imagen"));
                JSONObject jsonRecinto = jsonConcierto.getJSONObject("recinto");
                Recinto recinto = new Recinto();
                recinto.setNombre(jsonRecinto.getString("nombre"));
                recinto.setDireccion(jsonRecinto.getString("direccion"));
                recinto.setCiudad(jsonRecinto.getString("ciudad"));
                recinto.setTelefono(jsonRecinto.getString("telefono"));
                recinto.setEmail(jsonRecinto.getString("correo"));
                recinto.setEnlaceMaps(jsonRecinto.getString("enlace_maps"));
                concierto.setRecinto(recinto);
                concierto.setFecha(LocalDate.parse(jsonConcierto.getString("fecha")));
                concierto.setHora(LocalTime.parse(jsonConcierto.getString("hora")));
                concierto.setCantidadEntradas(jsonConcierto.getInt("cantidad_entradas"));
                concierto.setPrecioEntradas(jsonConcierto.getInt("precio_entradas"));
                concierto.setCantidadEntradasVip(jsonConcierto.getInt("cantidad_entradas_vip"));
                concierto.setPrecioEntradasVip(jsonConcierto.getInt("precio_entradas_vip"));
                JSONObject jsonGrupo = jsonConcierto.getJSONObject("grupo");
                Grupo grupo = new Grupo();
                grupo.setNombre(jsonGrupo.getString("nombre"));
                grupo.setDescripcion(jsonGrupo.getString("descripcion"));
                grupo.setGenero(jsonGrupo.getString("genero"));
                grupo.setCiudad(jsonGrupo.getString("ciudad"));
                grupo.setPais(jsonGrupo.getString("pais"));
                grupo.setImagen(jsonGrupo.getString("imagen"));
                grupo.setPerfilSpotify(jsonGrupo.getString("perfil_spotify"));
                concierto.setGrupo(grupo);
                concierto.setPromotor(UsuarioDao.obtenerUsuarioPorNomUsuario(jsonConcierto.getString("promotor")));
                conciertos.add(concierto);
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        return conciertos;
    }

    public List<Usuario> guardarUsuarios(File archivoUsuarios){
        List<Usuario> usuarios = new ArrayList<>();
        try (FileReader reader = new FileReader(archivoUsuarios)) {
            char[] buffer = new char[(int) archivoUsuarios.length()];
            reader.read(buffer);
            String jsonString = new String(buffer);

            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("usuarios");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonConcierto = jsonArray.getJSONObject(i);
                Usuario usuario = new Usuario();
                usuario.setNombre(jsonConcierto.getString("nombre"));
                usuario.setApellidos(jsonConcierto.getString("apellidos"));
                usuario.setEmail(jsonConcierto.getString("email"));
                usuario.setNomUsuario(jsonConcierto.getString("nomusuario"));
                usuario.setContrasenya(jsonConcierto.getString("contrasenya"));
                JSONObject jsonUsuarios = jsonConcierto.getJSONObject("privilegios");
                GrupoUsuarios grupoUsuarios = new GrupoUsuarios();
                grupoUsuarios.setTipo(Privilegios.valueOf(jsonUsuarios.getString("tipo").toUpperCase()));
                usuario.setGrupoUsuarios(grupoUsuarios);
                usuarios.add(usuario);
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        return usuarios;
    }

    public List<Cancion> guardarCanciones(File archivoCanciones){
        List<Cancion> canciones = new ArrayList<>();
        try (FileReader reader = new FileReader(archivoCanciones)) {
            char[] buffer = new char[(int) archivoCanciones.length()];
            reader.read(buffer);
            String jsonString = new String(buffer);

            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("canciones");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonConcierto = jsonArray.getJSONObject(i);
                Cancion cancion = new Cancion();
                cancion.setTitulo(jsonConcierto.getString("titulo"));
                cancion.setGrupo(GrupoDao.obtenerGrupoPorNombre(jsonConcierto.getString("grupo")));
                cancion.setEnlaceYoutube(jsonConcierto.getString("enlace_youtube"));
                canciones.add(cancion);
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        return canciones;
    }

    public List<EntradaConcierto> guardarEntradas(File archivoEntradas){
        List<EntradaConcierto> entradas = new ArrayList<>();
        try (FileReader reader = new FileReader(archivoEntradas)) {
            char[] buffer = new char[(int) archivoEntradas.length()];
            reader.read(buffer);
            String jsonString = new String(buffer);

            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("entradas");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonEntrada = jsonArray.getJSONObject(i);
                EntradaConcierto entrada = new EntradaConcierto();
                entrada.setPrecio(jsonEntrada.getDouble("precio"));
                entrada.setUsuario(UsuarioDao.obtenerUsuarioPorNomUsuario(jsonEntrada.getString("usuario")));
                entrada.setTipo(jsonEntrada.getString("tipo"));
                entrada.setFechaCompra(LocalDateTime.parse(jsonEntrada.getString("fecha_compra")));
                entrada.setConcierto(ConciertoDao.buscarConciertosPorNombre(jsonEntrada.getString("concierto")));
                entradas.add(entrada);
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        return entradas;
    }

    public List<Opinion> guardarOpiniones(File archivoOpiniones){
        List<Opinion> opiniones = new ArrayList<>();
        try (FileReader reader = new FileReader(archivoOpiniones)) {
            char[] buffer = new char[(int) archivoOpiniones.length()];
            reader.read(buffer);
            String jsonString = new String(buffer);

            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("opiniones");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonOpinion = jsonArray.getJSONObject(i);
                Opinion opinion = new Opinion();
                opinion.setUsuario(UsuarioDao.obtenerUsuarioPorNomUsuario(jsonOpinion.getString("usuario")));
                opinion.setGrupo(GrupoDao.obtenerGrupoPorNombre(jsonOpinion.getString("grupo")));
                opinion.setConcierto(ConciertoDao.buscarConciertosPorNombre(jsonOpinion.getString("concierto")));
                opinion.setComentario(jsonOpinion.getString("comentario"));
                opinion.setFecha(LocalDate.parse(jsonOpinion.getString("fecha")));
                opinion.setRecomendado(OpcionesOpinion.valueOf(jsonOpinion.getString("recomendado")));
                opiniones.add(opinion);
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        return opiniones;
    }

    @GetMapping("/crearRegistros")
    public void importar(){
        try {
            File archivoUsuarios = new File(Constantes.DIR_JSON_USUARIOS);
            List<Usuario> usuarios = guardarUsuarios(archivoUsuarios);
            for (Usuario usuario : usuarios) {
                GrupoUsuariosDao.introducirGrupoUsuarios(usuario);
                UsuarioDao.introducirUsuario(usuario);
            }
            File archivoConciertos = new File(Constantes.DIR_JSON_CONCIERTOS);
            List<Concierto> conciertos = guardarConciertos(archivoConciertos);
            for (Concierto concierto : conciertos) {
                RecintoDao.introducirRecinto(concierto.getRecinto());
                GrupoDao.introducirGrupo(concierto.getGrupo());
                ConciertoDao.introducirConcierto(concierto, concierto.getPromotor());
            }
            File archivoCanciones = new File(Constantes.DIR_JSON_CANCIONES);
            List<Cancion> canciones = guardarCanciones(archivoCanciones);
            for (Cancion cancion : canciones) {
                CancionDao.introducirCancion(cancion);
            }
            File archivoEntradas = new File(Constantes.DIR_JSON_ENTRADAS);
            List<EntradaConcierto> entradas = guardarEntradas(archivoEntradas);
            for (EntradaConcierto entrada : entradas) {
                EntradaDao.introducirEntradaConcierto(entrada);
            }
            File archivoOpiniones = new File(Constantes.DIR_JSON_OPINIONES);
            List<Opinion> opiniones = guardarOpiniones(archivoOpiniones);
            for (Opinion opinion : opiniones) {
                OpinionDao.introducirOpinion(opinion);
            }
        }catch(NullPointerException e){
            System.out.println("Salida sin archivo");
        }
    }
    
    @GetMapping("/demo")
    public Ejemplo prueba() {
    	Ejemplo ejemplo = new Ejemplo("Hola desde el servidor");
    	return ejemplo;
    }
    
    @GetMapping("/usuarios")
    public Usuario iniciarSesion(String user, String contrasenya, Concierto concierto){
        Usuario usuario = UsuarioDao.comprobarUsuario(user, contrasenya);
        return usuario;
    }

    @GetMapping("/conciertos")
    public ArrayList<Concierto> obtenerConciertos(){
       ArrayList<Concierto> conciertos = new ArrayList<>();
        try{
            ResultSet rs = ConciertoDao.buscarConciertosPorFecha();
            if (rs != null) {
                while (rs.next()) {
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
                    conciertos.add(concierto);
                }
                return conciertos;

            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
   }
    

    @PostMapping("/usuarios")
    public boolean registrarUsuario(@RequestBody Usuario usuario) {
        int resultado = UsuarioDao.introducirUsuario(usuario);
        System.out.println(resultado);
        if (resultado == 0) {
            return false;
        }else{
        	return true;
        }

    }
    
    @PostMapping("/conciertos")
    public void registrarConcierto(@RequestBody Concierto concierto, @RequestParam int user){
    	Usuario usuario = UsuarioDao.obtenerUsuarioPorId(user);
        ConciertoDao.introducirConcierto(concierto, usuario);
    }

    @PostMapping("/recintos")
    public boolean registrarRecinto(@RequestBody Recinto recinto){
        Pattern patronCorreo = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Pattern patronTelefono = Pattern.compile("^[0-9]{9}$");
        Matcher matcher = patronCorreo.matcher(recinto.getEmail());
        if (!matcher.matches()) {
            return false;
        }
        matcher = patronTelefono.matcher(recinto.getTelefono());
        if (!matcher.matches()) {
            return false;
        }
        RecintoDao.introducirRecinto(recinto);
        return true;
    }

	@PostMapping("/grupos")
    public void registrarGrupo(@RequestBody Grupo grupo){
        GrupoDao.introducirGrupo(grupo);
    }

    
	@PostMapping("/entradas/{tipo}/{campo}")
	public void comprarEntradas(
	    @RequestBody Concierto concierto, 
	    @PathVariable String tipo, 
	    @PathVariable String campo,
	    @RequestParam int user,
	    @RequestParam int cantidadSeleccionada) {
		
		Usuario usuario = UsuarioDao.obtenerUsuarioPorId(user);
	    if ("normal".equalsIgnoreCase(tipo)) {
	        for (int i = 0; i < cantidadSeleccionada; i++) {
	            EntradaConcierto entrada = new EntradaConcierto();
	            entrada.setConcierto(concierto);
	            entrada.setPrecio(concierto.getPrecioEntradas());
	            entrada.setUsuario(usuario);
	            entrada.setTipo("Normal");
	            entrada.setFechaCompra(LocalDateTime.now());
	            EntradaDao.introducirEntradaConcierto(entrada);
	        }
	        actualizarEntradasNormales(concierto, cantidadSeleccionada);
	    } else if ("vip".equalsIgnoreCase(tipo)) {
	        for (int i = 0; i < cantidadSeleccionada; i++) {
	            EntradaConcierto entrada = new EntradaConcierto();
	            entrada.setConcierto(concierto);
	            entrada.setPrecio(concierto.getPrecioEntradasVip());
	            entrada.setUsuario(usuario);
	            entrada.setTipo("Vip");
	            entrada.setFechaCompra(LocalDateTime.now());
	            EntradaDao.introducirEntradaConcierto(entrada);
	        }
	        actualizarEntradasVip(concierto, cantidadSeleccionada);
	    } else {
	        throw new IllegalArgumentException("Tipo de entrada inválido");
	    }
	}

    public void actualizarEntradasNormales(Concierto concierto, int cantidadSeleccionada){
        concierto.setCantidadEntradasVendidas(concierto.getCantidadEntradasVendidas() + cantidadSeleccionada);
        ConciertoDao.actualizarCantidadEntradasConciertos(concierto);
    }

    public void actualizarEntradasVip(Concierto concierto, int cantidadSeleccionada){
        concierto.setCantidadEntradasVipVendidas(concierto.getCantidadEntradasVipVendidas() + cantidadSeleccionada);
        ConciertoDao.actualizarCantidadEntradasVipConciertos(concierto);
    }

    @PostMapping("/conciertosGuardados")
    public int guardarConciertoGuardado(@RequestBody Concierto concierto, @RequestParam int user){
    	Usuario usuario = UsuarioDao.obtenerUsuarioPorId(user);
        return GuardadoDao.introducirConciertoGuardado(concierto, usuario);
    }
/*
 //CAMBIAR PARA PODER SUBIR IMAGEN DESDE EL MOVIL
    public String guardarImagenGrupo(File selectedFile, Label imagenSeleccionada){
        if (selectedFile != null) {
            try {
                File destDir = new File("src/main/resources/img/grupos");
                if (!destDir.exists()) {
                    destDir.mkdirs();
                }
                File destFile = new File(destDir, selectedFile.getName());
                Files.copy(selectedFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                imagenSeleccionada.setText("Imagen seleccionada: " + destFile.getName());
                return "/img/grupos/" + destFile.getName();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return null;
    }

    public void exportarEntradas(List<EntradaConcierto> entradas){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exportar entradas");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Archivos JSON (*.json)", "*.json");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir") + "\\src\\main\\resources\\entradas"));
        File file = fileChooser.showSaveDialog(null);
        if(file != null){
            try (FileWriter fileWriter = new FileWriter(file)) {
                JSONArray jsonArray = new JSONArray();
                for (EntradaConcierto ent : entradas) {
                    JSONObject jsonEntrada = new JSONObject();
                    jsonEntrada.put("id", ent.getId());
                    jsonEntrada.put("precio", ent.getPrecio());
                    jsonEntrada.put("usuario", ent.getUsuario().getNombre() + " " + ent.getUsuario().getApellidos());
                    jsonEntrada.put("tipo", ent.getTipo());
                    jsonEntrada.put("fecha_compra", ent.getFechaCompra());
                    jsonEntrada.put("concierto", ent.getConcierto().getNombre());
                    jsonArray.put(jsonEntrada);
                }
                fileWriter.write(jsonArray.toString(4));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }*/
    
    @DeleteMapping("/conciertos/{id}")
    public void eliminarConcierto(@PathVariable int id){
        ConciertoDao.eliminarConciertoPorId(id);
    }

    @GetMapping("/entradas")
    public Map<String, List<EntradaConcierto>> obtenerEntradasConcierto(@RequestParam int user){
    	Usuario usuario = UsuarioDao.obtenerUsuarioPorId(user);
        List<EntradaConcierto> entradas = EntradaDao.buscarEntradasPorUsuario(usuario);
        Map<String, List<EntradaConcierto>> entradasPorConcierto = new HashMap<>();
        for (EntradaConcierto entrada : entradas) {
            entradasPorConcierto
                    .computeIfAbsent(entrada.getConcierto().getNombre(), k -> new ArrayList<>())
                    .add(entrada);
        }
        return entradasPorConcierto;
    }

    @GetMapping("/conciertos/promotor/")
    public List<Concierto> obtenerConciertosPorPromotor(@RequestParam int promotor){
        return ConciertoDao.buscarConciertoPorPromotor(promotor);
    }

    @GetMapping("/canciones")
    public List<Cancion> obtenerCancionesPorGrupo(@RequestParam int grupoId){
        return CancionDao.buscarCancionPorGrupo(grupoId);
    }

    @GetMapping("/conciertos/guardados/")
    public List<Concierto> obtenerConciertosGuardadosPorUsuario(@RequestParam int user){
    	Usuario usuario = UsuarioDao.obtenerUsuarioPorId(user);
        return GuardadoDao.buscarGuardadosPorUsuario(usuario);
    }

    @GetMapping("/conciertos/filtro?{filtro}={campo}")
    public ArrayList<Concierto> buscarConciertos(@PathVariable String filtro, @PathVariable String campo){
    	ArrayList<Concierto> conciertos = new ArrayList<>();
        if (filtro.equals("Artista")){
            conciertos.addAll(ConciertoDao.buscarConciertosPorGrupoYFecha(campo));
        }else if (filtro.equals("Ciudad")){
            conciertos.addAll(ConciertoDao.buscarConciertosPorCiudadYFecha(campo));
        }
        return conciertos;
    }

    @GetMapping("/conciertos/old/")
    public Map<String, List<Concierto>> obtenerConciertosAnteriores(@RequestParam int user){
    	Usuario usuario = UsuarioDao.obtenerUsuarioPorId(user);
        List<Concierto> conciertos = ConciertoDao.buscarConciertosPorUsuarioYFechaAnterior(usuario);
        Map<String, List<Concierto>> entradasPorConcierto = new HashMap<>();
        for (Concierto concierto : conciertos) {
            entradasPorConcierto
                    .computeIfAbsent(concierto.getNombre(), k -> new ArrayList<>())
                    .add(concierto);
        }
        return entradasPorConcierto;
    }

    @PostMapping("/canciones")
    public void registrarCancion(@RequestBody Cancion cancion){
        CancionDao.introducirCancion(cancion);
    }

    @DeleteMapping("/guardados/{idConcierto}")
    public void eliminarGuardado(@RequestParam int user, @PathVariable int idConcierto){
    	Usuario usuario = UsuarioDao.obtenerUsuarioPorId(user);
    	Concierto concierto = ConciertoDao.buscarConciertoPorId(idConcierto);
        GuardadoDao.eliminarGuardado(concierto, usuario);
    }

    @PostMapping("/opiniones/{idConcierto}/{idUsuario}")
    public void registrarComentario(@PathVariable int idConcierto,
    		@PathVariable int idUsuario, 
    		@RequestParam OpcionesOpinion recomendado,
    		@RequestBody String comentario){
    	Concierto concierto = ConciertoDao.buscarConciertoPorId(idConcierto);
        Opinion opinion = new Opinion();
        opinion.setComentario(comentario);
        opinion.setUsuario(UsuarioDao.obtenerUsuarioPorId(idUsuario));
        opinion.setGrupo(concierto.getGrupo());
        opinion.setConcierto(concierto);
        opinion.setRecomendado(recomendado);
        OpinionDao.introducirOpinion(opinion);
    }

    @GetMapping("/opiniones")
    public List<Opinion> obtenerOpinionesPorGrupo(@RequestParam int grupoId){
        return OpinionDao.buscarOpinionesPorGrupo(GrupoDao.obtenerGrupoPorId(grupoId));
    }

    @GetMapping("/opiniones/{idConcierto}")
    public boolean comprobarComentarioPorUsuarioYConcierto(@RequestParam int user, @PathVariable int idConcierto){
    	Concierto concierto = ConciertoDao.buscarConciertoPorId(idConcierto);
    	Usuario usuario = UsuarioDao.obtenerUsuarioPorId(user);
        return OpinionDao.buscarOpinionesPorUsuarioYConcierto(usuario, concierto);
    }


}