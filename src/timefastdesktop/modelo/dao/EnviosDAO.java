package timefastdesktop.modelo.dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import timefastdesktop.modelo.ConexionWS;
import timefastdesktop.pojo.Cliente;
import timefastdesktop.pojo.Colaborador;
import timefastdesktop.pojo.Direccion;
import timefastdesktop.pojo.Envio;
import timefastdesktop.pojo.Mensaje;
import timefastdesktop.pojo.RespuestaHTTP;
import timefastdesktop.utilidades.Constantes;

public class EnviosDAO {

    public static Mensaje agregarEnvio(Envio envio) {
        Mensaje mensaje = new Mensaje();
        String url = Constantes.URL_WS + "/envios/agregar";
        Gson gson = new Gson();
        try {
            String parametros = gson.toJson(envio);
            RespuestaHTTP respuesta = ConexionWS.peticionPOSTJSON(url, parametros);
            if (respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK) {
                mensaje = gson.fromJson(respuesta.getContenido(), Mensaje.class);
            } else {
                mensaje.setError(true);
                mensaje.setMensaje(respuesta.getContenido());
            }
        } catch (Exception e) {
            mensaje.setError(true);
            mensaje.setMensaje(e.getMessage());
        }
        return mensaje;
    }

    public static Mensaje actualizarEnvio(Envio envio) {
        Mensaje mensaje = new Mensaje();
        String url = Constantes.URL_WS + "/envios/actualizar";
        Gson gson = new Gson();
        try {
            String parametros = gson.toJson(envio);
            RespuestaHTTP respuesta = ConexionWS.peticionPUTJSON(url, parametros);
            if (respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK) {
                mensaje = gson.fromJson(respuesta.getContenido(), Mensaje.class);
            } else {
                mensaje.setError(true);
                mensaje.setMensaje(respuesta.getContenido());
            }
        } catch (Exception e) {
            mensaje.setError(true);
            mensaje.setMensaje(e.getMessage());
        }
        return mensaje;
    }

    public static Mensaje consultarEnvioPorNumGuia(String numGuia) {
        Mensaje mensaje = new Mensaje();
        String url = Constantes.URL_WS + "/envios/consultar/" + numGuia;
        RespuestaHTTP respuestaWS = ConexionWS.peticionGET(url);
        if (respuestaWS.getCodigoRespuesta() == HttpURLConnection.HTTP_OK) {
            Gson gson = new Gson();
            try {
                mensaje = gson.fromJson(respuestaWS.getContenido(), Mensaje.class);
            } catch (Exception e) {
                mensaje.setError(true);
                mensaje.setMensaje(e.getMessage());
            }
        } else {
            mensaje.setError(true);
            mensaje.setMensaje("Error al consultar el envío con número de guía.");
        }
        return mensaje;
    }

    public static Mensaje obtenerTodosLosNumGuia() {
        Mensaje mensaje = new Mensaje();
        String url = Constantes.URL_WS + "/envios/todos-num-guia";
        RespuestaHTTP respuestaWS = ConexionWS.peticionGET(url);
        if (respuestaWS.getCodigoRespuesta() == HttpURLConnection.HTTP_OK) {
            Gson gson = new Gson();
            try {
                Type tipoListaString = new TypeToken<List<String>>() {}.getType();
                List<String> numerosGuia = gson.fromJson(respuestaWS.getContenido(), tipoListaString);
                mensaje.setObjeto(numerosGuia);
                mensaje.setError(false);
                mensaje.setMensaje("Números de guía obtenidos exitosamente.");
            } catch (Exception e) {
                mensaje.setError(true);
                mensaje.setMensaje(e.getMessage());
            }
        } else {
            mensaje.setError(true);
            mensaje.setMensaje("Error al obtener los números de guía.");
        }
        return mensaje;
    }

    public static Mensaje obtenerTodosLosIdEnvio() {
        Mensaje mensaje = new Mensaje();
        String url = Constantes.URL_WS + "/envios/todos-id-envio";
        RespuestaHTTP respuestaWS = ConexionWS.peticionGET(url);
        if (respuestaWS.getCodigoRespuesta() == HttpURLConnection.HTTP_OK) {
            Gson gson = new Gson();
            try {
                Type tipoListaInteger = new TypeToken<List<Integer>>() {}.getType();
                List<Integer> idsEnvio = gson.fromJson(respuestaWS.getContenido(), tipoListaInteger);
                mensaje.setObjeto(idsEnvio);
                mensaje.setError(false);
                mensaje.setMensaje("IDs de envío obtenidos exitosamente.");
            } catch (Exception e) {
                mensaje.setError(true);
                mensaje.setMensaje(e.getMessage());
            }
        } else {
            mensaje.setError(true);
            mensaje.setMensaje("Error al obtener los IDs de envío.");
        }
        return mensaje;
    }

    public static Mensaje actualizarEstadoEnvio(String jsonEstado) {
        Mensaje mensaje = new Mensaje();
        String url = Constantes.URL_WS + "/envios/actualizar-estado-envio";
        Gson gson = new Gson();
        try {
            RespuestaHTTP respuesta = ConexionWS.peticionPUTJSON(url, jsonEstado);
            if (respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK) {
                mensaje = gson.fromJson(respuesta.getContenido(), Mensaje.class);
            } else {
                mensaje.setError(true);
                mensaje.setMensaje(respuesta.getContenido());
            }
        } catch (Exception e) {
            mensaje.setError(true);
            mensaje.setMensaje(e.getMessage());
        }
        return mensaje;
    }
    
    public static Mensaje obtenerTodosLosEnvios() {
        Mensaje mensaje = new Mensaje();
        String url = Constantes.URL_WS + "/envios/todos-envios";
        RespuestaHTTP respuestaWS = ConexionWS.peticionGET(url);

        if (respuestaWS.getCodigoRespuesta() == HttpURLConnection.HTTP_OK) {
            Gson gson = new Gson();
            try {
                mensaje = gson.fromJson(respuestaWS.getContenido(), Mensaje.class);
            } catch (Exception e) {
                mensaje.setError(true);
                mensaje.setMensaje("Error al procesar la respuesta: " + e.getMessage());
            }
        } else {
            mensaje.setError(true);
            mensaje.setMensaje("Error al obtener la lista de envíos. Código HTTP: " + respuestaWS.getCodigoRespuesta());
        }
        return mensaje;
    }

    public static Mensaje obtenerConductores() {
    Mensaje mensaje = new Mensaje();
    String url = Constantes.URL_WS + "/colaborador/obtener-conductores";
    RespuestaHTTP respuestaWS = ConexionWS.peticionGET(url);

    if (respuestaWS.getCodigoRespuesta() == HttpURLConnection.HTTP_OK) {
        Gson gson = new Gson();
        try {
            Type tipoListaConductores = new TypeToken<List<Colaborador>>() {}.getType();
            List<Colaborador> listaConductores = gson.fromJson(respuestaWS.getContenido(), tipoListaConductores);
            mensaje.setObjeto(listaConductores);
            mensaje.setError(false);
            mensaje.setMensaje("Conductores obtenidos exitosamente.");
        } catch (Exception e) {
            mensaje.setError(true);
            mensaje.setMensaje("Error al procesar la respuesta: " + e.getMessage());
        }
    } else {
        mensaje.setError(true);
        mensaje.setMensaje("Error al obtener los conductores. Código HTTP: " + respuestaWS.getCodigoRespuesta());
    }

    return mensaje;
}
    
public static Mensaje asignarConductor(int idEnvio, int idConductor) {
    Mensaje mensaje = new Mensaje();
    String url = Constantes.URL_WS + "/envios/asignar-conductor/" + idEnvio + "/" + idConductor;

    try {
        RespuestaHTTP respuesta = ConexionWS.peticionPUTJSON(url, "{}");
       
        if (respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK) {
            Gson gson = new Gson();
            mensaje = gson.fromJson(respuesta.getContenido(), Mensaje.class);
        } else {
            mensaje.setError(true);
            mensaje.setMensaje("Error en la asignación: " + respuesta.getContenido());
        }
    } catch (Exception e) {
        mensaje.setError(true);
        mensaje.setMensaje("Error en la conexión al servidor: " + e.getMessage());
    }

    return mensaje;
}

public static Mensaje obtenerDireccionesOrigen() {
    Mensaje mensaje = new Mensaje();
    String url = Constantes.URL_WS + "/direccion/origen"; // URL del endpoint del WS
    RespuestaHTTP respuestaWS = ConexionWS.peticionGET(url);

    if (respuestaWS.getCodigoRespuesta() == HttpURLConnection.HTTP_OK) {
        Gson gson = new Gson();
        try {
            Type tipoListaDirecciones = new TypeToken<List<Direccion>>() {}.getType();
            List<Direccion> direcciones = gson.fromJson(respuestaWS.getContenido(), tipoListaDirecciones);
            mensaje.setObjeto(direcciones);
            mensaje.setError(false);
            mensaje.setMensaje("Direcciones de origen obtenidas exitosamente.");
        } catch (Exception e) {
            mensaje.setError(true);
            mensaje.setMensaje("Error al procesar la respuesta: " + e.getMessage());
        }
    } else {
        mensaje.setError(true);
        mensaje.setMensaje("Error al obtener las direcciones de origen. Código HTTP: " + respuestaWS.getCodigoRespuesta());
    }

    return mensaje;
}

    
}
