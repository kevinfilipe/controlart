package com.controlart.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.controlart.dao.factory.ConnFactory;
import com.controlart.dao.utils.DaoUtils;
import com.controlart.transfer.ClassificacaoT;

public class ClassificacaoDao {
	private Connection connection;

	private static final String SQL_CONSULT_ALL = "SELECT tc.* FROM tb_classificacao tc ORDER BY tc.nm_classificacao";
	private static final String SQL_INSERT = "INSERT INTO tb_classificacao (nm_classificacao, ds_classificacao, in_ativo) VALUES (?, ?, ?)";
	private static final String SQL_UPDATE = "UPDATE tb_classificacao SET nm_classificacao = ?, ds_classificacao = ?, in_ativo = ? WHERE id_classificacao = ?";
	private static final String SQL_INACTIVATE = "UPDATE tb_classificacao SET in_ativo = 0 WHERE id_classificacao = ?";

	public ClassificacaoDao() throws SQLException {
		connection = ConnFactory.getConnection();
	}

	public List<ClassificacaoT> consultAll() throws SQLException {
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

	private List<ClassificacaoT> resultsetToObjectT(ResultSet rs)
			throws SQLException {
		List<ClassificacaoT> listaClassificacaoT = new ArrayList<ClassificacaoT>();

		while (rs.next()) {
			ClassificacaoT classificacaoT = new ClassificacaoT();

			classificacaoT.setId(rs.getInt("ID_CLASSIFICACAO"));
			classificacaoT.setNome(rs.getString("NM_CLASSIFICACAO"));
			classificacaoT.setDescricao(rs.getString("DS_CLASSIFICACAO"));
			classificacaoT.setAtivo(rs.getInt("IN_ATIVO"));

			listaClassificacaoT.add(classificacaoT);
		}

		return listaClassificacaoT;
	}

	public void insert(ClassificacaoT classificacaoT) throws SQLException {
		PreparedStatement pStmt = null;

		try {
			pStmt = connection.prepareStatement(SQL_INSERT);
			pStmt.setObject(1, classificacaoT.getNome());
			pStmt.setObject(2, classificacaoT.getDescricao());
			pStmt.setObject(3, classificacaoT.getAtivo());

			pStmt.execute();
		} finally {
			DaoUtils.closePreparedStatement(pStmt);
			DaoUtils.closeConnection(connection);
		}
	}

	public void update(ClassificacaoT classificacaoT) throws SQLException {
		PreparedStatement pStmt = null;

		try {
			pStmt = connection.prepareStatement(SQL_UPDATE);
			pStmt.setObject(1, classificacaoT.getNome());
			pStmt.setObject(2, classificacaoT.getDescricao());
			pStmt.setObject(3, classificacaoT.getAtivo());
			pStmt.setObject(4, classificacaoT.getId());

			pStmt.executeUpdate();
		} finally {
			DaoUtils.closePreparedStatement(pStmt);
			DaoUtils.closeConnection(connection);
		}
	}

	public void inactivate(ClassificacaoT classificacaoT) throws SQLException {
		PreparedStatement pStmt = null;

		try {
			pStmt = connection.prepareStatement(SQL_INACTIVATE);
			pStmt.setObject(1, classificacaoT.getId());

			pStmt.executeUpdate();
		} finally {
			DaoUtils.closePreparedStatement(pStmt);
			DaoUtils.closeConnection(connection);
		}
	}
}
