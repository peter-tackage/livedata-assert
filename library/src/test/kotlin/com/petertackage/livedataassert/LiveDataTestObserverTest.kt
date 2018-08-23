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
    fun `assertValueCount() does not assert when values match (zero values)`() {
        val ld = MutableLiveData<String>()

        val lda = ld.test()

        lda.assertValueCount(0)
    }

    @Test
    fun `assertValueCount() does not assert when values match (one value)`() {
        val ld = MutableLiveData<String>()
        val lda = ld.test()

        ld.postValue("abc")

        lda.assertValueCount(1)
    }

    @Test
    fun `assertValueCount() asserts when values do not match (zero values)`() {
        val ld = MutableLiveData<String>()
        val lda = ld.test()

        val ts = assertThrows<AssertionError> {lda.assertValueCount(1)}

        ts.isInstanceOf(AssertionError::class.java)
        ts.hasMessageThat().isEqualTo("Expected: 1 value(s), but has: 0")
    }

    @Test
    fun `assertValueCount() asserts when values do not match (one value)`() {
        val ld = MutableLiveData<String>()
        val lda = ld.test()
        ld.postValue("abc")

        val ts = assertThrows<AssertionError> {lda.assertValueCount(0) }

        ts.isInstanceOf(AssertionError::class.java)
        ts.hasMessageThat().isEqualTo("Expected: 0 value(s), but has: 1")
    }

    @Test
    fun `assertValueCount() expectedCount cannot be negative`() {
        val ld = MutableLiveData<String>()
        val lda = ld.test()
        ld.postValue("abc")

        val ts = assertThrows<IllegalArgumentException> {lda.assertValueCount(-1) }

        ts.isInstanceOf(IllegalArgumentException::class.java)
        ts.hasMessageThat().isEqualTo("Expected count parameter must be non-negative")
    }

    @Test
    fun `assertOnlyValue() does not assert when only one value which matches`() {
        val ld = MutableLiveData<String>()
        val lda = ld.test()

        ld.postValue("abc")

        lda.assertOnlyValue("abc")
    }

    @Test
    fun `assertOnlyValue() asserts when more than one value which matches`() {
        val ld = MutableLiveData<String>()
        val lda = ld.test()
        ld.postValue("abc")
        ld.postValue("def")

        val ts = assertThrows<AssertionError> { lda.assertOnlyValue("def") }

        ts.isInstanceOf(AssertionError::class.java)
        ts.hasMessageThat().isEqualTo("Expected a single value, but has: 2")
    }

    @Test
    fun `assertOnlyValue() asserts when one value which does not match`() {
        val ld = MutableLiveData<String>()
        val lda = ld.test()
        ld.postValue("abc")

        val ts = assertThrows<AssertionError> { lda.assertOnlyValue("def") }

        ts.isInstanceOf(AssertionError::class.java)
        ts.hasMessageThat().isEqualTo("expected:<def> but was:<abc>")
    }

    @Test
    fun `assertValue() does not assert when only one value which matches`() {
        val ld = MutableLiveData<String>()
        val lda = ld.test()

        ld.postValue("abc")

        lda.assertValue("abc")
    }

    @Test
    fun `assertValue() does not assert when only multiple values, the last of which matches`() {
        val ld = MutableLiveData<String>()
        val lda = ld.test()

        ld.postValue("abc")
        ld.postValue("def")
        ld.postValue("ghi")

        lda.assertValue("ghi")
    }

    @Test
    fun `assertValue() asserts when no value posted`() {
        val ld = MutableLiveData<String>()
        val lda = ld.test()

        val ts = assertThrows<AssertionError> { lda.assertValue("abc") }

        ts.isInstanceOf(AssertionError::class.java)
        ts.hasMessageThat().isEqualTo("Expected at least one value.")
    }

    @Test
    fun `assertValue() asserts when single value posted does not match`() {
        val ld = MutableLiveData<String>()
        val lda = ld.test()
        ld.postValue("abc")

        val ts = assertThrows<AssertionError> { lda.assertValue("def") }

        ts.isInstanceOf(AssertionError::class.java)
        ts.hasMessageThat().isEqualTo("expected:<def> but was:<abc>")
    }

    @Test
    fun `assertValue() asserts when multiple value posted, but last of which does not match`() {
        val ld = MutableLiveData<String>()
        val lda = ld.test()
        ld.postValue("abc")
        ld.postValue("def")
        ld.postValue("ghi")

        val ts = assertThrows<AssertionError> { lda.assertValue("def") }

        ts.isInstanceOf(AssertionError::class.java)
        ts.hasMessageThat().isEqualTo("expected:<def> but was:<ghi>")
    }


    @Test
    fun assertValuePredicate() {
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