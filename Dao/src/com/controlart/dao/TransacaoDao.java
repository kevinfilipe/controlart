package com.controlart.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.controlart.dao.factory.ConnFactory;
import com.controlart.dao.utils.DaoUtils;
import com.controlart.transfer.TransacaoT;

public class TransacaoDao {
	private Connection connection;

	private static final String SQL_CONSULT_ALL = "SELECT tt.* FROM tb_transacao tt ORDER BY tt.id_peca";
	private static final String SQL_INSERT = "INSERT INTO tb_transacao (id_peca, id_tipo_transacao, id_acervo_origem, id_acervo_destino, dt_entrada, dt_saida, vl_preco, in_online) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

	public TransacaoDao() throws SQLException {
		connection = ConnFactory.getConnection();
	}

	public List<TransacaoT> consultAll() throws SQLException {
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

	private List<TransacaoT> resultsetToObjectT(ResultSet rs)
			throws SQLException {
		List<TransacaoT> listaTransacaoT = new ArrayList<TransacaoT>();

		while (rs.next()) {
			TransacaoT transacaoT = new TransacaoT();

			transacaoT.setId(rs.getInt("ID_TRANSACAO"));
			transacaoT.setPeca(rs.getInt("ID_PECA"));
			transacaoT.setTipo(rs.getInt("ID_TIPO_TRANSACAO"));
			transacaoT.setAcervoOrigem(rs.getInt("ID_ACERVO_ORIGEM"));
			transacaoT.setAcervoDestino(rs.getInt("ID_ACERVO_DESTINO"));
			transacaoT.setDataEntrada(rs.getTimestamp("DT_ENTRADA"));
			transacaoT.setDataSaida(rs.getTimestamp("DT_SAIDA"));
			transacaoT.setPreco(rs.getBigDecimal("VL_PRECO"));
			transacaoT.setOnline(rs.getInt("IN_ONLINE"));

			listaTransacaoT.add(transacaoT);
		}

		return listaTransacaoT;
	}

	public void insert(TransacaoT transacaoT) throws SQLException {
		PreparedStatement pStmt = null;

		try {
			pStmt = connection.prepareStatement(SQL_INSERT);
			pStmt.setObject(1, transacaoT.getPeca());
			pStmt.setObject(2, transacaoT.getTipo());
			pStmt.setObject(3, transacaoT.getAcervoOrigem());
			pStmt.setObject(4, transacaoT.getAcervoDestino());
			pStmt.setObject(5, transacaoT.getDataEntrada() == null ? null
					: new Date(transacaoT.getDataEntrada().getTime()));
			pStmt.setObject(6, transacaoT.getDataSaida() == null ? null
					: new Date(transacaoT.getDataSaida().getTime()));
			pStmt.setObject(7, transacaoT.getPreco());
			pStmt.setObject(8, transacaoT.getOnline());

			pStmt.execute();
		} finally {
			DaoUtils.closePreparedStatement(pStmt);
			DaoUtils.closeConnection(connection);
		}
	}
}