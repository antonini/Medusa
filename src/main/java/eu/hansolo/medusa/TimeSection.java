/*
 * Copyright (c) 2016 by Gerrit Grunwald
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.hansolo.medusa;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.time.LocalTime;


/**
 * Created by hansolo on 31.01.16.
 */
public class TimeSection implements Comparable<TimeSection> {
    public final TimeSectionEvent ENTERED_EVENT = new TimeSectionEvent(this, null, TimeSectionEvent.TIME_SECTION_ENTERED);
    public final TimeSectionEvent LEFT_EVENT    = new TimeSectionEvent(this, null, TimeSectionEvent.TIME_SECTION_LEFT);
    private LocalTime                 _start;
    private ObjectProperty<LocalTime> start;
    private LocalTime                 _stop;
    private ObjectProperty<LocalTime> stop;
    private String                    _text;
    private StringProperty            text;
    private Image                     _icon;
    private ObjectProperty<Image>     icon;
    private Color                     _color;
    private ObjectProperty<Color>     color;
    private Color                     _textColor;
    private ObjectProperty<Color>     textColor;
    private LocalTime                 checkedValue;


    // ******************** Constructors **************************************
    public TimeSection() {
        this(LocalTime.now(), LocalTime.now(), "", null, Color.TRANSPARENT, Color.TRANSPARENT);
    }
    public TimeSection(final LocalTime START, final LocalTime STOP) {
        this(START, STOP, "", null, Color.TRANSPARENT, Color.TRANSPARENT);
    }
    public TimeSection(final LocalTime START, final LocalTime STOP, final Color COLOR) {
        this(START, STOP, "", null, COLOR, Color.TRANSPARENT);
    }
    public TimeSection(final LocalTime START, final LocalTime STOP, final Image ICON, final Color COLOR) {
        this(START, STOP, "", ICON, COLOR, Color.WHITE);
    }
    public TimeSection(final LocalTime START, final LocalTime STOP, final String TEXT, final Color COLOR) {
        this(START, STOP, TEXT, null, COLOR, Color.WHITE);
    }
    public TimeSection(final LocalTime START, final LocalTime STOP, final String TEXT, final Color COLOR, final Color TEXT_COLOR) {
        this(START, STOP, TEXT, null, COLOR, TEXT_COLOR);
    }
    public TimeSection(final LocalTime START, final LocalTime STOP, final String TEXT, final Image ICON, final Color COLOR, final Color TEXT_COLOR) {
        _start       = START;
        _stop        = STOP;
        _text        = TEXT;
        _icon        = ICON;
        _color       = COLOR;
        _textColor   = TEXT_COLOR;
        checkedValue = LocalTime.MIN;
    }


    // ******************** Methods *******************************************
    public LocalTime getStart() { return null == start ? _start : start.get(); }
    public void setStart(final LocalTime START) {
        if (null == start) {
            _start = START;
        } else {
            start.set(START);
        }
    }
    public ObjectProperty<LocalTime> startProperty() {
        if (null == start) { start = new SimpleObjectProperty<>(TimeSection.this, "start", _start); }
        return start;
    }

    public LocalTime getStop() { return null == stop ? _stop : stop.get(); }
    public void setStop(final LocalTime STOP) {
        if (null == stop) {
            _stop = STOP;
        } else {
            stop.set(STOP);
        }
    }
    public ObjectProperty<LocalTime> stopProperty() {
        if (null == stop) { stop = new SimpleObjectProperty<>(TimeSection.this, "stop", _stop); }
        return stop;
    }

    public String getText() { return null == text ? _text : text.get(); }
    public void setText(final String TEXT) {
        if (null == text) {
            _text = TEXT;
        } else {
            text.set(TEXT);
        }
    }
    public StringProperty textProperty() {
        if (null == text) { text = new SimpleStringProperty(TimeSection.this, "text", _text); }
        return text;
    }

    public Image getImage() { return null == icon ? _icon : icon.get(); }
    public void setIcon(final Image IMAGE) {
        if (null == icon) {
            _icon = IMAGE;
        } else {
            icon.set(IMAGE);
        }
    }
    public ObjectProperty<Image> iconProperty() {
        if (null == icon) { icon = new SimpleObjectProperty<>(this, "icon", _icon); }
        return icon;
    }

    public Color getColor() { return null == color ? _color : color.get(); }
    public void setColor(final Color COLOR) {
        if (null == color) {
            _color = COLOR;
        } else {
            color.set(COLOR);
        }
    }
    public ObjectProperty<Color> colorProperty() {
        if (null == color) { color = new SimpleObjectProperty<>(TimeSection.this, "color", _color); }
        return color;
    }

    public Color getTextColor() { return null == textColor ? _textColor : textColor.get(); }
    public void setTextColor(final Color COLOR) {
        if (null == textColor) {
            _textColor = COLOR;
        } else {
            textColor.set(COLOR);
        }
    }
    public ObjectProperty<Color> textColorProperty() {
        if (null == textColor) { textColor = new SimpleObjectProperty<>(TimeSection.this, "textColor", _textColor); }
        return textColor;
    }

    public boolean contains(final LocalTime VALUE) {
        return VALUE.isAfter(getStart()) && VALUE.isBefore(getStop());
    }

    public void checkForValue(final LocalTime VALUE) {
        boolean wasInSection = contains(checkedValue);
        boolean isInSection  = contains(VALUE);
        if (!wasInSection && isInSection) {
            fireTimeSectionEvent(ENTERED_EVENT);
        } else if (wasInSection && !isInSection) {
            fireTimeSectionEvent(LEFT_EVENT);
        }
        checkedValue = VALUE;
    }

    public boolean equals(final TimeSection SECTION) {
        return (SECTION.getStart().equals(getStart()) &&
                SECTION.getStop().equals(getStop()) &&
                SECTION.getText().equals(getText()));
    }

    @Override public int compareTo(final TimeSection SECTION) {
        if (getStart().isBefore(SECTION.getStart())) return -1;
        if (getStart().isAfter(SECTION.getStart())) return 1;
        return 0;
    }

    @Override public String toString() {
        return new StringBuilder()
            .append("Section   : ").append("\n")
            .append("text      : ").append(getText()).append("\n")
            .append("startValue: ").append(getStart()).append("\n")
            .append("stopValue : ").append(getStop()).append("\n")
            .append("color     : ").append(getColor().toString())
            .append("textColor : ").append(getTextColor().toString()).toString();
    }


    // ******************** Event handling ************************************
    public final ObjectProperty<EventHandler<TimeSectionEvent>> onTimeSectionEnteredProperty() { return onTimeSectionEntered; }
    public final void setOnTimeSectionEntered(EventHandler<TimeSectionEvent> value) { onTimeSectionEnteredProperty().set(value); }
    public final EventHandler<TimeSectionEvent> getOnTimeSectionEntered() { return onTimeSectionEnteredProperty().get(); }
    private ObjectProperty<EventHandler<TimeSectionEvent>> onTimeSectionEntered = new SimpleObjectProperty<>(this, "onTimeSectionEntered");

    public final ObjectProperty<EventHandler<TimeSectionEvent>> onTimeSectionLeftProperty() { return onTimeSectionLeft; }
    public final void setOnTimeSectionLeft(EventHandler<TimeSectionEvent> value) { onTimeSectionLeftProperty().set(value); }
    public final EventHandler<TimeSectionEvent> getOnTimeSectionLeft() { return onTimeSectionLeftProperty().get(); }
    private ObjectProperty<EventHandler<TimeSectionEvent>> onTimeSectionLeft = new SimpleObjectProperty<>(this, "onTimeSectionLeft");

    public void fireTimeSectionEvent(final TimeSectionEvent EVENT) {
        final EventHandler<TimeSectionEvent> HANDLER;
        final EventType                      TYPE = EVENT.getEventType();
        if (TimeSectionEvent.TIME_SECTION_ENTERED == TYPE) {
            HANDLER = getOnTimeSectionEntered();
        } else if (TimeSectionEvent.TIME_SECTION_LEFT == TYPE) {
            HANDLER = getOnTimeSectionLeft();
        } else {
            HANDLER = null;
        }

        if (null == HANDLER) return;

        HANDLER.handle(EVENT);
    }


    // ******************** Inner Classes *************************************
    public static class TimeSectionEvent extends Event {
        public static final EventType<TimeSectionEvent> TIME_SECTION_ENTERED = new EventType(ANY, "TIME_SECTION_ENTERED");
        public static final EventType<TimeSectionEvent> TIME_SECTION_LEFT    = new EventType(ANY, "TIME_SECTION_LEFT");

        // ******************** Constructors **************************************
        public TimeSectionEvent(final Object SOURCE, final EventTarget TARGET, EventType<TimeSectionEvent> TYPE) {
            super(SOURCE, TARGET, TYPE);
        }
    }
}

