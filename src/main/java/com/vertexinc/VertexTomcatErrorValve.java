package com.vertexinc;

import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ErrorReportValve;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

/**
 * A custom Tomcat error valve for handling various errors.
 */
public class VertexTomcatErrorValve extends ErrorReportValve {

  protected static final String ERROR_PAGE = "vertex-tomcat-error.html";

  // Create a simple logger
  Logger log = Logger.getLogger(VertexTomcatErrorValve.class.getName());

  /**
   * Display a static, custom Vertex error page when in trouble.
   * This replaces the use of standard Tomcat error pages for 400, 404, etc.
   * which are flagged for information leakage.
   * To use, deploy this to the Tomcat lib directory and update the Host configuration
   * in Tomcat's conf/server.xml to include the line:
   * <code>errorReportValveClass="com.vertexinc.VertexTomcatErrorValve"</code>
   *
   * @param request   the request
   * @param response  the response
   * @param throwable the exception
   */
  @Override
  protected void report(Request request, Response response, Throwable throwable) {
    try (InputStream inputStream = getClass().getResourceAsStream(ERROR_PAGE)) {
      IOUtils.copy(inputStream, response.getOutputStream());
    } catch (IOException e) {
      log.severe("Exception loading the error page:" + e.getMessage());
      e.printStackTrace();
    }
  }
}