package org.iplantc.de.teams.client.views;

import org.iplantc.de.teams.client.TeamsView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.Composite;

/**
 * @author aramsey
 */
public class TeamsViewImpl extends Composite implements TeamsView {

    interface MyUiBinder extends UiBinder<Widget, TeamsViewImpl> {}
    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @UiField(provided = true) TeamsViewAppearance appearance;

    @Inject
    public TeamsViewImpl(TeamsViewAppearance appearance) {
        this.appearance = appearance;
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);
    }
}
