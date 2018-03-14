package sns.com;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SnsDAOimple implements SnsDAO{
	
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	private final String url="jdbc:oracle:thin:@localhost:1521:xe";
	private final String user="hdsns";
	private final String password="hi123456";
	
	//����媛���
	private String joinSql="insert into usertot(unum,email,password,nicname,joindate)"
			+ "values(usertot_seq_num.nextval,?,?,?,?)";
	//����媛����� �대��쇱�蹂듯����
	private String joinIdSql="select count(*) as userok from usertot where email=?";
	//����媛����� ���ㅼ��以�蹂듯����
	private String joinNicSql="select count(*) as userok from usertot where nicname=?";
	//濡�洹몄��(移쇰�쇰� userok濡� 媛��몄�ㅺ� �댁�)
	private String loginSql="select nicname from usertot where email=? and password=?";
	//寃���湲����� ���ㅼ�� 媛��몄�ㅺ린
	private String boardnicSearchSql="select nicname from usertot where unum=?";
	private String findUnum = "select * from usertot where email=?";
	private String friendSql="select * from usertot where nicname like ?";

	
	public SnsDAOimple(){
		System.out.println("SnsDAOimple()");
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			System.out.println("Find Successed");
		} catch (ClassNotFoundException e) {
			System.out.println("Find Failed");
			e.printStackTrace();
		}
	}
	//����媛���
	@Override
	public int joinInsert(SnsVO vo) {
		int flag=0;
		
		try {
			conn=DriverManager.getConnection(url,user,password);
			String sql=joinSql;
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, vo.getEmail());
			pstmt.setString(2, vo.getPassword());
			pstmt.setString(3, vo.getNicname());
			pstmt.setString(4, vo.getJoinDate());
			
			flag=pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
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
	//濡�洹몄��
	@Override
	public SnsVO loginSearch(SnsVO vo) {
		SnsVO vo2=new SnsVO();
		String nic="";
		try {
			conn=DriverManager.getConnection(url,user,password);
			String sql=loginSql;
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, vo.getEmail());
			pstmt.setString(2, vo.getPassword());
			
			rs=pstmt.executeQuery();
			while(rs.next()){
				 nic=rs.getString("nicname");
			}
		
			if(!nic.equals("")){
				vo2.setEmail(vo.getEmail());//�대��쇨��� ����
				vo2.setNicname(nic);//���ㅼ��媛� 媛��몄�ㅺ린
			}else{
				vo2.setEmail("fail");//�ㅽ�④� ����
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
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
		return vo2;
	}
	//���대�� 以�蹂� 泥댄��
	@Override
	public int joinIdCheck(SnsVO vo) {
		int flag=0;
		try {
			conn=DriverManager.getConnection(url,user,password);
			String sql=joinIdSql;
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, vo.getEmail());
			
			rs=pstmt.executeQuery();
			while(rs.next()){
				 flag=rs.getInt("userok");//1�대㈃ �대��� 以�蹂�, 0�대㈃ �대��� ����
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
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
	
	//���ㅼ�� 以�蹂� 泥댄��
	@Override
	public int joinNicCheck(SnsVO vo) {
		int flag=0;
		try {
			conn=DriverManager.getConnection(url,user,password);
			String sql=joinNicSql;
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, vo.getNicname());
			
			rs=pstmt.executeQuery();
			while(rs.next()){
				 flag=rs.getInt("userok");//1�대㈃ ���ㅼ�� 以�蹂�, 0�대㈃ ���ㅼ�� ����
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
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
	//寃���湲� ���ㅼ�� 李얘린
	@Override
	public SnsVO boardfindnic(SnsVO vo) {
		SnsVO vo2=new SnsVO();
		String nic="";
		try {
			conn=DriverManager.getConnection(url,user,password);
			String sql=boardnicSearchSql;
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, vo.getNum());
			
			rs=pstmt.executeQuery();
			while(rs.next()){
				 nic=rs.getString("nicname");
			}
			vo2.setNicname(nic);//���ㅼ��媛� 媛��몄�ㅺ린
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
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
		return vo2;
	}
	@Override
	public SnsVO findUnum(SnsVO vo) {
SnsVO vo2=new SnsVO();
		
		int flag=0;
		try {
			conn=DriverManager.getConnection(url,user,password);
			pstmt=conn.prepareStatement(findUnum);
			pstmt.setString(1, vo.getEmail());
			rs=pstmt.executeQuery();
			
			while (rs.next()) {
				vo2.setNum(rs.getInt("unum"));
				vo2.setEmail(rs.getString("email"));
				vo2.setPassword(rs.getString("password"));
				vo2.setNicname(rs.getString("nicname"));
				vo2.setJoinDate(rs.getString("joinDate"));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
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
		return vo2;
	}
	@Override
	public List<SnsVO> searchList(SnsVO vo) {
		System.out.println("SearchList");
		List<SnsVO> list = new ArrayList<SnsVO>();
		try {
			conn=DriverManager.getConnection(url,user,password);
			String sql=friendSql;
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1,"%"+ vo.getNicname()+"%");
	
			rs=pstmt.executeQuery();
			while(rs.next()){
				SnsVO vo3=new SnsVO();
				 vo3.setNicname(rs.getString("nicname"));
				 vo3.setEmail(rs.getString("email"));
				 vo3.setNum(rs.getInt("unum"));
				 vo3.setPassword(rs.getString("password"));
				 list.add(vo3);
			}
			System.out.println(list.toString());
			
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
		return list;
	}
}
