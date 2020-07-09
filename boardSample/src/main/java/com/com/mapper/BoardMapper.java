package com.com.mapper;

import java.util.List;
import java.util.Map;

import com.com.dto.BoardVO;

public interface BoardMapper {
	int insertBoard(BoardVO board);
	int updateBoard(BoardVO board);
	int deleteBoard(int no);

	List<BoardVO> selectBoard(Map<String, Object> map);
	BoardVO selectBoardNo(int no);
	int selectCountBoard();
}
