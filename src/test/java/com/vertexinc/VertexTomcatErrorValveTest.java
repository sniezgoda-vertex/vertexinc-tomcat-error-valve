package com.vertexinc;

import org.apache.catalina.connector.CoyoteOutputStream;
import org.apache.catalina.connector.Response;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static com.vertexinc.VertexTomcatErrorValve.ERROR_PAGE;
import static org.junit.jupiter.api.Assertions.*;

class VertexTomcatErrorValveTest {

  @Test
  void report() throws IOException {
    VertexTomcatErrorValve valve = new VertexTomcatErrorValve();

    MockResponse response = new MockResponse();
    valve.report(null, response, null);

    String result = response.read();
    assertNotNull(result);

    String errorPage = IOUtils.toString(this.getClass().getResourceAsStream(ERROR_PAGE));
    assertEquals(errorPage, result);
  }

  private class MockResponse extends Response {
    private MockServletOutputStream mockServletOutputStream;

    public ServletOutputStream getOutputStream() {
      mockServletOutputStream = new MockServletOutputStream();
      return mockServletOutputStream;
    }

    public String read() {
      return mockServletOutputStream.read();
    }

    private class MockServletOutputStream extends ServletOutputStream {
      private ByteArrayOutputStream byteArrayOutputStream;

      public MockServletOutputStream() {
        byteArrayOutputStream = new ByteArrayOutputStream();
      }

      @Override
      public boolean isReady() {
        return true;
      }

      @Override
      public void setWriteListener(WriteListener writeListener) {

      }

      @Override
      public void write(int b) throws IOException {
        byteArrayOutputStream.write(b);
      }

      public String read() {
        return new String(byteArrayOutputStream.toByteArray());
      }
    }
  }
}