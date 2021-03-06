package com.controlart.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.controlart.dao.factory.ConnFactory;
import com.controlart.dao.utils.DaoUtils;
import com.controlart.transfer.TipoPessoaT;

public class TipoPessoaDao {
	private Connection connection;

	private static final String SQL_CONSULT_ALL_FOR_VIEW = "SELECT ttp.id_tipo_pessoa, ttp.nm_tipo_pessoa, ttp.in_ativo FROM tb_tipo_pessoa ttp ORDER BY ttp.nm_tipo_pessoa";
	private static final String SQL_CONSULT_ALL = "SELECT ttp.* FROM tb_tipo_pessoa ttp ORDER BY ttp.nm_tipo_pessoa";
	private static final String SQL_INSERT = "INSERT INTO tb_tipo_pessoa (nm_tipo_pessoa, ds_tipo_pessoa, in_ativo) VALUES (?, ?, ?)";
	private static final String SQL_UPDATE = "UPDATE tb_tipo_pessoa SET nm_tipo_pessoa = ?, ds_tipo_pessoa = ?, in_ativo = ? WHERE id_tipo_pessoa = ?";
	private static final String SQL_INACTIVATE = "UPDATE tb_tipo_pessoa SET in_ativo = 0 WHERE id_tipo_pessoa = ?";

	public TipoPessoaDao() throws SQLException {
		connection = ConnFactory.getConnection();
	}

	/*
	 * Objetivo: M�todo utilizado para consultar todos os Tipos de Pessoa
	 * (TipoPessoaT) do sistema.
	 * 
	 * @param
	 * 
	 * @return List<TipoPessoaT>. Obs: Apenas as informa��es utilizadas por
	 * Converters e SelecItems ser�o retornadas.
	 * 
	 * @throws SQLException.
	 */

	public List<TipoPessoaT> consultAllForView() throws SQLException {
		PreparedStatement pStmt = null;
		ResultSet rs = null;

		try {
			pStmt = connection.prepareStatement(SQL_CONSULT_ALL_FOR_VIEW);

			rs = pStmt.executeQuery();

			return resultsetToObjectA(rs);
		} finally {
			DaoUtils.closeStatementAndResultSet(pStmt, rs);
			DaoUtils.closeConnection(connection);
		}
	}

	/*
	 * Objetivo: M�todo utilizado para consultar todos os Tipos de Pessoa
	 * (TipoPessoaT) do sistema.
	 * 
	 * @param
	 * 
	 * @return List<TipoPessoaT>. Obs: Todas as informa��es ser�o retornadas.
	 * 
	 * @throws SQLException.
	 */

	public List<TipoPessoaT> consultAll() throws SQLException {
		PreparedStatement pStmt = null;
		ResultSet rs = null;

		try {
			pStmt = connection.prepareStatement(SQL_CONSULT_ALL);

			rs = pStmt.executeQuery();

			return resultsetToObjectB(rs);
		} finally {
			DaoUtils.closeStatementAndResultSet(pStmt, rs);
			DaoUtils.closeConnection(connection);
		}
	}

	/*
	 * Objetivo: M�todo utilizado para mapear dados de um ResultSet (Que
	 * armazena resultados de consultas em uma Base de Dados) em informa��es de
	 * Tipos de Pessoa (TipoPessoaT).
	 * 
	 * @param ResultSet.
	 * 
	 * @return List<TipoPessoaT>. Obs: Apenas as informa��es utilizadas por
	 * Converters e SelecItems ser�o retornadas.
	 * 
	 * @throws SQLException.
	 */

	private List<TipoPessoaT> resultsetToObjectA(ResultSet rs)
			throws SQLException {
		List<TipoPessoaT> listaTipoPessoaT = new ArrayList<TipoPessoaT>();

		while (rs.next()) {
			TipoPessoaT tipoPessoaT = new TipoPessoaT();

			tipoPessoaT.setId(rs.getInt("ID_TIPO_PESSOA"));
			tipoPessoaT.setNome(rs.getString("NM_TIPO_PESSOA"));
			tipoPessoaT.setAtivo(rs.getInt("IN_ATIVO"));

			listaTipoPessoaT.add(tipoPessoaT);
		}

		return listaTipoPessoaT;
	}

	/*
	 * Objetivo: M�todo utilizado para mapear dados de um ResultSet (Que
	 * armazena resultados de consultas em uma Base de Dados) em informa��es de
	 * Tipos de Pessoa (TipoPessoaT).
	 * 
	 * @param ResultSet.
	 * 
	 * @return List<TipoPessoaT>. Obs: Todas as informa��es ser�o retornadas.
	 * 
	 * @throws SQLException.
	 */

	private List<TipoPessoaT> resultsetToObjectB(ResultSet rs)
			throws SQLException {
		List<TipoPessoaT> listaTipoPessoaT = new ArrayList<TipoPessoaT>();

		while (rs.next()) {
			TipoPessoaT tipoPessoaT = new TipoPessoaT();

			tipoPessoaT.setId(rs.getInt("ID_TIPO_PESSOA"));
			tipoPessoaT.setNome(rs.getString("NM_TIPO_PESSOA"));
			tipoPessoaT.setDescricao(rs.getString("DS_TIPO_PESSOA"));
			tipoPessoaT.setAtivo(rs.getInt("IN_ATIVO"));

			listaTipoPessoaT.add(tipoPessoaT);
		}

		return listaTipoPessoaT;
	}

	public void insert(TipoPessoaT tipoPessoaT) throws SQLException {
		PreparedStatement pStmt = null;

		try {
			pStmt = connection.prepareStatement(SQL_INSERT);
			pStmt.setObject(1, tipoPessoaT.getNome());
			pStmt.setObject(2, tipoPessoaT.getDescricao());
			pStmt.setObject(3, tipoPessoaT.getAtivo());

			pStmt.execute();
		} finally {
			DaoUtils.closePreparedStatement(pStmt);
			DaoUtils.closeConnection(connection);
		}
	}

	public void update(TipoPessoaT tipoPessoaT) throws SQLException {
		PreparedStatement pStmt = null;

		try {
			pStmt = connection.prepareStatement(SQL_UPDATE);
			pStmt.setObject(1, tipoPessoaT.getNome());
			pStmt.setObject(2, tipoPessoaT.getDescricao());
			pStmt.setObject(3, tipoPessoaT.getAtivo());
			pStmt.setObject(4, tipoPessoaT.getId());

			pStmt.executeUpdate();
		} finally {
			DaoUtils.closePreparedStatement(pStmt);
			DaoUtils.closeConnection(connection);
		}
	}

	public void inactivate(TipoPessoaT tipoPessoaT) throws SQLException {
		PreparedStatement pStmt = null;

		try {
			pStmt = connection.prepareStatement(SQL_INACTIVATE);
			pStmt.setObject(1, tipoPessoaT.getId());

			pStmt.executeUpdate();
		} finally {
			DaoUtils.closePreparedStatement(pStmt);
			DaoUtils.closeConnection(connection);
		}
	}
}
