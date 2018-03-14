package sns.com;

import java.util.List;

public interface SnsDAO {
	
	public int joinInsert(SnsVO vo);//����媛���
	
	public SnsVO loginSearch(SnsVO vo);//濡�洹몄��
	
	public int joinIdCheck(SnsVO vo);//���대�� 以�蹂�
	
	public int joinNicCheck(SnsVO vo);//���ㅼ�� 以�蹂�
	
	public SnsVO boardfindnic(SnsVO vo); //寃���湲� ���ㅼ�� 李얘린
	
	public SnsVO findUnum(SnsVO vo);
	
	public List<SnsVO> searchList(SnsVO vo);//친구검색
}
