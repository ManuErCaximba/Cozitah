<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<security:authorize access="hasRole('COMPANY')">
    <!DOCTYPE>
    <html>
    <head>
    </head>
    <body>
    <form:form action ="auditor/debuk/edit.do" modelAttribute="debuk">

        <form:hidden path="id"/>
        <form:hidden path="audit"/>

        <!-- Single areas -->

        <acme:textboxbs code="debuk.body" path="body"/>
        <acme:textboxbs code="debuk.picture" path="picture"/>
        <br>
        <br>
        <acme:submit name="final" code="debuk.save.final"/>&nbsp
        <acme:submit name="draft" code="debuk.save.draft"/>&nbsp
        <jstl:if test="${!debuk.isFinal}">
            <acme:cancel code="debuk.delete" url="/auditor/debuk/delete.do?id=${row.id}"/>
        </jstl:if>
        <acme:cancel url="/"
                     code="configuration.edit.cancel"/>&nbsp


    </form:form>
    </body>
    </html>
</security:authorize>

