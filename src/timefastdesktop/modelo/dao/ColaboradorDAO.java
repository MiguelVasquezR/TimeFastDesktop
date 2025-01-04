package timefastdesktop.modelo.dao;

import com.google.gson.Gson;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import timefastdesktop.modelo.ConexionWS;
import timefastdesktop.pojo.Colaborador;
import timefastdesktop.pojo.Mensaje;
import timefastdesktop.pojo.RespuestaHTTP;
import timefastdesktop.utilidades.Constantes;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

public class ColaboradorDAO {
    
     public static Mensaje agregarColaborador(Colaborador colaborador) {
        Mensaje msj = new Mensaje();
        String url = Constantes.URL_WS + "/colaborador/agregar";
        Gson gson = new Gson();
        try {
            String parametros = gson.toJson(colaborador);
            RespuestaHTTP respuesta = ConexionWS.peticionPOSTJSON(url, parametros);
            if (respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK) {
                msj = gson.fromJson(respuesta.getContenido(), Mensaje.class);
            } else {
                msj.setError(true);
                msj.setMensaje(respuesta.getContenido());
            }
        } catch (Exception e) {
            msj.setError(true);
            msj.setMensaje(e.getMessage());
        }

        return msj;
    }
    
    public static Mensaje actualizarFoto(Integer id, byte[] foto) {
        Mensaje msj = new Mensaje();
        String url = Constantes.URL_WS + "/colaborador/actualizar-foto/" + id;
        Gson gson = new Gson();
        try {
            RespuestaHTTP respuesta = ConexionWS.peticionPUTJPEG(url, foto);
            if (respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK) {
                msj = gson.fromJson(respuesta.getContenido(), Mensaje.class);
            } else {
                msj.setError(true);
                msj.setMensaje(respuesta.getContenido());
            }
        } catch (Exception e) {
            msj.setError(true);
            msj.setMensaje(e.getMessage());
        }
        return msj;
    }
    
     public static String obtenerUltimoID() {
        String id = "";
        String url = Constantes.URL_WS + "/colaborador/obtener-ultimo-id";
        Gson gson = new Gson();
        try {
            RespuestaHTTP respuesta = ConexionWS.peticionGET(url);
            if (respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK) {
                id = respuesta.getContenido();
            } else {
                id = "";
            }
        } catch (Exception e) {
            id = "";
        }
        return id;
    }

    public static List<Colaborador> obtenerClientes() {
         List<Colaborador> clientes = new ArrayList<>();
        String url = Constantes.URL_WS + "/colaborador/obtener-colaboradores";
        RespuestaHTTP respuestaWS = ConexionWS.peticionGET(url);
        if (respuestaWS.getCodigoRespuesta() == HttpURLConnection.HTTP_OK) {
            Gson gson = new Gson();
            try {
                Type tipoListaColaborador = new TypeToken<List<Colaborador>>() {
                }.getType();
                clientes = gson.fromJson(respuestaWS.getContenido(), tipoListaColaborador);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return clientes;
    }
    
     public static Mensaje eliminarColaborador(Colaborador colaborador) {
        Mensaje msj = new Mensaje();
        String url = Constantes.URL_WS + "/colaborador/eliminar";
        Gson gson = new Gson();
        RespuestaHTTP respuestaWS = ConexionWS.peticionDELETE(url, gson.toJson(colaborador));
        if (respuestaWS.getCodigoRespuesta() == HttpURLConnection.HTTP_OK) {
            try {
                msj = gson.fromJson(respuestaWS.getContenido(), Mensaje.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return msj;
    }

    public static Mensaje editarColaborador(Colaborador colaborador) {
        Mensaje msj = new Mensaje();
        String url = Constantes.URL_WS + "/colaborador/editar";
        Gson gson = new Gson();
        try {
            String parametros = gson.toJson(colaborador);
            RespuestaHTTP respuesta = ConexionWS.peticionPUTJSON(url, parametros);
            if (respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK) {
                msj = gson.fromJson(respuesta.getContenido(), Mensaje.class);
            } else {
                msj.setError(true);
                msj.setMensaje(respuesta.getContenido());
            }
        } catch (Exception e) {
            msj.setError(true);
            msj.setMensaje(e.getMessage());
        }
        return msj;
    }
    
    
}
