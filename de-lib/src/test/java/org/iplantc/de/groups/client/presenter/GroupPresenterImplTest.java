package org.iplantc.de.groups.client.presenter;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.models.groups.GroupAutoBeanFactory;
import org.iplantc.de.client.services.GroupServiceFacade;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.groups.client.GroupView;
import org.iplantc.de.groups.client.gin.GroupViewFactory;
import org.iplantc.de.groups.client.model.GroupProperties;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwtmockito.GwtMockitoTestRunner;

import com.sencha.gxt.data.shared.ListStore;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.List;

/**
 * @author aramsey
 */
@RunWith(GwtMockitoTestRunner.class)
public class GroupPresenterImplTest {

    @Mock GroupView viewMock;
    @Mock GroupAutoBeanFactory factoryMock;
    @Mock GroupView.GroupViewAppearance appearanceMock;
    @Mock GroupServiceFacade serviceFacadeMock;
    @Mock ListStore<Group> listStoreMock;
    @Mock IplantAnnouncer announcerMock;
    @Mock GroupViewFactory viewFactoryMock;
    @Mock GroupProperties propertiesMock;
    @Mock List<Group> groupListMock;

    @Captor ArgumentCaptor<AsyncCallback<List<Group>>> groupListCaptor;

    private GroupPresenterImpl uut;

    @Before
    public void setUp() {
        when(viewFactoryMock.create(listStoreMock)).thenReturn(viewMock);


        uut = new GroupPresenterImpl(viewFactoryMock,
                                     factoryMock,
                                     propertiesMock,
                                     appearanceMock,
                                     serviceFacadeMock) {
            @Override
            ListStore<Group> createListStore(GroupProperties properties) {
                return listStoreMock;
            }
        };
    }

    @Test
    public void go() {
        HasOneWidget containerMock = mock(HasOneWidget.class);

        /** CALL METHOD UNDER TEST **/
        uut.go(containerMock);
        verify(containerMock).setWidget(eq(viewMock));
    }

    @Test
    public void updateView() {
        String searchTerm = "search";

        /** CALL METHOD UNDER TEST **/
        uut.updateView(searchTerm);

        verify(serviceFacadeMock).getGroups(eq(searchTerm), groupListCaptor.capture());

        groupListCaptor.getValue().onSuccess(groupListMock);
        verify(listStoreMock).replaceAll(eq(groupListMock));
    }

}
