/**
 * 
 */
package org.iplantc.de.client.models.collaborators;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

/**
 * @author sriram
 * 
 */
public interface CollaboratorAutoBeanFactory extends AutoBeanFactory {

    AutoBean<SubjectList> getSubjectList();

    AutoBean<Subject> getSubject();

    AutoBean<SubjectMemberList> getSubjectMemberList();

}
