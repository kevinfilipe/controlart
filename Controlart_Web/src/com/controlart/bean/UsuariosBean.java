package com.controlart.bean;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import com.controlart.bean.interfac.ControlArtBeanInterface;
import com.controlart.bean.utils.BeanUtils;
import com.controlart.dao.PessoaDao;
import com.controlart.dao.TipoUsuarioDao;
import com.controlart.dao.UsuariosDao;
import com.controlart.transfer.PessoaT;
import com.controlart.transfer.TipoUsuarioT;
import com.controlart.transfer.UsuarioT;

@ManagedBean(name = "usuariosBean")
@ViewScoped
public class UsuariosBean extends ControlArtBean implements
		ControlArtBeanInterface {

	private static final long 			serialVersionUID = 1L;

	private UsuarioT 					usuario;
	private List<UsuarioT> 				listUsuarios;
	private boolean 					novoRegistro;

	private List<SelectItem> 			listTipoUsuario;
	private List<SelectItem> 			listPessoas;

	private HashMap<Integer, String> 	hashTipoUsuario;
	private HashMap<Integer, String> 	hashPessoas;

	public UsuariosBean() {
		listUsuarios = new ArrayList<UsuarioT>(0);

		clearAction();
		consultAction();
		consultTipoUsuario();
		consultPessoas();
	}

	@Override
	public void clearAction() {
		usuario = new UsuarioT();
	}

	@Override
	public void consultAction() {
		try {
			UsuariosDao usuariosDao = new UsuariosDao();

			listUsuarios = usuariosDao.consultAll();
		} catch (SQLException sql) {
			sql.printStackTrace();
		}
	}

	private void consultTipoUsuario() {
		try {
			TipoUsuarioDao tipoUsuarioDao = new TipoUsuarioDao();

			List<TipoUsuarioT> _listTipoUsuarioT = tipoUsuarioDao.consultAll();

			listTipoUsuario = new ArrayList<SelectItem>(0);
			hashTipoUsuario = new HashMap<Integer, String>(0);

			for (TipoUsuarioT tipoUsuarioT : _listTipoUsuarioT) {
				if (tipoUsuarioT.getSituacao() == 1) {
					listTipoUsuario.add(new SelectItem(tipoUsuarioT
							.getIdTipoUsuario(), tipoUsuarioT
							.getNmTipoUsuario()));
				}

				hashTipoUsuario.put(tipoUsuarioT.getIdTipoUsuario(),
						tipoUsuarioT.getNmTipoUsuario());
			}
		} catch (SQLException sql) {
			sql.printStackTrace();
		}
	}

	private void consultPessoas() {
		try {
			PessoaDao pessoaDao = new PessoaDao();

			List<PessoaT> _listPessoaT = pessoaDao.consultAll();

			hashPessoas = new HashMap<Integer, String>(0);

			for (PessoaT pessoaT : _listPessoaT) {
				hashPessoas.put(pessoaT.getIdPessoa(), pessoaT.getNmPessoa());
			}
		} catch (SQLException sql) {
			sql.printStackTrace();
		}
	}

	private void consultPessoasSemUsuario() {
		try {
			PessoaDao pessoaDao = new PessoaDao();

			List<PessoaT> _listPessoas = pessoaDao.consultAllWithoutUser();

			listPessoas = new ArrayList<SelectItem>(0);

			for (PessoaT pessoaT : _listPessoas) {
				listPessoas.add(new SelectItem(pessoaT.getIdPessoa(), pessoaT
						.getNmPessoa()));
			}
		} catch (SQLException sql) {
			sql.printStackTrace();
		}
	}

	private void consultPessoasComUsuario() {
		try {
			PessoaDao pessoaDao = new PessoaDao();

			List<PessoaT> _listPessoas = pessoaDao.consultAllWithUser();

			listPessoas = new ArrayList<SelectItem>(0);

			for (PessoaT pessoaT : _listPessoas) {
				listPessoas.add(new SelectItem(pessoaT.getIdPessoa(), pessoaT
						.getNmPessoa()));
			}
		} catch (SQLException sql) {
			sql.printStackTrace();
		}
	}

	@Override
	public void definirNovo() {
		novoRegistro = true;

		clearAction();

		usuario.setSituacao(1);

		consultPessoasSemUsuario();
	}

	@Override
	public void definirEditar() {
		novoRegistro = false;

		try {
			usuario = (UsuarioT) ((UsuarioT) getFacesObject("listaUsuarios"))
					.clone();
		} catch (CloneNotSupportedException cns) {
			cns.printStackTrace();
		}

		consultPessoasComUsuario();
	}

	@Override
	public void saveAction() {
		if (novoRegistro) {
			insertAction();
		} else {
			updateAction();
		}
	}

	@Override
	public void insertAction() {
		try {
			if (senhaConfirmada()) {
				UsuariosDao usuariosDao = new UsuariosDao();
				usuariosDao.insert(usuario);

				definirNovo();
				consultAction();

				addFacesMessage(getObjectFromBundle("msRegistroInserido"),
						null, BeanUtils.SEVERITY_INFO);
			} else {
				addFacesMessage(
						getObjectFromBundle("msNovaSenhaConfirmDiferem"), null,
						BeanUtils.SEVERITY_ERROR);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateAction() {
		try {
			if (senhaConfirmada()) {
				UsuariosDao usuariosDao = new UsuariosDao();
				usuariosDao.update(usuario);

				if (usuario.getIdUsuario() == getUsuarioLogado().getIdUsuario()) {
					getUsuarioLogado().setCdSenha(usuario.getCdSenha());
				}

				consultAction();

				addFacesMessage(getObjectFromBundle("msRegistroAtualizado"),
						null, BeanUtils.SEVERITY_INFO);
			} else {
				addFacesMessage(
						getObjectFromBundle("msNovaSenhaConfirmDiferem"), null,
						BeanUtils.SEVERITY_ERROR);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteAction() {
		usuario = (UsuarioT) getFacesObject("listaUsuarios");

		try {
			UsuariosDao usuariosDao = new UsuariosDao();
			usuariosDao.delete(usuario);

			consultAction();

			addFacesMessage(getObjectFromBundle("msRegistroRemovido"), null,
					BeanUtils.SEVERITY_INFO);
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
		}
	}

	private boolean senhaConfirmada() {
		try {
			String novaSenha = BeanUtils.encryptPassword(usuario
					.getCdNovaSenha());
			String confirmNovaSenha = BeanUtils.encryptPassword(usuario
					.getCdConfirmNovaSenha());

			if (novaSenha.equals(confirmNovaSenha)) {
				usuario.setCdSenha(novaSenha);

				return true;
			}
		} catch (NoSuchAlgorithmException nsa) {
			nsa.printStackTrace();
		}

		return false;
	}

	public String getTipoUsuario(int key) {
		return hashTipoUsuario.get(key);
	}

	public String getPessoa(int key) {
		return hashPessoas.get(key);
	}

	public UsuarioT getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioT usuario) {
		this.usuario = usuario;
	}

	public List<UsuarioT> getListUsuarios() {
		return listUsuarios;
	}

	public void setListUsuarios(List<UsuarioT> listUsuarios) {
		this.listUsuarios = listUsuarios;
	}

	public boolean isNovoRegistro() {
		return novoRegistro;
	}

	public void setNovoRegistro(boolean novoRegistro) {
		this.novoRegistro = novoRegistro;
	}

	public List<SelectItem> getListTipoUsuario() {
		return listTipoUsuario;
	}

	public void setListTipoUsuario(List<SelectItem> listTipoUsuario) {
		this.listTipoUsuario = listTipoUsuario;
	}

	public List<SelectItem> getListPessoas() {
		return listPessoas;
	}

	public void setListPessoas(List<SelectItem> listPessoas) {
		this.listPessoas = listPessoas;
	}

}