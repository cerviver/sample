package com.com.boardSample;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.com.dto.BoardVO;
import com.com.service.BoardService;
import com.com.util.Pager;

@Controller
public class BoardController {
	
	@Autowired
	private BoardService boardService;
	
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String main() throws Exception {
		System.out.println("index ========================");
		
		return "index";
	}
	
	@RequestMapping(value = "/board.do")
	public String board() throws Exception {
		return "board";
	}

	@RequestMapping(value = "/board2.do")
	public String board2() throws Exception {
		System.out.println("board2 ==========================");
		
		return "board2";
	}
	

	@RequestMapping(value = "/list.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> boardList(@RequestParam int pageNum) throws Exception {
		System.out.println("list ==========================");
		
		int totalArticle = boardService.getCountBoard();
		
		//System.out.println("pageNum = "+pageNum);
		
		int pageSize=10;
		int blockSize=5;
		Pager pager = new Pager(pageNum, totalArticle, pageSize, blockSize);
		
		Map<String, Object> pagerMap = new HashMap<String, Object>();
		
		pagerMap.put("startRow", pager.getStartRow());
		pagerMap.put("endRow", pager.getEndRow());
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("boardList", boardService.getBoardList(pagerMap));
		
		map.put("pager", pager);
		
		return map;
	}
	
	@RequestMapping(value = "/add.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> boardAdd(@RequestParam(value = "title",required = true)String title,
			@RequestParam(value = "contents",required = false, defaultValue = "")String contents,
			@RequestParam(value = "file",required = false) MultipartFile file) throws Exception{
		
		System.out.println("add ==========================");
		
		Map<String, Object> map=new HashMap<String, Object>();
		
		try {
			String repository = "C:/upload";
			String fname="";
			
			if(file != null && file.getSize() > 0) {
				fname = file.getOriginalFilename();
				FileOutputStream fos = new FileOutputStream(new File(repository+File.separator+fname));
				IOUtils.copy(file.getInputStream(), fos);
				fos.close();
			}
			
			BoardVO board=new BoardVO();
			
			board.setTitle(title);
			board.setContents(contents);
			board.setFname(fname);
			
			boardService.addBoard(board);
			
			map.put("returnCode", "success");
			map.put("returnDesc", "");
		} catch (Exception e) {
			map.put("returnCode", "error");
			map.put("returnDesc", "데이터 전송에 실패하였습니다.");
			
			e.printStackTrace();
		}
		
		return map;
	}
	
	@RequestMapping(value = "/add2.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> boardAdd2(@RequestParam(value = "title",required = true)String title,
			@RequestParam(value = "contents",required = false, defaultValue = "")String contents,
			@RequestParam(value = "file",required = false) List<MultipartFile> files) throws Exception{
		
		System.out.println("add2 ==========================");
		
		Map<String, Object> map=new HashMap<String, Object>();
		
		try {
			String repository = "C:/upload";
			String fnames="";
			
			for(MultipartFile file : files) {
				if(file != null && file.getSize() > 0) {
					String fname = file.getOriginalFilename();
					System.out.println("fname = "+fname);
					
					FileOutputStream fos = new FileOutputStream(new File(repository+File.separator+fname));
					IOUtils.copy(file.getInputStream(), fos);
					
					fos.close();
					
					if("".equals(fnames)) {
						fnames = fname;
					} else {
						fnames += ","+fname;
					}
				}
			}
			
			BoardVO board=new BoardVO();
			
			board.setTitle(title);
			board.setContents(contents);
			board.setFname(fnames);
			
			boardService.addBoard(board);
			
			map.put("returnCode", "success");
			map.put("returnDesc", "");
		} catch (Exception e) {
			map.put("returnCode", "error");
			map.put("returnDesc", "데이터 전송에 실패하였습니다.");
			
			e.printStackTrace();
		}
		
		return map;
	}
	
	@RequestMapping(value = "/modi.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> boardModi(@RequestParam(value="no", required = true) int no,
			@RequestParam(value = "title",required = true)String title,
			@RequestParam(value = "contents",required = false, defaultValue = "")String contents,
			@RequestParam(value = "file",required = false) MultipartFile file) throws Exception{
		
		System.out.println("modi ==========================");
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		
		try {
			String repository = "C:/upload";
			String fname="";
			
			if(file != null && file.getSize() > 0) {
				fname = file.getOriginalFilename();
				FileOutputStream fos = new FileOutputStream(new File(repository+File.separator+fname));
				IOUtils.copy(file.getInputStream(), fos);
				fos.close();
			}
			
			BoardVO board=boardService.getBoardNo(no);

			board.setTitle(title);
			board.setContents(contents);
			board.setFname(fname);
			
			boardService.modiBoard(board);
			
			map.put("returnCode", "success");
			map.put("returnDesc", "");
		} catch (Exception e) {
			map.put("returnCode", "error");
			map.put("returnDesc", "데이터 전송에 실패하였습니다.");
			
			e.printStackTrace();
		}
		
		return map;
	}
	
	@RequestMapping(value = "/modi2.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> boardModi2(@RequestParam(value="no", required = true) int no,
			@RequestParam(value = "title",required = true)String title,
			@RequestParam(value = "contents",required = false, defaultValue = "")String contents,
			@RequestParam(value = "file",required = false) List<MultipartFile> files) throws Exception{
		
		System.out.println("modi2 ==========================");
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		
		try {
			String repository = "C:/upload";
			String fnames="";
			
			for(MultipartFile file : files) {
				if(file != null && file.getSize() > 0) {
					String fname = file.getOriginalFilename();
					System.out.println("fname = "+fname);
					
					FileOutputStream fos = new FileOutputStream(new File(repository+File.separator+fname));
					IOUtils.copy(file.getInputStream(), fos);
					
					fos.close();
					
					if("".equals(fnames)) {
						fnames = fname;
					} else {
						fnames += ","+fname;
					}
				}
			}
			
			BoardVO board=boardService.getBoardNo(no);
			
			board.setTitle(title);
			board.setContents(contents);
			board.setFname(fnames);
			
			boardService.modiBoard(board);
			
			map.put("returnCode", "success");
			map.put("returnDesc", "");
		} catch (Exception e) {
			map.put("returnCode", "error");
			map.put("returnDesc", "데이터 전송에 실패하였습니다.");
			
			e.printStackTrace();
		}
		
		return map;
	}
	
	@RequestMapping(value = "/del.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> boardDel(@RequestParam(value="no", required = true) int no,
			@RequestParam(value = "title",required = true)String title,
			@RequestParam(value = "contents",required = false, defaultValue = "")String contents) throws Exception{
		
		System.out.println("del ==========================");
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			boardService.eraseBoard(no);
			
			map.put("returnCode", "success");
			map.put("returnDesc", "");
		} catch (Exception e) {
			map.put("returnCode", "error");
			map.put("returnDesc", "데이터 삭제에 실패하였습니다.");
		}
		
		return map;
	}
	
	@RequestMapping(value = "/delimg.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> boardDelimg(@RequestParam(value="no", required = true) int no,
			@RequestParam(value = "title",required = true)String title,
			@RequestParam(value = "contents",required = false, defaultValue = "")String contents) throws Exception{
		
		System.out.println("delimg ==========================");
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			BoardVO board=boardService.getBoardNo(no);

			String repository="C:/upload";
			String fname=repository+File.separator+board.getFname();
			
			File delFile = new File(fname);
			if(delFile.exists()) {
				System.out.println("del dir : "+fname);
				delFile.delete();
			}
			
			
			board.setFname("");
			
			boardService.modiBoard(board);
			
			map.put("returnCode", "success");
			map.put("returnDesc", "");
		} catch (Exception e) {
			map.put("returnCode", "error");
			map.put("returnDesc", "데이터 삭제에 실패하였습니다.");
		}
		
		return map;
	}
	
	@RequestMapping(value = "/img.do")
	@ResponseBody
	public String getImageWithMediaType(@RequestParam(value="fname", required=false, defaultValue="") String fname,
			HttpServletRequest request, HttpServletResponse response) throws IOException{
		System.out.println("img ============ "+fname);
		
		String repository = "C:/upload";
		
		String base64Encoded = "";
		if(fname.contains(",")) {
			fname = fname.substring(0,fname.indexOf(","));
		}
		
		fname = repository+File.separator+fname;
		
		System.out.println("dir : "+fname);
		
		File file = new File(fname);
		
		if(file.exists() && file.isFile()) {
			InputStream in = new FileInputStream(fname);
			byte[] bytes = IOUtils.toByteArray(in);
			byte[] encodedBasd64 = Base64.getEncoder().encode(bytes);
			base64Encoded = new String(encodedBasd64, "UTF-8");
			in.close();
		}
		
		return base64Encoded;
	}
	
	@RequestMapping(value = "/img2.do")
	@ResponseBody
	public String getImageWithMediaType2(@RequestParam(value="fname", required=false, defaultValue="") String fname,
			HttpServletRequest request, HttpServletResponse response) throws IOException{
		System.out.println("img2 ============ "+fname);
		String repository = "C:/upload";
		
		String rtnTag="";
		String base64Encoded = "";
		
		String[] fnameList = fname.split(",");
		
		for(int i=0; i<fnameList.length; i++) {
			fname = repository+File.separator+fnameList[i];
			
				File file = new File(fname);
				
				if(file.exists() && file.isFile()) {
					InputStream in = new FileInputStream(fname);
					byte[] bytes = IOUtils.toByteArray(in);
					byte[] encodedBasd64 = Base64.getEncoder().encode(bytes);
					base64Encoded = new String(encodedBasd64, "UTF-8");
					in.close();
					
					rtnTag += "<img src=\"data:image/jpeg;base64,"+base64Encoded+"\" alt=\"image\" id=\"imgs\" style=\"max-width: 100%\"/></br></br>";
				}
		}
		
		//System.out.println("rtnTag = "+rtnTag);
		
		return rtnTag;
	}

}
