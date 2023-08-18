package com.github.mobile.mock.dao;

import java.sql.SQLException;

import com.github.mobile.mock.domain.HttpCaseInfo;
import com.github.mobile.mock.jdbc.TxQueryRunner;
import com.github.mobile.mock.utils.ToolBox;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

public class HttpCaseInfoDao {
	private QueryRunner qr = new TxQueryRunner();
	
	/**
	 * 插入用例信息到数据库
	 * @param c
	 */
	public void add(HttpCaseInfo c){
		String sql = "insert into tb_http_mockcaseinfo values(?,?,?,?,?,?,?,?,?)";
		Object[] params = {
					            c.getCaseid(),
					            c.getCasename(),
					            c.getCmdcode(),
								c.getProductname(),
								c.getRespmsg(),
								c.getMatchid(),
								c.getRespMD5(),
								ToolBox.getCurrentStringDateTime(),
								ToolBox.getCurrentStringDateTime(),
							};
		try {
			qr.update(sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 更新数据库中的用例信息
	 * @param c
	 */
	public void update(HttpCaseInfo c){
		String sql = "update tb_http_mockcaseinfo set caseName=?,cmdcode=?,respMsg=?,matchid=?,respMD5=?,updateTime=? where caseid=?";
		Object[] params = {
				c.getCasename(),
				c.getCmdcode(),
				c.getRespmsg(),
				c.getMatchid(),
				c.getRespMD5(),
				ToolBox.getCurrentStringDateTime(),
				c.getCaseid()				
		};
		try {
			qr.update(sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取单个用例信息
	 * @param caseid
	 */
	public HttpCaseInfo load(String cid){
		String sql = "select * from tb_http_mockcaseinfo where caseid=?";
		try {
			return qr.query(sql, new BeanHandler<HttpCaseInfo>(HttpCaseInfo.class), cid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
