/**
 * 
 */
package org.iplantc.de.collaborators.client.models;

import org.iplantc.de.client.models.collaborators.Subject;

import com.sencha.gxt.data.shared.ModelKeyProvider;

public class SubjectKeyProvider implements ModelKeyProvider<Subject> {

    @Override
    public String getKey(Subject item) {
        return item.getId();
    }

}
