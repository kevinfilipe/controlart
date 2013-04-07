package com.controlart.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.controlart.dao.factory.ConnFactory;
import com.controlart.dao.utils.DaoUtils;
import com.controlart.transfer.PecaT;

public class PecaDao {
	private Connection connection;

	private static final String SQL_CONSULT_ALL_FOR_VIEW = "SELECT tp.id_peca, tp.nm_peca, tp.in_ativo FROM tb_peca tp ORDER BY tp.nm_peca";
	private static final String SQL_CONSULT_ALL = "SELECT tp.* FROM tb_peca tp ORDER BY tp.nm_peca";
	private static final String SQL_CONSULT_ID = "SELECT tp.id_peca FROM tb_peca tp WHERE tp.nm_peca = ?";
	private static final String SQL_INSERT = "INSERT INTO tb_peca (id_classificacao, id_acervo_atual, nm_peca, ds_peca, nm_autor, ds_periodo, vl_largura, vl_altura, ds_material, nr_registro, vl_profundidade, ds_historica, ds_status, ds_estado, vl_preco, in_leilao, in_ativo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String SQL_UPDATE = "UPDATE tb_peca SET id_classificacao = ?, id_acervo_atual = ?, nm_peca = ?, ds_peca = ?, nm_autor = ?, ds_periodo = ?, vl_largura = ?, vl_altura = ?, ds_material = ?, nr_registro = ?, vl_profundidade = ?, ds_historica = ?, ds_status = ?, ds_estado = ?, vl_preco = ?, in_leilao = ?, in_ativo = ? WHERE id_peca = ?";
	private static final String SQL_INACTIVATE = "UPDATE tb_peca SET in_ativo = 0 WHERE id_peca = ?";

	public PecaDao() throws SQLException {
		connection = ConnFactory.getConnection();
	}

	/*
	 * Objetivo: M�todo utilizado para consultar todas as Pe�as (PecaT) do
	 * sistema.
	 * 
	 * @param
	 * 
	 * @return List<PecaT>. Obs: Apenas as informa��es utilizadas por Converters
	 * e Selecitems ser�o retornadas.
	 * 
	 * @throws SQLException.
	 */

	public List<PecaT> consultAllForView() throws SQLException {
		PreparedStatement pStmt = null;
		ResultSet rs = null;

		try {
			pStmt = connection.prepareStatement(SQL_CONSULT_ALL_FOR_VIEW);

			rs = pStmt.executeQuery();

			return resultsetToObjectA(rs);
		} finally {
			DaoUtils.closePreparedStatement(pStmt);
			DaoUtils.closeConnection(connection);
		}
	}

	/*
	 * Objetivo: M�todo utilizado para consultar todas as Pe�as (PecaT) do
	 * sistema.
	 * 
	 * @param
	 * 
	 * @return List<PecaT>. Obs: Todas as informa��es ser�o retornadas.
	 * 
	 * @throws SQLException.
	 */

	public List<PecaT> consultAll() throws SQLException {
		PreparedStatement pStmt = null;
		ResultSet rs = null;

		try {
			pStmt = connection.prepareStatement(SQL_CONSULT_ALL);

			rs = pStmt.executeQuery();

			return resultsetToObjectB(rs);
		} finally {
			DaoUtils.closePreparedStatement(pStmt);
			DaoUtils.closeConnection(connection);
		}
	}

	/*
	 * Objetivo: M�todo utilizado para consultar o Identificador (id) de uma
	 * Pe�a (PecaT).
	 * 
	 * @param pecaT (id).
	 * 
	 * @return int. Obs: Apenas o identificador da Pe�a (Pe�aT) ser� retornado.
	 * 
	 * @throws SQLException.
	 */

	public int consultId(PecaT pecaT) throws SQLException {
		PreparedStatement pStmt = null;
		ResultSet rs = null;

		try {
			pStmt = connection.prepareStatement(SQL_CONSULT_ID);
			pStmt.setObject(1, pecaT.getNome());

			rs = pStmt.executeQuery();

			return resultsetToObjectTC(rs);
		} finally {
			DaoUtils.closePreparedStatement(pStmt);
		}
	}

	/*
	 * Objetivo: M�todo utilizado para mapear dados de um ResultSet (Que
	 * armazena resultados de consultas em uma Base de Dados) em informa��es de
	 * Pe�a (PecaT).
	 * 
	 * @param ResultSet.
	 * 
	 * @return List<PecaT>. Obs: Apenas as informa��es utilizadas por Converters
	 * e Selecitems ser�o retornadas.
	 * 
	 * @throws SQLException.
	 */

	private List<PecaT> resultsetToObjectA(ResultSet rs) throws SQLException {
		List<PecaT> listaPecaT = new ArrayList<PecaT>();

		while (rs.next()) {
			PecaT pecaT = new PecaT();

			pecaT.setId(rs.getInt("ID_PECA"));
			pecaT.setNome(rs.getString("NM_PECA"));
			pecaT.setAtivo(rs.getInt("IN_ATIVO"));

			listaPecaT.add(pecaT);
		}

		return listaPecaT;
	}

	/*
	 * Objetivo: M�todo utilizado para mapear dados de um ResultSet (Que
	 * armazena resultados de consultas em uma Base de Dados) em informa��es de
	 * Pe�a (PecaT).
	 * 
	 * @param ResultSet.
	 * 
	 * @return List<PecaT>. Obs: Todas as informa��es ser�o retornadas.
	 * 
	 * @throws SQLException.
	 */

	private List<PecaT> resultsetToObjectB(ResultSet rs) throws SQLException {
		List<PecaT> listaPecaT = new ArrayList<PecaT>();

		while (rs.next()) {
			PecaT pecaT = new PecaT();

			pecaT.setId(rs.getInt("ID_PECA"));
			pecaT.setAcervo(rs.getInt("ID_ACERVO_ATUAL"));
			pecaT.setClassificacao(rs.getInt("ID_CLASSIFICACAO"));
			pecaT.setNome(rs.getString("NM_PECA"));
			pecaT.setDescricao(rs.getString("DS_PECA"));
			pecaT.setAutor(rs.getString("NM_AUTOR"));
			pecaT.setPeriodo(rs.getString("DS_PERIODO"));
			pecaT.setLargura(rs.getDouble("VL_LARGURA"));
			pecaT.setAltura(rs.getDouble("VL_ALTURA"));
			pecaT.setMaterial(rs.getString("DS_MATERIAL"));
			pecaT.setNumeroRegistro(rs.getDouble("NR_REGISTRO"));
			pecaT.setProfundidade(rs.getDouble("VL_PROFUNDIDADE"));
			pecaT.setHistorico(rs.getString("DS_HISTORICA"));
			pecaT.setStatus(rs.getString("DS_STATUS"));
			pecaT.setEstado(rs.getString("DS_ESTADO"));
			pecaT.setPreco(rs.getBigDecimal("VL_PRECO"));
			pecaT.setDisponivelLeilao(rs.getInt("IN_LEILAO"));
			pecaT.setAtivo(rs.getInt("IN_ATIVO"));

			listaPecaT.add(pecaT);
		}

		return listaPecaT;
	}

	/*
	 * Objetivo: M�todo utilizado para mapear dados de um ResultSet (Que
	 * armazena resultados de consultas em uma Base de Dados) em informa��es de
	 * Pe�a (PecaT).
	 * 
	 * @param ResultSet.
	 * 
	 * @return int. Obs: Apenas o Identificador (id) de Pe�a (pecaT) ser�
	 * retornado.
	 * 
	 * @throws SQLException.
	 */

	private int resultsetToObjectTC(ResultSet rs) throws SQLException {
		PecaT pecaT = new PecaT();

		while (rs.next()) {
			pecaT.setId(rs.getInt("ID_PECA"));
		}

		return pecaT.getId();
	}

	public void insert(PecaT pecaT) throws SQLException {
		PreparedStatement pStmt = null;

		try {
			pStmt = connection.prepareStatement(SQL_INSERT);
			pStmt.setObject(1, pecaT.getClassificacao());
			pStmt.setObject(2, pecaT.getAcervo());
			pStmt.setObject(3, pecaT.getNome());
			pStmt.setObject(4, pecaT.getDescricao());
			pStmt.setObject(5, pecaT.getAutor());
			pStmt.setObject(6, pecaT.getPeriodo());
			pStmt.setObject(7, pecaT.getLargura());
			pStmt.setObject(8, pecaT.getAltura());
			pStmt.setObject(9, pecaT.getMaterial());
			pStmt.setObject(10, pecaT.getNumeroRegistro());
			pStmt.setObject(11, pecaT.getProfundidade());
			pStmt.setObject(12, pecaT.getHistorico());
			pStmt.setObject(13, pecaT.getStatus());
			pStmt.setObject(14, pecaT.getEstado());
			pStmt.setObject(15, pecaT.getPreco());
			pStmt.setObject(16, pecaT.getDisponivelLeilao());
			pStmt.setObject(17, pecaT.getAtivo());

			pStmt.execute();

			pecaT.setId(consultId(pecaT));
		} finally {
			DaoUtils.closePreparedStatement(pStmt);
			DaoUtils.closeConnection(connection);
		}
	}

	public void update(PecaT pecaT) throws SQLException {
		PreparedStatement pStmt = null;

		try {
			pStmt = connection.prepareStatement(SQL_UPDATE);
			pStmt.setObject(1, pecaT.getClassificacao());
			pStmt.setObject(2, pecaT.getAcervo());
			pStmt.setObject(3, pecaT.getNome());
			pStmt.setObject(4, pecaT.getDescricao());
			pStmt.setObject(5, pecaT.getAutor());
			pStmt.setObject(6, pecaT.getPeriodo());
			pStmt.setObject(7, pecaT.getLargura());
			pStmt.setObject(8, pecaT.getAltura());
			pStmt.setObject(9, pecaT.getMaterial());
			pStmt.setObject(10, pecaT.getNumeroRegistro());
			pStmt.setObject(11, pecaT.getProfundidade());
			pStmt.setObject(12, pecaT.getHistorico());
			pStmt.setObject(13, pecaT.getStatus());
			pStmt.setObject(14, pecaT.getEstado());
			pStmt.setObject(15, pecaT.getPreco());
			pStmt.setObject(16, pecaT.getDisponivelLeilao());
			pStmt.setObject(17, pecaT.getAtivo());
			pStmt.setObject(18, pecaT.getId());

			pStmt.executeUpdate();
		} finally {
			DaoUtils.closePreparedStatement(pStmt);
			DaoUtils.closeConnection(connection);
		}
	}

	public void inactivate(PecaT pecaT) throws SQLException {
		PreparedStatement pStmt = null;

		try {
			pStmt = connection.prepareStatement(SQL_INACTIVATE);
			pStmt.setObject(1, pecaT.getId());

			pStmt.executeUpdate();
		} finally {
			DaoUtils.closePreparedStatement(pStmt);
			DaoUtils.closeConnection(connection);
		}
	}
}
