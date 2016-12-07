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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

public class NillableTest {
    @Test
    public void testMap() {
        assertThat(Nillable.present(42).map(Object::toString), is(present("42")));
        assertThat(Nillable.absent().map(Object::toString), is(absent()));
        assertThat(Nillable.nil().map(Object::toString), is(nil()));
    }

    @Test
    public void testFold() {
        assertThat(Nillable.present("present").fold(x -> x, () -> "absent", () -> "nil"), is("present"));
        assertThat(Nillable.absent().fold(x -> x, () -> "absent", () -> "nil"), is("absent"));
        assertThat(Nillable.nil().fold(x -> x, () -> "absent", () -> "nil"), is("nil"));
    }

    @Test
    public void testFlatMap() {
        assertThat(Nillable.present("present").flatMap(Nillable::present), is(present("present")));
        assertThat(Nillable.present("present").flatMap(x -> Nillable.absent()), is(absent()));
        assertThat(Nillable.nil().flatMap(Nillable::present), is(nil()));
        assertThat(Nillable.absent().flatMap(Nillable::present), is(absent()));
    }

    @Test
    public void testOrElse() {
        assertThat(Nillable.present(1).orElse(() -> Nillable.present(2)), is(present(1)));
        assertThat(Nillable.absent().orElse(() -> Nillable.present(2)), is(present(2)));
        assertThat(Nillable.nil().orElse(() -> Nillable.present(2)), is(nil()));
    }

    private <T> Matcher<Nillable<T>> present(T expected) {
        return new TypeSafeMatcher<Nillable<T>>() {
            protected boolean matchesSafely(Nillable<T> nillable) {
                T actual = nillable.fold(x -> x, () -> null, () -> null);
                return Objects.equals(expected, actual);
            }

            public void describeTo(Description description) {
                description.appendText("present")
                    .appendValue(expected);
            }
        };
    }

    private <T> Matcher<Nillable<T>> nil() {
        return new TypeSafeMatcher<Nillable<T>>() {
            protected boolean matchesSafely(Nillable<T> nillable) {
                return nillable.fold((x) -> false, () -> false, () -> true);
            }

            public void describeTo(Description description) {
                description.appendText("nil");
            }
        };
    }

    private <T> Matcher<Nillable<T>> absent() {
        return new TypeSafeMatcher<Nillable<T>>() {
            protected boolean matchesSafely(Nillable<T> nillable) {
                return nillable.fold((x) -> false, () -> true, () -> false);
            }

            public void describeTo(Description description) {
                description.appendText("absent");
            }
        };
    }
}
