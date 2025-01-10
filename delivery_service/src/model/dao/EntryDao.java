package model.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.dto.EntryDto;
import model.dto.RoadAddressDto;


public class EntryDao extends Dao {
//	싱글톤
	private EntryDao() {}
	private static EntryDao instance = new EntryDao();
	public static EntryDao getInstance () {
		return instance;
	} 
	
//	메소드
	
//	1. 입점신청
	public boolean entryJoin( EntryDto entryDto , RoadAddressDto roadAddress ) {
		try {
			String sql1 = "insert into entry(ename , espot , mno) values( ? , ? , ? )";
			PreparedStatement ps1 = conn.prepareStatement(sql1);
				ps1.setString(1, entryDto.getEname());
				ps1.setString(2, entryDto.getEspot());
				ps1.setInt(3, selectMno(entryDto.getLogInMno()));
			int count1 = ps1.executeUpdate();
			
			String sql2 = "select eno from entry where ename = ? and espot = ? and mno = ?";
			PreparedStatement ps2 = conn.prepareStatement(sql2);
				ps2.setString(1, entryDto.getEname());
				ps2.setString(2, entryDto.getEspot());
				ps2.setInt(3, selectMno(entryDto.getLogInMno()));
			ResultSet rs = ps2.executeQuery();
			int eno = -1;
			if( rs.next() ) {
				eno = rs.getInt("eno");
			}
				
			String sql3 = "insert into entryaddress(eazipcode , earoad , eastreet , eadetail , eno )"
					+ "values( ? , ? , ? , ? , ? )";
			PreparedStatement ps3 = conn.prepareStatement(sql3);
				ps3.setString(1, roadAddress.getZipCode());
				ps3.setString(2, roadAddress.getRoadAddress());
				ps3.setString(3, roadAddress.getJibunAddress());
				ps3.setString(4, roadAddress.getDetailAddress());
				ps3.setInt(5, eno);
			int count2 = ps3.executeUpdate();
			if( count1 == 1 && count2 == 1 ) { return true; }
		}catch( SQLException e ) {
			System.out.println( e );
		}
		return false;
	} // f end
	
//	2. 입점목록
	public ArrayList<EntryDto> entryList() {
		ArrayList<EntryDto> result = new ArrayList<>();
		try {
			String sql = "select * from entry";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while( rs.next() ) {
				EntryDto entryDto = new EntryDto();
				entryDto.setEno(rs.getInt("eno"));
				entryDto.setEname(rs.getString("ename"));
				entryDto.setEspot(rs.getString("espot"));
				entryDto.setEtype(rs.getInt("etype"));
				entryDto.setMno(rs.getInt("mno"));
				result.add( entryDto );
			} // w end
		}catch( SQLException e ) { System.out.println( e );	}
		return result;
	} // f end
	
//	3. 메뉴 리스트
	public ArrayList<EntryDto> menuList() {
		ArrayList<EntryDto> result = new ArrayList<>();
		try {
			String sql = "select * from menu";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while( rs.next() ) {
				EntryDto entryDto = new EntryDto();
				entryDto.setEno(rs.getInt("eno"));
				entryDto.setMename(rs.getString("mename"));
				entryDto.setMeprice(rs.getString("meprice"));
				entryDto.setCno(rs.getInt("cno"));
				entryDto.setEno(rs.getInt("eno"));
				result.add( entryDto );
			} // w end
		}catch( SQLException e ) { System.out.println( e ); }
		return result;
	} // f end
	
	
}
