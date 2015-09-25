package next.controller.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.mvc.AbstractController;
import core.mvc.ModelAndView;
import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.model.Answer;
import next.model.Question;

public class AddAnswerController extends AbstractController {
	private static final Logger logger = LoggerFactory.getLogger(AddAnswerController.class);
	
	private Answer answer;
	private AnswerDao answerDao = AnswerDao.getInstance();
	private List<Answer> answers;
	private Question question;
	private QuestionDao questionDao = QuestionDao.getInstance();
	
	@Override
	public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long questionId = Long.parseLong(request.getParameter("questionId"));
		String writer = request.getParameter("writer");
		String contents = request.getParameter("contents");
		answer = new Answer(writer, contents, questionId);
		answerDao.insert(answer);
		logger.info("new answer inserted!");
		
		question = questionDao.findById(questionId);
		questionDao.plusCountOfComments(questionId);
		logger.info("plused count of comments!");
		logger.info(Integer.toString(question.getCountOfComment()));
		
		ModelAndView mav = jstlView("redirect:/show.jsp");
		answers = answerDao.findAllByQuestionId(questionId);
		mav.addObject("question", question);
		mav.addObject("answers", answers);	
		
		return mav;
	}

}
