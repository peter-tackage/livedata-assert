package com.petertackage.livedataassert

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import com.google.common.truth.ThrowableSubject
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.rules.ExpectedException

class LiveDataTestObserverTest {

    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    var thrown = ExpectedException.none()

    @Test
    fun `getValues returns empty when no values posted` () {
        val ld = MutableLiveData<String>()
        val lda = ld.test()

        assertThat(lda.values).isEmpty()
    }

    @Test
    fun `getValues() returns all values posted after test()` () {
        val ld = MutableLiveData<String>()
        val lda = ld.test()

        ld.postValue("abc")
        ld.postValue("def")
        ld.postValue("ghi")

        assertThat(lda.values).containsExactly("abc", "def", "ghi")
    }

    @Test
    fun `getValues() returns last value posted before test()` () {
        val ld = MutableLiveData<String>()
        ld.postValue("uvw")
        ld.postValue("xyz")

        val lda = ld.test()

        assertThat(lda.values).containsExactly("xyz")
    }

    @Test
    fun `getValue() returns value when single value`() {
        val ld = MutableLiveData<String>()
        val lda = ld.test()

        ld.postValue("abc")

        assertThat(lda.getValue()).isEqualTo("abc")
    }

    @Test
    fun `getValue() returns last value when multiple values posted`() {
        val ld = MutableLiveData<String>()
        val lda = ld.test()

        ld.postValue("abc")
        ld.postValue("def")

        assertThat(lda.getValue()).isEqualTo("def")
    }

    @Test
    fun `getValue() returns null when no values posted`() {
        val ld = MutableLiveData<String>()
        val lda = ld.test()

        assertThat(lda.getValue()).isNull()
    }

    @Test
    fun `skip() skips one value by default`() {
        val ld = MutableLiveData<String>()
        val lda = ld.test()

        ld.postValue("abc")
        ld.postValue("def")

        assertThat(lda.skip().getValue()).isEqualTo("def")
    }


    @Test
    fun `skip() skips single value when skipCount 1 and 3 posted events`() {
        val ld = MutableLiveData<String>()
        val lda = ld.test()

        ld.postValue("abc")
        ld.postValue("def")
        ld.postValue("ghi")

        assertThat(lda.skip(count = 1).getValue()).isEqualTo("def")
    }

    @Test
    fun `skip() skips single value when skipCount 1 and 2 posted events`() {
        val ld = MutableLiveData<String>()
        val lda = ld.test()

        ld.postValue("abc")
        ld.postValue("def")

        assertThat(lda.skip(count = 1).getValue()).isEqualTo("def")
    }

    @Test
    fun `skip() getValue() is last value when skips count param values`() {
        val ld = MutableLiveData<String>()
        val lda = ld.test()

        ld.postValue("abc")
        ld.postValue("def")
        ld.postValue("ghi")

        assertThat(lda.skip(count = 2).getValue()).isEqualTo("ghi")
    }

    @Test
    fun `skip() getValue() is null when skips when no values posted`() {
        val ld = MutableLiveData<String>()
        val lda = ld.test()

        assertThat(lda.skip().getValue()).isNull()
    }

    @Test
    fun `skip() getValue() is null when skips all values`() {
        val ld = MutableLiveData<String>()
        val lda = ld.test()

        ld.postValue("abc")
        ld.postValue("def")
        ld.postValue("ghi")

        // TODO Fix the behaviour here. this should return null, right?
        assertThat(lda.skip(count = 3).getValue()).isNull()
    }

    @Test
    fun `skip() asserts when skipping more than posted value count`() {
        val ld = MutableLiveData<String>()
        val lda = ld.test()

        ld.postValue("abc")
        ld.postValue("def")
        ld.postValue("ghi")

        val ts = assertThrows<AssertionError> {lda.skip(4)}

        ts.isInstanceOf(AssertionError::class.java)
        ts.hasMessageThat().isEqualTo("Cannot skip: 4 value(s), when only: 3 values")
    }

    @Test
    fun assertValueCount() {
    }

    @Test
    fun assertOnlyValue() {
    }

    @Test
    fun assertValue() {
    }

    @Test
    fun assertValue1() {
    }

    @Test
    fun assertValueAt() {
    }

    @Test
    fun assertNoValues() {
    }

    @Test
    fun assertValues() {
    }

    @Test
    fun assertOnlyValues() {
    }

    // Refer: https://github.com/google/truth/issues/404
    private inline fun <reified T> assertThrows(block: () -> Unit): ThrowableSubject {
        try {
            block()
        } catch (e: Throwable) {
            if (e is T) {
                return assertThat(e)
            } else {
                throw e
            }
        }
        throw AssertionError("Expected ${T::class}")
    }
}