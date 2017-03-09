package org.iplantc.de.analysis.client.views.dialogs;

import org.iplantc.de.analysis.client.AnalysesView;
import org.iplantc.de.client.models.analysis.Analysis;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;

import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.form.TextArea;

/**
 * @author jstroot
 */
public class AnalysisCommentsDialog extends IPlantDialog {

    private Analysis analysis;
    private final TextArea ta;
    private AnalysesView.Presenter.Appearance appearance;

    @Inject
    public AnalysisCommentsDialog(AnalysesView.Presenter.Appearance appearance){
        this.appearance = appearance;
        setSize(appearance.commentsDialogWidth(), appearance.commentsDialogHeight());
        ta = new TextArea();
        add(ta);
    }

    public void show(Analysis analysis) {
        this.analysis = analysis;
        String comments = analysis.getComments();
        setHeading(appearance.comments());
        ta.setValue(comments);
        super.show();
    }

    @Override
    public void show() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("This method is not supported. Use show(Analysis) method instead.");
    }

    public String getComment() {
        return ta.getValue();
    }

    public boolean isCommentChanged() {
        return !getComment().equals(analysis.getComments());
    }
}
