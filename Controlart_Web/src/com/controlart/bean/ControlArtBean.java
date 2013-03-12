package com.controlart.bean;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.controlart.bean.utils.BeanUtils;
import com.controlart.bundle.ControlArtBundle;
import com.controlart.transfer.UsuarioT;

public class ControlArtBean implements Serializable {

	private UsuarioT usuarioLogado;

	private static final long serialVersionUID = 1L;

	public ControlArtBean() {
		usuarioLogado = (UsuarioT) getSessionAtribute(BeanUtils.LOGGEDUSER_SESSION_ATTRIBUTE);
	}

	protected final void addFacesMessage(String message, String detail, int type) {
		FacesContext ctx = FacesContext.getCurrentInstance();

		switch (type) {
		case 1: {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					message, detail));
			break;
		}
		case 2: {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,
					message, detail));
			break;
		}
		case 3: {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					message, detail));
			break;
		}
		case 4: {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
					message, detail));
			break;
		}
		}
	}

	@SuppressWarnings("unchecked")
	protected final <T> T getFacesObject(String key) {
		return (T) FacesContext.getCurrentInstance().getExternalContext()
				.getRequestMap().get(key);
	}

	protected final void addSessionAtribute(String key, Object value) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.put(key, value);
	}

	protected final Object getSessionAtribute(String key) {
		return FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().get(key);
	}

	protected final void removeSessionAtribute(String key) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.remove(key);
	}

	protected final void invalidateSession() {
		FacesContext.getCurrentInstance().getExternalContext()
				.invalidateSession();
	}

	protected final String getObjectFromBundle(String key) {
		ControlArtBundle text = new ControlArtBundle();

		return text.getString(key);
	}

	protected final UsuarioT getUsuarioLogado() {
		return usuarioLogado;
	}

	protected final void setUsuarioLogado(UsuarioT usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}
}