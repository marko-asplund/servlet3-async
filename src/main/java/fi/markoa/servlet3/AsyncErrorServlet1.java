package fi.markoa.servlet3;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet 3.1 API / Asynchronous processing / error handling: approach #1
 *
 * @author marko asplund
 */
@WebServlet(urlPatterns={"/error1"}, asyncSupported=true)
public class AsyncErrorServlet1 extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    final AsyncContext asyncContext = req.startAsync();
    asyncContext.start(new Runnable() {
      @Override
      public void run() {
        if(isOperationFailed(asyncContext.getRequest().getParameter("fail"))) {
          sendError(asyncContext, 500, "hello, erroneous world");
        } else {
          try {
            asyncContext.getResponse().getWriter().write(generateResponse());
          } catch (IOException e1) {
            e1.printStackTrace();
          }
        }
        asyncContext.complete(); // AsyncContext must be completed in both scenarios
      }
    });
  }

  private void sendError(AsyncContext asyncContext, int statusCode, String message) {
    if(asyncContext.getResponse() instanceof HttpServletResponse)
      try {
        ((HttpServletResponse) asyncContext.getResponse()).sendError(statusCode, message);
      } catch (IOException e) {
        e.printStackTrace();
      }
  }

  private String generateResponse() {
    return "hello, happy world";
  }

  private boolean isOperationFailed(String fail) {
    return Boolean.valueOf(fail);
  }
}
