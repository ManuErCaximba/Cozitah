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

<style type="text/css">
    .GREEN{
        background-color: Indigo;
    }
    .ORANGE{
        background-color: DarkSlateGray;
    }
    .RED{
        background-color: PapayaWhip;
    }
</style>

<display:table name="debuks" id="row" requestURI="${requestURI}"
               pagesize="10" class="displaytag">

    <jstl:choose>
        <jstl:when test="${row.moment!=null}">
            <jstl:choose>
                <jstl:when test="${row.moment > haceUnMes }">
                    <jstl:set var="css" value="GREEN"/>
                </jstl:when>
                <jstl:when test="${row.moment < haceUnMes && row.moment > haceDosMeses}">
                    <jstl:set var="css" value="ORANGE"/>
                </jstl:when>
                <jstl:otherwise>
                    <jstl:set var="css" value="RED" />
                </jstl:otherwise>
            </jstl:choose>
        </jstl:when>
        <jstl:otherwise>
            <jstl:set var="css" value="null" />
        </jstl:otherwise>
    </jstl:choose>

    <spring:message code="debuk.ticker" var="ticker"/>
    <display:column class="${css}" property="ticker" title="${ticker}"/>

    <spring:message code="debuk.moment" var="moment"/>
    <jstl:choose>
        <jstl:when test="${lang=='es'}">
            <display:column class="${css}" property="moment" title="${moment}" format="{0,date,dd-MM-yy HH:mm}"/>
        </jstl:when>
        <jstl:otherwise>
            <display:column class="${css}" property="moment" title="${moment}" format="{0,date,yy/MM/dd HH:mm}"/>
        </jstl:otherwise>
    </jstl:choose>

    <display:column class="${css}">
        <acme:cancel code="debuk.show" url="/auditor/debuk/show.do?id=${row.id}"/>
    </display:column>

    <display:column class="${css}">
    <jstl:if test="${!row.isFinal && company != null}">
            <acme:cancel code="debuk.edit" url="/auditor/debuk/edit.do?id=${row.id}"/>
    </jstl:if>
    </display:column>

    <display:column class="${css}">
    <jstl:if test="${!row.isFinal && company != null}">
            <acme:cancel code="debuk.delete" url="/auditor/debuk/delete.do?id=${row.id}"/>
    </jstl:if>
    </display:column>

</display:table>
<br>
<br>
<acme:cancel url="/" code="item.goBack"/>

