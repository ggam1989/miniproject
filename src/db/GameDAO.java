package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class GameDAO {
	ConnUtil cu = new ConnUtil();

	public int insertOne(GameVo vo) {
		int rst = 0; // 디폴트 리턴 밸류
		String sql;

		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = ConnUtil.getConnection();

			sql = "insert into game(name, id, pw, pw2, email,phone,nick,score,levels) values(?,?,?,?,?,?,?,?,?)";
			ps = conn.prepareStatement(sql);

			ps.setString(1, vo.getName());
			ps.setString(2, vo.getId());
			ps.setString(3, vo.getPw());
			ps.setString(4, vo.getPw2());
			ps.setString(5, vo.getEmail());
			ps.setString(6, vo.getPhone());
			ps.setString(7, vo.getNick());
			ps.setInt(8, vo.getScore());
			ps.setString(9, vo.getLevel());

			System.out.println(sql);
			rst = ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnUtil.close(conn, ps);
		}
		return rst;
	}

	public void insertTwo(String a) {
		String sql;

		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = ConnUtil.getConnection();

			sql = "insert into login(id) values(?)";
			ps = conn.prepareStatement(sql);

			ps.setString(1, a);

			System.out.println(sql);
			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnUtil.close(conn, ps);
		}
	}

	public void bye(String a) {
		String sql;

		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = ConnUtil.getConnection();

			sql = "Delete from login where id = ?";
			ps = conn.prepareStatement(sql);

			ps.setString(1, a);

			System.out.println(sql);
			ps.executeQuery();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnUtil.close(conn, ps);
		}
	}


	public GameVo ScoreInfo(String id) {
		GameVo vo = new GameVo();

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		 String sql; 
		try {

			conn = ConnUtil.getConnection();
			
			
			sql = "select score from game where id =  ? ";
			ps = conn.prepareStatement(sql);
			
			
			 ps.setString(1, id);

			rs = ps.executeQuery();

			if (rs.next()) {
//				 vo = new GameVo();
				vo.setScore(rs.getInt("score"));
			}

			System.out.println(sql);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnUtil.close(conn, ps, rs);
		}
		return vo;
	}
	
//	public void InScore(int game_Score) {
		public void InScore(String id, int game_Score) {
			
		String sql;

		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = ConnUtil.getConnection();

			sql = "update game set score = ? where id=?";
			ps = conn.prepareStatement(sql);		
			ps.setInt(1, game_Score);
			ps.setString(2, id);

			System.out.println(sql);	
			
/*			sql = "update game set score = ? ";

			ps = conn.prepareStatement(sql);
			
			ps.setInt(1, game_Score);
*/
			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnUtil.close(conn, ps);
		}
	}

		public void InLevel(String id, String Level) {
			
			String sql;

			Connection conn = null;
			PreparedStatement ps = null;

			try {
				conn = ConnUtil.getConnection();

				sql = "update game set levels = ? where id=?";
				ps = conn.prepareStatement(sql);		
				ps.setString(1, Level);
				ps.setString(2, id);

				System.out.println(sql);	
				
	/*			sql = "update game set score = ? ";

				ps = conn.prepareStatement(sql);
				
				ps.setInt(1, game_Score);
	*/
				ps.executeUpdate();

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				ConnUtil.close(conn, ps);
			}
		}

		
	public ArrayList<GameVo> Select_c() {
		GameVo vo = new GameVo();

		ArrayList<GameVo> list = new ArrayList<GameVo>();

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = ConnUtil.getConnection();

			String sql = "select id from login ";

			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				vo = new GameVo();
				vo.setId(rs.getString("id"));
				list.add(vo);
			}
			System.out.println(sql);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnUtil.close(conn, ps, rs);
		}
		return list;
	}

	public GameVo Select_Lg_Me(String id) {
		GameVo vo = new GameVo();

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		 String sql; 
		 //String sql = "select id,nick,score from game id = '" + id + "'";
		try {

			conn = ConnUtil.getConnection();
			
			
			sql = "select name,id,nick,score,levels from game where id =  ? ";
			ps = conn.prepareStatement(sql);
			
			
			 ps.setString(1, id);

			rs = ps.executeQuery();

			if (rs.next()) {
//				 vo = new GameVo();
				vo.setName(rs.getString("name"));
				vo.setId(rs.getString("id"));
				vo.setNick(rs.getString("nick"));
				vo.setScore(rs.getInt("score"));
				vo.setLevel(rs.getString("levels"));
			}

			System.out.println(sql);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnUtil.close(conn, ps, rs);
		}
		return vo;
	}

	public ArrayList<GameVo> Select_Lg_All() {
		GameVo vo = new GameVo();

		ArrayList<GameVo> list = new ArrayList<GameVo>();

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = ConnUtil.getConnection();

			String sql = "select name,id,nick,score,levels from game order by score desc  Nulls last";

			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				
				vo = new GameVo();
				vo.setName(rs.getString("name"));
				vo.setId(rs.getString("id"));
				vo.setNick(rs.getString("nick"));
				vo.setScore(rs.getInt("score"));
				vo.setLevel(rs.getString("levels"));
				
				list.add(vo);
			
			}
			System.out.println(sql);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnUtil.close(conn, ps, rs);
		}
		return list;
	}

	public GameVo selectOne(String a, String b) {
		GameVo vo = new GameVo();

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = ConnUtil.getConnection();

			String sql = "select id,pw from game where phone = '" + a
					+ "' and pw2 = '" + b + "' ";

			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {
				vo = new GameVo();
				vo.setId(rs.getString("id"));
				vo.setPw(rs.getString("pw"));
			}
			System.out.println(sql);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnUtil.close(conn, ps, rs);
		}
		return vo;
	}

	public GameVo selectTwo(String a) {
		GameVo vo = new GameVo();

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = ConnUtil.getConnection();

			String sql = "select id from game where id = '" + a + "' ";

			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {
				vo = new GameVo();
				vo.setId(rs.getString("id"));
			}
			System.out.println(sql);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnUtil.close(conn, ps, rs);
		}
		return vo;
	}

	public GameVo ac(String a, String b) {
		GameVo vo = new GameVo();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = ConnUtil.getConnection();

			String sql = "select id, pw from game where id = '" + a
					+ "' and pw = '" + b + "' ";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {
				vo.setId(rs.getString("id"));
				vo.setPw(rs.getString("pw"));
			}

			// 4. 실행
			System.out.println(sql);

			conn.commit();

			// 6. 자원반납
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnUtil.close(conn, ps, rs);
		}
		return vo;
	}
	public GameVo samsam(String a) {
		GameVo gg = new GameVo();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = ConnUtil.getConnection();

			String sql = "select id from Login where id = '" + a+  "' ";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {
				gg.setId(rs.getString("id"));
			}

			// 4. 실행
			System.out.println(sql);

			conn.commit();

			// 6. 자원반납
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnUtil.close(conn, ps, rs);
		}
		return gg;
	}
	
	
}

/*
 * public ArrayList<GameVo> selectAll() { // ArrayList : 무한배열 ArrayList<GameVo>
 * list = new ArrayList<GameVo>(); try { // 드라이버 검색
 * Class.forName("oracle.jdbc.driver.OracleDriver"); // 연결 String url =
 * "jdbc:oracle:thin:@localhost:1521:xe"; String user ="hr"; String password
 * ="hr";
 * 
 * Connection conn = DriverManager.getConnection(url, user, password); // 준비
 * String sql ="select * from info order by id asc"; PreparedStatement ps =
 * conn.prepareStatement(sql); // 실행 ResultSet rs = ps.executeQuery(); // 처리
 * while(rs.next()) { // rs 에서 데이타가 있는 동안.... GameVo vo = new GameVo();
 * 
 * String id =rs.getString("id"); vo.setId(id); vo.setPw(rs.getString("pw"));
 * vo.setName(rs.getString("name")); vo.setEmail(rs.getString("Email"));
 * vo.setPhone(rs.getString("Phone")); vo.setScore(rs.getString("score"));
 * 
 * list.add(vo); } // 반납 rs.close(); ps.close(); conn.close();
 * 
 * 
 * } catch (Exception e) { e.printStackTrace(); } return list; } }
 */