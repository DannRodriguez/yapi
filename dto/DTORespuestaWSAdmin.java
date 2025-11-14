package mx.ine.sustseycae.dto;

import java.util.List;

public class DTORespuestaWSAdmin {
	
	private Integer codigoRespuesta;
	private String descripcionRespuesta;
	private Integer codigoError;
	private String descripcionError;
	private String detalle;
	private List<Usuario> usuarios;
	private List<ParametrosLdap> parametrosLdap;
	private List<Atributo> listUids;
	private List<String> cuentasProcesadas;
    
    public class Usuario {
		private String dn;
		private String uid;
		private String password;
		private List<Atributo> atributos;
		private List<Atributo> attrCtaLdapExt;
		private List<String> roles;
		private List<DTOCPermisosCta> permisosAsignados;
		private Integer codigoError;
		private Integer accionError;
		private String mensajeError;
		
		public String getDn() {
			return dn;
		}
		
		public void setDn(String dn) {
			this.dn = dn;
		}
		
		public String getUid() {
			return uid;
		}
		
		public void setUid(String uid) {
			this.uid = uid;
		}
		
		public String getPassword() {
			return password;
		}
		
		public void setPassword(String password) {
			this.password = password;
		}
		
		public List<Atributo> getAtributos() {
			return atributos;
		}
		
		public void setAtributos(List<Atributo> atributos) {
			this.atributos = atributos;
		}
		
		public List<Atributo> getAttrCtaLdapExt() {
			return attrCtaLdapExt;
		}
		
		public void setAttrCtaLdapExt(List<Atributo> attrCtaLdapExt) {
			this.attrCtaLdapExt = attrCtaLdapExt;
		}
		
		public List<String> getRoles() {
			return roles;
		}
		
		public void setRoles(List<String> roles) {
			this.roles = roles;
		}

		public List<DTOCPermisosCta> getPermisosAsignados() {
			return permisosAsignados;
		}

		public void setPermisosAsignados(List<DTOCPermisosCta> permisosAsignados) {
			this.permisosAsignados = permisosAsignados;
		}

		public Integer getCodigoError() {
			return codigoError;
		}

		public void setCodigoError(Integer codigoError) {
			this.codigoError = codigoError;
		}

		public Integer getAccionError() {
			return accionError;
		}

		public void setAccionError(Integer accionError) {
			this.accionError = accionError;
		}

		public String getMensajeError() {
			return mensajeError;
		}

		public void setMensajeError(String mensajeError) {
			this.mensajeError = mensajeError;
		}

		@Override
		public String toString() {
			return "Usuario [dn=" + dn + ", uid=" + uid + ", password=" + password + ", atributos=" + atributos
					+ ", attrCtaLdapExt=" + attrCtaLdapExt + ", roles=" + roles + ", permisosAsignados="
					+ permisosAsignados + ", codigoError=" + codigoError + ", accionError=" + accionError
					+ ", mensajeError=" + mensajeError + "]";
		}
		
	}
    
    public class ParametrosLdap {
    	private String alias;
    	private String base;
    	private String ldapUserDn;
    	private String ldapPass;
    	private Integer grupoLdap;
    	
		public String getAlias() {
			return alias;
		}
		
		public void setAlias(String alias) {
			this.alias = alias;
		}
		
		public String getBase() {
			return base;
		}
		
		public void setBase(String base) {
			this.base = base;
		}
		
		public String getLdapUserDn() {
			return ldapUserDn;
		}
		
		public void setLdapUserDn(String ldapUserDn) {
			this.ldapUserDn = ldapUserDn;
		}
		
		public String getLdapPass() {
			return ldapPass;
		}
		
		public void setLdapPass(String ldapPass) {
			this.ldapPass = ldapPass;
		}
		
		public Integer getGrupoLdap() {
			return grupoLdap;
		}
		
		public void setGrupoLdap(Integer grupoLdap) {
			this.grupoLdap = grupoLdap;
		}
		
		@Override
		public String toString() {
			return "ParametrosLdap [alias=" + alias + ", base=" + base + ", ldapUserDn=" + ldapUserDn + ", ldapPass="
					+ ldapPass + ", grupoLdap=" + grupoLdap + "]";
		}
    	
    }
    
    public class Atributo {
		private String nombre;
		private List<String> valores;
		
		public String getNombre() {
			return nombre;
		}
		
		public void setNombre(String nombre) {
			this.nombre = nombre;
		}
		
		public List<String> getValores() {
			return valores;
		}
		
		public void setValores(List<String> valores) {
			this.valores = valores;
		}
		
		@Override
		public String toString() {
			return "Atributo [nombre=" + nombre + ", valores=" + valores + "]";
		}
		
	}

	public Integer getCodigoRespuesta() {
		return codigoRespuesta;
	}

	public void setCodigoRespuesta(Integer codigoRespuesta) {
		this.codigoRespuesta = codigoRespuesta;
	}

	public String getDescripcionRespuesta() {
		return descripcionRespuesta;
	}

	public void setDescripcionRespuesta(String descripcionRespuesta) {
		this.descripcionRespuesta = descripcionRespuesta;
	}

	public Integer getCodigoError() {
		return codigoError;
	}

	public void setCodigoError(Integer codigoError) {
		this.codigoError = codigoError;
	}

	public String getDescripcionError() {
		return descripcionError;
	}

	public void setDescripcionError(String descripcionError) {
		this.descripcionError = descripcionError;
	}

	public String getDetalle() {
		return detalle;
	}

	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public List<ParametrosLdap> getParametrosLdap() {
		return parametrosLdap;
	}

	public void setParametrosLdap(List<ParametrosLdap> parametrosLdap) {
		this.parametrosLdap = parametrosLdap;
	}

	public List<Atributo> getListUids() {
		return listUids;
	}

	public void setListUids(List<Atributo> listUids) {
		this.listUids = listUids;
	}

	public List<String> getCuentasProcesadas() {
		return cuentasProcesadas;
	}

	public void setCuentasProcesadas(List<String> cuentasProcesadas) {
		this.cuentasProcesadas = cuentasProcesadas;
	}

	@Override
	public String toString() {
		return "DTORespuestaWSAdmin [codigoRespuesta=" + codigoRespuesta + ", descripcionRespuesta="
				+ descripcionRespuesta + ", codigoError=" + codigoError + ", descripcionError=" + descripcionError
				+ ", detalle=" + detalle + ", usuarios=" + usuarios + ", parametrosLdap=" + parametrosLdap
				+ ", listUids=" + listUids + ", cuentasProcesadas=" + cuentasProcesadas + "]";
	}
}