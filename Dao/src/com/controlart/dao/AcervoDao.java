package com.controlart.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.controlart.dao.factory.ConnFactory;
import com.controlart.dao.utils.DaoUtils;
import com.controlart.transfer.AcervoT;

public class AcervoDao {
	private Connection connection;

	private static final String SQL_CONSULT_ALL = "SELECT ta.* FROM tb_acervo ta ORDER BY ta.nm_acervo";
	private static final String SQL_INSERT = "INSERT INTO tb_acervo (id_pessoa, nm_acervo, ds_acervo, in_ativo) VALUES (?, ?, ?, ?)";
	private static final String SQL_UPDATE = "UPDATE tb_acervo SET id_pessoa = ?, nm_acervo = ?, ds_acervo = ?, in_ativo = ? WHERE id_acervo = ?";
	private static final String SQL_INACTIVATE = "UPDATE tb_acervo SET in_ativo = 0 WHERE id_acervo = ?";

	public AcervoDao() throws SQLException {
		connection = ConnFactory.getConnection();
	}

	public List<AcervoT> consultAll() throws SQLException {
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

	private List<AcervoT> resultsetToObjectT(ResultSet rs) throws SQLException {
		List<AcervoT> listaAcervoT = new ArrayList<AcervoT>();

		while (rs.next()) {
			AcervoT acervoT = new AcervoT();

			acervoT.setId(rs.getInt("ID_ACERVO"));
			acervoT.setDono(rs.getInt("ID_PESSOA"));
			acervoT.setNome(rs.getString("NM_ACERVO"));
			acervoT.setDescricao(rs.getString("DS_ACERVO"));
			acervoT.setAtivo(rs.getInt("IN_ATIVO"));

			listaAcervoT.add(acervoT);
		}

		return listaAcervoT;
	}

	public void insert(AcervoT acervoT) throws SQLException {
		PreparedStatement pStmt = null;

		try {
			pStmt = connection.prepareStatement(SQL_INSERT);
			pStmt.setObject(1, acervoT.getDono());
			pStmt.setObject(2, acervoT.getNome());
			pStmt.setObject(3, acervoT.getDescricao());
			pStmt.setObject(4, acervoT.getAtivo());

			pStmt.execute();
		} finally {
			DaoUtils.closePreparedStatement(pStmt);
			DaoUtils.closeConnection(connection);
		}
	}

	public void update(AcervoT acervoT) throws SQLException {
		PreparedStatement pStmt = null;

		try {
			pStmt = connection.prepareStatement(SQL_UPDATE);
			pStmt.setObject(1, acervoT.getDono());
			pStmt.setObject(2, acervoT.getNome());
			pStmt.setObject(3, acervoT.getDescricao());
			pStmt.setObject(4, acervoT.getAtivo());
			pStmt.setObject(5, acervoT.getId());

			pStmt.executeUpdate();
		} finally {
			DaoUtils.closePreparedStatement(pStmt);
			DaoUtils.closeConnection(connection);
		}
	}

	public void inactivate(AcervoT acervoT) throws SQLException {
		PreparedStatement pStmt = null;

		try {
			pStmt = connection.prepareStatement(SQL_INACTIVATE);
			pStmt.setObject(1, acervoT.getId());

			pStmt.executeUpdate();
		} finally {
			DaoUtils.closePreparedStatement(pStmt);
			DaoUtils.closeConnection(connection);
		}
	}
}
