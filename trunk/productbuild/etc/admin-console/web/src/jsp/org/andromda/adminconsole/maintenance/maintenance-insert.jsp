<%@ page import="org.andromda.adminconsole.db.Column,
                 org.andromda.adminconsole.db.RowData,
                 org.andromda.adminconsole.config.AdminConsoleConfigurator"%>
<%@ include file="/taglib-imports.jspf" %>

<div id="insert" class="action">
    <h3><bean:message key="maintenance.maintenance.insert"/></h3>
    <div class="trigger">
        <html:form action="/Maintenance/MaintenanceInsert" onsubmit="">
            <table>
                <c:set var="currentTable" value="${metaDataSession.currentTable}" scope="page"/>
                <c:forEach items="${currentTable.columns}" var="column">
                    <bean:parameter id="value" name="${column.name}" value=""/>
                    <tr>
                        <td>${column.name}</td>
                        <td>${acf:renderInsertWidget(databaseLoginSession.configurator,column,value)}</td>
                        <td>
                            <c:if test="${column.foreignKeyColumn}">
                                <c:set var="foreignTableName" value="${column.importedTableName}" scope="page"/>
                                <c:if test="${currentTable.name != foreignTableName}"> <%-- don't render link to yourself --%>
                                    <c:if test="${acf:contains(metaDataSession.tableNames,foreignTableName)}"> <%-- only render allowed tables --%>
                                        <bean:message key="show.table" bundle="custom"/>
                                        <html:link action="/Maintenance/MaintenanceChangeTable" styleClass="foreignTableLink"
                                            paramId="name" paramName="foreignTableName" paramScope="page">
                                            ${foreignTableName}
                                        </html:link>
                                    </c:if>
                                </c:if>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
                <tr>
                    <td>
                    </td>
                    <td id="exactMatches">
                        <html:checkbox name="form" property="exactMatches"/>
                        <bean:message key="maintenance.maintenance.param.exact.matches"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        <input type="submit" value="<bean:message key="maintenance.maintenance.insert"/>"
                               onclick="this.form.name='maintenanceMaintenanceInsertForm';this.form.action='<html:rewrite action="/Maintenance/MaintenanceInsert"/>';"/>
                    </td>
                    <td>
                        <input type="submit" value="<bean:message key="maintenance.maintenance.search"/>"
                               onclick="this.form.name='maintenanceMaintenanceSearchForm';this.form.action='<html:rewrite action="/Maintenance/MaintenanceSearch"/>';"/>
                    </td>
                </tr>
            </table>
        </html:form>
    </div>
</div>
