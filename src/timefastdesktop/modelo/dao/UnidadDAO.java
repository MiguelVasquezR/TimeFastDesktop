package timefastdesktop.modelo.dao;

import com.google.gson.Gson;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import timefastdesktop.modelo.ConexionWS;
import timefastdesktop.pojo.Mensaje;
import timefastdesktop.pojo.RespuestaHTTP;
import timefastdesktop.utilidades.Constantes;
import timefastdesktop.pojo.Unidad;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

public class UnidadDAO {

    public static Mensaje agregarUnidad(Unidad unidad) {
        Mensaje msj = new Mensaje();
        String url = Constantes.URL_WS + "/unidades/agregar";
        Gson gson = new Gson();
        try {
            String parametros = gson.toJson(unidad);
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
        String url = Constantes.URL_WS + "/unidades/actualizar-foto/" + id;
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
        String url = Constantes.URL_WS + "/unidades/obtener-ultimo-id";
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

    public static List<Unidad> obtenerUnidades() {
        List<Unidad> unidades = new ArrayList<>();
        String url = Constantes.URL_WS + "/unidades/obtener-unidades";
        RespuestaHTTP respuestaWS = ConexionWS.peticionGET(url);
        if (respuestaWS.getCodigoRespuesta() == HttpURLConnection.HTTP_OK) {
            Gson gson = new Gson();
            try {
                Type tipoListaUnidades = new TypeToken<List<Unidad>>() {
                }.getType();
                unidades = gson.fromJson(respuestaWS.getContenido(), tipoListaUnidades);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return unidades;
    }

    public static Mensaje eliminarUnidad(Integer id) {
         Mensaje msj = new Mensaje();
        String url = Constantes.URL_WS + "/unidades/eliminar/" + id;
        Gson gson = new Gson();
        try {
            RespuestaHTTP respuesta = ConexionWS.peticionDELETEJSON(url, "");
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
    
    public static Mensaje editarUnidad(Unidad unidad){
         Mensaje msj = new Mensaje();
        String url = Constantes.URL_WS + "/unidades/actualizar";
        Gson gson = new Gson();
        try {
            String parametros = gson.toJson(unidad);
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
