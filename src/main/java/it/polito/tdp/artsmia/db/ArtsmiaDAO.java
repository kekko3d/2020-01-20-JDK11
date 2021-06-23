package it.polito.tdp.artsmia.db;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.artsmia.model.Adiacenza;
import it.polito.tdp.artsmia.model.ArtObject;
import it.polito.tdp.artsmia.model.Artist;
import it.polito.tdp.artsmia.model.Exhibition;

public class ArtsmiaDAO {

	public List<ArtObject> listObjects() {
		
		String sql = "SELECT * from objects";
		List<ArtObject> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				ArtObject artObj = new ArtObject(res.getInt("object_id"), res.getString("classification"), res.getString("continent"), 
						res.getString("country"), res.getInt("curator_approved"), res.getString("dated"), res.getString("department"), 
						res.getString("medium"), res.getString("nationality"), res.getString("object_name"), res.getInt("restricted"), 
						res.getString("rights_type"), res.getString("role"), res.getString("room"), res.getString("style"), res.getString("title"));
				
				result.add(artObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Exhibition> listExhibitions() {
		
		String sql = "SELECT * from exhibitions";
		List<Exhibition> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Exhibition exObj = new Exhibition(res.getInt("exhibition_id"), res.getString("exhibition_department"), res.getString("exhibition_title"), 
						res.getInt("begin"), res.getInt("end"));
				
				result.add(exObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
public List<Artist> listV(String ruolo) {
		String sql = "SELECT DISTINCT(art.`artist_id`) as id, art.`name` as name "
				+ "FROM artists as art, authorship as aut "
				+ "WHERE art.`artist_id` = aut.`artist_id` "
				+ "AND aut.`role` = ? ";
//		String sql = "select a.artist_id as id, a. from artists a.name as name, authorship au " +
//				"where a.artist_id = au.artist_id and au.role = ?";
		List<Artist> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, ruolo);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Artist artist = new Artist(res.getInt("id"), res.getString("name"));
				result.add(artist);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}


public List <Adiacenza> getAdiacenze() {
	
	String sql = "SELECT aut1.`artist_id` as id1, aut2.`artist_id` as id2, COUNT(DISTINCT(ex1.`exhibition_id`)) as peso "
			+ "FROM authorship as aut1, authorship as aut2, objects as obj1, objects as obj2, exhibition_objects as ex1, exhibition_objects as ex2 "
			+ "WHERE aut1.`artist_id` > aut2.`artist_id` "
			+ "AND aut1.`object_id` =  obj1.`object_id` "
			+ "AND aut2.`object_id` =  obj2.`object_id` "
			+ "AND obj1.`object_id` = ex1.`object_id` "
			+ "AND obj2.`object_id` = ex2.`object_id` "
			+ "AND ex1.`exhibition_id` = ex2.`exhibition_id` "
			+ "GROUP BY aut1.`artist_id`, aut2.`artist_id` ";
	
	try {
		Connection conn = DBConnect.getConnection() ;

		PreparedStatement st = conn.prepareStatement(sql) ;
//		st.setDouble(1, calorie);
//		st.setDouble(2, calorie);

		List<Adiacenza> list = new ArrayList<>() ;
		
		ResultSet res = st.executeQuery() ;
		
		while(res.next()) {
			list.add( new Adiacenza( res.getInt("id1"), res.getInt("id2"), res.getInt("peso")) );

		}
		
		conn.close();
		return list ;

	} catch (SQLException e) {
		e.printStackTrace();
		return null ;
	}		
}
	
}
