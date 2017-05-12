/**
 * 
 */
package org.iplantc.de.collaborators.client.models;

import org.iplantc.de.client.models.collaborators.Subject;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

/**
 * @author sriram
 * 
 */
public interface SubjectProperties extends PropertyAccess<Subject> {

    ValueProvider<Subject, String> institution();

}
