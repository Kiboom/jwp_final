#### 1. Tomcat 서버를 시작할 때 웹 애플리케이션이 초기화하는 과정을 설명하라.
* ##### 1) ContextLoaderListener
Tomcat 서버가 시작하여 WebServerLaunch가 구동되면, ContextLoaderListener가 시행된다.
ContextLoaderListener는 ServletContextListener를 구현하는 클래스이다. 이 클래스는 웹 애플리케이션의 라이프 사이클을 감지하여 그에 따라 사용자가 정의한 코드를 실행한다. ContextLoaderListener 안에는 contextInitialized( )라는 메서드가 있다. 이 메서드는 WebServerLaunch가 웹 애플리케이션을 초기화 했을 때 이를 감지하여 사용자가 정의한 초기화 과정을 실행한다. 현재 프로젝트에서 contextInitialized에 정의한 초기화는 데이터베이스와 관련된 초기화다. 왜냐면 DAO 객체나 Connection 관련 객체들은 여러 Controller들이 공통적으로 사용하는데, 매번 인스턴스가 생성되면 많은 garbage이 발생하여 반응시간이 느려지기 때문이다. contextInitialized 메서드에서는 이러한 중복을 제거하기 위해 ResourceDatabasePopulator를 이용한다. ConnectionManager의 getDataSource를 통해 DB 연결 정보를 얻고, 이를 통해 DB와의 연결을 초기화하는 작업을 시행한다. 이렇게 초기화 작업이 완료되면 “Completed Load SerletContext!”라는 로그를 찍는다.

* ##### 2) DispatcherServlet
<B>(Q. ContextLoaderListener에서 DispatcherServlet으로 넘어가는 과정을 아직 잘 모르겠습니다)</B> DispatcherServlet이 서블릿 컨테이너에 담기면 init( ) 메서드를 통해 초기화가 이뤄진다. DispatcherServlet에서 재정의된 init에서는, RequestMapping의 initMapping 메서드를 통해 서블릿과 주소를 연결하는 작업을 한다. RequestMapping 안에 있는 Map에 주소와 서블릿 객체를 짝지어 담는 것이다. 즉, @WebServlet( )의 역할을 한 군데서 처리하는 것이다. 이렇게 한 클래스 안에서 매핑을 처리하는 이유는, 다른 여러 서블릿들의 중복을 DispatcherServlet 한 군데서 도맡아 처리하기 위해서다.


#### 2. Tomcat 서버를 시작한 후 http://localhost:8080으로 접근시 호출 순서 및 흐름을 설명하라.
* 1) localhost:8080에 접속하면 브라우저는 서버에 index.jsp 페이지를 요청한다.
* 2) index.jsp의 response.sendRedirect(“/list.next”) 코드에 의해 /list.next URI를 다시 요청한다.
* 3) <B>(Q. 서블릿 컨테이너가 DispatcherServlet을 호출하는 과정을 아직 명확히 이해 못했습니다)</B> 클라이언트로부터 요청을 받은 서블릿 컨테이너는 (HttpServlet로부터 상속받은) DispatcherServlet의 service( ) 메서드를 호출한다. 이 때 서블릿 컨테이너는 request와 response에 대한 정보를 인자로 전달해준다. 
* 4) service 메서드는 request 객체를 통해 사용자가 어떤 URI를 요청했는지 파악하고, 그 URI가 어떤 컨트롤러 서블릿과 연결되어있는지 찾는다. 즉, “/list.next”와 연결되어 있는 ListController를 찾는다.
* 5) 모든 컨트롤러는 execute라는 메서드를 상속받는다. execute 메서드는 컨트롤러가 어떤 view를 쏴줄 것인지, 그리고 어떤 데이터를 다룰 것인지에 대한 정보를 담는다. 이러한 정보는 ModelAndView라는 객체에 담겨서 반환된다! 
* 6) 구체적으로 ModelAndView에는 다음 정보가 들어간다. 
<BR>** a. 컨트롤러가 다룰 jsp 페이지 이름
<BR>** b. view를 보내는 형식이 jstl인지 json인지
<BR>** c. view에서 활용할 데이터가 담긴 리스트 객체들 
<BR>a와 b는 ModelAndView 생성자를 통해서 저장되며, c는 ModelAndView의 addObject 메서드를 활용해서 Map에 넣는다.
* 7) 컨트롤러의 execute를 실행하여 ModelAndView를 반환받은 DispatcherServlet은, View 객체의 render( ) 메서드를 통해 새로운 페이지를 forward한다. View는 JstlView와 JsonView의 인터페이스로서, 각각의 객체는 View의 render 메서드를 Override하고 있다. 
* 8) render( ) 메서드가 실행되면 일단 “viewName" 변수를 통해 어떤 jsp를 보낼 것인지 파악한다. 
* 9) viewName이 만약 Redirect 형식이 아니라면 인자로 전달 받은 데이터를 setAttribute 처리한다. 
* 10) 마지막으로 RequestDispatcher를 통해 list.jsp를 클라이언트에 전달한다.
* <B>(Q. 아직 filter가 어떤 흐름에 의해 실행되는지는 파악하지 못했습니다..)</B>

#### 8. ListController와 ShowController가 멀티 쓰레드 상황에서 문제가 발생하는 이유에 대해 설명하라.
* 기본적으로 Servlet은 톰캣 컨테이너에서 하나의 인스턴스로 만들어져서 여러 쓰레드가 공유하는 방식이다. 
* 이런 경우 Servlet 안에 인스턴스 변수를 만들어 놓으면 해당 변수에 여러 쓰레드가 접근 가능하게 된다. 여러 사람이 동시에 해당 변수에 접근하게 되면 상대방이 수정한 결과가 나에게 전달되어 보이는 문제가 발생할 수 있다.

