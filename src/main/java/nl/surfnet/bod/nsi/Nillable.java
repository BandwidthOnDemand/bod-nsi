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

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

import jakarta.xml.bind.JAXBElement;

public interface Nillable<T> {
    <R> Nillable<R> map(Function<T, R> f);
    <R> Nillable<R> flatMap(Function<T, Nillable<R>> f);
    <R> R fold(Function<T, R> present, Supplier<R> absent, Supplier<R> nil);
    Nillable<T> orElse(Supplier<Nillable<T>> f);

    static final Nillable<?> ABSENT = new Absent<>();
    static final Nillable<?> NIL = new Nil<>();

    static <T> Nillable<T> ofXmlElement(JAXBElement<T> element) {
        if (element == null) {
            return absent();
        } else if (element.isNil()) {
            return nil();
        } else {
            return present(element.getValue());
        }
    }

    static <T> Nillable<T> ofNullable(T maybeValue) {
        if (maybeValue == null) {
            return absent();
        } else {
            return present(maybeValue);
        }
    }

    static <T> Nillable<T> present(T value) {
        return new Present<>(value);
    }

    @SuppressWarnings("unchecked")
    static <T> Nillable<T> absent() {
        return (Nillable<T>) ABSENT;
    }

    @SuppressWarnings("unchecked")
    static <T> Nillable<T> nil() {
        return (Nillable<T>) NIL;
    }

    class Present<T> implements Nillable<T> {
        private final T value;

        Present(T value) {
            this.value = value;
        }

        @Override
        public <R> Nillable<R> map(Function<T, R> f) {
            return present(f.apply(value));
        }

        @Override
        public <R> Nillable<R> flatMap(Function<T, Nillable<R>> f) {
            return f.apply(value);
        }

        @Override
        public <R> R fold(Function<T, R> present, Supplier<R> absent, Supplier<R> nil) {
            return present.apply(value);
        }

        @Override
        public Nillable<T> orElse(Supplier<Nillable<T>> f) {
            return this;
        }

        @Override
        public String toString() {
            return "Present " + value.toString();
        }

        @Override
        public int hashCode() {
        	return 23 + 61 * Objects.hashCode(value);
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) { return false; }
            if (o == this) { return true; }

            return o instanceof Present that && Objects.equals(value, that.value);
        }
    }

    class Absent<T> implements Nillable<T> {
        @Override
        @SuppressWarnings("unchecked")
        public <R> Nillable<R> map(Function<T, R> f) {
            return (Absent<R>) this;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <R> Nillable<R> flatMap(Function<T, Nillable<R>> f) {
            return (Absent<R>) this;
        }

        @Override
        public <R> R fold(Function<T, R> present, Supplier<R> absent, Supplier<R> nil) {
            return absent.get();
        }

        @Override
        public Nillable<T> orElse(Supplier<Nillable<T>> f) {
            return f.get();
        }

        @Override
        public String toString() {
            return "Absent";
        }

        @Override
        public int hashCode() {
            return 29;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) { return false; }
            if (o == this) { return true; }
            return o.getClass() == this.getClass();
        }
    }

    class Nil<T> implements Nillable<T> {
        @Override
        @SuppressWarnings("unchecked")
        public <R> Nillable<R> map(Function<T, R> f) {
            return (Nil<R>) this;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <R> Nillable<R> flatMap(Function<T, Nillable<R>> f) {
            return (Nil<R>) this;
        }

        @Override
        public <R> R fold(Function<T, R> present, Supplier<R> absent, Supplier<R> nil) {
            return nil.get();
        }

        @Override
        public Nillable<T> orElse(Supplier<Nillable<T>> f) {
            return this;
        }

        @Override
        public String toString() {
            return "Nil";
        }

        @Override
        public int hashCode() {
            return 31;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) { return false; }
            if (o == this) { return true; }
            return o.getClass() == this.getClass();
        }
    }
}
