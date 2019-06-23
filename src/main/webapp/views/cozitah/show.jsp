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
    <acme:showtext code="cozitah.ticker"
                   value="${cozitah.ticker}"
                   fieldset="true"/>
</p>
<p>
    <acme:showtext code="cozitah.body"
                   value="${cozitah.body}"
                   fieldset="true"/>
</p>
<p>
    <jstl:choose>
        <jstl:when test="${lang=='es'}">
            <fieldset>
                <legend><b><spring:message code="cozitah.moment"/></b></legend>
                <a><fmt:formatDate value="${cozitah.moment}" pattern="dd-MM-yy HH:mm" /></a>
            </fieldset>
        </jstl:when>
        <jstl:otherwise>
            <fieldset>
                <legend><b><spring:message code="cozitah.moment"/></b></legend>
                <a><fmt:formatDate value="${cozitah.moment}" pattern="yy/MM/dd HH:mm" /></a>
            </fieldset>
        </jstl:otherwise>
    </jstl:choose>
</p>
    <jstl:if test="${cozitah.picture==null || cozitah.picture==''}">
    <p>
        <fieldset>
            <legend><b><spring:message code="cozitah.picture"/></b></legend>
            <a href="#"><img src="${cozitah.picture}" alt="${cozitah.picture}" height="150" width="150"/></a>
        </fieldset>
    </p>
    </jstl:if>

<!-- Buttons -->

<jstl:if test="${auditor != null && !cozitah.isFinal}">
    <acme:cancel url="/auditor/cozitah/edit.do?id=${cozitah.id}" code="cozitah.edit"/>
</jstl:if>

<acme:cancel url="/" code="cozitah.cancel"/>

</body>
</html>
