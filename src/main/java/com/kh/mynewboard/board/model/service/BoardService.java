package com.kh.mynewboard.board.model.service;

import java.util.List;

import com.kh.mynewboard.board.model.vo.Board;

public interface BoardService {
	public int totalCount();

	public Board selectBoard(int chk, String board_num);

	public List<Board> selectList(int startPage, int limit);

	public List<Board> selectSearch(String keyword);

	public void insertBoard(Board b);
	
//	public int addReadCount(String board_num);

	public Board updateBoard(Board b);

	public void deleteBoard(String board_num);
}
