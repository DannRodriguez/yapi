package mx.ine.sustseycae.controllers.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.Response;
import mx.ine.sustseycae.bsd.BSDDeshacerSustituciones;
import mx.ine.sustseycae.controllers.ControllerDeshacerSustituciones;
import mx.ine.sustseycae.dto.vo.VOConsultaDesSustitucionesSupycap;
import mx.ine.sustseycae.models.requests.DTORequestConsultaDeshacerSustituciones;
import mx.ine.sustseycae.models.requests.DTORequestDeshacerSustitucion;
import mx.ine.sustseycae.models.responses.ModelGenericResponse;
import mx.ine.sustseycae.util.Exceptions.ExceptionValidacionAreZore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ControllerDeshacerSustitucionesImpl implements ControllerDeshacerSustituciones {

    @Autowired
    BSDDeshacerSustituciones bsdDeshacerSustituciones;

    @Override
    public Response consultaDeshacerSustitucion(DTORequestConsultaDeshacerSustituciones deshacerSustituciones, HttpServletRequest request) {
        ModelGenericResponse response = new ModelGenericResponse();
        try {

            List<VOConsultaDesSustitucionesSupycap> data =  bsdDeshacerSustituciones.consultaDeshacerSustitucion(deshacerSustituciones);
            response.setCode(200);
            response.setMessage("Lista de consutla deshacer sustitucion.");
            response.setData(data);
            return Response.ok(response).build();
        }catch (Exception e) {
            response.setCode(500);
            response.setMessage("Ocurrio un error en el servidor.");
            response.setCausa(e.getMessage());
            response.setData("");
            //return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
            return Response.serverError().entity(response).build();
        }
    }

    @Override
    public Response consultaSustitucionesDeshechas(DTORequestConsultaDeshacerSustituciones deshacerSustituciones, HttpServletRequest request) {
        ModelGenericResponse response = new ModelGenericResponse();
        try {

            List<VOConsultaDesSustitucionesSupycap> data =  bsdDeshacerSustituciones.consultaSustitucionesDeshechas(deshacerSustituciones);
            response.setCode(200);
            response.setMessage("Lista de consutla deshacer sustitucion.");
            response.setData(data);
            return Response.ok(response).build();
        }catch (Exception e) {
            response.setCode(500);
            response.setMessage("Ocurrio un error en el servidor.");
            response.setCausa(e.getMessage());
            response.setData("");
            //return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
            return Response.serverError().entity(response).build();
        }
    }

    @Override
    public Response deshacerSustitucion(DTORequestDeshacerSustitucion requestSustitucion, HttpServletRequest request)throws ExceptionValidacionAreZore {
        ModelGenericResponse response = new ModelGenericResponse();
        try {
            requestSustitucion.setIpUsuario(obtenerIpCliente(request));

            bsdDeshacerSustituciones.deshacerSustitucion(requestSustitucion);

            response.setCode(200);
            response.setMessage("Lista de consutla deshacer sustitucion.");
            response.setData("Ok");
            return Response.ok(response).build();
        }
        catch(ExceptionValidacionAreZore e){
            response.setCode(406);
            response.setMessage(e.getMessage());
            response.setCausa(e.getMessage());
            response.setData("");
            //return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(response).build();
        }
        catch (Exception e) {
            response.setCode(500);
            response.setMessage("Ocurrio un error en el servidor.");
            response.setCausa(e.getMessage());
            response.setData("");
            //return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
            return Response.serverError().entity(response).build();
        }
    }

    private String obtenerIpCliente(HttpServletRequest request) {
        try{
            String ip = request.getHeader("X-Forwarded-For"); // Revisar si hay un proxy o balanceador
            if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr(); // Dirección IP directa
            }
            return ip;

        }catch (Exception e){
            return null;
        }
    }
}
