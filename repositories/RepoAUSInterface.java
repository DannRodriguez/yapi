package mx.ine.sustseycae.repositories;

import java.util.List;

import com.google.gson.JsonObject;

import mx.ine.sustseycae.dto.DTOCPermisosCta;
import mx.ine.sustseycae.dto.DTORespuestaWSAdmin;

public interface RepoAUSInterface {

    public DTORespuestaWSAdmin crearCuenta(JsonObject request, String url) throws Exception;

    public DTORespuestaWSAdmin eliminarCuenta(JsonObject request, String url) throws Exception;

    public DTORespuestaWSAdmin modificarCuenta(JsonObject request, String url) throws Exception;

    public List<DTOCPermisosCta> asignarPermisosCuenta(List<JsonObject> requestPermisos, String url, String uid);

    public DTORespuestaWSAdmin obtenerUsuarioPorMail(JsonObject request, String url) throws Exception;

}
