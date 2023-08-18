package com.github.mobile.mock.dao;

import java.sql.SQLException;
import java.util.List;

import com.github.mobile.mock.domain.HttpReqRule;
import com.github.mobile.mock.jdbc.TxQueryRunner;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

public class HttpReqRuleDao {
	private QueryRunner qr = new TxQueryRunner();
	
	/**
	 * 加载matchid与请求路径对应关系表
	 */
	public List<HttpReqRule> loadAll(){
		String sql = "select * from tb_http_mockmatch";
		 try {
			return qr.query(sql, new BeanListHandler<>(HttpReqRule.class));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		 
		return null;
	}
}
