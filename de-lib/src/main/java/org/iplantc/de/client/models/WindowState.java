package org.iplantc.de.client.models;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

public interface WindowState {


    String LOCAL_STORAGE_PREFIX = "de.";
    String WIDTH = ".width";
    String HEIGHT = ".height";
    String TOP = ".top";
    String LEFT = ".left";
    String MAXIMIZED = ".maximized";
    String MINIMIZED = ".minimized";

    WindowType getWindowType();

    void setWindowType(WindowType type);

    boolean isMaximized();

    boolean isMinimized();

    @PropertyName("win_left")
    int getWinLeft();

    @PropertyName("win_top")
    int getWinTop();

    String getWidth();

    String getHeight();

    void setMaximized(boolean maximized);
    
    void setMinimized(boolean minimized);
    
    @PropertyName("win_left")
    void setWinLeft(int winLeft);
    
    @PropertyName("win_top")
    void setWinTop(int winTop);
    
    void setWidth(String width);
    
    void setHeight(String height);
    
}
