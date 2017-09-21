package org.iplantc.de.theme.base.client.collaborators;

import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.models.groups.UpdateMemberResult;
import org.iplantc.de.collaborators.client.ManageCollaboratorsView;
import org.iplantc.de.resources.client.IplantResources;
import org.iplantc.de.resources.client.messages.IplantContextualHelpStrings;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;
import org.iplantc.de.resources.client.messages.IplantErrorStrings;
import org.iplantc.de.resources.client.messages.IplantValidationMessages;

import com.google.common.base.Strings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;

import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.theme.base.client.grid.CheckBoxColumnDefaultAppearance.CheckBoxColumnResources;
import com.sencha.gxt.theme.base.client.grid.CheckBoxColumnDefaultAppearance.CheckBoxColumnStyle;
import com.sencha.gxt.theme.base.client.grid.CheckBoxColumnDefaultAppearance.CheckBoxColumnTemplates;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jstroot
 */
public class ManageCollaboratorsViewDefaultAppearance implements ManageCollaboratorsView.Appearance {

    /**
     * This class is a copy of {@link CheckBoxColumnTemplates}, but with fields for
     * debug ids.
     */
    public interface CheckBoxColumnDebugTemplates extends XTemplates {
       @XTemplates.XTemplate("<div id='{debugId}' class='{style.hdChecker}'></div>")
        SafeHtml renderDebugHeader(CheckBoxColumnStyle style, String debugId);

        @XTemplates.XTemplate("<span style='color: red;'>*&nbsp</span>{label}")
        SafeHtml requiredFieldLabel(String label);
    }

    private final CheckBoxColumnResources resources;
    private final CheckBoxColumnStyle style;
    private final CheckBoxColumnDebugTemplates templates;
    private IplantDisplayStrings iplantDisplayStrings;
    private IplantContextualHelpStrings iplantContextualHelpStrings;
    private IplantResources iplantResources;
    private CollaboratorsDisplayStrings displayStrings;
    private IplantErrorStrings iplantErrorStrings;
    private IplantValidationMessages iplantValidationMessages;

    public ManageCollaboratorsViewDefaultAppearance() {
        this(GWT.<CheckBoxColumnResources> create(CheckBoxColumnResources.class),
             GWT.<CheckBoxColumnDebugTemplates>create(CheckBoxColumnDebugTemplates.class),
             GWT.<IplantDisplayStrings>create(IplantDisplayStrings.class),
             GWT.<IplantContextualHelpStrings>create(IplantContextualHelpStrings.class),
             GWT.<IplantResources> create(IplantResources.class),
             GWT.<CollaboratorsDisplayStrings> create(CollaboratorsDisplayStrings.class),
             GWT.<IplantErrorStrings> create(IplantErrorStrings.class),
             GWT.<IplantValidationMessages> create(IplantValidationMessages.class));
    }

    ManageCollaboratorsViewDefaultAppearance(final CheckBoxColumnResources resources,
                                             final CheckBoxColumnDebugTemplates templates,
                                             IplantDisplayStrings iplantDisplayStrings,
                                             IplantContextualHelpStrings iplantContextualHelpStrings,
                                             IplantResources iplantResources,
                                             CollaboratorsDisplayStrings displayStrings,
                                             IplantErrorStrings iplantErrorStrings,
                                             IplantValidationMessages iplantValidationMessages) {
        this.resources = resources;
        this.style = resources.style();
        this.templates = templates;
        this.iplantDisplayStrings = iplantDisplayStrings;
        this.iplantContextualHelpStrings = iplantContextualHelpStrings;
        this.iplantResources = iplantResources;
        this.displayStrings = displayStrings;
        this.iplantErrorStrings = iplantErrorStrings;
        this.iplantValidationMessages = iplantValidationMessages;

        style.ensureInjected();
    }

    @Override
    public SafeHtml renderCheckBoxColumnHeader(String debugId) {
        // Pull in checkbox column appearance resources
        return templates.renderDebugHeader(style, debugId);
    }

    @Override
    public String collaborators() {
        return displayStrings.collaborationWindowHeading();
    }

    @Override
    public String collaboratorsHelp() {
        return iplantContextualHelpStrings.collaboratorsHelp();
    }

    @Override
    public String manageGroups() {
        return displayStrings.manageGroups();
    }

    @Override
    public String delete() {
        return iplantDisplayStrings.delete();
    }

    @Override
    public ImageResource deleteIcon() {
        return iplantResources.delete();
    }

    @Override
    public String manageCollaborators() {
        return displayStrings.manageCollaborators();
    }

    @Override
    public ImageResource shareIcon() {
        return iplantResources.share();
    }

    @Override
    public String noCollaborators() {
        return iplantDisplayStrings.noCollaborators();
    }

    @Override
    public String myCollaborators() {
        return iplantDisplayStrings.myCollaborators();
    }

    @Override
    public String selectCollabs() {
        return iplantDisplayStrings.selectCollabs();
    }

    @Override
    public ImageResource groupsIcon() {
        return iplantResources.group();
    }

    @Override
    public String collaboratorTab() {
        return displayStrings.collaboratorTab();
    }

    @Override
    public String collaboratorListTab() {
        return displayStrings.collaboratorListTab();
    }

    @Override
    public String loadingMask() {
        return iplantDisplayStrings.loadingMask();
    }

    @Override
    public String addGroup() {
        return displayStrings.addGroup();
    }

    @Override
    public ImageResource addIcon() {
        return iplantResources.add();
    }

    @Override
    public SafeHtml groupNameLabel() {
        return templates.requiredFieldLabel(displayStrings.groupNameLabel());
    }
    @Override
    public String groupDescriptionLabel() {
        return displayStrings.groupDescriptionLabel();
    }

    @Override
    public int groupDetailsWidth() {
        return 500;
    }

    @Override
    public int groupDetailsHeight() {
        return 500;
    }

    @Override
    public String groupDetailsHeading(Subject subject) {
        if (subject == null || Strings.isNullOrEmpty(subject.getName())) {
            return displayStrings.newGroupDetailsHeading();
        } else {
            return displayStrings.editGroupDetailsHeading(subject.getName());
        }
    }

    @Override
    public String completeRequiredFieldsError() {
        return iplantDisplayStrings.completeRequiredFieldsError();
    }

    @Override
    public String deleteGroupConfirmHeading(List<Subject> groups) {
        List<String> groupNames = groups.stream().map(Subject::getSubjectDisplayName).collect(Collectors.toList());
        return displayStrings.deleteGroupConfirmHeading(groupNames);
    }

    @Override
    public String deleteGroupConfirm(List<Subject> groups) {
        List<String> groupNames = groups.stream().map(Subject::getSubjectDisplayName).collect(Collectors.toList());
        return displayStrings.deleteGroupConfirm(groupNames);
    }

    @Override
    public String unableToAddMembers(List<UpdateMemberResult> failures) {
        List<String> memberNames = failures.stream().map(UpdateMemberResult::getSubjectName).collect(
                Collectors.toList());
        return displayStrings.unableToAddMembers(memberNames);
    }

    @Override
    public String groupCreatedSuccess(Group group) {
        return displayStrings.groupCreatedSuccess(group.getName());
    }

    @Override
    public String memberDeleteFail(List<UpdateMemberResult> subjects) {
        List<String> memberNames = subjects.stream().map(UpdateMemberResult::getSubjectName).collect(
                Collectors.toList());
        return displayStrings.memberDeleteFail(memberNames);
    }

    @Override
    public String collaboratorsSelfAdd() {
        return iplantDisplayStrings.collaboratorSelfAdd();
    }

    @Override
    public String groupSelfAdd() {
        return displayStrings.groupSelfAdd();
    }

    @Override
    public String collaboratorRemoveConfirm(String names) {
        return iplantDisplayStrings.collaboratorRemoveConfirm(names);
    }

    @Override
    public String collaboratorAddConfirm(String names) {
        return iplantDisplayStrings.collaboratorAddConfirm(names);
    }

    @Override
    public String addCollabErrorMsg() {
        return iplantErrorStrings.addCollabErrorMsg();
    }

    @Override
    public String memberAddToGroupsSuccess(Subject subject) {
        return displayStrings.memberAddToGroupsSuccess(subject.getSubjectDisplayName());
    }

    @Override
    public String groupNameValidationMsg(String restrictedChars) {
        return displayStrings.groupNameValidationMsg(restrictedChars);
    }

    @Override
    public String invalidChars(String restrictedChars) {
        return iplantValidationMessages.invalidChars(restrictedChars);
    }

    @Override
    public String nameHeader() {
        return iplantDisplayStrings.name();
    }

    @Override
    public String institutionOrDescriptionHeader() {
        return displayStrings.institutionOrDescriptionHeader();
    }

    @Override
    public String onlyDNDToListSupported() {
        return displayStrings.onlyDNDToListSupported();
    }

    @Override
    public String membersAddedToGroupSuccess(Subject group, List<UpdateMemberResult> userSuccesses) {
        List<String> names = userSuccesses.stream().map(UpdateMemberResult::getSubjectName).collect(Collectors.toList());
        return displayStrings.membersAddedToGroupSuccess(group.getSubjectDisplayName(), names);
    }

    @Override
    public String windowHeading() {
        return displayStrings.collaborationWindowHeading();
    }

    @Override
    public String windowWidth() {
        return "600px";
    }

    @Override
    public String windowHeight() {
        return "400px";
    }

    @Override
    public int windowMinWidth() {
        return 200;
    }

    @Override
    public String retainPermissionsHeader() {
        return displayStrings.retainPermissionsHeader();
    }

    @Override
    public String retainPermissionsMessage() {
        return displayStrings.retainPermissionsMessage();
    }

    @Override
    public String retainPermissionsBtn() {
        return displayStrings.retainPermissionsBtn();
    }

    @Override
    public String removePermissionsBtn() {
        return displayStrings.removePermissionsBtn();
    }

    @Override
    public int retainPermissionsWidth() {
        return 350;
    }

    @Override
    public int chooseCollaboratorsWidth() {
        return 550;
    }

    @Override
    public int chooseCollaboratorsHeight() {
        return 400;
    }
}
