package mx.ine.sustseycae.dto.db;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mx.ine.sustseycae.dto.vo.VOConsultaDesSustitucionesSupycap;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@SqlResultSetMapping(name = "consultaDesSustituciones", classes = {
                @ConstructorResult(targetClass = VOConsultaDesSustitucionesSupycap.class, columns = {
                                @ColumnResult(name = "id_sustitucion", type = Integer.class),
                                @ColumnResult(name = "id_relacion_sustituciones", type = String.class),
                                @ColumnResult(name = "id_causa", type = Integer.class),
                                @ColumnResult(name = "tipo_causa", type = Integer.class),
                                @ColumnResult(name = "id_aspirante_sustituido", type = Integer.class),
                                @ColumnResult(name = "id_aspirante_sustituto", type = Integer.class),
                                @ColumnResult(name = "puesto_sustituido", type = String.class),
                                @ColumnResult(name = "nombre_sustituido", type = String.class),
                                @ColumnResult(name = "causa", type = String.class),
                                @ColumnResult(name = "fecha_baja", type = String.class),
                                @ColumnResult(name = "fecha_alta", type = String.class),
                                @ColumnResult(name = "fecha_sustitucion", type = String.class),
                                @ColumnResult(name = "puesto_sustituto", type = String.class),
                                @ColumnResult(name = "nombre_sustituto", type = String.class),

                })
})

@NamedNativeQuery(name = "DTODesSustitucionesSupycap.consultaDesSustituciones", query = """
                SELECT ID_SUSTITUCION,
                        ID_RELACION_SUSTITUCIONES,
                        ID_CAUSA,
                        TIPO_CAUSA,
                        ID_ASPIRANTE_SUSTITUIDO,
                        ID_ASPIRANTE_SUSTITUTO,
                        PUESTO_SUSTITUIDO,
                        NOMBRE_SUSTITUIDO,
                        CAUSA,
                        FECHA_BAJA,
                        FECHA_ALTA,
                        FECHA_SUSTITUCION,
                        nombre_sustituto,
                        puesto_sustituto
                FROM (SELECT SUS.ID_SUSTITUCION AS ID_SUSTITUCION,
                        SUS.ID_RELACION_SUSTITUCIONES AS ID_RELACION_SUSTITUCIONES,
                        SUS.ID_CAUSA_VACANTE AS ID_CAUSA,
                        SUS.TIPO_CAUSA_VACANTE AS TIPO_CAUSA,
                        SUS.ID_ASPIRANTE_SUSTITUIDO AS ID_ASPIRANTE_SUSTITUIDO,
                        SUS.ID_ASPIRANTE_SUSTITUTO AS ID_ASPIRANTE_SUSTITUTO,
                        pustituido.puesto AS PUESTO_SUSTITUIDO,
                        (CASE WHEN ASP.APELLIDO_PATERNO IS NULL THEN ''
                                WHEN ASP.APELLIDO_PATERNO IS NOT NULL THEN ASP.APELLIDO_PATERNO END)
                        ||' '|| (CASE WHEN ASP.APELLIDO_MATERNO IS NULL THEN ''
                                WHEN ASP.APELLIDO_MATERNO IS NOT NULL THEN ASP.APELLIDO_MATERNO END)
                        ||' '|| (CASE WHEN ASP.NOMBRE IS NULL THEN ''
                                WHEN ASP.NOMBRE IS NOT NULL THEN ASP.NOMBRE END) AS NOMBRE_SUSTITUIDO,
                        CAU.DESCRIPCION AS CAUSA,
                        REPLACE ((TO_CHAR (FECHA_BAJA, 'DD/MM/YYYY')), ' ', '') AS FECHA_BAJA,
                        REPLACE ((TO_CHAR (FECHA_ALTA, 'DD/MM/YYYY')), ' ', '') AS FECHA_ALTA,
                        REPLACE ((TO_CHAR (FECHA_SUSTITUCION, 'DD/MM/YYYY')), ' ', '') AS FECHA_SUSTITUCION,
                        (CASE WHEN ASP_SUST.APELLIDO_PATERNO IS NULL THEN ''
                                WHEN ASP_SUST.APELLIDO_PATERNO IS NOT NULL THEN ASP_SUST.APELLIDO_PATERNO END)
                        ||' '|| (CASE WHEN ASP_SUST.APELLIDO_MATERNO IS NULL THEN ''
                                WHEN ASP_SUST.APELLIDO_MATERNO IS NOT NULL THEN ASP_SUST.APELLIDO_MATERNO END)
                        ||' '|| (CASE WHEN ASP_SUST.NOMBRE IS NULL THEN ''
                                WHEN ASP_SUST.NOMBRE IS NOT NULL THEN ASP_SUST.NOMBRE END) AS nombre_sustituto,
                        pustituto.puesto AS puesto_sustituto,
                        ROW_NUMBER() OVER (PARTITION BY id_relacion_sustituciones
                                        ORDER BY fecha_movimiento, id_sustitucion) AS rn,
                        MAX(fecha_movimiento) OVER (PARTITION BY id_relacion_sustituciones) AS group_key
                FROM SUSTITUCIONES_SUPYCAP SUS
                LEFT OUTER JOIN ASPIRANTES ASP ON(SUS.ID_ASPIRANTE_SUSTITUIDO = ASP.ID_ASPIRANTE
                                                AND SUS.ID_PROCESO_ELECTORAL = ASP.ID_PROCESO_ELECTORAL
                                                AND SUS.ID_DETALLE_PROCESO = ASP.ID_DETALLE_PROCESO
                                                AND SUS.ID_PARTICIPACION = ASP.ID_PARTICIPACION)
                LEFT OUTER JOIN C_CAUSAS_VACANTE CAU ON(SUS.ID_CAUSA_VACANTE = CAU.ID_CAUSA_VACANTE
                                                        AND SUS.TIPO_CAUSA_VACANTE = CAU.TIPO_CAUSA_VACANTE)
                LEFT OUTER JOIN c_puestos pustituido ON (sus.id_puesto_sustituido = pustituido.id_puesto)
                LEFT OUTER JOIN c_puestos pustituto ON (sus.id_puesto_sustituto = pustituto.id_puesto)
                LEFT OUTER JOIN ASPIRANTES ASP_SUST ON(SUS.ID_ASPIRANTE_SUSTITUTO = ASP_SUST.ID_ASPIRANTE
                                                        AND SUS.ID_PROCESO_ELECTORAL = ASP_SUST.ID_PROCESO_ELECTORAL
                                                        AND SUS.ID_DETALLE_PROCESO = ASP_SUST.ID_DETALLE_PROCESO
                                                        AND SUS.ID_PARTICIPACION = ASP_SUST.ID_PARTICIPACION)
                WHERE SUS.ID_PROCESO_ELECTORAL = :idProcesoElectoral
                        AND SUS.ID_DETALLE_PROCESO = :idDetalleProceso
                        AND SUS.ID_PARTICIPACION = :idParticipacion)
                ORDER BY group_key,
                        rn,
                        id_sustitucion""", resultSetMapping = "consultaDesSustituciones")

@NamedNativeQuery(name = "DTODesSustitucionesSupycap.consultaSustitucionesDeshechas", query = """
                SELECT DESUS.ID_SUSTITUCION AS ID_SUSTITUCION,
                        DESUS.ID_RELACION_SUSTITUCIONES AS ID_RELACION_SUSTITUCIONES,
                        DESUS.ID_CAUSA_VACANTE AS ID_CAUSA,
                        DESUS.TIPO_CAUSA_VACANTE TIPO_CAUSA,
                        DESUS.ID_ASPIRANTE_SUSTITUIDO AS ID_ASPIRANTE_SUSTITUIDO,
                        DESUS.ID_ASPIRANTE_SUSTITUTO AS ID_ASPIRANTE_SUSTITUTO,
                        pustituido.puesto AS PUESTO_SUSTITUIDO,
                        (CASE WHEN ASP.APELLIDO_PATERNO IS NULL THEN ''
                                WHEN ASP.APELLIDO_PATERNO IS NOT NULL THEN ASP.APELLIDO_PATERNO END)
                        ||' '|| (CASE WHEN ASP.APELLIDO_MATERNO IS NULL THEN ''
                                WHEN ASP.APELLIDO_MATERNO IS NOT NULL THEN ASP.APELLIDO_MATERNO END)
                        ||' '|| (CASE WHEN ASP.NOMBRE IS NULL THEN ''
                                WHEN ASP.NOMBRE IS NOT NULL THEN ASP.NOMBRE END) AS NOMBRE_SUSTITUIDO,
                        CAU.DESCRIPCION AS CAUSA,
                        REPLACE ((TO_CHAR (FECHA_BAJA, 'DD/MM/YYYY')), ' ', '') AS FECHA_BAJA,
                        REPLACE ((TO_CHAR (FECHA_ALTA, 'DD/MM/YYYY')), ' ', '') AS FECHA_ALTA,
                        REPLACE ((TO_CHAR (FECHA_SUSTITUCION, 'DD/MM/YYYY')), ' ', '') AS FECHA_SUSTITUCION,
                        (CASE WHEN ASP_SUST.APELLIDO_PATERNO IS NULL THEN ''
                                WHEN ASP_SUST.APELLIDO_PATERNO IS NOT NULL THEN ASP_SUST.APELLIDO_PATERNO END)
                        ||' '|| (CASE WHEN ASP_SUST.APELLIDO_MATERNO IS NULL THEN ''
                                WHEN ASP_SUST.APELLIDO_MATERNO IS NOT NULL THEN ASP_SUST.APELLIDO_MATERNO END)
                        ||' '|| (CASE WHEN ASP_SUST.NOMBRE IS NULL THEN ''
                                WHEN ASP_SUST.NOMBRE IS NOT NULL THEN ASP_SUST.NOMBRE END) AS NOMBRE_SUSTITUTO,
                        pustituto.puesto AS PUESTO_SUSTITUTO
                FROM DES_SUSTITUCIONES_SUPYCAP DESUS
                LEFT OUTER JOIN ASPIRANTES ASP ON(DESUS.ID_ASPIRANTE_SUSTITUIDO = ASP.ID_ASPIRANTE
                                                        AND DESUS.ID_PROCESO_ELECTORAL = ASP.ID_PROCESO_ELECTORAL
                                                        AND DESUS.ID_DETALLE_PROCESO = ASP.ID_DETALLE_PROCESO
                                                        AND DESUS.ID_PARTICIPACION = ASP.ID_PARTICIPACION)
                LEFT OUTER JOIN C_CAUSAS_VACANTE CAU ON(DESUS.ID_CAUSA_VACANTE = CAU.ID_CAUSA_VACANTE
                                                        AND DESUS.TIPO_CAUSA_VACANTE = CAU.TIPO_CAUSA_VACANTE)
                LEFT OUTER JOIN ASPIRANTES ASP_SUST ON(DESUS.ID_ASPIRANTE_SUSTITUTO = ASP_SUST.ID_ASPIRANTE
                                                        AND DESUS.ID_PROCESO_ELECTORAL = ASP_SUST.ID_PROCESO_ELECTORAL
                                                        AND DESUS.ID_DETALLE_PROCESO = ASP_SUST.ID_DETALLE_PROCESO
                                                        AND DESUS.ID_PARTICIPACION = ASP_SUST.ID_PARTICIPACION)
                LEFT OUTER JOIN c_puestos pustituido ON (DESUS.id_puesto_sustituido = pustituido.id_puesto)
                LEFT OUTER JOIN c_puestos pustituto ON (DESUS.id_puesto_sustituto = pustituto.id_puesto)
                WHERE DESUS.ID_PROCESO_ELECTORAL = :idProcesoElectoral
                        AND DESUS.ID_DETALLE_PROCESO = :idDetalleProceso
                        AND DESUS.ID_PARTICIPACION = :idParticipacion
                ORDER BY DESUS.ID_DESHACER""", resultSetMapping = "consultaDesSustituciones")

@NamedNativeQuery(name = "DTODesSustitucionesSupycap.consultaDesSustitucionesRelacionadas", query = """
                SELECT SUS.ID_SUSTITUCION AS ID_SUSTITUCION,
                        SUS.ID_RELACION_SUSTITUCIONES AS ID_RELACION_SUSTITUCIONES,
                        SUS.ID_CAUSA_VACANTE AS ID_CAUSA,
                        SUS.TIPO_CAUSA_VACANTE AS TIPO_CAUSA,
                        SUS.ID_ASPIRANTE_SUSTITUIDO AS ID_ASPIRANTE_SUSTITUIDO,
                        SUS.ID_ASPIRANTE_SUSTITUTO AS ID_ASPIRANTE_SUSTITUTO,
                        pustituido.puesto AS PUESTO_SUSTITUIDO,
                        (CASE WHEN ASP.APELLIDO_PATERNO IS NULL THEN ''
                                WHEN ASP.APELLIDO_PATERNO IS NOT NULL THEN ASP.APELLIDO_PATERNO END)
                        ||' '|| (CASE WHEN ASP.APELLIDO_MATERNO IS NULL THEN ''
                                WHEN ASP.APELLIDO_MATERNO IS NOT NULL THEN ASP.APELLIDO_MATERNO END)
                        ||' '|| (CASE WHEN ASP.NOMBRE IS NULL THEN ''
                                WHEN ASP.NOMBRE IS NOT NULL THEN ASP.NOMBRE END) AS NOMBRE_SUSTITUIDO,
                        CAU.DESCRIPCION AS CAUSA,
                        REPLACE ((TO_CHAR (FECHA_BAJA, 'DD/MM/YYYY')), ' ', '') AS FECHA_BAJA,
                        REPLACE ((TO_CHAR (FECHA_ALTA, 'DD/MM/YYYY')),' ', '') AS FECHA_ALTA,
                        REPLACE ((TO_CHAR (FECHA_SUSTITUCION, 'DD/MM/YYYY')),' ', '') AS FECHA_SUSTITUCION,
                        (CASE WHEN ASP_SUST.APELLIDO_PATERNO IS NULL THEN ''
                                WHEN ASP_SUST.APELLIDO_PATERNO IS NOT NULL THEN ASP_SUST.APELLIDO_PATERNO END)
                        ||' '|| (CASE WHEN ASP_SUST.APELLIDO_MATERNO IS NULL THEN ''
                                WHEN ASP_SUST.APELLIDO_MATERNO IS NOT NULL THEN ASP_SUST.APELLIDO_MATERNO END)
                        ||' '|| (CASE WHEN ASP_SUST.NOMBRE IS NULL THEN ''
                                WHEN ASP_SUST.NOMBRE IS NOT NULL THEN ASP_SUST.NOMBRE END) AS nombre_sustituto,
                        pustituto.puesto AS puesto_sustituto
                FROM SUSTITUCIONES_SUPYCAP SUS
                LEFT OUTER JOIN ASPIRANTES ASP ON(SUS.ID_ASPIRANTE_SUSTITUIDO = ASP.ID_ASPIRANTE
                                                AND SUS.ID_PROCESO_ELECTORAL = ASP.ID_PROCESO_ELECTORAL
                                                AND SUS.ID_DETALLE_PROCESO = ASP.ID_DETALLE_PROCESO
                                                AND SUS.ID_PARTICIPACION = ASP.ID_PARTICIPACION)
                LEFT OUTER JOIN C_CAUSAS_VACANTE CAU ON(SUS.ID_CAUSA_VACANTE = CAU.ID_CAUSA_VACANTE
                                                        AND SUS.TIPO_CAUSA_VACANTE = CAU.TIPO_CAUSA_VACANTE)
                LEFT OUTER JOIN c_puestos pustituido on (sus.id_puesto_sustituido = pustituido.id_puesto)
                LEFT OUTER JOIN c_puestos pustituto on (sus.id_puesto_sustituto = pustituto.id_puesto)
                LEFT OUTER JOIN ASPIRANTES ASP_SUST ON(SUS.ID_ASPIRANTE_SUSTITUTO = ASP_SUST.ID_ASPIRANTE
                                                        AND SUS.ID_PROCESO_ELECTORAL = ASP_SUST.ID_PROCESO_ELECTORAL
                                                        AND SUS.ID_DETALLE_PROCESO = ASP_SUST.ID_DETALLE_PROCESO
                                                        AND SUS.ID_PARTICIPACION = ASP_SUST.ID_PARTICIPACION)
                WHERE SUS.ID_PROCESO_ELECTORAL = :idProcesoElectoral
                        AND SUS.ID_DETALLE_PROCESO = :idDetalleProceso
                        AND SUS.ID_PARTICIPACION = :idParticipacion
                        AND sus.id_relacion_sustituciones = :idRelacionSustituciones
                ORDER BY SUS.ID_SUSTITUCION""", resultSetMapping = "consultaDesSustituciones")

@Entity
@Table(schema = "SUPYCAP", name = "DES_SUSTITUCIONES_SUPYCAP")
@Cacheable(true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class DTODesSustitucionesSupycap implements Serializable {

        @Serial
        private static final long serialVersionUID = 1084133946625558098L;

        @EmbeddedId
        private DTODesSustitucionesSupycapId id;

        @NotNull
        @Column(name = "ID_PROCESO_ELECTORAL", nullable = false, precision = 5, scale = 0)
        private Integer idProcesoElectoral;

        @NotNull
        @Column(name = "ID_SUSTITUCION", nullable = false, precision = 5, scale = 0)
        private Integer idSustitucion;

        @NotNull
        @Column(name = "ID_RELACION_SUSTITUCIONES", nullable = false, precision = 36, scale = 0)
        private String idRelacionSustituciones;

        @Column(name = "ID_ASPIRANTE_SUSTITUIDO", nullable = true, precision = 6, scale = 0)
        private Integer idAspiranteSutituido;

        @Column(name = "ID_PUESTO_SUSTITUIDO", nullable = false, precision = 2, scale = 0)
        private Integer idPuestoSustituido;

        @Column(name = "CORREO_CTA_CREADA_SUSTITUIDO", nullable = true, precision = 60, scale = 0)
        private String correoCtaCreadaSustituido;

        @Column(name = "CORREO_CTA_NOTIF_SUSTITUIDO", nullable = true, precision = 60, scale = 0)
        private String correoCtaNotifSustituido;

        @Column(name = "ID_ASPIRANTE_SUSTITUTO", nullable = true, precision = 6, scale = 0)
        private Integer idAspiranteSutituto;

        @Column(name = "ID_PUESTO_SUSTITUTO", nullable = true, precision = 2, scale = 0)
        private Integer idPuestoSustituto;

        @Column(name = "CORREO_CTA_CREADA_SUSTITUTO", nullable = true, precision = 60, scale = 0)
        private String correoCtaCreadaSustituto;

        @Column(name = "CORREO_CTA_NOTIF_SUSTITUTO", nullable = true, precision = 60, scale = 0)
        private String correoCtaNotifSustituto;

        @Column(name = "ID_CAUSA_VACANTE", nullable = false, precision = 2, scale = 0)
        private Integer idCausaVacante;

        @Column(name = "TIPO_CAUSA_VACANTE", nullable = false, precision = 2, scale = 0)
        private Integer tipoCausaVacante;

        @Column(name = "FECHA_BAJA", nullable = true)
        private Date fechaBaja;

        @Column(name = "FECHA_ALTA", nullable = true)
        private Date fechaAlta;

        @Column(name = "FECHA_SUSTITUCION", nullable = true)
        private Date fechaSustitucion;

        @Column(name = "ID_AREA_RESPONSABILIDAD_1E", nullable = true, precision = 5, scale = 0)
        private Integer idAreaResponsabilidad1e;

        @Column(name = "ID_ZONA_RESPONSABILIDAD_1E", nullable = true, precision = 5, scale = 0)
        private Integer idZonaResponsabilidad1e;

        @Column(name = "ID_AREA_RESPONSABILIDAD_2E", nullable = true, precision = 5, scale = 0)
        private Integer idAreaResponsabilidad2e;

        @Column(name = "ID_ZONA_RESPONSABILIDAD_2E", nullable = true, precision = 5, scale = 0)
        private Integer idZonaResponsabilidad2e;

        @Column(name = "UID_CUENTA_SUSTITUTO", nullable = true, precision = 50, scale = 0)
        private String uidCuentaSustituto;

        @Column(name = "UID_CUENTA_SUSTITUIDO", nullable = true, precision = 50, scale = 0)
        private String uidCuentaSustituido;

        @Column(name = "DECLINO_CARGO", nullable = true, precision = 1, scale = 0)
        private Integer declinoCargo;

        @Column(name = "ETAPA", nullable = true, precision = 1, scale = 0)
        private Integer etapa;

        @Column(name = "IP_USUARIO", nullable = false, precision = 15, scale = 0)
        private String ipUsuario;

        @Column(name = "USUARIO", nullable = false, precision = 50, scale = 0)
        private String usuario;

        @Temporal(TemporalType.TIMESTAMP)
        @Column(name = "FECHA_HORA", nullable = false, precision = 5, scale = 0)
        private Date fechaHora;

}
