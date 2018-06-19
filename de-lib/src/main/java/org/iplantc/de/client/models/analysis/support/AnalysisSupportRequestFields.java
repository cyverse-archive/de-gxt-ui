package org.iplantc.de.client.models.analysis.support;

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

    String getStartDate();

    void setStartDate(String startDate);

    String getEndDate();

    void setEndDate(String endDate);

    String getComment();

    void setComment(String comment);

    String getStatus();

    void setStatus(String status);

    String getEmail();

    void setEmail(String email);

    String getUser();

    void setUser(String user);

    String getAnalysisId();

    void setAnalysisId(String id);

}
