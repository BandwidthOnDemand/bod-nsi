/**
 * Copyright (c) 2012-2024 SURFnet BV
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 *
 *   * Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 *     disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following
 *     disclaimer in the documentation and/or other materials provided with the distribution.
 *   * Neither the name of the SURFnet BV nor the names of its contributors may be used to endorse or promote products
 *     derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package nl.surfnet.bod.nsi;

import jakarta.xml.bind.JAXBElement;

import java.util.Objects;

import javax.xml.datatype.XMLGregorianCalendar;

import org.ogf.schemas.nsi._2013._12.connection.types.ObjectFactory;
import org.ogf.schemas.nsi._2013._12.connection.types.ScheduleType;

public abstract class ScheduleExt {
    private static final ObjectFactory XML_SCHEDULE_TYPES = new ObjectFactory();

    public abstract JAXBElement<XMLGregorianCalendar> getXmlStartTime();

    public abstract JAXBElement<XMLGregorianCalendar> getXmlEndTime();

    public abstract ScheduleType withXmlStartTime(JAXBElement<XMLGregorianCalendar> value);

    public abstract ScheduleType withXmlEndTime(JAXBElement<XMLGregorianCalendar> value);

    public final Nillable<XMLGregorianCalendar> getStartTime() {
        return Nillable.ofXmlElement(getXmlStartTime());
    }

    public final Nillable<XMLGregorianCalendar> getEndTime() {
        return Nillable.ofXmlElement(getXmlEndTime());
    }

    public final ScheduleType withStartTime(Nillable<XMLGregorianCalendar> startTime) {
        return withXmlStartTime(switch (startTime) {
        case Nillable.Absent<?> unused -> null;
        case Nillable.Nil<?> unused -> XML_SCHEDULE_TYPES.createScheduleTypeStartTime(null);
        case Nillable.Present(var value) -> XML_SCHEDULE_TYPES.createScheduleTypeStartTime(value);
        });
    }

    public final ScheduleType withEndTime(Nillable<XMLGregorianCalendar> endTime) {
        return withXmlStartTime(switch (endTime) {
        case Nillable.Absent<?> unused -> null;
        case Nillable.Nil<?> unused -> XML_SCHEDULE_TYPES.createScheduleTypeEndTime(null);
        case Nillable.Present(var value) -> XML_SCHEDULE_TYPES.createScheduleTypeEndTime(value);
        });
    }

    public final ScheduleType withStartTime(XMLGregorianCalendar startTime) {
        return withXmlStartTime(startTime != null ? XML_SCHEDULE_TYPES.createScheduleTypeStartTime(startTime) : null);
    }

    public final ScheduleType withEndTime(XMLGregorianCalendar endTime) {
        return withXmlEndTime(endTime != null ? XML_SCHEDULE_TYPES.createScheduleTypeEndTime(endTime) : null);
    }

    @Override
    public final boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (o instanceof ScheduleExt that) {
            return Objects.equals(this.getXmlStartTime(), that.getXmlStartTime())
                    && Objects.equals(this.getXmlEndTime(), that.getXmlEndTime());
        } else {
            return false;
        }
    }

    @Override
    public final int hashCode() {
        return 41 + Objects.hash(getXmlStartTime(), getXmlEndTime());
    }
}
