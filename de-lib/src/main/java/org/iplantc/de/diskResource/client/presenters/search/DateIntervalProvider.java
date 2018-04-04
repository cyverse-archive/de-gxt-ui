package org.iplantc.de.diskResource.client.presenters.search;

import org.iplantc.de.client.models.search.DateInterval;
import org.iplantc.de.client.models.search.SearchAutoBeanFactory;

import com.google.gwt.core.client.GWT;
import com.google.inject.Provider;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;

import com.sencha.gxt.core.client.util.DateWrapper;

import java.util.Date;

/**
 * A provider for the date intervals provided in the Data window's search form
 */
public class DateIntervalProvider implements Provider<Splittable> {

    private SearchAutoBeanFactory factory = GWT.create(SearchAutoBeanFactory.class);

    @Override
    public Splittable get() {
        Splittable list = StringQuoter.createIndexed();

        Date now = new Date();

        Splittable interval = createDateInterval(null, null, "");
        interval.assign(list, list.size());

        final DateWrapper dateWrapper = new DateWrapper(now).clearTime();
        interval = createDateInterval(dateWrapper.addDays(-1).asDate(), now, "1 day");
        interval.assign(list, list.size());

        interval = createDateInterval(dateWrapper.addDays(-3).asDate(), now, "3 days");
        interval.assign(list, list.size());

        interval = createDateInterval(dateWrapper.addDays(-7).asDate(), now, "1 week");
        interval.assign(list, list.size());

        interval = createDateInterval(dateWrapper.addDays(-14).asDate(), now, "2 weeks");
        interval.assign(list, list.size());

        interval = createDateInterval(dateWrapper.addMonths(-1).asDate(), now, "1 month");
        interval.assign(list, list.size());

        interval = createDateInterval(dateWrapper.addMonths(-2).asDate(), now, "2 months");
        interval.assign(list, list.size());

        interval = createDateInterval(dateWrapper.addMonths(-6).asDate(), now, "6 months");
        interval.assign(list, list.size());

        interval = createDateInterval(dateWrapper.addYears(-1).asDate(), now, "1 year");
        interval.assign(list, list.size());

        return list;
    }

    Splittable createDateInterval(Date from, Date to, String label) {
        DateInterval interval = factory.dateInterval().as();
        interval.setFrom(from);
        interval.setTo(to);
        interval.setLabel(label);

        return AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(interval));
    }
}
