package org.andstatus.todoagenda.prefs;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;

import static org.andstatus.todoagenda.prefs.EventSource.EMPTY;

/**
 * @author yvolk@yurivolkov.com
 */
public class OrderedEventSource {
    public final EventSource source;
    public final int order;

    public OrderedEventSource(EventSource source, int order) {
        this.source = source;
        this.order = order;
    }

    public static List<OrderedEventSource> fromJsonString(String sources) {
        if (sources == null) return Collections.emptyList();

        try {
            return fromJsonArray(new JSONArray(sources));
        } catch (JSONException e) {
            Log.w(EventSource.class.getSimpleName(), "Failed to parse event sources: " + sources, e);
            return Collections.emptyList();
        }
    }

    @NonNull
    public static List<OrderedEventSource> fromJsonArray(JSONArray jsonArray) {
        List<OrderedEventSource> list = new ArrayList<>();
        for (int index = 0; index < jsonArray.length(); index++) {
            String value = jsonArray.optString(index);
            add(list, EventSource.fromStoredString(value));
        }
        return list;
    }

    public static List<OrderedEventSource> fromSources(List<EventSource> sources) {
        return addAll(new ArrayList<OrderedEventSource>(), sources);
    }

    public static List<OrderedEventSource> addAll(List<OrderedEventSource> list, List<EventSource> sources) {
        for(EventSource source: sources) {
            add(list, source);
        }
        return list;
    }

    private static void add(List<OrderedEventSource> list, EventSource source) {
        if (source != EMPTY) {
            list.add(new OrderedEventSource(source, list.size() + 1));
        }
    }

    @NonNull
    public static String toJsonString(List<OrderedEventSource> eventSources) {
        return toJsonArray(eventSources).toString();
    }

    @NonNull
    public static JSONArray toJsonArray(List<OrderedEventSource> sources) {
        List<String> strings = new ArrayList<>();
        for(OrderedEventSource source: sources) {
            strings.add(source.source.toStoredString());
        }
        return new JSONArray(strings);
    }
}
