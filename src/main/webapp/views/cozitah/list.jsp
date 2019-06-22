<%--
 * action-1.jsp
 *
 * Copyright (C) 2018 Universidad de Sevilla
 *
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
        pageEncoding="ISO-8859-1" %>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="security"
          uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<display:table name="cozitahs" id="row" requestURI="${requestURI}"
               pagesize="5" class="displaytag">


    <spring:message code="cozitah.ticker" var="ticker"/>
    <display:column property="ticker" title="${ticker}"/>

    <spring:message code="cozitah.moment" var="moment"/>
    <jstl:choose>
        <jstl:when test="${lang=es}">
            <display:column property="moment">
                <fmt:formatDate value="${moment}" pattern="dd-MM-yy hh:mm" />
            </display:column>
        </jstl:when>
        <jstl:otherwise>
            <display:column property="moment">
                <fmt:formatDate value="${moment}" pattern="yy/MM/dd hh:mm" />
            </display:column>
        </jstl:otherwise>
    </jstl:choose>

    <spring:message code="cozitah.draft" var="draft"/>
    <spring:message code="cozitah.final" var="final"/>

    <jstl:choose>
        <jstl:when test="${row.isFinal}">
            <display:column property="moment" title="${final}"/>
        </jstl:when>
        <jstl:otherwise>
        <display:column property="moment" title="${draft}"/>
        </jstl:otherwise>
    </jstl:choose>

    <jstl:if test="${!row.isFinal}">
        <display:column>
            <acme:cancel code="cozitah.edit" url="/company/cozitah/edit.do?id=${row.id}"/>
        </display:column>
    </jstl:if>

</display:table>

<acme:cancel url="/" code="item.goBack"/>

