package timefastdesktop.modelo.dao;

import com.google.gson.Gson;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import timefastdesktop.modelo.ConexionWS;
import timefastdesktop.pojo.Mensaje;
import timefastdesktop.pojo.RespuestaHTTP;
import timefastdesktop.utilidades.Constantes;
import timefastdesktop.pojo.Cliente;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.net.URLEncoder;

public class ClienteDAO {

    public static Mensaje agregarCliente(Cliente cliente) {
        Mensaje msj = new Mensaje();

        String url = Constantes.URL_WS + "/cliente/agregar";
        Gson gson = new Gson();
        try {
            String parametros = gson.toJson(cliente);
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

    public static List<Cliente> obtenerClientes() {
        List<Cliente> clientes = new ArrayList<>();
        String url = Constantes.URL_WS + "/cliente/obtener-clientes";
        RespuestaHTTP respuestaWS = ConexionWS.peticionGET(url);
        if (respuestaWS.getCodigoRespuesta() == HttpURLConnection.HTTP_OK) {
            Gson gson = new Gson();
            try {
                Type tipoListaCliente = new TypeToken<List<Cliente>>() {
                }.getType();
                clientes = gson.fromJson(respuestaWS.getContenido(), tipoListaCliente);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return clientes;
    }

    public static Mensaje eliminarCliente(Cliente cliente) {
        Mensaje msj = new Mensaje();
        String url = Constantes.URL_WS + "/cliente/eliminar-cliente";
        Gson gson = new Gson();
        RespuestaHTTP respuestaWS = ConexionWS.peticionDELETE(url, gson.toJson(cliente));
        if (respuestaWS.getCodigoRespuesta() == HttpURLConnection.HTTP_OK) {
            try {
                msj = gson.fromJson(respuestaWS.getContenido(), Mensaje.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return msj;
    }

    public static Mensaje editarCliente(Cliente cliente) {
        Mensaje msj = new Mensaje();
        String url = Constantes.URL_WS + "/cliente/actualizar-cliente";
        Gson gson = new Gson();
        try {
            String parametros = gson.toJson(cliente);
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

    public static Mensaje buscarClienteNombre(String nombre) {
        Mensaje msj = new Mensaje();
        String url = Constantes.URL_WS + "/cliente/obtener-cliente-nombre";
        Gson gson = new Gson();
        try {
            String parametros = "nombre=" + URLEncoder.encode(nombre, "UTF-8");
            RespuestaHTTP respuesta = ConexionWS.peticionPOST(url, parametros);
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
