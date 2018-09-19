package com.petertackage.livedataassert

/*
 * Copyright 2018 Peter Tackage
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

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData

import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.rules.TestRule

class LiveDataTestObserverTest {

    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    var thrown : ExpectedException = ExpectedException.none()

    @Test
    fun `value throws NoSuchElementException when no values posted`() {
        val ld = MutableLiveData<String>()
        val lda = ld.test()
        thrown.expect(NoSuchElementException::class.java)

        lda.value
    }

    @Test
    fun `value returns single value when single value posted`() {
        val ld = MutableLiveData<String>()
        val lda = ld.test()

        ld.postValue("abc")

        assertEquals("abc", lda.value)
    }

    @Test
    fun `value returns last value when multiple values posted`() {
        val ld = MutableLiveData<String>()
        val lda = ld.test()

        ld.postValue("abc")
        ld.postValue("def")
        ld.postValue("ghi")

        assertEquals("ghi", lda.value)
    }

    @Test
    fun `values return empty list when no values posted`() {
        val ld = MutableLiveData<String>()
        val lda = ld.test()

        assertEquals(emptyList<String>(), lda.values)
    }

    @Test
    fun `values returns singleton list when single value posted`() {
        val ld = MutableLiveData<String>()
        val lda = ld.test()

        ld.postValue("abc")

        assertEquals(listOf("abc"), lda.values)
    }

    @Test
    fun `values returns all values when multiple values posted`() {
        val ld = MutableLiveData<String>()
        val lda = ld.test()

        ld.postValue("abc")
        ld.postValue("def")
        ld.postValue("abc")
        ld.postValue("ghi")

        assertEquals(listOf("abc", "def", "abc", "ghi"), lda.values)
    }

    @Test
    fun `skip defaults to 1 value skipped`() {
        val ld = MutableLiveData<String>()
        val lda = ld.test()

        ld.postValue("abc")
        ld.postValue("def")

        val values = lda.skip().values
        assertEquals(listOf("def"), values)
    }

    @Test
    fun `skip skips according to count`() {
        val ld = MutableLiveData<String>()
        val lda = ld.test()

        ld.postValue("abc")
        ld.postValue("def")
        ld.postValue("ghi")
        ld.postValue("jkl")

        val values = lda.skip(count = 2).values
        assertEquals(listOf("ghi", "jkl"), values)
    }

    @Test
    fun `skip throws IllegalArgumentException when no values posted`() {
        val ld = MutableLiveData<String>()
        val lda = ld.test()
        thrown.expect(IllegalArgumentException::class.java)

        lda.skip()
    }

    @Test
    fun `skip throws IllegalArgumentException when count is greater than posted value count`() {
        val ld = MutableLiveData<String>()
        val lda = ld.test()
        thrown.expect(IllegalArgumentException::class.java)

        ld.postValue("abc")
        ld.postValue("def")

        lda.skip(count = 3)
    }

    @Test
    fun `skip returns empty list when count is equal to posted value count`() {
        val ld = MutableLiveData<String>()
        val lda = ld.test()

        ld.postValue("abc")
        ld.postValue("def")

        val values = lda.skip(count = 2).values
        assertEquals(emptyList<String>(), values)
    }

}
