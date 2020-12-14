package com.kh.mynewboard.board.controller;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.kh.mynewboard.board.model.service.BoardReplyService;
import com.kh.mynewboard.board.model.service.BoardService;
import com.kh.mynewboard.board.model.vo.Board;

@Controller
//@RequestMapping(value="/board")	
//board를 1차적으로 걸어주고 다음 어노테이션 걸러줌 어노테이션은 바로 밑에 있는 것이 적용되는 것 
//메소드 이름 같아도 상관없음 하지만 파라미터까지 같다면 다형성으로도 자바에서 성립할 수 없음
public class BoardController {
	// 1. url에 뭐가 들어오는지
		// 2. view 부분 결정
		// 3. service 에 있는 이름 호출
	@Autowired
	private BoardService bService;
	@Autowired
	private BoardReplyService brService;
	public static final int LIMIT = 10;

	@RequestMapping(value = "/bList.do", method = RequestMethod.GET)
	
	public ModelAndView boardListService(
			//여기서 defaultValue 대신에 required = false를 사용할수도 있다, 처음 list로 들어가면 page 값이 없기 때문에
			@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "keyword", required = false) String keyword, 
			ModelAndView mv) {
			//required = 의 기본값은 true, true로 되어 잇으면 해당 필드 값이 꼭 있어야한다
			//만약 true인데 필드 값이 없는경우 예외가 발생한다
			//그래서 값이 잇을수도 있고 없을 수도 있는 경우에는 false를 사용한다
		try {
			int currentPage = page;
			// 한 페이지당 출력할 목록 갯수, 페이징
			int listCount = bService.totalCount();
			int maxPage = (int) ((double) listCount / LIMIT + 0.9);
			//검색
			if (keyword != null && !keyword.equals(""))
				mv.addObject("list", bService.selectSearch(keyword));
			else
			mv.addObject("list", bService.selectList(currentPage, LIMIT));
			mv.addObject("currentPage", currentPage);
			mv.addObject("maxPage", maxPage);
			mv.addObject("listCount", listCount);
			mv.setViewName("board/blist");
		} catch (Exception e) {
			mv.addObject("msg", e.getMessage());
			mv.setViewName("errorPage");
		}
		return mv;
	}

	@RequestMapping(value = "/bDetail.do", method = RequestMethod.GET)
	public ModelAndView boardDetail(@RequestParam(name = "board_num") String board_num,
			//page는 boardDetail.jsp에 있는 수정페이지로 이동할때 들고 들어가는 currentPage name값이다
			//원래는 String 형태이지만 @RequestParam이 int형으로 알아서 바꿔준다
			@RequestParam(name = "page", defaultValue = "1") int page, ModelAndView mv) {
//		try {
			int currentPage = page;
			// 한 페이지당 출력할 목록 갯수
			mv.addObject("board", bService.selectBoard(0, board_num));
			mv.addObject("commentList", brService.selectList(board_num));
			mv.addObject("currentPage", currentPage);
			mv.setViewName("board/boardDetail");

//		} catch (Exception e) {
//			mv.addObject("msg", e.getMessage());
//			mv.setViewName("errorPage");
//		}
		return mv;
	}

	@RequestMapping(value = "/bRenew.do", method = RequestMethod.GET)
	//위에 /bDetail.do와 똑같은 이름을 가지고 있지만 @RequestMapping 어노테이션이 바로 위에 걸려있고 파라미터 갯수와 종류가 다른 다형성으로 이루어져잇어서 
	//알아서 /bRenew.do에 걸린다 그래서 메소드 이름이 같아도 상관은 없지만 이름은 바꿔주는게 좋다
	public ModelAndView boardDetail(@RequestParam(name = "board_num") String board_num, ModelAndView mv) {
		try {
			mv.addObject("board", bService.selectBoard(1, board_num));
			mv.setViewName("board/boardRenew");
		} catch (Exception e) {
			mv.addObject("msg", e.getMessage());
			mv.setViewName("errorPage");
		}
		return mv;
	}

	@RequestMapping(value = "/writeForm.do", method = RequestMethod.GET)
	public String boardInsertForm(ModelAndView mv) {
		return "board/writeForm";
	}

	@RequestMapping(value = "/bInsert.do", method = RequestMethod.POST)
	public ModelAndView boardInsert(Board b, 
			//upfile의 경우 jsp에 잇는 input에 upfile을 적어줬기때문에 여기에 null값이 들어오는 경우는 없지만 
			//만약 null값이 들어올 수 잇을 경우 required = false를 사용해준다
			@RequestParam(name = "upfile", required = false) MultipartFile report,
			HttpServletRequest request, ModelAndView mv) {
		
			if (report != null && !report.equals(""))
				saveFile(report, request);
			b.setBoard_file(report.getOriginalFilename());
			bService.insertBoard(b);
			mv.setViewName("redirect:bList.do");
		
		return mv;
	}

	@RequestMapping(value = "/bUpdate.do", method = RequestMethod.POST)
	public ModelAndView boardUpdate(Board b, @RequestParam(name = "page", defaultValue = "1") int page,
	@RequestParam(name = "upfile") MultipartFile report, HttpServletRequest request, ModelAndView mv) {
		try {
			//첨부파일 저장
			if (report != null && !report.equals("")) {
				removeFile(b.getBoard_file(),request); // 기존에 있던 파일이 있다면 삭제해줘라
				saveFile(report, request); 
				b.setBoard_file(report.getOriginalFilename()); 
			}
			if(bService.updateBoard(b) != null) {
				mv.addObject("board_num", bService.updateBoard(b).getBoard_num()); //가지고 들어가는 것
				//bService.updateBoard(b) 얘가 null일 수 있다(업데이트 실패할 경우). 그래서 사실은 위와 같이 써주지 않는게 좋다
				mv.setViewName("redirect:bDetail.do"); //sendRedirect와 같은 개념,
				//방금 업데이트 했던 정보를 bDetail에서 다시 읽어나와야함
				mv.addObject("currentPage", page); //가지고 들어가는 것
			} else {
				//이전화면으로 이동
			}
				
		} catch (Exception e) { 
			mv.addObject("msg", e.getMessage()); 
			mv.setViewName("errorPage");
		}
			return mv; 
		}

	@RequestMapping(value = "/bDelete.do", method = RequestMethod.GET)
	public ModelAndView boardDelete(@RequestParam(name = "board_num") String board_num,
			@RequestParam(name = "page", defaultValue = "1") int page, HttpServletRequest request, ModelAndView mv) {
		try {
			Board b = bService.selectBoard(1, board_num);
			removeFile(b.getBoard_file(), request);
			bService.deleteBoard(board_num);
			mv.addObject("currentPage", page);
			mv.setViewName("redirect:bList.do");
		} catch (Exception e) {
			mv.addObject("msg", e.getMessage());
			mv.setViewName("errorPage");
		}
		return mv;
	}

	private void saveFile(MultipartFile report, HttpServletRequest request) {
		String root = request.getSession().getServletContext().getRealPath("resources");
		String savePath = root + "/uploadFiles";
		File folder = new File(savePath);
		if (!folder.exists()) {
			folder.mkdir(); // 폴더가 없다면 생성한다.
		}
		String filePath = null;
		try {
			// 파일 저장
			System.out.println(report.getOriginalFilename() + "을 저장합니다.");
			System.out.println("저장 경로 : " + savePath);
			filePath = folder + "/" + report.getOriginalFilename();
			report.transferTo(new File(filePath)); // 파일을 저장한다 
			System.out.println("파일 명 : " + report.getOriginalFilename()); 
			System.out.println("파일 경로 : " + filePath); System.out.println("파일 전송이 완료되었습니다.");
		} catch (Exception e) {
			System.out.println("파일 전송 에러 : " + e.getMessage());
		}
	}

	private void removeFile(String board_file, HttpServletRequest request) {
		String root = request.getSession().getServletContext().getRealPath("resources");
		String savePath = root + "/uploadFiles";
		String filePath = savePath + "/" + board_file;
		try { //잘못 지울 경우를 대비해 try-catch를 묶어준다
			// 파일 저장
			System.out.println(board_file + "을 삭제합니다.");
			System.out.println("기존 저장 경로 : " + savePath);
			File delFile = new File(filePath); //filePath의 주소를 delFile에 넣어준
			delFile.delete(); // 파일을 지운다
			System.out.println("파일 삭제가 완료되었습니다.");
		} catch (Exception e) {
			System.out.println("파일 삭제 에러 : " + e.getMessage()); 
			//geMessage는 마지막에 에러난 멧세지만 보여준다 
		}
	}
}
