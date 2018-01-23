package org.iplantc.de.desktop.client.presenter;

import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.WindowState;
import org.iplantc.de.desktop.client.DesktopView;
import org.iplantc.de.desktop.client.presenter.util.WindowStateStorageWrapper;

import com.google.gwt.core.client.GWT;

import java.util.List;

/**
 * An utility run by a thread every 15s to store window states to local storage.
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


    /**
     * Save WindowState local storage.
     *
     * @param ws WindowState object
     */
    void saveWindowState(WindowState ws) {
        WindowStateStorageWrapper wssw =
                new WindowStateStorageWrapper(userInfo, ws.getWindowType(), ws.getTag());
        wssw.saveWindowState(ws);
    }
}
