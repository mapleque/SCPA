package yy.nlsde.buaa.patternlibrary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import yy.nlsde.buaa.base.configer.DBPool;

public class PatternUtil {
	private String patternFile="G:\\cardData\\pattern\\pattern_30";
	private Connection conn;
	
	public static void main(String[] args){
		new PatternUtil().file2DB();
	}
	
	public void file2DB(){
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		File pfile=new File(this.patternFile);
		if (!pfile.exists())
			return;
		try {
			BufferedReader pin = new BufferedReader(new InputStreamReader(
					new FileInputStream(pfile), "gbk"));
			String line;
			int num=0;
			this.conn=DBPool.getDBConnection();
			while ((line=pin.readLine())!=null){
				num++;
				if (num%10000==0)
					System.out.println(num+":"+df.format(new Date()));
				String[] data=line.split("\t");
				this.savePattern(data[0], data[1]);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void savePattern(String id,String pattern){
		try {
			String sql="insert into pattern_st values(?,?)";
			PreparedStatement st=conn.prepareStatement(sql);
			st.setString(1, id);
			st.setString(2, pattern);
			st.executeUpdate();
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
