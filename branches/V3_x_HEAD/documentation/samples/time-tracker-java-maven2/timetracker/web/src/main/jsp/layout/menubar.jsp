<%@ include file="/taglib-imports.jspf" %>

<ul>
    <li class="first">
        <html:link action="/TimeTrackerHome/TimeTrackerHome">
            <bean:message key="time.tracker.home"/>
        </html:link>
    </li>
    <li>
        <a href="timecard-details.html">Timecard Details</a>
    </li>
    <li>
        <html:link styleClass="selected" action="/TimeTrackerHome/TimeTrackerHomeSearchTimecards">
            <bean:message key="search.timecards"/>
        </html:link>
    </li>
    <li>
        <a href="approve-timecards.html">Approve timecards</a>
    </li>
</ul>
