package org.iplantc.de.client.models.analysis.support;

import java.util.Date;

/**
 * Created by sriram on 2/22/17.
 */
public interface AnalysisSupportRequestFields {

    String getName();

    void setName(String name);

    String getApp();

    void setApp(String app);

    String getOutputFolder();

    void setOutputFolder(String folder);

    Date getStartDate();

    void setStartDate(Date startDate);

    Date getEndDate();

    void setEndDate(Date endDate);

    String getComment();

    void setComment(String comment);

    String getStatus();

    void setStatus(String status);

    String getEmail();

    void setEmail(String email);

    String getUser();

    void setUser(String user);

}
