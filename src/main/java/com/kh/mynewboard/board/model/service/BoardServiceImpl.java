package com.kh.mynewboard.board.model.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.mynewboard.board.model.dao.BoardDao;
import com.kh.mynewboard.board.model.vo.Board;

@Service("bService")
public class BoardServiceImpl implements BoardService {
	@Autowired
	private BoardDao bDao;

	@Override
	public int totalCount() {
		return bDao.listCount();
	}

	@Override
	public Board selectBoard(int chk, String board_num) {

		if (chk == 0)
			bDao.addReadCount(board_num);
		return bDao.selectOne(board_num);
	}

	@Override
	public List<Board> selectList(int startPage, int limit) {
		return bDao.selectList(startPage, limit);

	}

	@Override
	public List<Board> selectSearch(String keyword) {
		return bDao.searchList(keyword);
	}

	@Override
	public void insertBoard(Board b) {
		bDao.insertBoard(b);
	}
	
////TEST
	//update로 시작하는 service와 dao의 메소드에 aop를 설정하도록 pointcut
	//expression="execution(* com.kh.mynewboard..Board*.update*(..)))"
	
	@Override
//	@Transactional(rollback = SQLException) //트랜잭션을 어노테이션으로 처리하는 방법
	public Board updateBoard(Board b) {
		Board b1 = new Board("tx1", "tx1", "tx1", null, "tx1");
		int result1 = bDao.insertBoard(b1); // 여기서 해러가나면 아래 update를 수행하지 않는다

		int result = bDao.updateBoard(b);
		System.out.println("result1 : "+ result);
		if (result > 0) { // 읽어나온게 있다면
			System.out.println("result2 : "+ result);
			b = bDao.selectOne(b.getBoard_num());
		} else {
//			throw new MemberNotFoundExcep(); 위에 트랜잭션 어노테이션에 별 다른걸 쓰지 않는다면 여기서 써줄수 있다
			System.out.println("result3 : "+ result);
			b = null; //읽어나온게 없다면
		}
		return b;
	}

	@Override
	public void deleteBoard(String board_num) { // delete의 결과물을 컨트롤러로 보낼필요가 없을때는 형태를 void로 준다
		bDao.deleteBoard(board_num); // 컨트롤러로 보낼필요가 없을때 return도 없어도 된다
		//하지만 만약 삭제 했다 안했다의 결과를 가져나오고 싶다면 자료형은 int, return값도 있어야한다
	}
}
