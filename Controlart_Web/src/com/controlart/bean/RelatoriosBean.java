package com.controlart.bean;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;

import com.controlart.dao.AcervoDao;
import com.controlart.dao.RelatoriosDao;
import com.controlart.transfer.AcervoT;

@ManagedBean(name = "relatoriosBean")
@ViewScoped
public class RelatoriosBean extends ControlArtBean {

	private static final long serialVersionUID = 1L;
	private static final String PATCH_LOGO = "/resources/image/logo_ireport.gif";
	private FacesContext facesContext;
	private List<AcervoT> listAcervo;
	private String acervo;

	public List<AcervoT> getListAcervo() {
		try {
			if (listAcervo == null) {
				listAcervo = new ArrayList<AcervoT>();
				AcervoDao acervoDao = new AcervoDao();
				List<AcervoT> acervos = acervoDao.consultAll();
				for (AcervoT a : acervos) {
					listAcervo.add(new AcervoT(a.getId(), a.getNome()));				
				}
			}
			return listAcervo;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	public void setListAcervo(List<AcervoT> listAcervo) {
		this.listAcervo = listAcervo;
	}
	
	public String getAcervo() {
		return acervo;
	}

	public void setAcervo(String acervo) {
		this.acervo = acervo;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void gerarRelatorio(String arquivoJasper, HashMap parametros,
			Connection connection, String titulo) throws JRException,
			IOException {

		JasperPrint jasperPrint = JasperFillManager.fillReport(arquivoJasper,
				parametros, connection);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		JRPdfExporter exporter = new JRPdfExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
		exporter.exportReport();

		byte[] bytes = baos.toByteArray();

		if (bytes != null && bytes.length > 0) {
			HttpServletResponse response = (HttpServletResponse) facesContext
					.getExternalContext().getResponse();
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition", "inline; filename=\""
					+ titulo + ".pdf\"");
			response.setContentLength(bytes.length);
			ServletOutputStream outputStream = response.getOutputStream();
			outputStream.write(bytes, 0, bytes.length);
			outputStream.flush();
			outputStream.close();
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void gerarRelatorioTabelaPreco() {
		try {

			RelatoriosDao relatoriosDao = new RelatoriosDao();
			Connection connection = relatoriosDao.getConnection();

			facesContext = FacesContext.getCurrentInstance();
			facesContext.responseComplete();
			ServletContext scontext = (ServletContext) facesContext
					.getExternalContext().getContext();

			String arquivoJasper = scontext
					.getRealPath("/resources/jasper/rel_tabela_preco.jasper");
			String arquivoLogo = scontext.getRealPath(PATCH_LOGO);
			String titulo = "Tabela de Pre�os";

			BufferedImage imagem = ImageIO.read(new File(arquivoLogo));
			HashMap parametros = new HashMap();
			parametros.put("ACERVO", this.acervo);
			parametros.put("LOGO", imagem);
			
			gerarRelatorio(arquivoJasper, parametros, connection, titulo);

		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void gerarRelatorioEntradaPeca() {
		try {

			RelatoriosDao relatoriosDao = new RelatoriosDao();
			Connection connection = relatoriosDao.getConnection();

			facesContext = FacesContext.getCurrentInstance();
			facesContext.responseComplete();
			ServletContext scontext = (ServletContext) facesContext
					.getExternalContext().getContext();

			String arquivoJasper = scontext
					.getRealPath("/resources/jasper/rel_entrada_peca.jasper");
			String arquivoLogo = scontext.getRealPath(PATCH_LOGO);
			String titulo = "Entrada de Pe�as";

			BufferedImage imagem = ImageIO.read(new File(arquivoLogo));
			HashMap parametros = new HashMap();
			parametros.put("LOGO", imagem);

			gerarRelatorio(arquivoJasper, parametros, connection, titulo);

		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void gerarRelatorioPecasAcervo() {
		try {

			RelatoriosDao relatoriosDao = new RelatoriosDao();
			Connection connection = relatoriosDao.getConnection();

			facesContext = FacesContext.getCurrentInstance();
			facesContext.responseComplete();
			ServletContext scontext = (ServletContext) facesContext
					.getExternalContext().getContext();

			String arquivoJasper = scontext
					.getRealPath("/resources/jasper/rel_pecas_acervo.jasper");
			String arquivoLogo = scontext.getRealPath(PATCH_LOGO);
			String titulo = "Pe�as por Acervo";

			BufferedImage imagem = ImageIO.read(new File(arquivoLogo));
			HashMap parametros = new HashMap();
			parametros.put("LOGO", imagem);

			gerarRelatorio(arquivoJasper, parametros, connection, titulo);

		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void gerarRelatorioPecasClassificacao() {
		try {

			RelatoriosDao relatoriosDao = new RelatoriosDao();
			Connection connection = relatoriosDao.getConnection();

			facesContext = FacesContext.getCurrentInstance();
			facesContext.responseComplete();
			ServletContext scontext = (ServletContext) facesContext
					.getExternalContext().getContext();

			String arquivoJasper = scontext
					.getRealPath("/resources/jasper/rel_pecas_classificacao.jasper");
			String arquivoLogo = scontext.getRealPath(PATCH_LOGO);
			String titulo = "Pe�as por Classifica��o";

			BufferedImage imagem = ImageIO.read(new File(arquivoLogo));
			HashMap parametros = new HashMap();
			parametros.put("LOGO", imagem);

			gerarRelatorio(arquivoJasper, parametros, connection, titulo);

		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void gerarRelatorioPecasTransacao() {
		try {

			RelatoriosDao relatoriosDao = new RelatoriosDao();
			Connection connection = relatoriosDao.getConnection();

			facesContext = FacesContext.getCurrentInstance();
			facesContext.responseComplete();
			ServletContext scontext = (ServletContext) facesContext
					.getExternalContext().getContext();

			String arquivoJasper = scontext
					.getRealPath("/resources/jasper/rel_pecas_transacao.jasper");
			String arquivoLogo = scontext.getRealPath(PATCH_LOGO);
			String titulo = "Pe�as por Transa��o";

			BufferedImage imagem = ImageIO.read(new File(arquivoLogo));
			HashMap parametros = new HashMap();
			parametros.put("LOGO", imagem);

			gerarRelatorio(arquivoJasper, parametros, connection, titulo);

		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void gerarRelatorioPrazoDevolucao() {
		try {

			RelatoriosDao relatoriosDao = new RelatoriosDao();
			Connection connection = relatoriosDao.getConnection();

			facesContext = FacesContext.getCurrentInstance();
			facesContext.responseComplete();
			ServletContext scontext = (ServletContext) facesContext
					.getExternalContext().getContext();

			String arquivoJasper = scontext
					.getRealPath("/resources/jasper/rel_prazo_devolucao.jasper");
			String arquivoLogo = scontext.getRealPath(PATCH_LOGO);
			String titulo = "Prazo para Devolu��o";

			BufferedImage imagem = ImageIO.read(new File(arquivoLogo));
			HashMap parametros = new HashMap();
			parametros.put("LOGO", imagem);

			gerarRelatorio(arquivoJasper, parametros, connection, titulo);

		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void gerarRelatorioSaidaPeca() {
		try {

			RelatoriosDao relatoriosDao = new RelatoriosDao();
			Connection connection = relatoriosDao.getConnection();

			facesContext = FacesContext.getCurrentInstance();
			facesContext.responseComplete();
			ServletContext scontext = (ServletContext) facesContext
					.getExternalContext().getContext();

			String arquivoJasper = scontext
					.getRealPath("/resources/jasper/rel_saida_peca.jasper");
			String arquivoLogo = scontext.getRealPath(PATCH_LOGO);
			String titulo = "Sa�da de Pe�as";

			BufferedImage imagem = ImageIO.read(new File(arquivoLogo));
			HashMap parametros = new HashMap();
			parametros.put("LOGO", imagem);

			gerarRelatorio(arquivoJasper, parametros, connection, titulo);

		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
