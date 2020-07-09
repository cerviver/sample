package com.com.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.com.dto.BoardVO;
import com.com.mapper.BoardMapper;

@Repository
public class BoardMybatisDAO implements BoardDAO{

	@Autowired
	private SqlSession sqlSession;
	
	@Override
	public int insertBoard(BoardVO board) {
		return sqlSession.getMapper(BoardMapper.class).insertBoard(board);
	}

	@Override
	public List<BoardVO> selectBoard(Map<String, Object> map) {
		return sqlSession.getMapper(BoardMapper.class).selectBoard(map);
	}

	@Override
	public int updateBoard(BoardVO board) {
		return sqlSession.getMapper(BoardMapper.class).updateBoard(board);
	}

	@Override
	public int deleteBoard(int no) {
		return sqlSession.getMapper(BoardMapper.class).deleteBoard(no);
	}

	@Override
	public BoardVO selectBoardNo(int no) {
		return sqlSession.getMapper(BoardMapper.class).selectBoardNo(no);
	}

	@Override
	public int selectCountBoard() {
		return sqlSession.getMapper(BoardMapper.class).selectCountBoard();
	}



}
