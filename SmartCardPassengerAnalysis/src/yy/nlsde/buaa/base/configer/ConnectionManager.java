package yy.nlsde.buaa.base.configer;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

public class ConnectionManager {
	private static ConnectionManager instance;

	public ComboPooledDataSource ds;
	private static String c3p0Properties = "c3p0.properties";

	private ConnectionManager() throws Exception {
		Properties p = new Properties();
		p.load(this.getClass().getResourceAsStream(c3p0Properties));
		ds = new ComboPooledDataSource();
	}

	public static final ConnectionManager getInstance() {
		if (instance == null) {
			try {
				instance = new ConnectionManager();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return instance;
	}

	public synchronized final Connection getConnection() {
		try {
			return ds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected void finalize() throws Throwable {
		DataSources.destroy(ds); // 关闭datasource
		super.finalize();
	}
}
