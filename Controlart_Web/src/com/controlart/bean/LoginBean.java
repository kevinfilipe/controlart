package com.controlart.bean;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.controlart.bean.utils.BeanUtils;
import com.controlart.dao.UsuariosDao;
import com.controlart.transfer.UsuarioT;

@ManagedBean(name = "loginBean")
@ViewScoped
public class LoginBean extends ControlArtBean {

	private static final long serialVersionUID = 1L;

	private UsuarioT usuario;

	public LoginBean() {
		clearAction();

		if (getSessionAtribute(BeanUtils.LOGGEDUSER_SESSION_ATTRIBUTE) != null) {
			removeSessionAtribute(BeanUtils.LOGGEDUSER_SESSION_ATTRIBUTE);
			invalidateSession();
		}
	}

	private void clearAction() {
		usuario = new UsuarioT();
	}

	public String login() {
		try {
			usuario.setSenha(BeanUtils.encryptPassword(usuario.getSenha()));

			UsuariosDao usuariosDao = new UsuariosDao();

			setUsuarioLogado(usuariosDao.consult(usuario));

			if (getUsuarioLogado() == null) {
				addFacesMessage(getObjectFromBundle("msUsuarioNaoEncontrado"),
						null, BeanUtils.SEVERITY_WARN);

				return null;
			} else {
				addSessionAtribute(BeanUtils.LOGGEDUSER_SESSION_ATTRIBUTE,
						getUsuarioLogado());

				return "/pages/home.xhtml";
			}
		} catch (NoSuchAlgorithmException nsa) {
			nsa.printStackTrace();
			addFacesMessage(getObjectFromBundle("msErroGenerico"), null,
					BeanUtils.SEVERITY_ERROR);
		} catch (SQLException sql) {
			sql.printStackTrace();
			addFacesMessage(getObjectFromBundle("msErroGenerico"), null,
					BeanUtils.SEVERITY_ERROR);
		}

		return null;
	}

	public UsuarioT getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioT usuario) {
		this.usuario = usuario;
	}
}
