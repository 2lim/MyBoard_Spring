package com.kh.mynewboard.board.model.dao;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.mynewboard.board.model.vo.Board;

@Repository("bDao")
public class BoardDao {
	@Autowired
	private SqlSession sqlSession;

	public int listCount() { // 전체 글 수 조회
		return sqlSession.selectOne("Board.listCount");
	}

	public Board selectOne(String board_num) { // 글 가져오기 
		return sqlSession.selectOne("Board.selectOne", board_num);
	}
	////Test
//	JointPoint
	//expression="execution(* com.kh.mynewboard..*Impl.*(..)))" 를 아래처럼 수정
	
	//selectList()에 걸리게 하고 싶을 경우
//	-> "execution(* com.kh.myspringb..BoardServiceImpl.selectList()))"
	//listCount에도걸리고 selectList에도 걸리게 하고 싶을 경우
//	->"execution(* com.kh.myspringb..BoardDao.*()))"
	//selectOne, addReadCount, deleteBoard에 걸고 싶을때
//	->"execution(* com.kh.myspringb..BoardDao.*(String)))" // 여러개 걸고자할때 공통점이 잇다면 공통점 하나만 작성
	
//	->"execution(* com.kh.myspringb..BoardDao.*(int, int)))" // 여러개 걸려고하는데 공통점이 없다면 , 로 구분
//	->"execution(* com.kh.myspringb..BoardDao.*(int, ..)))" // int도 있고 ..(더 있을수도 잇고 없을 수도 있고)

	
	public List<Board> searchList(String keyword) { // 게시글 검색 조회 
		return sqlSession.selectList("Board.searchList", keyword);
	}

	public List<Board> selectList(int startPage, int limit) { // 특정 페이지 단위의 게시글 조회
		//limit : 한페이지당 글 개수
		int startRow = (startPage - 1) * limit; // 시작 페이지를 가져옴, 0~9, 10~19
		RowBounds row = new RowBounds(startRow, limit); //ibatis 세션의 rowbounds
//		new RowBounds(offset, limit) : 여기서 offset은 만약 offset 값을 10이라고 한다면 앞에 값은 없다 치고 10부터 시
		return sqlSession.selectList("Board.selectList",null,row);
	}

	public int insertBoard(Board b) { // 글 입력 
		return sqlSession.insert("Board.insertBoard",b);
	}

	public int addReadCount(String board_num) { // 글 조회 수 증가 
		return sqlSession.update("Board.addReadCount", board_num);
	}

	public int updateBoard(Board b) { // 글 수정 
		return sqlSession.update("Board.updateBoard", b);
	}

	public int deleteBoard(String board_num) { // 글 삭제 
		return sqlSession.delete("Board.deleteBoard", board_num);
	}
}
