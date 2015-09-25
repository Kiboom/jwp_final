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

public class DeleteAnswerController extends AbstractController {
	private static final Logger logger = LoggerFactory.getLogger(AddAnswerController.class);
	
	private Answer answer;
	private AnswerDao answerDao = AnswerDao.getInstance();
	private List<Answer> answers;
	private Question question;
	private QuestionDao questionDao = QuestionDao.getInstance();
	
	@Override
	public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long answerId = Long.parseLong(request.getParameter("answerId"));
		answer = answerDao.findByAnswerId(answerId);
		question = questionDao.findById(answer.getQuestionId());
		questionDao.minusCountOfComments(answer.getQuestionId());
		logger.info("minused count of comments!");
		logger.info(Integer.toString(question.getCountOfComment()));

		answerDao.delete(answerId);
		logger.info("the answer deleted!");

		ModelAndView mav = jstlView("redirect:/show.jsp");
		
		answers = answerDao.findAllByQuestionId(answer.getQuestionId());
		mav.addObject("question", question);
		mav.addObject("answers", answers);	
		
		return mav;
	}
}
