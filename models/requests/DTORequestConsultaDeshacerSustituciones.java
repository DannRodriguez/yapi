package mx.ine.sustseycae.models.requests;

public class DTORequestConsultaDeshacerSustituciones {

    private Integer idProcesoElectoral;
    private Integer idDetalleProceso;
    private Integer idParticipacion;

    public Integer getIdProcesoElectoral() {
        return idProcesoElectoral;
    }

    public void setIdProcesoElectoral(Integer idProcesoElectoral) {
        this.idProcesoElectoral = idProcesoElectoral;
    }

    public Integer getIdDetalleProceso() {
        return idDetalleProceso;
    }

    public void setIdDetalleProceso(Integer idDetalleProceso) {
        this.idDetalleProceso = idDetalleProceso;
    }

    public Integer getIdParticipacion() {
        return idParticipacion;
    }

    public void setIdParticipacion(Integer idParticipacion) {
        this.idParticipacion = idParticipacion;
    }
}
