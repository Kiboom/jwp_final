package next.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.mvc.AbstractController;
import core.mvc.ModelAndView;
import next.dao.QuestionDao;
import next.model.Question;

public class SaveController extends AbstractController {
	private static final Logger logger = LoggerFactory.getLogger(SaveController.class);
	private QuestionDao questionDao = QuestionDao.getInstance();
	
	
	@Override
	public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Question question;
		List<Question> questions;

		String writer = request.getParameter("writer");
		String title = request.getParameter("title");
		String contents = request.getParameter("contents");
		question = new Question(writer, title, contents);
		questionDao.insert(question);
		logger.info("new question inserted!");		// 왜 DB에 영구적으로 저장이 안되지?
		
		questions = questionDao.findAll();
		ModelAndView mav = jstlView("redirect:/list.next");	// redirect로 만들어야하나?
		mav.addObject("questions", questions);

		return mav;	
	}

}
