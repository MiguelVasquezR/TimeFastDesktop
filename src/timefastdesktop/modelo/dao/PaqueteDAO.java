package timefastdesktop.modelo.dao;

import com.google.gson.Gson;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import timefastdesktop.modelo.ConexionWS;
import timefastdesktop.pojo.Mensaje;
import timefastdesktop.pojo.RespuestaHTTP;
import timefastdesktop.pojo.Paquete;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.stream.Collectors;
import timefastdesktop.utilidades.Constantes;

public class PaqueteDAO {

    public static Mensaje agregarPaquete(Paquete paquete) {
        Mensaje msj = new Mensaje();
        String url = Constantes.URL_WS + "/paquetes/agregar";
        Gson gson = new Gson();
        try {
            String parametros = gson.toJson(paquete);
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

    public static List<Paquete> obtenerPaquetes() {
        List<Paquete> paquetes = new ArrayList<>();
        String url = Constantes.URL_WS + "/paquetes/obtener-paquetes";
        RespuestaHTTP respuestaWS = ConexionWS.peticionGET(url);
        if (respuestaWS.getCodigoRespuesta() == HttpURLConnection.HTTP_OK) {
            Gson gson = new Gson();
            try {
                Type tipoListaPaquete = new TypeToken<List<Paquete>>() {}.getType();
                paquetes = gson.fromJson(respuestaWS.getContenido(), tipoListaPaquete);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return paquetes;
    }

    public static Mensaje eliminarPaquete(int idPaquete) {
        Mensaje msj = new Mensaje();
        String url = Constantes.URL_WS + "/paquetes/eliminar/" + idPaquete;
        RespuestaHTTP respuestaWS = ConexionWS.peticionDELETE(url, "");
        if (respuestaWS.getCodigoRespuesta() == HttpURLConnection.HTTP_OK) {
            Gson gson = new Gson();
            try {
                msj = gson.fromJson(respuestaWS.getContenido(), Mensaje.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return msj;
    }

    public static Mensaje editarPaquete(Paquete paquete) {
        Mensaje msj = new Mensaje();
        String url = Constantes.URL_WS + "/paquetes/actualizar";
        Gson gson = new Gson();
        try {
            String parametros = gson.toJson(paquete);
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

    public static Mensaje obtenerPaquetePorID(int idPaquete) {
        Mensaje msj = new Mensaje();
        String url = Constantes.URL_WS + "/paquetes/consultar-paquete/" + idPaquete;
        RespuestaHTTP respuestaWS = ConexionWS.peticionGET(url);
        if (respuestaWS.getCodigoRespuesta() == HttpURLConnection.HTTP_OK) {
            Gson gson = new Gson();
            try {
                Paquete paquete = gson.fromJson(respuestaWS.getContenido(), Paquete.class);
                msj.setObjeto(paquete);
                msj.setError(false);
                msj.setMensaje("Paquete obtenido correctamente.");
            } catch (Exception e) {
                msj.setError(true);
                msj.setMensaje(e.getMessage());
            }
        } else {
            msj.setError(true);
            msj.setMensaje("Error al obtener el paquete.");
        }
        return msj;
    }


    public static Mensaje obtenerTodosLosNumGuia() {
        Mensaje mensaje = new Mensaje();
        String url = Constantes.URL_WS + "/envios/todos-num-guia";
        RespuestaHTTP respuestaWS = ConexionWS.peticionGET(url);

        if (respuestaWS.getCodigoRespuesta() == HttpURLConnection.HTTP_OK) {
            Gson gson = new Gson();
            try {
                Type tipoRespuesta = new TypeToken<Mensaje>() {}.getType();
                Mensaje respuestaServidor = gson.fromJson(respuestaWS.getContenido(), tipoRespuesta);

                if (!respuestaServidor.getError()) {
                    // Convierte el objeto en una lista manualmente
                    String objetoComoString = (String) respuestaServidor.getObjeto();
                    List<String> numerosGuia = Arrays.asList(objetoComoString.split("\\s+"));

                    mensaje.setObjeto(numerosGuia);
                    mensaje.setError(false);
                    mensaje.setMensaje("Números de guía obtenidos exitosamente");
                } else {
                    mensaje.setError(true);
                    mensaje.setMensaje(respuestaServidor.getMensaje());
                }
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
                    Type tipoRespuesta = new TypeToken<Mensaje>() {}.getType();
                    Mensaje respuestaServidor = gson.fromJson(respuestaWS.getContenido(), tipoRespuesta);
                    if (!respuestaServidor.getError()) {

                        String objetoComoString = (String) respuestaServidor.getObjeto();
                        List<Integer> idsEnvio = Arrays.stream(objetoComoString.split("\\s+"))
                                                       .map(Integer::parseInt)
                                                       .collect(Collectors.toList());
                        mensaje.setObjeto(idsEnvio);
                        mensaje.setError(false);
                        mensaje.setMensaje("IDs de envío obtenidos exitosamente");
                    } else {
                        mensaje.setError(true);
                        mensaje.setMensaje(respuestaServidor.getMensaje());
                    }
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
    
    public static Mensaje obtenerPaquetesPorEnvio(int idEnvio) {
    Mensaje mensaje = new Mensaje();
    String url = Constantes.URL_WS + "/paquetes/obtener-paquetes-por-envio/" + idEnvio;
    RespuestaHTTP respuestaWS = ConexionWS.peticionGET(url);
    
    if (respuestaWS.getCodigoRespuesta() == HttpURLConnection.HTTP_OK) {
        Gson gson = new Gson();
        try {
            Type tipoListaPaquete = new TypeToken<List<Paquete>>() {}.getType();
            List<Paquete> paquetes = gson.fromJson(respuestaWS.getContenido(), tipoListaPaquete);
            
            if (paquetes != null && !paquetes.isEmpty()) {
                mensaje.setObjeto(paquetes);
                mensaje.setError(false);
                mensaje.setMensaje("Paquetes obtenidos correctamente");
            } else {
                mensaje.setError(true);
                mensaje.setMensaje("No se encontraron paquetes para el envío con ID: " + idEnvio);
            }
        } catch (Exception e) {
            mensaje.setError(true);
            mensaje.setMensaje(e.getMessage());
        }
    } else {
        mensaje.setError(true);
        mensaje.setMensaje("Error al obtener los paquetes.");
    }
    
    return mensaje;
}



}
