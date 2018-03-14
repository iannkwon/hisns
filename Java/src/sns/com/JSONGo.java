package sns.com;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Servlet implementation class JSONGo
 */
@WebServlet({"/joinGo.do","/loginGo.do","/Boardwrite.do","/HidBoardwrite.do","/BoardMapView.do"
	,"/BoardSearch.do","/myBoard.do","/hiddenBoard.do","/searchGo.do"})
public class JSONGo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public JSONGo() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		doProcess(request, response);
	}
	
	protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "nocache");
		response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();
        
        
    	if(request.getServletPath().equals("/joinGo.do")){//����媛���
    		String email=request.getParameter("email");    		
    		String password=request.getParameter("password");    		
    		String nicname=request.getParameter("nicname");    		
    		String joinDate=request.getParameter("joindate");
    		
    		if(!email.equals("") && !password.equals("") && !nicname.equals("") && !joinDate.equals("")){
    			SnsVO vo=new SnsVO();
        		SnsDAO dao=new SnsDAOimple();
        		
        		vo.setEmail(email);
        		vo.setPassword(password);
        		vo.setNicname(nicname);
        		vo.setJoinDate(joinDate);
        		
        		int result1=dao.joinIdCheck(vo);//�대��� 泥댄��
        		int result2=dao.joinNicCheck(vo);//���ㅼ�� 泥댄��
        		
        		if(result1==1){ //�대��쇱�� �ъ�⑷��ν��硫�
        			out.append("emfail");
        		}if(result2==1){ //���ㅼ���� �ъ�⑷��ν��硫�
        			out.append("nicfail");
        		}else if(result1==0 && result2==0){
        			int result3=dao.joinInsert(vo);//����媛���
            		if(result3==1){
            			out.append("joinsuccess");
            			System.out.println("揶�占쏙옙占쏙옙源���");
            		}else{
            			out.append("joinfail");
            			System.out.println("揶�占쏙옙占쏙옙�쏙옙占�");
            		}
        		}
    		}
    	}else if(request.getServletPath().equals("/loginGo.do")){//濡�洹몄��
    		String email=request.getParameter("email");    		
    		String password=request.getParameter("password");
    		
    		if(!email.equals("") && !password.equals("")){
    			SnsVO vo=new SnsVO();
        		SnsDAO dao=new SnsDAOimple();
        		
        		vo.setEmail(email);
        		vo.setPassword(password);
        		SnsVO vo2=dao.loginSearch(vo);
        		
        		if(vo2.getEmail().equals("fail")){
        			out.append("fail");
        		}else{
            		JSONObject jObj = new JSONObject();
            		jObj.put("email", vo2.getEmail());
            		jObj.put("nicname", vo2.getNicname());
            		
            		out.append(jObj.toString());
        		}
    		}else{
    			out.append("fail");
    		}
    	}else if(request.getServletPath().equals("/Boardwrite.do")){
    		String btitle=request.getParameter("btitle");    		
    		String bcontent=request.getParameter("bcontent");    		
    			String unum = request.getParameter("unum");
    			System.out.println("String - unum:"+unum);
    			SnsVO vo1=new SnsVO();
    			SnsVO vo3=new SnsVO();
    			SnsDAO dao1=new SnsDAOimple();
    			
    			vo1.setEmail(unum);
				vo3 = dao1.findUnum(vo1);
			
				int num=  vo3.getNum();
				System.out.println(num);  		
    		String wdate=request.getParameter("wdate");
    		String blocation=request.getParameter("blocation");
    		String blocation2=request.getParameter("blocation2");
    		String bfilenum=request.getParameter("bfilenum");
//    		if(bfilenum==""){
//    			Date sd = new Date(System.currentTimeMillis());//날짜 가져오기
//                SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//    			bfilenum=String.valueOf(df2.format(sd));
//    		}
    		System.out.println("btitle"+btitle);
    		System.out.println("bcontent"+bcontent);
    		System.out.println("unum"+unum);
    		System.out.println("wdate"+wdate);
    		System.out.println("blocation"+blocation);
    		System.out.println("bfilenum"+bfilenum);
    		if(!btitle.equals("") && !bcontent.equals("") && !wdate.equals("")){
    			BoardVO vo=new BoardVO();
    			BoardDAO dao=new BoardDAOimple();
        		
        		vo.setBtitle(btitle);
        		vo.setBcontent(bcontent);
        		vo.setUnum(num);
        		vo.setWdate(wdate);
        		vo.setBlocation(blocation);
        		vo.setBlocation2(blocation2);
        		vo.setBfilenum(bfilenum);
        		
        		int result1=dao.fileCheck(vo);
        		
        		if(result1==1){ 
        			out.append("fcfail");
        		}else if(result1==0){
        			int result2=dao.insert(vo);
            		if(result2==1){
            			out.append("writeok");
            			System.out.println("board insert ok");
            		}else{
            			out.append("writefail");
            			System.out.println("board insert fail");
            		}
        		}
    		}
//    		String dir = application.getRealPath("uploadimg");
//    		System.out.println("file...getRealPath>>"+dir);
//    		
//    		
//    		// 2. 理��� �ш린 
//    		int max = 10 * 1024 * 1024;
//
//    		boolean isMultipart = 
//    				ServletFileUpload.isMultipartContent(request);
//
//    		String comment1 = "";
//    		String comment2 = "";
//    		String originalName = "";
//    		String saveName = "";
//
//    		if (isMultipart) {
//    			File temporaryDir = new File(dir);
//    			DiskFileItemFactory factory = new DiskFileItemFactory();
//    			factory.setSizeThreshold(max);
//    			factory.setRepository(temporaryDir);
//    			ServletFileUpload upload = new ServletFileUpload(factory);
//
//    			List<FileItem> items = null;
//    			try {
//    				items = upload.parseRequest((RequestContext) request);
//    				System.out.println("items.size():" + items.size());
//    				for(FileItem fi : items){
//    					String fieldName = fi.getFieldName();
//    					if(fieldName.equals("file")){
//    						System.out.println(fieldName+":" + fi.getName());
//    						if(fi.getName().length() !=0){
//    		 					originalName = fi.getName();
//    		 					System.out.println(originalName+":" + originalName);
//    							
//    		 					long nowTime = System.currentTimeMillis();
//    		 					String format = originalName.substring(originalName.lastIndexOf('.'));
//    		 					saveName = nowTime+format;
//    		 					
//    		 					try {
//    								File uploadedFile = new File(dir, saveName);
//    								fi.write(uploadedFile);
//    							} catch (Exception e) {
//    								System.out.println(e);
//    							}
//    		 				}
//    					}
//    					
//    					if(fieldName.equals("multipartFile")){
//    						System.out.println(fieldName+":" + fi.getName());
//    						if(fi.getName().length() !=0){
//    		 					originalName = fi.getName();
//    		 					System.out.println(originalName+":" + originalName);
//    							
//    		 					long nowTime = System.currentTimeMillis();
//    		 					String format = originalName.substring(originalName.lastIndexOf('.'));
//    		 					saveName = nowTime+format;
//    		 					
//    		 					try {
//    								File uploadedFile = new File(dir, saveName);
//    								fi.write(uploadedFile);
//    							} catch (Exception e) {
//    								System.out.println(e);
//    							}
//    		 				}
//    					}
//    					
//    					
//    				}
//    			} catch (FileUploadException fe) {
//    				System.out.println(fe);
//    			}
//    		}
    	}else if(request.getServletPath().equals("/HidBoardwrite.do")){
    		String btitle=request.getParameter("btitle");    		
    		String bcontent=request.getParameter("bcontent");    		
    		String unum = request.getParameter("unum");
			System.out.println("String - unum:"+unum);
			SnsVO vo1=new SnsVO();
			SnsVO vo3=new SnsVO();
			SnsDAO dao1=new SnsDAOimple();
			
			vo1.setEmail(unum);
			vo3 = dao1.findUnum(vo1);
			
			int num=  vo3.getNum();
			System.out.println(num);
//    		int unum= Integer.parseInt(request.getParameter("unum"));    		
    		String wdate=request.getParameter("wdate");
    		String blocation=request.getParameter("blocation");
    		String blocation2=request.getParameter("blocation2");
    		String bfilenum=request.getParameter("bfilenum");
    		System.out.println("btitle"+btitle);
    		System.out.println("bcontent"+bcontent);
    		System.out.println("unum"+unum);
    		System.out.println("wdate"+wdate);
    		System.out.println("blocation"+blocation);
    		System.out.println("bfilenum"+bfilenum);
    		if(!btitle.equals("") && !bcontent.equals("") && !wdate.equals("")){
    			BoardVO vo=new BoardVO();
    			BoardDAO dao=new BoardDAOimple();
        		
        		vo.setBtitle(btitle);
        		vo.setBcontent(bcontent);
        		vo.setUnum(num);
        		vo.setWdate(wdate);
        		vo.setBlocation(blocation);
        		vo.setBlocation2(blocation2);
        		vo.setBfilenum(bfilenum);
        		
        		int result1=dao.fileCheck(vo);
        		
        		if(result1==1){ 
        			out.append("fcfail");
        		}else if(result1==0){
        			int result2=dao.insert2(vo);
            		if(result2==1){
            			out.append("writeok");
            			System.out.println("board insert ok");
            		}else{
            			out.append("writefail");
            			System.out.println("board insert fail");
            		}
        		}
    		}
    	}else if(request.getServletPath().equals("/BoardMapView.do")){//지도에 마커표시
    		String blocation=request.getParameter("blocation");    		
    		String blocation2=request.getParameter("blocation2");
    		
    		BoardVO vo=new BoardVO();
			BoardDAO dao=new BoardDAOimple();
			
			vo.setBlocation(blocation);
			vo.setBlocation2(blocation2);
			
			List<BoardVO> result5 = dao.selectBoardMap(vo);
			JSONArray jArray = new JSONArray(result5);
			out.append(jArray.toString());
    	}else if(request.getServletPath().equals("/BoardSearch.do")){//해당 게시글정보 가져옴
    		String bnum=request.getParameter("bnum"); 
    		
    		SnsVO vo1=new SnsVO();
			SnsDAO dao1=new SnsDAOimple();
			
    		BoardVO vo2=new BoardVO();
			BoardDAO dao2=new BoardDAOimple();
			
			vo2.setBnum(Integer.parseInt(bnum));
			vo2=dao2.searchBoard(vo2);
			
			//board usernumber-> usernicname
			vo1.setNum(vo2.getUnum());
			vo1=dao1.boardfindnic(vo1);
			
			JSONObject object=new JSONObject(vo2);
			object.put("nicname",vo1.getNicname());//닉네임값 추가
			out.append(object.toString());

			System.out.println(object.toString());
    	}else if(request.getServletPath().equals("/myBoard.do")){
    		String unum = request.getParameter("unum");
			System.out.println("String - unum:"+unum);
			SnsVO vo1=new SnsVO();
			SnsVO vo3=new SnsVO();
			SnsDAO dao1=new SnsDAOimple();
			
			vo1.setEmail(unum);
			vo3 = dao1.findUnum(vo1);
			
			int num=  vo3.getNum();
			System.out.println(num);
			
			
			BoardVO vo=new BoardVO();
			BoardDAO dao=new BoardDAOimple();
			
			vo.setUnum(num);
			
			List<BoardVO> list = new ArrayList<BoardVO>();
			list = dao.myBoard(vo);
			
			JSONArray list1 = new JSONArray(list);
			System.out.println(list1.toString());
			out.append(list1.toString());
    	}else if(request.getServletPath().equals("/hiddenBoard.do")){
    		String unum = request.getParameter("unum");
			System.out.println("String - unum:"+unum);
			SnsVO vo1=new SnsVO();
			SnsVO vo3=new SnsVO();
			SnsDAO dao1=new SnsDAOimple();
			
			vo1.setEmail(unum);
			vo3 = dao1.findUnum(vo1);
			
			int num=  vo3.getNum();
			System.out.println(num);
			
			
			BoardVO vo=new BoardVO();
			BoardDAO dao=new BoardDAOimple();
			
			vo.setUnum(num);
			
			List<BoardVO> list = new ArrayList<BoardVO>();
			list = dao.hidBoard(vo);
			
			JSONArray list1 = new JSONArray(list);
			System.out.println(list1.toString());
			out.append(list1.toString());
    	}else if(request.getServletPath().equals("/searchGo.do")){//서블릿 설정
    		
    		String nicname=request.getParameter("searchtot"); //안드로이드로 부터 데이트 수신
    		System.out.println(nicname);
    		
    			SnsVO vo=new SnsVO();
        		SnsDAO dao=new SnsDAOimple();
        		
        		vo.setNicname(nicname);
        		
        		List<SnsVO> list =new ArrayList<SnsVO>();
        		list=dao.searchList(vo);
        		
        		JSONArray list1 = new JSONArray(list);
	
        		
        		System.out.println(list1.toString());
        		
        		out.append(list1.toString());
    	}
    		
	}
}
