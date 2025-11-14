package mx.ine.sustseycae.as;

import java.util.List;
import java.util.Set;

import mx.ine.sustseycae.dto.DTOCPermisosCta;
import mx.ine.sustseycae.dto.DTORespuestaWSAdmin;
import mx.ine.sustseycae.dto.db.DTOAspirantes;
import mx.ine.sustseycae.dto.db.DTOCEtiquetas;
import mx.ine.sustseycae.dto.db.DTOCParametros;
import mx.ine.sustseycae.dto.db.DTOCreacionCuentas;
import mx.ine.sustseycae.dto.db.DTOCreacionCuentasId;
import mx.ine.sustseycae.dto.db.DTOGruposDefaultSistema;
import mx.ine.sustseycae.dto.db.DTOParticipacionGeografica;

public interface ASGestionCuentasInterface {
	
	/**
	 * Método para llevar a cabo la creación de una cuenta
	 * 
	 * @param aspirante
	 * @param cuentaPendiente
	 * @param urlAus
	 * 
	 * @return DTORespuestaWSAdmin.Usuario
	 */
	public DTORespuestaWSAdmin.Usuario crearCuenta(DTOAspirantes aspirante, DTOCreacionCuentas cuentaPendiente, String urlAus );

	/**
	 * Método para eliminar una cuenta
	 * 
	 * @param cuentaPendiente
	 * @param usuario
	 * @param ipUsuario
	 * @param ldapCuentas
	 * @param url
	 * 
	 * @return boolean
	 */
	public boolean eliminarCuenta(DTOCreacionCuentas cuentaPendiente, String usuario, String ipUsuario, Integer ldapCuentas, String url);

	/**
	 *  Método para obtener por correo electrónico la información de cuenta de un usuario
	 * 
	 * @param grupoLdap
	 * @param mail
	 * @param urlAus
	 * 
	 * @return DTORespuestaWSAdmin.Usuario
	 */
	public DTORespuestaWSAdmin.Usuario obtenerUsuarioPorMail(Integer grupoLdap, String mail, String urlAus);

	
	/**
	 * método para obtener el registro de creación de cuenta de un aspirante
	 * 
	 * @param idProcesoElectoral
	 * @param idDetalleProceso
	 * @param idParticipacion
	 * @param idAspirante
	 * 
	 * @return DTOCreacionCuentas
	 */
	public DTOCreacionCuentas obtenerCreacionCtaAspirante(Integer idProcesoElectoral, Integer idDetalleProceso, Integer idParticipacion, Integer idAspirante);
		
	/**
	 * Método para recuperar la descripción valor de un parametro CtasSeCae
	 * @param idParametro
	 * 
	 * @return String descripción valor del parametro
	 */
	public String obtenerDescripcionParametroCtasSeCae(Integer idParametro);
	
	/**
	 * Método para obtener la etiqueta qyue corresponde al host para consumir los AUS
	 * como creación de cuenta
	 * 
	 * @param idEtiqueta
	 * @return etiqueta de la claveEtiqueta indicada
	 */
	public String obtenerUrlWsAUS(Integer idEtiqueta);
	
	/**
	 * para obtener parametro a utilizar en la gesti�n de cuentas
	 * 
	 * @param idProcesoElectoral
	 * @param idDetalleProceso
	 * @param idParametros
	 * @return
	 */
	public List<DTOCParametros> obtenerCParametros(Integer idProcesoElectoral, Integer idDetalleProceso, Set<Integer> idParametros);

	
	/**
	 * obtiene participacion geografica 
	 * 
	 * @param idProcesoElectoral
	 * @param idDetalleProceso
	 * @param idParticipacion
	 * @return
	 */
	public DTOParticipacionGeografica obtenerParticipacionGeo(Integer idDetalleProceso, Integer idParticipacion); 

	/**
	 * Método para obtener el número de una area de responsabilildad
	 * 
	 * @param idProcesoElectoral
	 * @param idDetalleProceso
	 * @param idParticipacion
	 * @param idAre
	 * 
	 * @return Integer
	 */
	public Integer obtenerNumeroARE(Integer idProcesoElectoral, Integer idDetalleProceso, Integer idParticipacion, Integer idAre);

	/**
	 * Método para obtener el número de una zona de responsabilildad
	 * 
	 * @param idProcesoElectoral
	 * @param idDetalleProceso
	 * @param idParticipacion
	 * @param idZore
	 * 
	 * @return Integer
	 */
	public Integer obtenerNumeroZORE(Integer idProcesoElectoral, Integer idDetalleProceso, Integer idParticipacion, Integer idZore);
	
	/**
	 * Método para guardar o actualizar el registro de CreacionCuentas
	 * 
	 * @param cuentaPendiente
	 */
	public void guardarOActalizarCreacionCuenta(DTOCreacionCuentas creacionCuenta);
	
	
	/**
	 * Crea la estructura de una cuenta de correo
	 * 
	 * @param idProceso
	 * @param idDetalle
	 * @param idParticipacion
	 * @param idAspirante
	 * @param idPuesto
	 * @return estructura de cuenta de correo electrónico
	 */
	public String creaEstructuraCorreoSistema(Integer idProceso, Integer idDetalle, Integer idParticipacion, Integer idAspirante, Integer idPuesto);

	
	/**
	 * Actualizar el valor uid y servicio Usado cuenta de un aspirante
	 * 
	 * @param idProcesoElectoral
	 * @param idDetalleProceso
	 * @param idParticipacion
	 * @param idAspirante
	 * @param uidCuenta
	 * @param serviocioUsado
	 */
	public void actualizarAspiranteUidServicoUsado(Integer idProcesoElectoral, Integer idDetalleProceso, Integer idParticipacion, Integer idAspirante, String uidCuenta, Integer serviocioUsado);

	/**
	 * Método paea obtener los permisos que debe tener un SE/CAE 
	 * 
	 * @param idActor
	 * @return List<DTOGruposDefaultSistema>
	 */
	public List<DTOGruposDefaultSistema> obtenerPermisosCuenta(Integer idActor);
	
	/**
	 * Método para asignar los permisos a una cuenta de SE/CAE
	 * 
	 * @param permisos
	 * @param url
	 * @param uid
	 * 
	 * @return List<DTOCPermisosCta>
	 */
	public List<DTOCPermisosCta> asignarPermisosCuenta(List<DTOGruposDefaultSistema> permisos, String url, String uid, String ipEjecucion, String usuario);

	/**
	 * Método para obtener max etiqueta por  proceso e idEtiqueta
	 * 
	 * @param idProcesoElectoral
	 * @param idEtiqueta
	 * 
	 * @return DTOCEtiquetas
	 */
	public DTOCEtiquetas obtenerEtiquetaPorIdEtiquetaProceso( Integer idProcesoElectoral, Integer idEtiqueta);
	
	/**
	 * Método para eliminar el registro de creacion de una cuenta
	 * 
	 * @param  DTOCreacionCuentasId
	 */
	public void eliminarRegistroCreacionCuenta(DTOCreacionCuentasId IdCreacionCuenta);

	public DTOAspirantes obtenerAspirante(Integer idProcesoElectoral, Integer idDetalleProceso, Integer idParticipacion, Integer idAspirante);
	

	
}
