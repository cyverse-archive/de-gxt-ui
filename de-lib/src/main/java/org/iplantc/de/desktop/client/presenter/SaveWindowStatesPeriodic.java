package org.iplantc.de.desktop.client.presenter;

import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.WindowState;
import org.iplantc.de.client.util.WebStorageUtil;
import org.iplantc.de.desktop.client.DesktopView;

import com.google.gwt.core.client.GWT;

import java.util.List;

/**
 * Created by sriram on 1/11/18.
 */
public class SaveWindowStatesPeriodic implements Runnable {


    private final DesktopView.Presenter presenter;

    public SaveWindowStatesPeriodic(DesktopView.Presenter presenter)   {
      this.presenter = presenter;
    }

    @Override
    public void run() {
      List<WindowState> states = presenter.getWindowStates();
      GWT.log("saving window state periodic...");
      for (WindowState ws : states) {
          saveWindowState(ws);
      }
    }


    private void saveWindowState(WindowState ws) {
        UserInfo userInfo = UserInfo.getInstance();
        WebStorageUtil.writeToStorage(WebStorageUtil.LOCAL_STORAGE_PREFIX + ws.getWindowType() + WindowState.HEIGHT + "#"
                                      + userInfo.getUsername(),ws.getHeight() + "");
        WebStorageUtil.writeToStorage(WebStorageUtil.LOCAL_STORAGE_PREFIX + ws.getWindowType() + WindowState.WIDTH + "#"
                                      + userInfo.getUsername(),ws.getWidth() + "");
        WebStorageUtil.writeToStorage(WebStorageUtil.LOCAL_STORAGE_PREFIX + ws.getWindowType() + WindowState.TOP + "#"
                                      + userInfo.getUsername(),ws.getWinLeft() + "");
        WebStorageUtil.writeToStorage(WebStorageUtil.LOCAL_STORAGE_PREFIX + ws.getWindowType() + WindowState.LEFT + "#"
                                      + userInfo.getUsername(),ws.getWinTop() + "");
        WebStorageUtil.writeToStorage(WebStorageUtil.LOCAL_STORAGE_PREFIX + ws.getWindowType() + WindowState.MINIMIZED + "#"
                                      + userInfo.getUsername(),ws.isMinimized() + "");
        WebStorageUtil.writeToStorage(WebStorageUtil.LOCAL_STORAGE_PREFIX + ws.getWindowType() + WindowState.MAXIMIZED + "#"
                                      + userInfo.getUsername(),ws.isMaximized() + "");
    }
}
