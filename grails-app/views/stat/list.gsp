
<%@ page import="com.traderapist.models.Stat" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'stat.label', default: 'Stat')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-stat" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-stat" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="season" title="${message(code: 'stat.season.label', default: 'Season')}" />
					
						<g:sortableColumn property="week" title="${message(code: 'stat.week.label', default: 'Week')}" />
					
						<g:sortableColumn property="statKey" title="${message(code: 'stat.statKey.label', default: 'Stat Key')}" />
					
						<g:sortableColumn property="statValue" title="${message(code: 'stat.statValue.label', default: 'Stat Value')}" />
					
						<th><g:message code="stat.player.label" default="Player" /></th>
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${statInstanceList}" status="i" var="statInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${statInstance.id}">${fieldValue(bean: statInstance, field: "season")}</g:link></td>
					
						<td>${fieldValue(bean: statInstance, field: "week")}</td>
					
						<td>${statInstance.translateStatKey()}</td>
					
						<td>${fieldValue(bean: statInstance, field: "statValue")}</td>
					
						<td><!--${fieldValue(bean: statInstance, field: "player")}-->${statInstance.player.name}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${statInstanceTotal}" />
			</div>
		</div>
	</body>
</html>