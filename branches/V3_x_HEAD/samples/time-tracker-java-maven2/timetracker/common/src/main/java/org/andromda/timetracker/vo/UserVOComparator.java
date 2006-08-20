package org.andromda.timetracker.vo;

import java.util.Comparator;

public class UserVOComparator implements Comparator<UserVO> {

    /**
     * Compares two UserVO objects based on their usernames. If the usernames are
     * not available (or null), it compares on id. <p>
     *
     * The comparison is null safe and places null objects less than non-null objects.<p>
     *
     * @see com.sapient.highenergy.vo.CompanySummaryVO
     */
    public int compare(UserVO o1, UserVO o2) {
        int result = 0; // assume equal

        if (o1 == null) {
            result = (o2 == null) ? 0 : -1;
        }
        else if (o2 == null) {
            result = 1;
        }
        else if ((o1.getUserName() != null) && (o2.getUserName() != null)) {
            // Both not-null, compare usernames
            result = o1.getUserName().compareTo(o2.getUserName());
        }
        else if ((o1.getId() != null) && (o2.getId() != null)) {
            // Both not-null but no names, check id's
            result = o1.getId().compareTo(o2.getId());
        }

        return result;
    }
}