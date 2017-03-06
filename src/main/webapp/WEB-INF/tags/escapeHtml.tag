<%@tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@tag import="org.apache.commons.lang3.StringUtils"%>
<%@tag import="yyl.mvc.core.util.codec.EncodeUtil"%>
<%@attribute name="value" type="java.lang.String" required="false"%>
<%=EncodeUtil.htmlEscape(StringUtils.defaultString((String) jspContext.getAttribute("value")))%>