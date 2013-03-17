package com.controlart.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.controlart.dao.factory.ConnFactory;
import com.controlart.dao.utils.DaoUtils;
import com.controlart.transfer.PessoaT;

public class PessoaDao {
	private Connection connection;

	private static final String SQL_CONSULT_ALL = "SELECT tp.* FROM tb_pessoa tp ORDER BY tp.nm_pessoa";
	private static final String SQL_INSERT = "INSERT INTO tb_pessoa (id_tipo_pessoa, nm_pessoa, nr_fone, ds_email, nm_rua, nr_imovel, nm_bairro, nm_cidade) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String SQL_UPDATE = "UPDATE tb_pessoa SET id_tipo_pessoa = ?, nm_pessoa = ?, nr_fone = ?, ds_email = ?, nm_rua = ?, nr_imovel = ?, nm_bairro = ?, nm_cidade = ?, in_ativo = ? WHERE id_pessoa = ?";
	private static final String SQL_INACTIVATE = "UPDATE tb_pessoa SET in_ativo = 0 WHERE id_pessoa = ?";
	private static final String SQL_CONSULT_ALL_WITHOUT_USER = "SELECT tp.id_pessoa, tp.nm_pessoa FROM tb_pessoa tp WHERE tp.in_ativo = 1 AND tp.id_pessoa NOT IN (SELECT tu.id_pessoa FROM tb_usuario tu)";
	private static final String SQL_CONSULT_ALL_WITH_USER = "SELECT tp.id_pessoa, tp.nm_pessoa FROM tb_pessoa tp WHERE tp.id_pessoa IN (SELECT tu.id_pessoa FROM tb_usuario tu)";

	public PessoaDao() throws SQLException {
		connection = ConnFactory.getConnection();
	}

	public List<PessoaT> consultAll() throws SQLException {
		PreparedStatement pStmt = null;
		ResultSet rs = null;

		try {
			pStmt = connection.prepareStatement(SQL_CONSULT_ALL);

			rs = pStmt.executeQuery();

			return resultsetToObjectPessoaT1(rs);
		} finally {
			DaoUtils.closeStatementAndResultSet(pStmt, rs);
			DaoUtils.closeConnection(connection);
		}
	}

	private List<PessoaT> resultsetToObjectPessoaT1(ResultSet rs)
			throws SQLException {
		List<PessoaT> listaPessoaT = new ArrayList<PessoaT>(0);

		while (rs.next()) {
			PessoaT pessoaT = new PessoaT();

			pessoaT.setIdPessoa(rs.getInt("ID_PESSOA"));
			pessoaT.setIdTipoPessoa(rs.getInt("ID_TIPO_PESSOA"));
			pessoaT.setNmPessoa(rs.getString("NM_PESSOA"));
			pessoaT.setNrFone(rs.getString("NR_FONE"));
			pessoaT.setDsEmail(rs.getString("DS_EMAIL"));
			pessoaT.setNmRua(rs.getString("NM_RUA"));
			pessoaT.setNrImovel(rs.getString("NR_IMOVEL"));
			pessoaT.setNmBairro(rs.getString("NM_BAIRRO"));
			pessoaT.setNmCidade(rs.getString("NM_CIDADE"));
			pessoaT.setSituacao(rs.getInt("IN_ATIVO"));

			listaPessoaT.add(pessoaT);
		}

		return listaPessoaT;
	}

	public List<PessoaT> consultAllWithoutUser() throws SQLException {
		PreparedStatement pStmt = null;
		ResultSet rs = null;

		try {
			pStmt = connection.prepareStatement(SQL_CONSULT_ALL_WITHOUT_USER);

			rs = pStmt.executeQuery();

			return resultsetToObjectPessoaT2(rs);
		} finally {
			DaoUtils.closeStatementAndResultSet(pStmt, rs);
			DaoUtils.closeConnection(connection);
		}
	}

	public List<PessoaT> consultAllWithUser() throws SQLException {
		PreparedStatement pStmt = null;
		ResultSet rs = null;

		try {
			pStmt = connection.prepareStatement(SQL_CONSULT_ALL_WITH_USER);

			rs = pStmt.executeQuery();

			return resultsetToObjectPessoaT2(rs);
		} finally {
			DaoUtils.closeStatementAndResultSet(pStmt, rs);
			DaoUtils.closeConnection(connection);
		}
	}

	private List<PessoaT> resultsetToObjectPessoaT2(ResultSet rs)
			throws SQLException {
		List<PessoaT> listaPessoaT = new ArrayList<PessoaT>(0);

		while (rs.next()) {
			PessoaT pessoaT = new PessoaT();

			pessoaT.setIdPessoa(rs.getInt("ID_PESSOA"));
			pessoaT.setNmPessoa(rs.getString("NM_PESSOA"));

			listaPessoaT.add(pessoaT);
		}

		return listaPessoaT;
	}

	public void insert(PessoaT pessoaT) throws SQLException {
		PreparedStatement pStmt = null;

		try {
			pStmt = connection.prepareStatement(SQL_INSERT);
			pStmt.setObject(1, pessoaT.getIdTipoPessoa());
			pStmt.setObject(2, pessoaT.getNmPessoa());
			pStmt.setObject(3, pessoaT.getNrFone());
			pStmt.setObject(4, pessoaT.getDsEmail());
			pStmt.setObject(5, pessoaT.getNmRua());
			pStmt.setObject(6, pessoaT.getNrImovel());
			pStmt.setObject(7, pessoaT.getNmBairro());
			pStmt.setObject(8, pessoaT.getNmCidade());

			pStmt.execute();
		} finally {
			DaoUtils.closePreparedStatement(pStmt);
			DaoUtils.closeConnection(connection);
		}
	}

	public void update(PessoaT pessoaT) throws SQLException {
		PreparedStatement pStmt = null;

		try {
			pStmt = connection.prepareStatement(SQL_UPDATE);
			pStmt.setObject(1, pessoaT.getIdTipoPessoa());
			pStmt.setObject(2, pessoaT.getNmPessoa());
			pStmt.setObject(3, pessoaT.getNrFone());
			pStmt.setObject(4, pessoaT.getDsEmail());
			pStmt.setObject(5, pessoaT.getNmRua());
			pStmt.setObject(6, pessoaT.getNrImovel());
			pStmt.setObject(7, pessoaT.getNmBairro());
			pStmt.setObject(8, pessoaT.getNmCidade());
			pStmt.setObject(9, pessoaT.getSituacao());
			pStmt.setObject(10, pessoaT.getIdPessoa());

			pStmt.executeUpdate();
		} finally {
			DaoUtils.closePreparedStatement(pStmt);
			DaoUtils.closeConnection(connection);
		}
	}

	public void delete(PessoaT pessoaT) throws SQLException {
		PreparedStatement pStmt = null;

		try {
			pStmt = connection.prepareStatement(SQL_INACTIVATE);
			pStmt.setObject(1, pessoaT.getIdPessoa());

			pStmt.executeUpdate();
		} finally {
			DaoUtils.closePreparedStatement(pStmt);
			DaoUtils.closeConnection(connection);
		}
	}
}
