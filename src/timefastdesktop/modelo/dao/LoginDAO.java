package timefastdesktop.modelo.dao;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import timefastdesktop.modelo.ConexionWS;
import timefastdesktop.pojo.Mensaje;
import timefastdesktop.pojo.RespuestaHTTP;
import timefastdesktop.utilidades.Constantes;

public class LoginDAO {
    
    public static Mensaje iniciarSesion(String numPersonal, String password){
       Mensaje msj = new Mensaje();
        String url = Constantes.URL_WS + "/login/login-colaborador";
        Gson gson = new Gson();
        try {
        String parametros = "noPersonal=" + URLEncoder.encode(numPersonal, "UTF-8") +
                            "&password=" + URLEncoder.encode(password, "UTF-8");
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
