# LiveData-Test

[![Build Status](https://travis-ci.org/peter-tackage/livedata-test.svg?branch=master)](https://travis-ci.org/peter-tackage/livedata-test)

A small Android Kotlin library to help you test Android's LiveData objects.

## Usage

The `values` property allows you to test all the values emitted by the LiveData .

```kotlin
val liveData = MutableLiveData<String>()
val liveDataTest = liveData.test()

// Act to trigger events on the LiveData object
liveData.postValue("abc")
liveData.postValue("def")
liveData.postValue("123")
liveData.postValue("ghi")

assertEquals(listOf("abc", "def", "123", "ghi"), liveDataTest.values)

```

The `value` property allows you to test the latest value emitted by the LiveData .

```kotlin
val liveData = MutableLiveData<String>()
val liveDataTest = liveData.test()

// Act to trigger events on the LiveData object
liveData.postValue("abc")
liveData.postValue("def")

assertEquals("def", liveDataTest.value)

```

# Acknowledgements

Brought to you by the power of the [Chilicorn](http://spiceprogram.org/chilicorn-history/) and the [Futurice Open Source Program](http://spiceprogram.org/).

![Chilicorn Logo](https://raw.githubusercontent.com/futurice/spiceprogram/gh-pages/assets/img/logo/chilicorn_no_text-256.png)

License
=======

    Copyright 2018 Peter Tackage

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.