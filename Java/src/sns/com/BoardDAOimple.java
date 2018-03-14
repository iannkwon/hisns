package sns.com;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BoardDAOimple implements BoardDAO {

	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	private final String url="jdbc:oracle:thin:@localhost:1521:xe";
	private final String user="hdsns";
	private final String password="hi123456";
	
	private final String SQL_BOARD_INSERT="insert into board(bnum,btitle,bcontent,unum,wdate,blocation,blocation2,bfilenum,bhidden) "
			+ "values(board_seq_num.nextval,?,?,?,?,?,?,?,0)"; // exist photo file
	private final String SQL_BOARD_INSERT_1="insert into board(bnum,btitle,bcontent,unum,wdate,blocation,blocation2,bhidden) "
			+ "values(board_seq_num.nextval,?,?,?,?,?,?,?,0)"; // no exist photo file
	private final String SQL_BOARD_INSERT2="insert into board(bnum,btitle,bcontent,unum,wdate,blocation,blocation2,bfilenum,bhidden) "
			+ "values(board_seq_num.nextval,?,?,?,?,?,?,?,1)"; // exist photo file
	private final String SQL_BOARD_INSERT2_1="insert into board(bnum,btitle,bcontent,unum,wdate,blocation,blocation2,bhidden) "
			+ "values(board_seq_num.nextval,?,?,?,?,?,?,1)"; // no exist photo file
	private final String SQL_BOARD_UPDATE=""; // 대기
	private final String SQL_BOARD_DELETE="delete from board where bnum=?"; //대기
	private final String SQL_BOARD_SEARCHBOARD="select * from board where bnum=?";
	private final String SQL_MY_BOARD="select * from board where unum=? and bhidden=0";
	private final String SQL_HID_BOARD="select * from board where unum=? and bhidden=1";
	private final String SQL_BOARD_SELECTBOARDTIT="select * from board where btitle like ?";
	private final String SQL_BOARD_SELECTBOARDCON="select * from board where bcontent like ?";
	private final String SQL_BOARD_FILECHECK="select count(*) as filecheck from board where bfilenum=?";

	
	//private final String SQL_BOARD_TOT="select * from board where bhidden=? order by wdate desc"; //紐⑤��寃���湲� 媛��몄�ㅺ린
	//my location in 200m
	private final String SQL_BOARD_TOT="select * from ("
			+"select bnum,btitle,bcontent,blocation,blocation2"
			+",DISTNACE_WGS84(?, ?, blocation, blocation2) as DISTANCE"
			+" from BOARD where bhidden=?) where DISTANCE between 0 and 0.2";
	
	public BoardDAOimple(){
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			System.out.println("Find Successed");
		} catch (ClassNotFoundException e) {
			System.out.println("Find Failed");
			e.printStackTrace();
		}
	}
	
	@Override
	public int insert(BoardVO vo) {
		int flag=0;
		String a="";
		if(vo.getBfilenum().equals("")){
			a = SQL_BOARD_INSERT_1;
			vo.setBfilenum("0");
		}else {
			a = SQL_BOARD_INSERT;
		}

		try {
			conn=DriverManager.getConnection(url,user,password);
			pstmt=conn.prepareStatement(a);
			pstmt.setString(1, vo.getBtitle());
			pstmt.setString(2, vo.getBcontent());
			pstmt.setInt(3, vo.getUnum());
			pstmt.setString(4, vo.getWdate());
			pstmt.setString(5, vo.getBlocation());
			pstmt.setString(6, vo.getBlocation2());
			pstmt.setString(7, vo.getBfilenum());
			flag=pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(conn!=null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		return flag;
	}

	@Override
	public int update(BoardVO vo) {
		return 0;
	}

	@Override
	public int delete(BoardVO vo) {
		int flag=0;

		try {
			conn=DriverManager.getConnection(url,user,password);
			pstmt=conn.prepareStatement(SQL_BOARD_DELETE);
			pstmt.setInt(1, vo.getBnum());
			
			flag=pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(conn!=null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		return flag;
	}

	@Override
	public BoardVO searchBoard(BoardVO vo) {
		BoardVO vo2 = new BoardVO();
		try {
			conn=DriverManager.getConnection(url,user,password);
			pstmt=conn.prepareStatement(SQL_BOARD_SEARCHBOARD);
			pstmt.setInt(1, vo.getBnum());
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				vo2.setBnum(rs.getInt("bnum"));
				vo2.setBtitle(rs.getString("btitle"));
				vo2.setBcontent(rs.getString("bcontent"));
				vo2.setUnum(rs.getInt("unum"));
				vo2.setWdate(rs.getString("wdate"));
				vo2.setBlocation(rs.getString("blocation"));
				vo2.setBlocation2(rs.getString("blocation2"));
				vo2.setBfilenum(rs.getString("bfilenum"));
				vo2.setBhidden(rs.getInt("bhidden"));
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(conn!=null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		return vo2;
	}

	@Override
	public List<BoardVO> selectBoardTit(BoardVO vo) {
		
		List<BoardVO> list = new ArrayList<BoardVO>();
		try {
			conn=DriverManager.getConnection(url,user,password);
			pstmt=conn.prepareStatement(SQL_BOARD_SELECTBOARDTIT);
			pstmt.setString(1,"%" + vo.getBtitle() + "%");
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				BoardVO vo2 = new BoardVO();
				vo2.setBnum(rs.getInt("bnum"));
				vo2.setBtitle(rs.getString("btitle"));
				vo2.setBcontent(rs.getString("bcontent"));
				vo2.setUnum(rs.getInt("unum"));
				vo2.setWdate(rs.getString("wdate"));
				vo2.setBlocation(rs.getString("blocation"));
				vo2.setBlocation2(rs.getString("blocation2"));
				vo2.setBfilenum(rs.getString("bfilenum"));
				vo2.setBhidden(rs.getInt("bhidden"));
				list.add(vo2);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(conn!=null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		return list;
	}

	@Override
	public List<BoardVO> selectBoardCon(BoardVO vo) {
		List<BoardVO> list = new ArrayList<BoardVO>();
		try {
			conn=DriverManager.getConnection(url,user,password);
			pstmt=conn.prepareStatement(SQL_BOARD_SELECTBOARDCON);
			pstmt.setString(1,"%" + vo.getBcontent() + "%");
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				BoardVO vo2 = new BoardVO();
				vo2.setBnum(rs.getInt("bnum"));
				vo2.setBtitle(rs.getString("btitle"));
				vo2.setBcontent(rs.getString("bcontent"));
				vo2.setUnum(rs.getInt("unum"));
				vo2.setWdate(rs.getString("wdate"));
				vo2.setBlocation(rs.getString("blocation"));
				vo2.setBlocation2(rs.getString("blocation2"));
				vo2.setBfilenum(rs.getString("bfilenum"));
				vo2.setBhidden(rs.getInt("bhidden"));
				list.add(vo2);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(conn!=null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		return list;
	}

	@Override
	public int fileCheck(BoardVO vo) {
		int flag=0;
		try {
			conn=DriverManager.getConnection(url,user,password);
			pstmt=conn.prepareStatement(SQL_BOARD_FILECHECK);
			pstmt.setString(1, vo.getBfilenum());
			
			rs=pstmt.executeQuery();
			while(rs.next()){
				 flag=rs.getInt("filecheck");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(conn!=null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return flag;
	}

	@Override
	public int insert2(BoardVO vo) {
		int flag=0;
		String a="";
		if(vo.getBfilenum().equals("")){
			a = SQL_BOARD_INSERT2_1;
			vo.setBfilenum("0");
		}else {
			a = SQL_BOARD_INSERT2;
		}
		
		try {
			conn=DriverManager.getConnection(url,user,password);
			pstmt=conn.prepareStatement(a);
			pstmt.setString(1, vo.getBtitle());
			pstmt.setString(2, vo.getBcontent());
			pstmt.setInt(3, vo.getUnum());
			pstmt.setString(4, vo.getWdate());
			pstmt.setString(5, vo.getBlocation());
			pstmt.setString(6, vo.getBlocation2());
			pstmt.setString(7, vo.getBfilenum());

			flag=pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(conn!=null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		return flag;
	}
	
	//紐⑤��寃���湲� 媛��몄�ㅺ린
	@Override
	public List<BoardVO> selectBoardMap(BoardVO vo) {
		List<BoardVO> list = new ArrayList<BoardVO>();
		try {
			conn=DriverManager.getConnection(url,user,password);
			pstmt=conn.prepareStatement(SQL_BOARD_TOT);
			pstmt.setString(1, vo.getBlocation()); //위도
			pstmt.setString(2, vo.getBlocation2()); //경도
			pstmt.setInt(3, 0); //0=hidden, 1=normalBoard
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				BoardVO vo2 = new BoardVO();
				vo2.setBnum(rs.getInt("bnum"));
				vo2.setBtitle(rs.getString("btitle"));
				//vo2.setBcontent(rs.getString("bcontent"));
				vo2.setBlocation(rs.getString("blocation"));
				vo2.setBlocation2(rs.getString("blocation2"));
				list.add(vo2);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(conn!=null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}

	@Override
	public List<BoardVO> myBoard(BoardVO vo) {
		List<BoardVO> list = new ArrayList<BoardVO>();
		try {
			conn=DriverManager.getConnection(url,user,password);
			pstmt=conn.prepareStatement(SQL_MY_BOARD);
			pstmt.setInt(1, vo.getUnum());
			System.out.println("myboard");

			rs = pstmt.executeQuery();
			while (rs.next()) {
				BoardVO vo2 = new BoardVO();
				vo2.setBnum(rs.getInt("bnum"));
				vo2.setBtitle(rs.getString("btitle"));
				vo2.setBcontent(rs.getString("bcontent"));
				vo2.setUnum(rs.getInt("unum"));
				vo2.setWdate(rs.getString("wdate"));
				vo2.setBlocation(rs.getString("blocation"));
				vo2.setBlocation2(rs.getString("blocation2"));
				vo2.setBfilenum(rs.getString("bfilenum"));
				vo2.setBhidden(rs.getInt("bhidden"));
				list.add(vo2);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(conn!=null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		return list;
	}

	@Override
	public List<BoardVO> hidBoard(BoardVO vo) {
		List<BoardVO> list = new ArrayList<BoardVO>();
		try {
			conn=DriverManager.getConnection(url,user,password);
			pstmt=conn.prepareStatement(SQL_HID_BOARD);
			pstmt.setInt(1, vo.getUnum());
			System.out.println("hid");
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				BoardVO vo2 = new BoardVO();
				vo2.setBnum(rs.getInt("bnum"));
				vo2.setBtitle(rs.getString("btitle"));
				vo2.setBcontent(rs.getString("bcontent"));
				vo2.setUnum(rs.getInt("unum"));
				vo2.setWdate(rs.getString("wdate"));
				vo2.setBlocation(rs.getString("blocation"));
				vo2.setBlocation2(rs.getString("blocation2"));
				vo2.setBfilenum(rs.getString("bfilenum"));
				vo2.setBhidden(rs.getInt("bhidden"));
				list.add(vo2);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(conn!=null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		return list;
	}

}
