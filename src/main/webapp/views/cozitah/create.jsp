<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<security:authorize access="hasRole('AUDITOR')">
    <!DOCTYPE>
    <html>
    <head>
    </head>
    <body>
    <form:form action ="auditor/cozitah/create.do" modelAttribute="cozitah">

        <form:hidden path="id"/>
        <form:hidden path="audit"/>

        <!-- Single areas -->

        <acme:textboxbs code="cozitah.body" path="body"/>
        <acme:textboxbs code="cozitah.picture" path="picture"/>
        <br>
        <br>
        <acme:submit name="final" code="cozitah.save.final"/>&nbsp
        <acme:submit name="draft" code="cozitah.save.draft"/>&nbsp
        <acme:cancel url="/"
                     code="cozitah.cancel"/>&nbsp


    </form:form>
    </body>
    </html>
</security:authorize>