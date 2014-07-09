package fi.markoa.servlet3;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet 3.1 API / Asynchronous processing / error handling: approach #2
 */
@WebServlet(urlPatterns={"/error2"}, asyncSupported=true)
public class AsyncErrorServlet2 extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    final AsyncContext asyncContext = req.startAsync();
    asyncContext.start(new Runnable() {
      @Override
      public void run() {
        if(isOperationFailed(asyncContext.getRequest().getParameter("fail"))) {
          errorDispatch(asyncContext, 500, "hello, erroneous world");
        } else {
          try {
            asyncContext.getResponse().getWriter().write(generateResponse());
            asyncContext.complete();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
    });
  }

  private void errorDispatch(AsyncContext asyncContext, int statusCode, String message) {
    asyncContext.getRequest().setAttribute("statusCode", statusCode);
    asyncContext.getRequest().setAttribute("message", message);
    asyncContext.dispatch("/jsp/error.jsp");
  }

  private String generateResponse() {
    return "hello, happy world";
  }

  private boolean isOperationFailed(String fail) {
    return Boolean.valueOf(fail);
  }
}
