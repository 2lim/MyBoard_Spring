package com.kh.mynewboard.board.model.service;

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

	@Override
	public Board updateBoard(Board b) {
		int result = bDao.updateBoard(b);
		if (result > 0) { // 읽어나온게 있다면
			b = bDao.selectOne(b.getBoard_num());
		} else {
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
