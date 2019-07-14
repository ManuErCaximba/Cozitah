<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<!DOCTYPE>
<html>
<head>
</head>
<body>

<!-- Single Attributes -->

<p>
    <acme:showtext code="debuk.ticker"
                   value="${debuk.ticker}"
                   fieldset="true"/>
</p>
<p>
    <acme:showtext code="debuk.body"
                   value="${debuk.body}"
                   fieldset="true"/>
</p>
<p>
    <jstl:choose>
        <jstl:when test="${lang=='es'}">
            <fieldset>
                <legend><b><spring:message code="debuk.moment"/></b></legend>
                <a><fmt:formatDate value="${debuk.moment}" pattern="dd-MM-yy HH:mm" /></a>
            </fieldset>
        </jstl:when>
        <jstl:otherwise>
            <fieldset>
                <legend><b><spring:message code="debuk.moment"/></b></legend>
                <a><fmt:formatDate value="${debuk.moment}" pattern="yy/MM/dd HH:mm" /></a>
            </fieldset>
        </jstl:otherwise>
    </jstl:choose>
</p>
    <jstl:if test="${debuk.picture==null || debuk.picture==''}">
    <p>
        <fieldset>
            <legend><b><spring:message code="debuk.picture"/></b></legend>
            <a href="#"><img src="${debuk.picture}" alt="${debuk.picture}" height="150" width="150"/></a>
        </fieldset>
    </p>
    </jstl:if>

<!-- Buttons -->

<jstl:if test="${auditor != null && !debuk.isFinal}">
    <acme:cancel url="/auditor/debuk/edit.do?id=${debuk.id}" code="debuk.edit"/>
</jstl:if>

<acme:cancel url="/" code="debuk.cancel"/>

</body>
</html>
