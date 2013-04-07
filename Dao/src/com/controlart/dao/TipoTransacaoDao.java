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

	private static final String SQL_CONSULT_ALL = "SELECT ttt.* FROM tb_tipo_transacao ttt ORDER BY ttt.nm_tipo_transacao";
	private static final String SQL_CONSULT_TIPO_TRANSACAO_COM_DEVOLUCAO = "SELECT ttt.* FROM tb_tipo_transacao ttt WHERE ttt.id_tipo_transacao IN (3,5) ORDER BY ttt.nm_tipo_transacao";
	private static final String SQL_CONSULT_TIPO_TRANSACAO_ENTRADA = "SELECT ttt.* FROM tb_tipo_transacao ttt WHERE ttt.tp_operacao='E' ORDER BY ttt.nm_tipo_transacao";

	public TipoTransacaoDao() throws SQLException {
		connection = ConnFactory.getConnection();
	}

	public List<TipoTransacaoT> consultAll() throws SQLException {
		PreparedStatement pStmt = null;
		ResultSet rs = null;

		try {
			pStmt = connection.prepareStatement(SQL_CONSULT_ALL);

			rs = pStmt.executeQuery();

			return resultsetToObjectT(rs);
		} finally {
			DaoUtils.closePreparedStatement(pStmt);
			DaoUtils.closeConnection(connection);
		}
	}

	public List<TipoTransacaoT> consultTipoTransacaoComDevolucao() throws SQLException {
		PreparedStatement pStmt = null;
		ResultSet rs = null;

		try {
			pStmt = connection.prepareStatement(SQL_CONSULT_TIPO_TRANSACAO_COM_DEVOLUCAO);

			rs = pStmt.executeQuery();

			return resultsetToObjectT(rs);
		} finally {
			DaoUtils.closePreparedStatement(pStmt);
			DaoUtils.closeConnection(connection);
		}
	}

	public List<TipoTransacaoT> consultTipoTransacaoEntrada() throws SQLException {
		PreparedStatement pStmt = null;
		ResultSet rs = null;

		try {
			pStmt = connection.prepareStatement(SQL_CONSULT_TIPO_TRANSACAO_ENTRADA);

			rs = pStmt.executeQuery();

			return resultsetToObjectT(rs);
		} finally {
			DaoUtils.closePreparedStatement(pStmt);
			DaoUtils.closeConnection(connection);
		}
	}

	private List<TipoTransacaoT> resultsetToObjectT(ResultSet rs)
			throws SQLException {
		List<TipoTransacaoT> listaTipoTransacaoT = new ArrayList<TipoTransacaoT>(0);

		while (rs.next()) {
			TipoTransacaoT tipoTransacaoT = new TipoTransacaoT();

			tipoTransacaoT.setId(rs.getInt("ID_TIPO_TRANSACAO"));
			tipoTransacaoT.setNome(rs.getString("NM_TIPO_TRANSACAO"));
			tipoTransacaoT.setOperacao(rs.getString("TP_OPERACAO").charAt(0));
			tipoTransacaoT.setDescricao(rs.getString("DS_TIPO_TRANSACAO"));
			tipoTransacaoT.setAtivo(rs.getInt("IN_ATIVO"));

			listaTipoTransacaoT.add(tipoTransacaoT);
		}

		return listaTipoTransacaoT;
	}
}
