
# Servlet 3.1 API Asynchronous processing error handling experiments

## Tomcat 7.0.54

http://localhost:8080/servlet3-async/error1?fail=true
- actual
  - status code: 500
  - standard status line; usually no response content. Sometimes standard TC error page is generated.
    - ==> Filed [bug 56739](https://issues.apache.org/bugzilla/show_bug.cgi?id=56739) in TC bug tracker.
- expected
  - status code: 500
  - "hello, erroneous world" message on status line; TC error page.

http://localhost:8080/servlet3-async/error2?fail=true
- actual
  - status code: 500 internal server error
  - app error.jsp content
- expected
  - status code: 500 internal server error
  - app error.jsp content

Note: Tried to reproduce the dispatching error condition encountered with TC 8.0.9
using [ab](http://httpd.apache.org/docs/2.2/programs/ab.html), but it couldn't be
reproduced with 5,000+ requests.


## Tomcat 8.0.9 (Http11NioProtocol connector)

http://localhost:8080/servlet3-async/error1?fail=true
- actual
  - status code: 500
  - standard status line; usually no response content. Sometimes standard TC error page is generated.
    - ==> Filed [bug 56739](https://issues.apache.org/bugzilla/show_bug.cgi?id=56739) in TC bug tracker.
- expected
  - status code: 500
  - "hello, erroneous world" message on status line; TC error page.

http://localhost:8080/servlet3-async/error2?fail=true
- actual
  - status code: 500 internal server error
  - app error.jsp content
- expected
  - status code: 500 internal server error
  - app error.jsp content

After a couple of requests a timeout will be encountered with the following error:
```
10-Jul-2014 01:40:33.145 SEVERE [http-nio-8080-exec-2] org.apache.catalina.connector.CoyoteAdapter.asyncDispatch Exception while processing an asynchronous request
 java.lang.IllegalStateException: Calling [asyncTimeout()] is not valid for a request with Async state [DISPATCHING]
	at org.apache.coyote.AsyncStateMachine.asyncTimeout(AsyncStateMachine.java:267)
	...
```

This issue occurs with:
- Ubuntu 14.04 / OpenJDK 1.7.0_55
- Mac OS X 10.8.5 / Oracle Java 1.7.0_55

Happens also with the Java Blocking Connector (Http11Protocol).

**this is caused by a bug in Tomcat. Filed bug [#56736](https://issues.apache.org/bugzilla/show_bug.cgi?id=56736)  in TC bug tracker.**

## Resin 4.0.40

http://localhost:8080/servlet3-async/error1?fail=true
- actual
  - status code: 500
  - "hello, erroneous world" message on status line + error page
- expected
  - status code: 500
  - "hello, erroneous world" message on status line + error page

http://localhost:8080/servlet3-async/error2?fail=true
- actual
  - status code: 500 internal server error
  - app error.jsp content
- expected
  - status code: 500 internal server error
  - app error.jsp content

Some invocation sequences seem to result in the following error:

```
java.lang.IllegalStateException: The servlet '/error1' at '/error1' does
not support async because the servlet or one of the filters does not support
asynchronous mode.  The servlet should be annotated with a @WebServlet(asyncSupported=true)
annotation or have a &lt;async-supported> tag in the web.xml.
	at com.caucho.server.http.HttpServletRequestImpl.startAsync(HttpServletRequestImpl.java:1503)
	at com.caucho.server.http.HttpServletRequestImpl.startAsync(HttpServletRequestImpl.java:1489)
	at fi.markoa.servlet3.AsyncErrorServlet1.doGet(AsyncErrorServlet1.java:19)
	...
```

One such sequence appears to be
- http://localhost:8080/servlet3-async/error2?fail=true
- http://localhost:8080/servlet3-async/error1

Whereas the following sequence seems to be OK:
- http://localhost:8080/servlet3-async/error1?fail=true
- http://localhost:8080/servlet3-async/error2?fail=true

**this is caused by a bug in Resin 4.0.40. Bug report [#5776](http://bugs.caucho.com/view.php?id=5776) has been filed in the Resin bug tracker.**

## Jetty 9.2.1.v20140609

http://localhost:8080/servlet3-async/error1?fail=true
- actual
  - status code: 500
  - "hello, erroneous world" message on status line + error page
- expected
  - status code: 500
  - "hello, erroneous world" message on status line + error page.

http://localhost:8080/servlet3-async/error2?fail=true
- actual
  - status code: 500 internal server error
  - app error.jsp content
- expected
  - status code: 500 internal server error
  - app error.jsp content
