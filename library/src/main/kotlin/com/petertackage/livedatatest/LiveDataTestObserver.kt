package com.petertackage.livedatatest

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

import android.arch.lifecycle.Observer

class LiveDataTestObserver<T> : Observer<T> {

    private val mutableValues: MutableList<T?>

    internal constructor() {
        mutableValues = ArrayList()
    }

    private constructor(values: MutableList<T?>) {
        LiveDataTestObserver<T?>()
        this.mutableValues = values
    }

    val value
        get() = values.last()

    val values
        get() = mutableValues.toList()

    override fun onChanged(t: T?) {
        mutableValues.add(t)
    }

}
