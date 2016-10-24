/**
 * Copyright (c) 2012, 2013, 2014, 2015, 2016 SURFnet BV
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

import java.util.function.Function;
import java.util.function.Supplier;

import javax.xml.bind.JAXBElement;

public interface NsiScheduleTime<T> {
    <R> NsiScheduleTime<R> map(Function<T, R> f);
    <R> R fold(Function<T, R> present, Supplier<R> absent, Supplier<R> nil);

    static <T> NsiScheduleTime<T> ofXmlElement(JAXBElement<T> element) {
        if (element.isNil()) {
            return new Nil<>();
        } else if (element.getValue() == null) {
            return new Absent<>();
        } else {
            return new Present<>(element.getValue());
        }
    }

    class Present<T> implements NsiScheduleTime<T> {
        private final T value;

        Present(T value) {
            this.value = value;
        }

        @Override
        public <R> NsiScheduleTime<R> map(Function<T, R> f) {
            return new Present<>(f.apply(value));
        }

        @Override
        public <R> R fold(Function<T, R> present, Supplier<R> absent, Supplier<R> nil) {
            return present.apply(value);
        }
    }

    class Absent<T> implements NsiScheduleTime<T> {
        @Override
        @SuppressWarnings("unchecked")
        public <R> NsiScheduleTime<R> map(Function<T, R> f) {
            return (Absent<R>) this;
        }

        @Override
        public <R> R fold(Function<T, R> present, Supplier<R> absent, Supplier<R> nil) {
            return absent.get();
        }
    }

    class Nil<T> implements NsiScheduleTime<T> {
        @Override
        @SuppressWarnings("unchecked")
        public <R> NsiScheduleTime<R> map(Function<T, R> f) {
            return (Nil<R>) this;
        }

        @Override
        public <R> R fold(Function<T, R> present, Supplier<R> absent, Supplier<R> nil) {
            return nil.get();
        }
    }
}
