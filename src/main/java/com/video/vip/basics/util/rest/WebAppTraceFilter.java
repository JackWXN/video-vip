package com.video.vip.basics.util.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WebAppTraceFilter implements Filter {
  
  public static final String HEADER_NAME = "vip-header-rid";
  
  private static final String IP = "client_ip";
  
  private static final String START_TIME = "startTime";
  
  private static final Logger logger = LoggerFactory.getLogger(WebAppTraceFilter.class);

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    
    if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
       chain.doFilter(request, response);
       return;
    }

    final HttpServletRequest req = (HttpServletRequest) request;
    try {
      String traceId = req.getParameter("_") == null ?  req.getHeader(HEADER_NAME) : req.getParameter("_");
      if (traceId == null || traceId.trim().equals("")) {
        traceId = Traces.genTraceId();
      }
      Traces.setTraceId(traceId);
      MDC.put(START_TIME, System.currentTimeMillis()+"");
      MDC.put(IP, getClientIpAddr(req));
      chain.doFilter(request, response);
    } catch (Exception e) {
      logger.error(e.getMessage(),e);
    }finally {
      logLatency();
      MDC.clear();
      Traces.removeUid();
      Traces.removeTraceId();
    }
  }

  @Override
  public void destroy() {
    
  }
  
  public static void logLatency() {
    String startTime =  MDC.get("startTime");
    Long  latency = -1L;
    if (startTime != null && !startTime.trim().equals("")) {
       latency =  (System.currentTimeMillis() - Long.valueOf(startTime));
    }
    logger.info("Request process latency_ms = {}",latency);
  }

  public static String getClientIpAddr(HttpServletRequest request) {
    String ip = request.getHeader("X-Forwarded-For");
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("WL-Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("HTTP_CLIENT_IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("HTTP_X_FORWARDED_FOR");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getRemoteAddr();
    }
    return ip;
  }
}
