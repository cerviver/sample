package com.com.service;

import java.util.List;
import java.util.Map;

import com.com.dto.BoardVO;

public interface BoardService {
	void addBoard(BoardVO board);
	void modiBoard(BoardVO board);
	void eraseBoard(int no);

	List<BoardVO> getBoardList(Map<String, Object> map);
	BoardVO getBoardNo(int no);
	int getCountBoard();
}
