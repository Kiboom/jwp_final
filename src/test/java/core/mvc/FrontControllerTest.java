package core.mvc;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class FrontControllerTest {
	private DispatcherServlet frontController;
	
	@Before
	public void setup() {
		frontController = new DispatcherServlet();
	}

	@Test
	public void urlExceptParameter() throws Exception {
		assertThat(frontController.urlExceptParameter("/show.next"), is("/show.next"));
		assertThat(frontController.urlExceptParameter("/show.next?id=2"), is("/show.next"));
	}
}
