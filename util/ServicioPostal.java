package mx.ine.sustseycae.util;

import java.io.File;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;

/**
 * Servicio encargado del envio de correos.
 *
 * @author Luis guichosun del Campo
 * @since Junio 2014
 * @copyright Direccion de sistemas - IFE
 */
public class ServicioPostal {

	private static Log log = LogFactory.getLog(ServicioPostal.class);

	private static String usernameFrom;
	private static String pasFrom;
	private static String cuentaDeEnvio;

	@Resource(lookup = "java:/util/aplicationCorreoPassword")
	private static String pass;

	static {
		try {
			cuentaDeEnvio = Constantes.APPLICATION_CORREO_CUENTA;//  (String) ApplicationContextUtils.getApplicationContext().getBean("cuentaCorreoAplicacion");
			usernameFrom = Constantes.APPLICATION_CORREO_USUARIO;// (String) ApplicationContextUtils.getApplicationContext().getBean("usuarioCorreoAplicacion");
			pasFrom = pass;//(String) ApplicationContextUtils.getApplicationContext().getBean("passwordCorreoAplicacion");
		} catch(Exception e) {
			log.error("No se ha configurado la cuenta del sistema. Se usa una por defecto", e);
			//cuentaDeEnvio = Utils.mensajeProperties("application.correo.cuenta");
		}
		log.info("cuentaDeEnvio: "+cuentaDeEnvio+". usernameFrom: "+usernameFrom+". passwordFrom"+pasFrom);
	}

	public ServicioPostal() {
	}

	/**
	 * Utileria para el envio de correos electronicos
	 *
	 * @param asunto
	 * @param contenido
	 * @param arrayTO
	 * @throws NotSendMailException Si no fue posible enviar el correo
	 */
	public static void envia(String asunto, String contenido, List<String> arrayTO, String rutaGluster)
			throws Exception {
		Mensajero mensajero = Mensajero.getInstancia(usernameFrom, pasFrom, cuentaDeEnvio);
		try {
			mensajero.envioMensajeSimple(asunto, contenido, arrayTO, rutaGluster);
		} catch (Exception e) {
			log.error("Error Servicio Postal - envia()", e);
			throw new Exception("Mail no enviado.", e);
		}
	}

	/**
	 * @param asunto identificador del asunto del mensaje
	 * @param contenido identificador del contenido del mensaje
	 * @param usuariosTo  ArrayList que contiene cadenas (String) con los emails de los usuarios que van a ser TO.
	 * @param archivos  ArrayList que contiene los archivos (File) con las rutas completas de los archivos que se quieren adjuntar.
	 * @throws Exception Excepcion generica que me permite cachar cualquier 
	 * excepcion ocurrida en el proceso y enmascarla para presentarla al cliente.
	 */
	public static void envioMensajeConAdjunto(String asunto, String contenido, List<String> usuariosTo,
											  List<File> archivos) throws Exception{
		Mensajero mensajero = Mensajero.getInstancia(usernameFrom, pasFrom, cuentaDeEnvio);
		try {
			mensajero.envioMensajeConAdjunto(asunto, contenido, usuariosTo, cuentaDeEnvio, archivos);
		} catch (MessagingException e) {
			log.error("Error Servicio Postal - envioMensajeConAdjunto()", e);
			throw new Exception("Mail no enviado.", e);
		}
	}

	/**
	 * Método encargado enviar un correo con un archivo pdf adjunto
	 * @param asunto : identificador del asunto del mensaje
	 * @param contenido : identificador del contenido del mensaje
	 * @param usuariosTo : cuentas de correos que seran enviados.
	 * @param cuentaDeEnvio : identificador de la cuenta de envio
	 * @param bytes : informacion del pdf
	 * @param nombreArchivo: solo el nombre, sin la extension pdf
	 * @throws MessagingException
	 */
	public static void envioMensajeConPdf(String asunto, String contenido, List<String> usuariosTo,
										  byte[] bytes, String nombreArchivo, String nombreSO, String uid, String rutaGlusterSistemasDeceyec,
										  String carpetaSupycap) throws Exception{
		Mensajero mensajero = Mensajero.getInstancia(usernameFrom, pasFrom, cuentaDeEnvio);
		try{
			mensajero.enviaMensajeConPdf(asunto, contenido, usuariosTo, cuentaDeEnvio, bytes, nombreArchivo,  nombreSO,  uid, rutaGlusterSistemasDeceyec, carpetaSupycap);
		}catch (MessagingException e){
			log.error("Error Servicio Postal - envioMensajeConPdf()", e);
			throw new Exception("Mail no enviado.", e);
		}
	}
}
