package mx.ine.sustseycae.repositories.impl;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import mx.ine.sustseycae.dto.DTOCPermisosCta;
import mx.ine.sustseycae.dto.DTORespuestaWSAdmin;
import mx.ine.sustseycae.repositories.RepoAUSInterface;

@Service("repoAUS")
@Scope("prototype")
public class RepoAUSImpl implements RepoAUSInterface{
	
	private Log log = LogFactory.getLog(RepoAUSImpl.class);
    
    private static final String CREAR_CUENTA_URL = "CrearUsuarioExternoRequest";
    private static final String MODIFICAR_CUENTA_URL = "ModificarUsuarioExternoRequest";
    private static final String ELIMINAR_CUENTA_URL = "EliminarUsuarioExternoRequest";
    private static final String AGREGAR_GRUPO_URL = "agregarUsuariosAGrupoExt";
    private static final String CONSULTAR_MAIL_URL = "consultarUsuarioPorMail";

	@Override
	public DTORespuestaWSAdmin crearCuenta(JsonObject request, String url) throws Exception{
		
		String responseStr = consumeAUS(request, url + CREAR_CUENTA_URL);
		
		if(responseStr == null) 
    		throw new Exception("Error al consumir el servicio para crear cuenta - response: null / request:" + request.toString());
		
		DTORespuestaWSAdmin response = new DTORespuestaWSAdmin();
		Gson gson = new Gson();
		response = gson.fromJson(responseStr, DTORespuestaWSAdmin.class);
		
		return response;
	}

	@Override
	public DTORespuestaWSAdmin eliminarCuenta(JsonObject request, String url) throws Exception {
		
		String responseStr = consumeAUS(request, url + ELIMINAR_CUENTA_URL);
		
		if(responseStr == null) 
    		throw new Exception("Error al consumir el servicio para eliminar cuenta - response: null / request:" + request.toString());
		
		DTORespuestaWSAdmin response = new DTORespuestaWSAdmin();
		Gson gson = new Gson();
		response = gson.fromJson(responseStr, DTORespuestaWSAdmin.class);
		
		return response;

	}

	@Override
	public DTORespuestaWSAdmin modificarCuenta(JsonObject request, String url) throws Exception {

		String responseStr = consumeAUS(request, url + MODIFICAR_CUENTA_URL);
		
		if(responseStr == null)
    		throw new Exception("Error al consumir el servicio para modificar cuenta - response: null / request:" + request.toString());
		
		DTORespuestaWSAdmin response = new DTORespuestaWSAdmin();
		Gson gson = new Gson();
		response = gson.fromJson(responseStr, DTORespuestaWSAdmin.class);
		
		return response;
	}
	
	@Override
	public List<DTOCPermisosCta> asignarPermisosCuenta(List<JsonObject> requestPermisos, String url, String uid) {
		
		List<DTOCPermisosCta> permisosAgregados = new ArrayList<>();
        JsonArray uidsJson = new JsonArray();
		uidsJson.add(uid);
		
		try {
			for(JsonObject request : requestPermisos) {
				
				String rol = request.get("rol").getAsString();
            	String sistema = request.get("nombreSistema").getAsString();
            	request.add("uids", uidsJson);
            	
            	//se utilizan posteriormente pero no se usan para la petición 
            	request.remove("rol");
            	request.remove("nombreSistema");
            	
            	String responseStr = consumeAUS(request, url + AGREGAR_GRUPO_URL);
            	
            	if(responseStr == null) 
            		throw new Exception("Error al consumir el servicio para asignar permiso a cuenta - response: null / request:" + request.toString());
        	
            	
            	DTORespuestaWSAdmin response = new DTORespuestaWSAdmin();
        		Gson gson = new Gson();
        		response = gson.fromJson(responseStr, DTORespuestaWSAdmin.class);
            	
				if(!response.getCodigoRespuesta().equals(1))
					log.error("Ocurrió un error al consumir el servicio para asignar permiso a cuenta - response: " + responseStr + "/ request:" + request.toString());
				
				for(DTORespuestaWSAdmin.Atributo atributo : response.getListUids()) {
					
					//comprueba que se asignó el permiso, cuando el uid enviado se encuentra
					//dentro de los valores del atributo uidsProcesados
					if(atributo.getNombre().equals("uidsProcesados") 
									&& !atributo.getValores().isEmpty()) {
		        			DTOCPermisosCta permisoCta = new DTOCPermisosCta();
		                    permisoCta.setDescripcion(rol);
		                    permisoCta.setSistema(sistema);
		                    permisosAgregados.add(permisoCta);
		        	}
					
					//si no se asignó el permiso el uid  enviado se encuentra en el atributo uidsNoProcesados
					if(atributo.getNombre().equals("uidsNoProcesados")
	        				&& !atributo.getValores().isEmpty())
						log.error("El servicio para asignar permiso a cuenta NO realizó la operación - response: " + responseStr + "/ request:" + request.toString());
				}
				
			}
			
		}catch(Exception e) {
			log.error("Ocurrió un error al consumir el servicio para asignar permiso a cuenta- "+requestPermisos.get(0).toString()+" -", e);
		}
		
		return permisosAgregados;
	}
	
	@Override
	public DTORespuestaWSAdmin obtenerUsuarioPorMail(JsonObject request, String url) throws Exception{

		String responseStr = consumeAUS(request, url + CONSULTAR_MAIL_URL);
		
		if(responseStr == null)
    		throw new Exception("Error al consumir el servicio para consultar usuario por mail - response: null / request:" + request.toString());
		
		DTORespuestaWSAdmin response = new DTORespuestaWSAdmin();
		Gson gson = new Gson();
		response = gson.fromJson(responseStr, DTORespuestaWSAdmin.class);
		
		return response;
	}		
	
	private String consumeAUS(JsonObject request, String url) {
		
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
		return restTemplate.postForObject( url, new HttpEntity<String>
												(request.toString(), httpHeaders), 
												String.class);
	}																															

}
