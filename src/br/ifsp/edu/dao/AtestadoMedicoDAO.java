package br.ifsp.edu.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Date;

import com.mysql.jdbc.Statement;

import br.ifsp.edu.model.AtestadoMedico;

public class AtestadoMedicoDAO {
	
	private Connection connection;

	public AtestadoMedicoDAO() {
		this.connection = ConnectionFactory.getinstance();
	}
	
	public AtestadoMedico registrar(AtestadoMedico atestado) {
		String sql = "INSERT INTO tblatestado(CVMATFUN,CCCID,CDDTINI,CVQTDDIAS,CCCRM,CCNOMMED)"+
                "VALUES(?,?,?,?,?,?)";
	PreparedStatement psmt;
	try {
		psmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		psmt.setLong(1, atestado.getFuncionario().getMatricula());
		psmt.setString(2, atestado.getCid());
		Date date = Date.from(atestado.getDataInicio().atStartOfDay(ZoneId.systemDefault()).toInstant());
		psmt.setDate(3, new java.sql.Date(date.getTime()));
		psmt.setLong(4, atestado.getQuantidadeDias());
		psmt.setString(5, atestado.getCrm());
		psmt.setString(6, atestado.getMedico());
		psmt.executeUpdate();

		ResultSet generatedKeys = psmt.getGeneratedKeys();

		while (generatedKeys.next()) {
			atestado.setId(generatedKeys.getLong(1));
		}
		return atestado;
	} catch (SQLException e) {
		e.printStackTrace();
	}
	return null;
	}
	
	
	public void excluir(long id) {
		String sql = "DELETE FROM tblatestado WHERE CVIDATST=?";
		PreparedStatement psmt;
		try {
			psmt = connection.prepareStatement(sql);
			psmt.setLong(1, id);
			psmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	

	public void limparTabela() {
		PreparedStatement psmt;
		try {
			psmt = connection.prepareStatement("SET FOREIGN_KEY_CHECKS=0");
			psmt.execute();
			psmt = connection.prepareStatement("TRUNCATE TABLE tblatestado");
			psmt.execute();
			psmt = connection.prepareStatement("SET FOREIGN_KEY_CHECKS=1");
			psmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
