package com.controlart.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.controlart.dao.factory.ConnFactory;
import com.controlart.dao.utils.DaoUtils;
import com.controlart.transfer.TipoTransacaoT;

public class TipoTransacaoDao {
	private Connection connection;

	private static final String SQL_CONSULT_ALL_FOR_VIEW = "SELECT ttt.id_tipo_transacao, ttt.nm_tipo_transacao, ttt.in_ativo FROM tb_tipo_transacao ttt ORDER BY ttt.nm_tipo_transacao";

	public TipoTransacaoDao() throws SQLException {
		connection = ConnFactory.getConnection();
	}

	/*
	 * Objetivo: M�todo utilizado para consultar todos os Tipos de Transa��o
	 * (TipoTransacaoT) do sistema.
	 * 
	 * @param
	 * 
	 * @return List<TipoTransacaoT>. Obs: Apenas as informa��es utilizadas por
	 * Converters e Selecitems ser�o retornadas.
	 * 
	 * @throws SQLException.
	 */

	public List<TipoTransacaoT> consultAllForView() throws SQLException {
		PreparedStatement pStmt = null;
		ResultSet rs = null;

		try {
			pStmt = connection.prepareStatement(SQL_CONSULT_ALL_FOR_VIEW);

			rs = pStmt.executeQuery();

			return resultsetToObject(rs);
		} finally {
			DaoUtils.closePreparedStatement(pStmt);
			DaoUtils.closeConnection(connection);
		}
	}

	/*
	 * Objetivo: M�todo utilizado para mapear dados de um ResultSet (Que
	 * armazena resultados de consultas em uma Base de Dados) em informa��es de
	 * Tipo de Transa��o (TipoTransacaoT).
	 * 
	 * @param ResultSet.
	 * 
	 * @return List<TipoTransacaoT>. Obs: Apenas as informa��es utilizadas por
	 * Converters e Selecitems ser�o retornadas.
	 * 
	 * @throws SQLException.
	 */

	private List<TipoTransacaoT> resultsetToObject(ResultSet rs)
			throws SQLException {
		List<TipoTransacaoT> listaTipoTransacaoT = new ArrayList<TipoTransacaoT>(
				0);

		while (rs.next()) {
			TipoTransacaoT tipoTransacaoT = new TipoTransacaoT();

			tipoTransacaoT.setId(rs.getInt("ID_TIPO_TRANSACAO"));
			tipoTransacaoT.setNome(rs.getString("NM_TIPO_TRANSACAO"));
			tipoTransacaoT.setAtivo(rs.getInt("IN_ATIVO"));

			listaTipoTransacaoT.add(tipoTransacaoT);
		}

		return listaTipoTransacaoT;
	}
}