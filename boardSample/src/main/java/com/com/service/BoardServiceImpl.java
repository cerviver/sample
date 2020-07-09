package com.com.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.com.dao.BoardDAO;
import com.com.dto.BoardVO;

@Service
public class BoardServiceImpl implements BoardService{
	
	@Autowired
	private BoardDAO boardDAO;
	
	@Transactional
	@Override
	public void addBoard(BoardVO board) {
		boardDAO.insertBoard(board);
	}

	@Override
	public List<BoardVO> getBoardList(Map<String, Object> map) {
		return boardDAO.selectBoard(map);
	}

	@Override
	public void modiBoard(BoardVO board) {
		boardDAO.updateBoard(board);
	}

	@Override
	public void eraseBoard(int no) {
		boardDAO.deleteBoard(no);
	}

	@Override
	public BoardVO getBoardNo(int no) {
		return boardDAO.selectBoardNo(no);
	}

	@Override
	public int getCountBoard() {
		return boardDAO.selectCountBoard();
	}
	
}
