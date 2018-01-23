package org.iplantc.de.desktop.client.presenter;

import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.WindowState;
import org.iplantc.de.client.util.WebStorageUtil;
import org.iplantc.de.desktop.client.DesktopView;

import com.google.gwt.core.client.GWT;

import java.util.List;
import java.util.Map;

/**
 * Created by sriram on 1/11/18.
 */
public class SaveWindowStatesPeriodic implements Runnable {

    DesktopView.Presenter presenter;
    UserInfo userInfo;

    public SaveWindowStatesPeriodic(DesktopView.Presenter presenter, UserInfo userInfo)   {
      this.presenter = presenter;
      this.userInfo = userInfo;
    }

    @Override
    public void run() {
      List<WindowState> states = presenter.getWindowStates();
      GWT.log("saving window state periodic...");
      for (WindowState ws : states) {
          saveWindowState(ws);
      }
    }


    void saveWindowState(WindowState ws) {
        String prefix = WebStorageUtil.LOCAL_STORAGE_PREFIX + ws.getWindowType();
        String suffix = "#" + ws.getTag() + "#" + userInfo.getUsername();
        WebStorageUtil.writeToStorage(prefix + WindowState.HEIGHT + suffix, ws.getHeight() + "");
        WebStorageUtil.writeToStorage(prefix + WindowState.WIDTH + suffix, ws.getWidth() + "");
        WebStorageUtil.writeToStorage(prefix + WindowState.TOP + suffix, ws.getWinTop() + "");
        WebStorageUtil.writeToStorage(prefix + WindowState.LEFT + suffix, ws.getWinLeft() + "");
        WebStorageUtil.writeToStorage(prefix + WindowState.MINIMIZED + suffix, ws.isMinimized() + "");
        WebStorageUtil.writeToStorage(prefix + WindowState.MAXIMIZED + suffix, ws.isMaximized() + "");
        Map<String, String> additionalWindowStates = ws.getAdditionalWindowStates();
        if (additionalWindowStates != null) {
            for (String key : additionalWindowStates.keySet()) {
                WebStorageUtil.writeToStorage(key,
                                              additionalWindowStates.get(key));
            }
        }
    }
}
