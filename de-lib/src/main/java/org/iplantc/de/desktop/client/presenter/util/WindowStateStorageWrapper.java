package org.iplantc.de.desktop.client.presenter.util;

import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.WindowState;
import org.iplantc.de.client.util.WebStorageUtil;
import org.iplantc.de.desktop.client.views.windows.WindowBase;

import com.google.common.base.Strings;
import com.google.gwt.core.client.GWT;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A wrapper to store and retrieve window state from web local storage
 *
 * Created by sriram on 1/23/18.
 */
public class WindowStateStorageWrapper {
    public static final String LOCAL_STORAGE_PREFIX = "de.";

    UserInfo userInfo;
    String windowType;
    String tag;

    String prefix;
    String suffix;
    WindowBase.WindowStateFactory wsf = GWT.create(WindowBase.WindowStateFactory.class);

    public WindowStateStorageWrapper(UserInfo userInfo, String windowType, String tag) {
        this.userInfo = userInfo;
        this.windowType = windowType;
        this.tag = tag;
        prefix = WindowStateStorageWrapper.LOCAL_STORAGE_PREFIX + windowType;
        suffix = "#" + tag + "#" + userInfo.getUsername();
        this.wsf = wsf;
    }


    /**
     * Save window state to web local storage
     *
     * @param ws
     */
    public void saveWindowState(WindowState ws) {
        if (ws != null) {
            saveHeight(ws.getHeight());
            saveWidth(ws.getWidth());
            saveLeft(ws.getWinLeft());
            saveTop(ws.getWinTop());
            saveMaximizedState(ws.isMaximized());
            saveMiniMinimizedState(ws.isMinimized());
            storeMap(ws.getAdditionalWindowStates());
        }
    }

    /**
     * Retrieve window state from web local storage
     *
     * @return WindowState object
     */
    public WindowState retrieveWindowState() {
        WindowState ws = wsf.windowState().as();
        ws.setMaximized(retrieveMaximizedState());
        ws.setMinimized(retrieveMinimizedState());
        ws.setWinTop(retrieveTop());
        ws.setWinLeft(retrieveLeft());
        ws.setWidth(retrieveWidth());
        ws.setHeight(retrieveHeight());
        ws.setTag(tag);
        ws.setAdditionalWindowStates(retrieveMap());
        return ws;
    }

    void saveHeight(String height) {
        WebStorageUtil.writeToStorage(getKey(WindowState.HEIGHT), height + "");
    }


    void saveWidth(String width) {
        WebStorageUtil.writeToStorage(getKey(WindowState.WIDTH), width + "");
    }

    void saveTop(int top) {
        WebStorageUtil.writeToStorage(getKey(WindowState.TOP), top + "");
    }

    void saveLeft(int left) {
        WebStorageUtil.writeToStorage(getKey(WindowState.LEFT), left + "");
    }

    void saveMaximizedState(boolean isMaximized) {
        WebStorageUtil.writeToStorage(getKey(WindowState.MAXIMIZED), isMaximized + "");
    }

    void saveMiniMinimizedState(boolean isMinimized) {
        WebStorageUtil.writeToStorage(getKey(WindowState.MINIMIZED), isMinimized + "");
    }


    String retrieveHeight() {
        return WebStorageUtil.readFromStorage(getKey(WindowState.HEIGHT));
    }

    String retrieveWidth() {
        return WebStorageUtil.readFromStorage(getKey(WindowState.WIDTH));
    }

    int retrieveTop() {
        String top = WebStorageUtil.readFromStorage(getKey(WindowState.TOP));
        return (Strings.isNullOrEmpty(top) ? 0 : Integer.parseInt(top));
    }


    int retrieveLeft() {
        String left = WebStorageUtil.readFromStorage(getKey(WindowState.LEFT));
        return (Strings.isNullOrEmpty(left) ? 0 : Integer.parseInt(left));
    }

    boolean retrieveMaximizedState() {
        String maximized = WebStorageUtil.readFromStorage(getKey(WindowState.MAXIMIZED));
        return (Strings.isNullOrEmpty(maximized) ? false : Boolean.parseBoolean(maximized));
    }

    boolean retrieveMinimizedState() {
        String minimized = WebStorageUtil.readFromStorage(getKey(WindowState.MINIMIZED));
        return (Strings.isNullOrEmpty(minimized) ? false : Boolean.parseBoolean(minimized));
    }

    void storeMap(Map<String, String> map) {
        if (map != null) {
            for (String key : map.keySet()) {
                WebStorageUtil.writeToStorage(key, map.get(key));
            }
        }
    }


    Map<String, String> retrieveMap() {
        Map<String, String> additionalWindowStates = new HashMap<>();
        Map<String, String> map = WebStorageUtil.getStorageAsMap();
        if (map != null) {
            Set<String> keys = map.keySet();
            for (String key : keys) {
                if (key.contains(
                        WindowState.ADDITIONAL + WindowStateStorageWrapper.LOCAL_STORAGE_PREFIX)) {
                    additionalWindowStates.put(key, map.get(key));
                }
            }
        }
        return additionalWindowStates;
    }


    private String getKey(String attribute) {
        return prefix + attribute + suffix;
    }
}


