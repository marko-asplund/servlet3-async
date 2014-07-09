
# Servlet 3.1 API Asynchronous processing error handling experiments

## Tomcat 7.0.54

TODO

## Tomcat 8.0.9

http://localhost:8080/servlet3-async/error1?fail=true
- actual
  - status code: 500 Service Unavailable
  - no content
- expected
  - status code: 500 internal server error
  - error.jsp content

http://localhost:8080/servlet3-async/error2?fail=true
- actual
  - status code: 500 Service Unavailable
  - error.jsp content
- expected
  - status code: 500 internal server error
  - error.jsp content

After a few requests a timeout will be encountered with the following error:
```
10-Jul-2014 01:40:33.145 SEVERE [http-nio-8080-exec-2] org.apache.catalina.connector.CoyoteAdapter.asyncDispatch Exception while processing an asynchronous request
 java.lang.IllegalStateException: Calling [asyncTimeout()] is not valid for a request with Async state [DISPATCHING]
	at org.apache.coyote.AsyncStateMachine.asyncTimeout(AsyncStateMachine.java:267)
	...
```


## Resin 4.0.40

TODO