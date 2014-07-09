
# Servlet 3.1 API Asynchronous processing error handling experiments

## Tomcat 7.0.54

http://localhost:8080/servlet3-async/error1?fail=true
- actual
  - status code: 500 internal server error
  - standard status line
- expected
  - status code: 500 internal server error
  - "hello, erroneous world" message on status line

http://localhost:8080/servlet3-async/error2?fail=true
- actual
  - status code: 500 internal server error
  - error.jsp content
- expected
  - status code: 500 internal server error
  - error.jsp content

Note: Unable to manually reproduce the dispatching error condition encountered with TC 8.0.9.

## Tomcat 8.0.9

http://localhost:8080/servlet3-async/error1?fail=true
- actual
  - status code: 500 internal server error
  - standard status line
- expected
  - status code: 500 internal server error
  - "hello, erroneous world" message on status line

http://localhost:8080/servlet3-async/error2?fail=true
- actual
  - status code: 500 internal server error
  - error.jsp content
- expected
  - status code: 500 internal server error
  - error.jsp content

After a couple of requests a timeout will be encountered with the following error:
```
10-Jul-2014 01:40:33.145 SEVERE [http-nio-8080-exec-2] org.apache.catalina.connector.CoyoteAdapter.asyncDispatch Exception while processing an asynchronous request
 java.lang.IllegalStateException: Calling [asyncTimeout()] is not valid for a request with Async state [DISPATCHING]
	at org.apache.coyote.AsyncStateMachine.asyncTimeout(AsyncStateMachine.java:267)
	...
```


## Resin 4.0.40

http://localhost:8080/servlet3-async/error1?fail=true
- actual
  - status code: 500 internal server error
  - "hello, erroneous world" message on status line
- expected
  - status code: 500 internal server error
  - "hello, erroneous world" message on status line

http://localhost:8080/servlet3-async/error2?fail=true
- actual
  - status code: 500 internal server error
  - error.jsp content
- expected
  - status code: 500 internal server error
  - error.jsp content

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

One such sequence appears to be be
1. http://localhost:8080/servlet3-async/error2?fail=true
2. http://localhost:8080/servlet3-async/error1

Whereas the following sequence seems to be OK:
1. http 'http://localhost:8080/servlet3-async/error1?fail=true'
2. http 'http://localhost:8080/servlet3-async/error2?fail=true'
