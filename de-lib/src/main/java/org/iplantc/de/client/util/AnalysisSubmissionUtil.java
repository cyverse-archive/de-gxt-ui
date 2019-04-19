package org.iplantc.de.client.util;

import org.iplantc.de.client.models.apps.integration.AppTemplate;
import org.iplantc.de.client.models.apps.integration.Argument;
import org.iplantc.de.client.models.apps.integration.ArgumentGroup;
import org.iplantc.de.client.models.apps.integration.ArgumentType;
import org.iplantc.de.client.models.apps.integration.JobExecution;
import org.iplantc.de.client.models.apps.integration.SelectionItem;
import org.iplantc.de.client.models.apps.integration.SelectionItemGroup;

import com.google.common.base.Strings;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;


public class AnalysisSubmissionUtil {

    public static Splittable assembleLaunchAnalysisPayload(final AppTemplateUtils appTemplateUtils,
                                                           AppTemplate at,
                                                           JobExecution je) {
        com.google.web.bindery.autobean.shared.Splittable assembledPayload =
                AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(je));
        com.google.web.bindery.autobean.shared.Splittable configSplit = StringQuoter.createSplittable();
        for (ArgumentGroup ag : at.getArgumentGroups()) {
            for (Argument arg : ag.getArguments()) {
                com.google.web.bindery.autobean.shared.Splittable value = arg.getValue();
                final boolean diskResourceArgumentType =
                        appTemplateUtils.isDiskResourceArgumentType(arg.getType());
                final boolean simpleSelectionArgumentType =
                        appTemplateUtils.isSimpleSelectionArgumentType(arg.getType());

                if (value == null) {
                    continue;
                }

                if (simpleSelectionArgumentType) {
                    value.assign(configSplit, arg.getId());
                } else if (diskResourceArgumentType && !arg.getType()
                                                           .equals(ArgumentType.MultiFileSelector)) {
                    if (value.get("path") != null) {
                        value.get("path").assign(configSplit, arg.getId());
                    }
                } else if (arg.getType().equals(ArgumentType.MultiFileSelector) && value.isIndexed()) {
                    value.assign(configSplit, arg.getId());
                } else if (arg.getType().equals(ArgumentType.TreeSelection) && (arg.getSelectionItems()
                                                                                != null) && (
                                   arg.getSelectionItems().size() == 1)) {
                    pruneArgumentsFromSelectionItemGroups(value);
                    pruneSelectedItemsWithNoFlagsNorValues(value).assign(configSplit, arg.getId());
                } else {
                    value.assign(configSplit, arg.getId());
                }
            }
        }
        configSplit.assign(assembledPayload, "config");
        return assembledPayload;
    }

    /**
     * @param value and indexed splittable.
     * @return an indexed splittable which contains {@link SelectionItem}s whose {@code "name"} or {@code
     * "value"} JSON keys with non-null values.
     */
    public static Splittable pruneSelectedItemsWithNoFlagsNorValues(final com.google.web.bindery.autobean.shared.Splittable value) {
        com.google.web.bindery.autobean.shared.Splittable ret = StringQuoter.createIndexed();
        for (int i = 0; i < value.size(); i++) {
            final com.google.web.bindery.autobean.shared.Splittable splittable = value.get(i);

            if (!splittable.isUndefined("value") && !Strings.isNullOrEmpty(splittable.get("value")
                                                                                     .asString())) {
                // If the item has a non-null value, add it
                splittable.assign(ret, ret.size());
            } else if (!splittable.isUndefined(SelectionItem.ARGUMENT_OPTION_KEY)
                       && !Strings.isNullOrEmpty(splittable.get(SelectionItem.ARGUMENT_OPTION_KEY)
                                                           .asString())) {
                // If the item has a non-null argument option, add it
                splittable.assign(ret, ret.size());
            }
        }

        return ret;
    }

    /**
     * Nulls out the value associated with {@link SelectionItemGroup#ARGUMENTS_KEY} JSON key in the
     * children of the given indexed splittable.
     *
     * @param value and indexed splittable
     */
    public static void pruneArgumentsFromSelectionItemGroups(final com.google.web.bindery.autobean.shared.Splittable value) {
        for (int i = 0; i < value.size(); i++) {
            final com.google.web.bindery.autobean.shared.Splittable splittable = value.get(i);
            if (splittable.isUndefined(SelectionItemGroup.ARGUMENTS_KEY)) {
                // If the key is undefined, continue
                continue;
            }
            // Remove arguments from selectionItemGroup
            com.google.web.bindery.autobean.shared.Splittable.NULL.assign(splittable,
                                                                          SelectionItemGroup.ARGUMENTS_KEY);
        }
    }

}
