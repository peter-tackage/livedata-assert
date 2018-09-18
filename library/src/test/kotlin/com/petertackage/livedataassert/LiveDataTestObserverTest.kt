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
import com.google.common.truth.ThrowableSubject
import com.google.common.truth.Truth
import org.junit.Assert
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
    var thrown = ExpectedException.none()

    @Test(expected = NoSuchElementException::class)
    fun `value throws when no value`() {
        val ld = MutableLiveData<String>()
        val lda = ld.test()

        lda.value
    }

    @Test
    fun `value single value`() {
        val ld = MutableLiveData<String>()
        val lda = ld.test()

        ld.postValue("abc")

        assertEquals("abc", lda.value)
    }

    @Test
    fun `value take last value value`() {
        val ld = MutableLiveData<String>()
        val lda = ld.test()

        ld.postValue("abc")
        ld.postValue("def")
        ld.postValue("ghi")

        assertEquals("ghi", lda.value)
    }

    @Test
    fun `values when empty`() {
        val ld = MutableLiveData<String>()
        val lda = ld.test()

        assertEquals(emptyList<String>(), lda.values)
    }

    @Test
    fun `values single value`() {
        val ld = MutableLiveData<String>()
        val lda = ld.test()

        ld.postValue("abc")

        assertEquals(listOf("abc"), lda.values)
    }

    @Test
    fun `values multiple values`() {
        val ld = MutableLiveData<String>()
        val lda = ld.test()

        ld.postValue("abc")
        ld.postValue("def")
        ld.postValue("abc")
        ld.postValue("ghi")

        assertEquals(listOf("abc", "def", "abc", "ghi"), lda.values)
    }

    @Test
    fun `skip defaults to 1`() {
        val ld = MutableLiveData<String>()
        val lda = ld.test()

        ld.postValue("abc")
        ld.postValue("def")

        val values = lda.skip().values

        assertEquals(listOf("def"), values)
    }

    @Test
    fun `skip with count not equal to 1`() {
        val ld = MutableLiveData<String>()
        val lda = ld.test()

        ld.postValue("abc")
        ld.postValue("def")
        ld.postValue("ghi")
        ld.postValue("jkl")

        val values = lda.skip(2).values

        assertEquals(listOf("ghi", "jkl"), values)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `skip throws when count higher than value count`() {
        val ld = MutableLiveData<String>()
        val lda = ld.test()

        lda.skip()
    }

    @Test(expected = IllegalArgumentException::class)
    fun `more of the same 2`() {

        val ld = MutableLiveData<String>()
        val lda = ld.test()

        ld.postValue("abc")
        ld.postValue("def")

        lda.skip(3)
    }

    @Test
    fun `more of the same`() {

        val ld = MutableLiveData<String>()
        val lda = ld.test()

        ld.postValue("abc")
        ld.postValue("def")

        val values = lda.skip(2).values
        assertEquals(emptyList<String>(), values)
    }

}
