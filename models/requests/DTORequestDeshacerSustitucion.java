package mx.ine.sustseycae.models.requests;

import mx.ine.sustseycae.dto.vo.VOConsultaDesSustitucionesSupycap;

public class DTORequestDeshacerSustitucion extends DTORequestConsultaDeshacerSustituciones{
    private VOConsultaDesSustitucionesSupycap sustitucionADeshacer;
    private String user;
    private String ipUsuario;

    public VOConsultaDesSustitucionesSupycap getSustitucionADeshacer() {
        return sustitucionADeshacer;
    }

    public void setSustitucionADeshacer(VOConsultaDesSustitucionesSupycap sustitucionADeshacer) {
        this.sustitucionADeshacer = sustitucionADeshacer;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getIpUsuario() {
        return ipUsuario;
    }

    public void setIpUsuario(String ipUsuario) {
        this.ipUsuario = ipUsuario;
    }
}
