package org.iplantc.de.client.models.comments;

import org.iplantc.de.client.models.HasId;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;
import org.iplantc.de.client.models.HasSettableId;

/**
 * @author jstroot
 */
public interface Comment extends HasSettableId {
    String COMMENT_TEXT_KEY = "comment";

    @PropertyName("post_time")
    long getTimestamp();

    @PropertyName("post_time")
    void setTimestamp(long timestamp);

    @PropertyName("commenter")
    String getCommentedBy();

    @PropertyName("commenter")
    void setCommentedBy(String user);

    @PropertyName(COMMENT_TEXT_KEY)
    String getCommentText();

    @PropertyName(COMMENT_TEXT_KEY)
    void setCommentText(String commentText);

    @PropertyName("retracted")
    void setRetracted(boolean retracted);

    @PropertyName("retracted")
    boolean isRetracted();
}
