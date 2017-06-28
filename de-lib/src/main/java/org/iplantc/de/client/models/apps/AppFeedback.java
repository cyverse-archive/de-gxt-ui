package org.iplantc.de.client.models.apps;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

public interface AppFeedback {

    @PropertyName("average")
    double getAverage();

    @PropertyName("user")
    int getUserRating();

    @PropertyName("comment_id")
    long getCommentId();

    @PropertyName("average")
    void setAverage(double averageRating);

    @PropertyName("user")
    void setUserRating(int userRating);

    @PropertyName("comment_id")
    void setCommentId(long commentId);

    void setTotal(int total);

    int getTotal();

}
