package mx.ine.sustseycae.as.impl;

import mx.ine.sustseycae.as.ASCommons;
import mx.ine.sustseycae.as.ASDeshacerSustituciones;
import mx.ine.sustseycae.dto.db.*;
import mx.ine.sustseycae.dto.vo.VOConsultaDesSustitucionesSupycap;
import mx.ine.sustseycae.dto.vo.VOSustitucionesSupycap;
import mx.ine.sustseycae.helper.impl.HLPCuentasSustitucionesImpl;
import mx.ine.sustseycae.models.requests.DTORequestConsultaDeshacerSustituciones;
import mx.ine.sustseycae.models.requests.DTORequestDeshacerSustitucion;
import mx.ine.sustseycae.repositories.RepoJPAAspirantes;
import mx.ine.sustseycae.repositories.RepoJPADesSustitucionesSupycap;
import mx.ine.sustseycae.repositories.RepoJPASustitucionesSupycap;
import mx.ine.sustseycae.util.Constantes;
import mx.ine.sustseycae.util.Exceptions.ExceptionValidacionAreZore;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Controller
public class ASDeshacerSustitucionesImpl implements ASDeshacerSustituciones {

        private Log log = LogFactory.getLog(ASDeshacerSustitucionesImpl.class);

        @Autowired
        RepoJPADesSustitucionesSupycap repoJPADesSustitucionesSupycap;

        @Autowired
        RepoJPAAspirantes repoJPAAspirantes;

        @Autowired
        RepoJPASustitucionesSupycap repoJPASustitucionesSupycap;

        @Autowired
        ASCommonsImpl asCommons;

        @Autowired
        HLPCuentasSustitucionesImpl hlpCuentasSustituciones;

        @Override
        public List<VOConsultaDesSustitucionesSupycap> consultaDeshacerSustitucion(
                        DTORequestConsultaDeshacerSustituciones deshacerSustituciones) {
                List<VOConsultaDesSustitucionesSupycap> listaConsulta = repoJPADesSustitucionesSupycap
                                .consultaDesSustituciones(
                                                deshacerSustituciones.getIdProcesoElectoral(),
                                                deshacerSustituciones.getIdDetalleProceso(),
                                                deshacerSustituciones.getIdParticipacion());

                List<VOConsultaDesSustitucionesSupycap> listaDevelve = new ArrayList<>();
                VOConsultaDesSustitucionesSupycap objeto = null;
                VOConsultaDesSustitucionesSupycap objetoSiguiente = null;
                Boolean exixteEnSustPosterior;
                for (int i = 0; i <= listaConsulta.size() - 1; i++) {
                        objeto = listaConsulta.get(i);

                        for (int j = i + 1; j <= listaConsulta.size() - 1; j++) {
                                objetoSiguiente = listaConsulta.get(j);

                                exixteEnSustPosterior = validaExisteEnSustPosteriores(objeto, objetoSiguiente);

                                if (exixteEnSustPosterior && !objeto.getId_relacion_sustituciones()
                                                .equals(objetoSiguiente.getId_relacion_sustituciones())) {
                                        objeto.setExistEnSustPosteriores(true);

                                        // Validar si el objeto anterior en la lista 'listaDevelve' tiene el mismo
                                        // id_relacion_sustituciones que el objeto actual
                                        // si el objeto anterior tiene un false, colocarlo en true, para evitar que
                                        // pueda ser eliminado si el segundo elemento de la relacion no puede ser
                                        // eliminado (homologar)
                                        if (!listaDevelve.isEmpty()) {
                                                // Obtener el último objeto agregado a listaDevelve
                                                VOConsultaDesSustitucionesSupycap anterior = listaDevelve
                                                                .get(listaDevelve.size() - 1);

                                                if (anterior.getId_relacion_sustituciones()
                                                                .equals(objeto.getId_relacion_sustituciones())
                                                                && anterior.getExistEnSustPosteriores() == false) {
                                                        // Eliminar el último objeto de la lista
                                                        listaDevelve.remove(listaDevelve.size() - 1);

                                                        // Modificar el objeto anterior
                                                        anterior.setExistEnSustPosteriores(true);

                                                        // Volver a agregar el objeto modificado
                                                        listaDevelve.add(anterior);
                                                }
                                        }
                                }

                        }

                        listaDevelve.add(objeto);
                }

                return listaDevelve;
        }

        @Override
        public List<VOConsultaDesSustitucionesSupycap> consultaSustitucionesDeshechas(
                        DTORequestConsultaDeshacerSustituciones deshacerSustituciones) {
                return repoJPADesSustitucionesSupycap.consultaSustitucionesDeshechas(
                                deshacerSustituciones.getIdProcesoElectoral(),
                                deshacerSustituciones.getIdDetalleProceso(),
                                deshacerSustituciones.getIdParticipacion());
        }

        @Override
        @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {
                        Exception.class }, transactionManager = "transactionManager")
        public void deshacerSustitucion(DTORequestDeshacerSustitucion requestSustitucion)
                        throws ExceptionValidacionAreZore {
                List<VOConsultaDesSustitucionesSupycap> listaDeshacer = repoJPADesSustitucionesSupycap
                                .consultaDesSustitucionesRelacionadas(requestSustitucion.getIdProcesoElectoral(),
                                                requestSustitucion.getIdDetalleProceso(),
                                                requestSustitucion.getIdParticipacion(),
                                                requestSustitucion.getSustitucionADeshacer()
                                                                .getId_relacion_sustituciones());

                Integer idPrimerSustituido = 0;
                Integer idPrimerSustituto = 0;
                Integer idSegundoSustituido = 0;
                Integer idSegundoSustituto = 0;

                Integer etapa = 1;
                try {
                        etapa = asCommons.obtieneEtapaActual(requestSustitucion.getIdDetalleProceso());
                } catch (Exception e) {

                }
                if (listaDeshacer.isEmpty()) {
                        throw new ExceptionValidacionAreZore("No se ha encontrado sustitucion con ese id relacion.");
                }
                if (listaDeshacer.size() == 1) {
                        // DESHACE UNA SUTITUCION
                        VOSustitucionesSupycap primeraSustitucion = repoJPASustitucionesSupycap
                                        .obtenerInfoSustitucionById(
                                                        requestSustitucion.getIdProcesoElectoral(),
                                                        requestSustitucion.getIdDetalleProceso(),
                                                        requestSustitucion.getIdParticipacion(),
                                                        listaDeshacer.get(0).getId_sustitucion());
                        idPrimerSustituido = primeraSustitucion.getIdAspiranteSutituido();
                        /**/DTOAspirantes primerAspiranteSustituido = repoJPAAspirantes
                                        .findById_IdProcesoElectoralAndId_IdDetalleProcesoAndId_IdParticipacionAndId_IdAspirante(
                                                        requestSustitucion.getIdProcesoElectoral(),
                                                        requestSustitucion.getIdDetalleProceso(),
                                                        requestSustitucion.getIdParticipacion(), idPrimerSustituido);
                        idPrimerSustituto = primeraSustitucion.getIdAspiranteSutituto();
                        /**/DTOAspirantes primerAspiranteSustituto = repoJPAAspirantes
                                        .findById_IdProcesoElectoralAndId_IdDetalleProcesoAndId_IdParticipacionAndId_IdAspirante(
                                                        requestSustitucion.getIdProcesoElectoral(),
                                                        requestSustitucion.getIdDetalleProceso(),
                                                        requestSustitucion.getIdParticipacion(), idPrimerSustituto);

                        idPrimerSustituto = primeraSustitucion.getIdAspiranteSutituto() != null
                                        ? primeraSustitucion.getIdAspiranteSutituto()
                                        : 0;
                        validaAresZores(primeraSustitucion, primerAspiranteSustituto, etapa);

                        // validar si la sustitucion del cae o del se es completa o pendiente
                        try {
                                deshacerSustitucion(primeraSustitucion, primerAspiranteSustituido,
                                                primerAspiranteSustituto,
                                                requestSustitucion, etapa, true);
                        } catch (ExceptionValidacionAreZore e) {
                                throw new ExceptionValidacionAreZore(e.getMessage());
                        }

                } else if (listaDeshacer.size() == 2) {
                        // DESHACE DOS SUTITUCIONES
                        VOSustitucionesSupycap primeraSustitucion = repoJPASustitucionesSupycap
                                        .obtenerInfoSustitucionById(
                                                        requestSustitucion.getIdProcesoElectoral(),
                                                        requestSustitucion.getIdDetalleProceso(),
                                                        requestSustitucion.getIdParticipacion(),
                                                        listaDeshacer.get(0).getId_sustitucion());
                        idPrimerSustituido = primeraSustitucion.getIdAspiranteSutituido();
                        /**/DTOAspirantes primerAspiranteSustituido = repoJPAAspirantes
                                        .findById_IdProcesoElectoralAndId_IdDetalleProcesoAndId_IdParticipacionAndId_IdAspirante(
                                                        requestSustitucion.getIdProcesoElectoral(),
                                                        requestSustitucion.getIdDetalleProceso(),
                                                        requestSustitucion.getIdParticipacion(), idPrimerSustituido);
                        idPrimerSustituto = primeraSustitucion.getIdAspiranteSutituto();
                        /**/DTOAspirantes primerAspiranteSustituto = repoJPAAspirantes
                                        .findById_IdProcesoElectoralAndId_IdDetalleProcesoAndId_IdParticipacionAndId_IdAspirante(
                                                        requestSustitucion.getIdProcesoElectoral(),
                                                        requestSustitucion.getIdDetalleProceso(),
                                                        requestSustitucion.getIdParticipacion(), idPrimerSustituto);

                        VOSustitucionesSupycap segundaSustitucion = repoJPASustitucionesSupycap
                                        .obtenerInfoSustitucionById(
                                                        requestSustitucion.getIdProcesoElectoral(),
                                                        requestSustitucion.getIdDetalleProceso(),
                                                        requestSustitucion.getIdParticipacion(),
                                                        listaDeshacer.get(1).getId_sustitucion());
                        idSegundoSustituido = segundaSustitucion.getIdAspiranteSutituido();
                        /**/DTOAspirantes segundoAspiranteSustituido = repoJPAAspirantes
                                        .findById_IdProcesoElectoralAndId_IdDetalleProcesoAndId_IdParticipacionAndId_IdAspirante(
                                                        requestSustitucion.getIdProcesoElectoral(),
                                                        requestSustitucion.getIdDetalleProceso(),
                                                        requestSustitucion.getIdParticipacion(), idSegundoSustituido);
                        idSegundoSustituto = segundaSustitucion.getIdAspiranteSutituto() != null
                                        ? segundaSustitucion.getIdAspiranteSutituto()
                                        : 0;
                        /**/DTOAspirantes segundoAspiranteSustituto = repoJPAAspirantes
                                        .findById_IdProcesoElectoralAndId_IdDetalleProcesoAndId_IdParticipacionAndId_IdAspirante(
                                                        requestSustitucion.getIdProcesoElectoral(),
                                                        requestSustitucion.getIdDetalleProceso(),
                                                        requestSustitucion.getIdParticipacion(), idSegundoSustituto);

                        validaAresZores(primeraSustitucion, primerAspiranteSustituto, etapa);
                        validaAresZores(segundaSustitucion, segundoAspiranteSustituto, etapa);

                        // aqui cuando son dos sustituciones hay que ver si es pendiente o completa
                        try {
                                if ((segundaSustitucion.getIdAspiranteSutituido() != null && Objects.equals(
                                                primeraSustitucion.getIdAspiranteSutituido(),
                                                segundaSustitucion.getIdAspiranteSutituto())) &&
                                                (primeraSustitucion.getIdAspiranteSutituto() != null
                                                                && Objects.equals(segundaSustitucion
                                                                                .getIdAspiranteSutituido(),
                                                                                primeraSustitucion
                                                                                                .getIdAspiranteSutituto()))) {
                                        deshacerSustitucion(segundaSustitucion, segundoAspiranteSustituido,
                                                        segundoAspiranteSustituto,
                                                        requestSustitucion, etapa, false);
                                        deshacerSustitucion(primeraSustitucion, primerAspiranteSustituido,
                                                        primerAspiranteSustituto,
                                                        requestSustitucion, etapa, true);
                                } else {
                                        deshacerSustitucion(primeraSustitucion, primerAspiranteSustituido,
                                                        primerAspiranteSustituto,
                                                        requestSustitucion, etapa, true);
                                        deshacerSustitucion(segundaSustitucion, segundoAspiranteSustituido,
                                                        segundoAspiranteSustituto,
                                                        requestSustitucion, etapa, false);
                                }

                        } catch (ExceptionValidacionAreZore e) {
                                throw new ExceptionValidacionAreZore(e.getMessage());
                        }
                } else {
                        throw new ExceptionValidacionAreZore("Existen más de dos sustituciones relacionadas.");
                }
        }

        public void deshacerSustitucion(VOSustitucionesSupycap sustitucion, DTOAspirantes sustituido,
                        DTOAspirantes sustituto, DTORequestDeshacerSustitucion requestDeshacerSustitucion,
                        Integer etapa,
                        boolean esSustDesencadenante) throws ExceptionValidacionAreZore {
                try {
                        DTODesSustitucionesSupycapId id = new DTODesSustitucionesSupycapId();
                        DTODesSustitucionesSupycap deshSustitucion = new DTODesSustitucionesSupycap();
                        id.setIdDeshacer(repoJPADesSustitucionesSupycap.encontrarMaxId(
                                        requestDeshacerSustitucion.getIdDetalleProceso(),
                                        requestDeshacerSustitucion.getIdParticipacion())
                                        .orElse(1));
                        id.setIdDetalleProceso(requestDeshacerSustitucion.getIdDetalleProceso());
                        id.setIdParticipacion(requestDeshacerSustitucion.getIdParticipacion());

                        deshSustitucion.setId(id);
                        deshSustitucion.setIdProcesoElectoral(requestDeshacerSustitucion.getIdProcesoElectoral());
                        deshSustitucion.setIdSustitucion(sustitucion.getIdSustitucion());
                        deshSustitucion.setIdRelacionSustituciones(sustitucion.getIdRelacionSustituciones());
                        deshSustitucion.setIdAspiranteSutituido(
                                        sustitucion.getIdAspiranteSutituido() != null
                                                        ? sustitucion.getIdAspiranteSutituido()
                                                        : null);
                        deshSustitucion.setIdPuestoSustituido(
                                        sustitucion.getIdPuestoSustituido() != null
                                                        ? sustitucion.getIdPuestoSustituido()
                                                        : null);
                        deshSustitucion.setCorreoCtaCreadaSustituido(
                                        sustitucion.getCorreoCtaCreadaSustituido() != null
                                                        ? sustitucion.getCorreoCtaCreadaSustituido()
                                                        : null);
                        deshSustitucion.setCorreoCtaNotifSustituido(
                                        sustitucion.getCorreoCtaNotifSustituido() != null
                                                        ? sustitucion.getCorreoCtaNotifSustituido()
                                                        : null);

                        deshSustitucion.setIdAspiranteSutituto(
                                        sustitucion.getIdAspiranteSutituto() != null
                                                        ? sustitucion.getIdAspiranteSutituto()
                                                        : null);
                        deshSustitucion.setIdPuestoSustituto(
                                        sustitucion.getIdPuestoSustituto() != null ? sustitucion.getIdPuestoSustituto()
                                                        : null);
                        deshSustitucion.setCorreoCtaCreadaSustituto(
                                        sustitucion.getCorreoCtaCreadaSustituto() != null
                                                        ? sustitucion.getCorreoCtaCreadaSustituto()
                                                        : null);
                        deshSustitucion.setCorreoCtaNotifSustituto(
                                        sustitucion.getCorreoCtaNotifSustituto() != null
                                                        ? sustitucion.getCorreoCtaNotifSustituto()
                                                        : null);
                        deshSustitucion.setIdCausaVacante(sustitucion.getIdCausaVacante());
                        deshSustitucion.setTipoCausaVacante(sustitucion.getTipoCausaVacante());
                        deshSustitucion.setFechaBaja(
                                        sustitucion.getFechaBaja() != null ? sustitucion.getFechaBaja() : null);
                        deshSustitucion.setFechaAlta(
                                        sustitucion.getFechaAlta() != null ? sustitucion.getFechaAlta() : null);
                        deshSustitucion.setFechaSustitucion(
                                        sustitucion.getFechaSustitucion() != null ? sustitucion.getFechaSustitucion()
                                                        : null);

                        deshSustitucion.setIdAreaResponsabilidad1e(
                                        sustitucion.getIdAreaResponsabilidad1e() != null
                                                        ? sustitucion.getIdAreaResponsabilidad1e()
                                                        : null);
                        deshSustitucion.setIdZonaResponsabilidad1e(
                                        sustitucion.getIdAZonaResponsabilidad1e() != null
                                                        ? sustitucion.getIdAZonaResponsabilidad1e()
                                                        : null);
                        deshSustitucion.setIdAreaResponsabilidad2e(
                                        sustitucion.getIdAreaResponsabilidad2e() != null
                                                        ? sustitucion.getIdAreaResponsabilidad2e()
                                                        : null);
                        deshSustitucion.setIdZonaResponsabilidad2e(
                                        sustitucion.getIdZonaResponsabilidad2e() != null
                                                        ? sustitucion.getIdZonaResponsabilidad2e()
                                                        : null);
                        deshSustitucion.setUidCuentaSustituido(
                                        sustitucion.getUidCuentaSustituido() != null
                                                        ? sustitucion.getUidCuentaSustituido()
                                                        : null);
                        deshSustitucion.setUidCuentaSustituto(
                                        sustitucion.getUidCuentaSustituto() != null
                                                        ? sustitucion.getUidCuentaSustituto()
                                                        : null);
                        deshSustitucion
                                        .setDeclinoCargo(sustitucion.getDeclinoCargo() != null
                                                        ? sustitucion.getDeclinoCargo()
                                                        : null);
                        deshSustitucion.setEtapa(etapa);
                        deshSustitucion.setUsuario(requestDeshacerSustitucion.getUser());
                        deshSustitucion.setIpUsuario(requestDeshacerSustitucion.getIpUsuario());
                        deshSustitucion.setFechaHora(new Date());
                        repoJPADesSustitucionesSupycap.saveAndFlush(deshSustitucion);

                        repoJPASustitucionesSupycap
                                        .deleteById(new DTOSustitucionesSupycapId(
                                                        requestDeshacerSustitucion.getIdDetalleProceso(),
                                                        requestDeshacerSustitucion.getIdParticipacion(),
                                                        sustitucion.getIdSustitucion()));

                        if (sustituto != null) {
                                // if( (sustitucion.getIdPuestoSustituto() == Constantes.ID_PUESTO_RESCISION_SE
                                // || sustitucion.getIdPuestoSustituto() ==
                                // Constantes.ID_PUESTO_RECONTRATACION_SE) && sustituto.getIdPuesto() ==
                                // Constantes.ID_PUESTO_SE){
                                // throw new ExceptionValidacionAreZore("Movimiento no permitido debido a que se
                                // perdería el puesto que ocupa el sustituido: "+(sustituto.getApellidoPaterno()
                                // == null ? "":sustituto.getApellidoPaterno()+" ") +
                                // (sustituto.getApellidoMaterno() == null ? "":sustituto.getApellidoMaterno()+"
                                // ")+(sustituto.getNombre() == null ? "":sustituto.getNombre()) +". Favor de
                                // contactar al administrador");
                                // }
                                Integer idPuestoSustitutoAnt = sustituto.getIdPuesto();

                                Integer idAre1Anterior = sustitucion.getIdAreaResponsabilidad1e() != null
                                                ? sustitucion.getIdAreaResponsabilidad1e()
                                                : null;
                                Integer idAre2Anterior = sustitucion.getIdAreaResponsabilidad2e() != null
                                                ? sustitucion.getIdAreaResponsabilidad2e()
                                                : null;
                                Integer idZore1Anterior = sustitucion.getIdAZonaResponsabilidad1e() != null
                                                ? sustitucion.getIdAZonaResponsabilidad1e()
                                                : null;
                                Integer idZore2Anterior = sustitucion.getIdZonaResponsabilidad2e() != null
                                                ? sustitucion.getIdZonaResponsabilidad2e()
                                                : null;

                                sustituto.setIdPuesto(sustitucion.getIdPuestoSustituto());

                                if (sustitucion.getIdCausaVacante() == 2 && sustitucion.getTipoCausaVacante() == 4) {
                                        if (sustituto.getIdPuesto() == Constantes.ID_PUESTO_SE_INCAP
                                                        || sustituto.getIdPuesto() == Constantes.ID_PUESTO_CAE_INCAP) {
                                                // ESTE ESCENARIO ES CUANDO SE DESHACE UN TERMINO DE CONTRATO PREVALEZCA
                                                // EL
                                                // VALOR DE LOS INCAPACITADOS REGISTRADO EN SUST (VALOR ORIGINAL)
                                                sustituto.setIdAreaResponsabilidad1e(idAre1Anterior);
                                                sustituto.setIdAreaResponsabilidad2e(idAre2Anterior);
                                                sustituto.setIdZonaResponsabilidad1e(idZore1Anterior);
                                                sustituto.setIdZonaResponsabilidad2e(idZore2Anterior);
                                        } else if (sustituto.getIdPuesto() == Constantes.ID_PUESTO_SE_TMP
                                                        || sustituto.getIdPuesto() == Constantes.ID_PUESTO_CAE_TMP) {
                                                // ESTE ESCENARIO ES CUANDO SE DESHACE UN TERMINO DE CONTRATO PREVALEZCA
                                                // EL
                                                // VALOR DE LOS SE o CAE TEMPORALES (VALOR ACTUAL NO SE MODIFICA)
                                        } else {
                                                sustituto.setIdAreaResponsabilidad1e(null);
                                                sustituto.setIdAreaResponsabilidad2e(null);
                                                sustituto.setIdZonaResponsabilidad1e(null);
                                                sustituto.setIdZonaResponsabilidad2e(null);
                                        }
                                } else {
                                        if (sustituto.getIdPuesto().equals(idPuestoSustitutoAnt)) {
                                                // ESTE IF ES PARA IDENTIFICAR CUANDO ESTOY DESHACIENDO UNA SUSTITUCION
                                                // DONDE EL
                                                // CAE YA ES CAE Y AL DESHACERLA SE HARÍA CAE DE TODOS MODOS
                                        } else {
                                                sustituto.setIdAreaResponsabilidad1e(null);
                                                sustituto.setIdAreaResponsabilidad2e(null);
                                                sustituto.setIdZonaResponsabilidad1e(null);
                                                sustituto.setIdZonaResponsabilidad2e(null);
                                        }
                                }

                                sustituto.setFechaHora(new Date());
                                sustituto.setExisteSustitucion(null);
                                sustituto.setUsuario(requestDeshacerSustitucion.getUser());
                                sustituto.setIpUsuario(sustitucion.getIpUsuario());
                                sustituto.setIdSistemaActualiza(Constantes.ID_SISTEMA);
                                repoJPAAspirantes.saveAndFlush(sustituto);

                                if ((idPuestoSustitutoAnt == Constantes.ID_PUESTO_SE_TMP
                                                || idPuestoSustitutoAnt == Constantes.ID_PUESTO_CAE_TMP
                                                || idPuestoSustitutoAnt == Constantes.ID_PUESTO_SE
                                                || idPuestoSustitutoAnt == Constantes.ID_PUESTO_CAE)
                                                && (sustitucion.getIdPuestoSustituto() == Constantes.ID_PUESTO_CAE
                                                                || sustitucion.getIdPuestoSustituto() == Constantes.ID_PUESTO_SE)) {
                                        hlpCuentasSustituciones.modificarCuentaSustitucion(sustituto,
                                                        requestDeshacerSustitucion.getUser());
                                } else if (!(idPuestoSustitutoAnt == Constantes.ID_PUESTO_SE_TMP
                                                || idPuestoSustitutoAnt == Constantes.ID_PUESTO_CAE_TMP
                                                || idPuestoSustitutoAnt == Constantes.ID_PUESTO_SE
                                                || idPuestoSustitutoAnt == Constantes.ID_PUESTO_CAE)
                                                && (sustitucion.getIdPuestoSustituto() == Constantes.ID_PUESTO_CAE
                                                                || sustitucion.getIdPuestoSustituto() == Constantes.ID_PUESTO_SE
                                                                || sustitucion.getIdPuestoSustituto() == Constantes.ID_PUESTO_SE_TMP
                                                                || sustitucion.getIdPuestoSustituto() == Constantes.ID_PUESTO_CAE_TMP)) {
                                        hlpCuentasSustituciones.crearCuentaSustitucion(sustituto,
                                                        requestDeshacerSustitucion.getUser());

                                } else if ((idPuestoSustitutoAnt == Constantes.ID_PUESTO_SE_TMP
                                                || idPuestoSustitutoAnt == Constantes.ID_PUESTO_CAE_TMP
                                                || idPuestoSustitutoAnt == Constantes.ID_PUESTO_SE
                                                || idPuestoSustitutoAnt == Constantes.ID_PUESTO_CAE)
                                                && !(sustitucion.getIdPuestoSustituto() == Constantes.ID_PUESTO_CAE
                                                                || sustitucion.getIdPuestoSustituto() == Constantes.ID_PUESTO_SE
                                                                || sustitucion.getIdPuestoSustituto() == Constantes.ID_PUESTO_SE_TMP
                                                                || sustitucion.getIdPuestoSustituto() == Constantes.ID_PUESTO_CAE_TMP)) {
                                        hlpCuentasSustituciones.eliminarCuentaSustitucion(sustituto,
                                                        requestDeshacerSustitucion.getUser());
                                }
                        }

                        Integer idPuestoSustituidoAnt = sustituido.getIdPuesto();

                        sustituido.setIdPuesto(sustitucion.getIdPuestoSustituido());
                        if (sustituido.getIdPuesto().equals(Constantes.ID_PUESTO_SE)
                                        && idPuestoSustituidoAnt == Constantes.ID_PUESTO_CAE && esSustDesencadenante) {
                                throw new ExceptionValidacionAreZore(
                                                "Movimiento no permitido debido a que se perdería el puesto que ocupa el sustituido: "
                                                                + (sustituido.getApellidoPaterno() == null ? ""
                                                                                : sustituido.getApellidoPaterno() + " ")
                                                                + (sustituido.getApellidoMaterno() == null ? ""
                                                                                : sustituido.getApellidoMaterno() + " ")
                                                                + (sustituido.getNombre() == null ? ""
                                                                                : sustituido.getNombre())
                                                                + " en ARE "
                                                                + (sustituido.getIdAreaResponsabilidad2e() == null
                                                                                ? sustituido.getIdAreaResponsabilidad1e()
                                                                                : sustituido.getIdAreaResponsabilidad2e())
                                                                + ".");
                        }
                        sustituido.setIdAreaResponsabilidad1e(
                                        sustitucion.getIdAreaResponsabilidad1e() != null
                                                        ? sustitucion.getIdAreaResponsabilidad1e()
                                                        : null);
                        sustituido.setIdAreaResponsabilidad2e(
                                        sustitucion.getIdAreaResponsabilidad2e() != null
                                                        ? sustitucion.getIdAreaResponsabilidad2e()
                                                        : null);
                        sustituido.setIdZonaResponsabilidad1e(
                                        sustitucion.getIdAZonaResponsabilidad1e() != null
                                                        ? sustitucion.getIdAZonaResponsabilidad1e()
                                                        : null);
                        sustituido.setIdZonaResponsabilidad2e(
                                        sustitucion.getIdZonaResponsabilidad2e() != null
                                                        ? sustitucion.getIdZonaResponsabilidad2e()
                                                        : null);
                        if (sustitucion.getIdCausaVacante() == 3 && sustitucion.getTipoCausaVacante() == 3) {
                                sustituido.setDeclinoCargo(null);
                        }
                        sustituido.setFechaHora(new Date());
                        sustituido.setExisteSustitucion(null);
                        sustituido.setUsuario(requestDeshacerSustitucion.getUser());
                        sustituido.setIpUsuario(sustitucion.getIpUsuario());
                        sustituido.setIdSistemaActualiza(Constantes.ID_SISTEMA);
                        repoJPAAspirantes.saveAndFlush(sustituido);

                        if ((idPuestoSustituidoAnt == Constantes.ID_PUESTO_SE_TMP
                                        || idPuestoSustituidoAnt == Constantes.ID_PUESTO_CAE_TMP
                                        || idPuestoSustituidoAnt == Constantes.ID_PUESTO_SE
                                        || idPuestoSustituidoAnt == Constantes.ID_PUESTO_CAE)
                                        && (sustitucion.getIdPuestoSustituido() == Constantes.ID_PUESTO_CAE
                                                        || sustitucion.getIdPuestoSustituido() == Constantes.ID_PUESTO_SE)) {
                                hlpCuentasSustituciones.modificarCuentaSustitucion(sustituido,
                                                requestDeshacerSustitucion.getUser());
                        } else if (!(idPuestoSustituidoAnt == Constantes.ID_PUESTO_SE_TMP
                                        || idPuestoSustituidoAnt == Constantes.ID_PUESTO_CAE_TMP
                                        || idPuestoSustituidoAnt == Constantes.ID_PUESTO_SE
                                        || idPuestoSustituidoAnt == Constantes.ID_PUESTO_CAE)
                                        && (sustitucion.getIdPuestoSustituido() == Constantes.ID_PUESTO_CAE
                                                        || sustitucion.getIdPuestoSustituido() == Constantes.ID_PUESTO_SE)) {
                                hlpCuentasSustituciones.crearCuentaSustitucion(sustituido,
                                                requestDeshacerSustitucion.getUser());

                        } else if ((idPuestoSustituidoAnt == Constantes.ID_PUESTO_SE_TMP
                                        || idPuestoSustituidoAnt == Constantes.ID_PUESTO_CAE_TMP
                                        || idPuestoSustituidoAnt == Constantes.ID_PUESTO_SE
                                        || idPuestoSustituidoAnt == Constantes.ID_PUESTO_CAE)
                                        && !(sustitucion.getIdPuestoSustituido() == Constantes.ID_PUESTO_CAE
                                                        || sustitucion.getIdPuestoSustituido() == Constantes.ID_PUESTO_SE)) {
                                hlpCuentasSustituciones.eliminarCuentaSustitucion(sustituido,
                                                requestDeshacerSustitucion.getUser());
                        }

                } catch (ExceptionValidacionAreZore e) {
                        log.error("Se va perder un puesto: " + sustitucion.getIdSustitucion() + ": ", e);
                        throw new ExceptionValidacionAreZore(e.getMessage());
                } catch (Exception e) {
                        log.error("error al deshacer la sustitucion: " + sustitucion.getIdSustitucion() + ": ", e);
                }

        }

        public void validaAresZores(VOSustitucionesSupycap sustitucion, DTOAspirantes sustituto, Integer etapa)
                        throws ExceptionValidacionAreZore {
                if (sustitucion != null && sustituto != null) {

                        if (etapa == 2) {
                                // >>>>>>SE COMPARAN ZORES
                                if (sustitucion.getIdZonaResponsabilidad2e() != null
                                                && sustituto.getIdZonaResponsabilidad2e() != null) {
                                        if (!sustitucion.getIdZonaResponsabilidad2e()
                                                        .equals(sustituto.getIdZonaResponsabilidad2e())) {
                                                throw new ExceptionValidacionAreZore(
                                                                "Las zores son diferentes entre el aspirante y la sustitucion");
                                        }
                                        // SE COMPARAN ZORES PARA LA PRIMERA ETAPA vs 2E de ASPIRANTES
                                } else if (sustitucion.getIdAZonaResponsabilidad1e() != null
                                                && sustituto.getIdZonaResponsabilidad2e() != null) {// A LO MEJOR
                                                                                                    // NECESITO AGREGAR
                                                                                                    // QUE LA
                                                                                                    // SUSTITUCION SEA
                                                                                                    // EN PRIMERA ETAPA
                                        if (!sustitucion.getIdAZonaResponsabilidad1e()
                                                        .equals(sustituto.getIdZonaResponsabilidad2e())) {
                                                throw new ExceptionValidacionAreZore(
                                                                "Las zores son diferentes entre el aspirante y la sustitucion al cambiar de etapa");
                                        }
                                } else if (sustitucion.getIdAZonaResponsabilidad1e() != null
                                                && sustituto.getIdZonaResponsabilidad1e() != null) {// A LO MEJOR
                                                                                                    // NECESITO AGREGAR
                                                                                                    // QUE LA
                                                                                                    // SUSTITUCION SEA
                                                                                                    // EN PRIMERA ETAPA
                                        if (!sustitucion.getIdAZonaResponsabilidad1e()
                                                        .equals(sustituto.getIdZonaResponsabilidad1e())) {
                                                throw new ExceptionValidacionAreZore(
                                                                "Las zores son diferentes entre el aspirante y la sustitucion para etapa 1");
                                        }
                                }
                                // >>>>>>SE COMPARAN ARES
                                if (sustitucion.getIdAreaResponsabilidad2e() != null
                                                && sustituto.getIdAreaResponsabilidad2e() != null) {
                                        if (!sustitucion.getIdAreaResponsabilidad2e()
                                                        .equals(sustituto.getIdAreaResponsabilidad2e())) {
                                                throw new ExceptionValidacionAreZore(
                                                                "Las ARES son diferentes entre el aspirante y la sustitucion");
                                        }
                                        // SE COMPARAN ZORES PARA LA PRIMERA ETAPA vs 2E de ASPIRANTES
                                } else if (sustitucion.getIdAreaResponsabilidad1e() != null
                                                && sustituto.getIdAreaResponsabilidad2e() != null) {// A LO MEJOR
                                                                                                    // NECESITO AGREGAR
                                                                                                    // QUE LA
                                                                                                    // SUSTITUCION SEA
                                                                                                    // EN PRIMERA ETAPA
                                        if (!sustitucion.getIdAreaResponsabilidad1e()
                                                        .equals(sustituto.getIdAreaResponsabilidad2e())) {
                                                throw new ExceptionValidacionAreZore(
                                                                "Las ARES son diferentes entre el aspirante y la sustitucion al cambiar de etapa");
                                        }
                                } else if (sustitucion.getIdAreaResponsabilidad1e() != null
                                                && sustituto.getIdAreaResponsabilidad1e() != null) {// A LO MEJOR
                                                                                                    // NECESITO AGREGAR
                                                                                                    // QUE LA
                                                                                                    // SUSTITUCION SEA
                                                                                                    // EN PRIMERA ETAPA
                                        if (!sustitucion.getIdAreaResponsabilidad1e()
                                                        .equals(sustituto.getIdAreaResponsabilidad1e())) {
                                                throw new ExceptionValidacionAreZore(
                                                                "Las ARES son diferentes entre el aspirante y la sustitucion para etapa 1");
                                        }
                                }
                        } else {
                                if (sustitucion.getIdAZonaResponsabilidad1e() != null
                                                && sustituto.getIdZonaResponsabilidad1e() != null) {
                                        if (!sustitucion.getIdAZonaResponsabilidad1e()
                                                        .equals(sustituto.getIdZonaResponsabilidad1e())) {
                                                throw new ExceptionValidacionAreZore(
                                                                "Las zores son diferentes entre el aspirante y la sustitucion");
                                        }
                                }
                                if (sustitucion.getIdAreaResponsabilidad1e() != null
                                                && sustituto.getIdAreaResponsabilidad1e() != null) {
                                        if (!sustitucion.getIdAreaResponsabilidad1e()
                                                        .equals(sustituto.getIdAreaResponsabilidad1e())) {
                                                throw new ExceptionValidacionAreZore(
                                                                "Las ARES son diferentes entre el aspirante");
                                        }
                                }
                        }
                }

        }

        public Boolean validaExisteEnSustPosteriores(VOConsultaDesSustitucionesSupycap objetoCompara,
                        VOConsultaDesSustitucionesSupycap objetoAComparar) {
                Boolean exixteEnSustPosterior = Boolean.FALSE;

                if (objetoAComparar == null) {
                        exixteEnSustPosterior = Boolean.FALSE;
                } else {
                        Integer sustituidoObj = objetoCompara.getId_aspirante_sustituido() == null ? Integer
                                        .valueOf(0) : objetoCompara.getId_aspirante_sustituido();
                        Integer sustitutoObj = objetoCompara.getid_aspirante_sustituto() == null ? Integer
                                        .valueOf(0) : objetoCompara.getid_aspirante_sustituto();
                        Integer sustituidoObjNext = objetoAComparar.getId_aspirante_sustituido() == null ? Integer
                                        .valueOf(0) : objetoAComparar.getId_aspirante_sustituido();
                        Integer sustitutoObjNext = objetoAComparar.getid_aspirante_sustituto() == null ? Integer
                                        .valueOf(0) : objetoAComparar.getid_aspirante_sustituto();

                        if (sustituidoObj.equals(0)) {
                                if (!sustitutoObj.equals(0)) {
                                        if (sustitutoObj.equals(sustituidoObjNext)
                                                        || sustitutoObj.equals(sustitutoObjNext)) {
                                                exixteEnSustPosterior = Boolean.TRUE;
                                        }
                                }

                        } else if (!sustituidoObj.equals(0)) {
                                if (sustitutoObj.equals(0)) {
                                        if (sustituidoObj.equals(sustituidoObjNext)
                                                        || sustituidoObj.equals(sustitutoObjNext)) {
                                                exixteEnSustPosterior = Boolean.TRUE;
                                        }
                                } else if (!sustitutoObj.equals(0)) {
                                        if (sustituidoObj.equals(sustituidoObjNext)
                                                        || sustituidoObj.equals(sustitutoObjNext)) {
                                                exixteEnSustPosterior = Boolean.TRUE;
                                        } else if (sustitutoObj.equals(sustituidoObjNext)
                                                        || sustitutoObj.equals(sustitutoObjNext)) {
                                                exixteEnSustPosterior = Boolean.TRUE;
                                        }
                                }
                        }
                }
                return exixteEnSustPosterior;
        }

        public static Date convertStringADate(String fechaStr) {
                // Define el formato del String
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                try {
                        // Convierte el String a LocalDate
                        LocalDate localDate = LocalDate.parse(fechaStr, formatter);

                        // Convierte LocalDate a Date
                        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                } catch (DateTimeParseException e) {
                        System.out.println("Error al convertir la fecha: " + e.getMessage());
                        return null; // Devuelve null en caso de error
                }
        }

}
