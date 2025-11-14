package mx.ine.sustseycae.dto.vo;

public class VOConsultaDesSustitucionesSupycap {

    private Integer id_sustitucion;

    private String id_relacion_sustituciones;

    private Integer id_causa;

    private Integer tipo_causa;

    private Integer id_aspirante_sustituido;

    private Integer id_aspirante_sustituto;

    private String puesto_sustituido;

    private String nombre_sustituido;

    private String causa;

    private String fecha_baja;

    private String fecha_alta;

    private String fecha_sustitucion;

    private String puesto_sustituto;

    private String nombre_sustituto;

    private Boolean existEnSustPosteriores = false;

    public Integer getId_sustitucion() {
        return id_sustitucion;
    }

    public void setId_sustitucion(Integer id_sustitucion) {
        this.id_sustitucion = id_sustitucion;
    }

    public String getId_relacion_sustituciones() {
        return id_relacion_sustituciones;
    }

    public void setId_relacion_sustituciones(String id_relacion_sustituciones) {
        this.id_relacion_sustituciones = id_relacion_sustituciones;
    }

    public Integer getId_causa() {
        return id_causa;
    }

    public void setId_causa(Integer id_causa) {
        this.id_causa = id_causa;
    }

    public Integer getTipo_causa() {
        return tipo_causa;
    }

    public void setTipo_causa(Integer tipo_causa) {
        this.tipo_causa = tipo_causa;
    }

    public Integer getId_aspirante_sustituido() {
        return id_aspirante_sustituido;
    }

    public void setId_aspirante_sustituido(Integer id_aspirante_sustituido) {
        this.id_aspirante_sustituido = id_aspirante_sustituido;
    }

    public Integer getid_aspirante_sustituto() {
        return id_aspirante_sustituto;
    }

    public void setid_aspirante_sustituto(Integer id_aspirante_sustituto) {
        this.id_aspirante_sustituto = id_aspirante_sustituto;
    }

    public String getPuesto_sustituido() {
        return puesto_sustituido;
    }

    public void setPuesto_sustituido(String puesto_sustituido) {
        this.puesto_sustituido = puesto_sustituido;
    }

    public String getNombre_sustituido() {
        return nombre_sustituido;
    }

    public void setNombre_sustituido(String nombre_sustituido) {
        this.nombre_sustituido = nombre_sustituido;
    }

    public String getCausa() {
        return causa;
    }

    public void setCausa(String causa) {
        this.causa = causa;
    }

    public String getFecha_baja() {
        return fecha_baja;
    }

    public void setFecha_baja(String fecha_baja) {
        this.fecha_baja = fecha_baja;
    }

    public String getFecha_alta() {
        return fecha_alta;
    }

    public void setFecha_alta(String fecha_alta) {
        this.fecha_alta = fecha_alta;
    }

    public String getFecha_sustitucion() {
        return fecha_sustitucion;
    }

    public void setFecha_sustitucion(String fecha_sustitucion) {
        this.fecha_sustitucion = fecha_sustitucion;
    }

    public String getPuesto_sustituto() {
        return puesto_sustituto;
    }

    public void setPuesto_sustituto(String puesto_sustituto) {
        this.puesto_sustituto = puesto_sustituto;
    }

    public String getNombre_sustituto() {
        return nombre_sustituto;
    }

    public void setNombre_sustituto(String nombre_sustituto) {
        this.nombre_sustituto = nombre_sustituto;
    }

    public Boolean getExistEnSustPosteriores() {
        return existEnSustPosteriores;
    }

    public void setExistEnSustPosteriores(Boolean existEnSustPosteriores) {
        this.existEnSustPosteriores = existEnSustPosteriores;
    }
}
