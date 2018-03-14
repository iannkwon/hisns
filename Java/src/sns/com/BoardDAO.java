package sns.com;

import java.util.List;

public interface BoardDAO {
	public int insert(BoardVO vo);
	public int insert2(BoardVO vo);
	public int update(BoardVO vo);
	public int delete(BoardVO vo);
	public BoardVO searchBoard(BoardVO vo); // each board search by num
	public List<BoardVO> selectBoardTit(BoardVO vo);//all board search by title
	public List<BoardVO> selectBoardCon(BoardVO vo);//all board search by content
	public int fileCheck(BoardVO vo);
	
	public List<BoardVO> selectBoardMap(BoardVO vo);//紐⑤�� 寃���湲� 媛��몄�ㅺ린
	public List<BoardVO> myBoard(BoardVO vo); // each board search by bnum
	public List<BoardVO> hidBoard(BoardVO vo); // each board search by bnum
}
