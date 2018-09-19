# LiveData-Test

A small Android Kotlin library to help you test Android's LiveData objects.

## Usage

```kotlin
// Define the LiveData object
val livedata = MutableLiveData<String>()

// Use the test() extension to capture the most recent and subsequent events emitted by the LiveData
val liveDataTest = livedata.test()

// Act to trigger events on the LiveData object
livedata.postValue("abc")
livedata.postValue("def")
livedata.postValue("123")
livedata.postValue("ghi")

// Use the value or values properties to assert your test conditions however you like
assertEquals(listOf("abc", "def", "123", "ghi"), liveDataTest.values)

```

The `skip` function allows you ignore initial emissions from the LiveData object.

```kotlin
val livedata = MutableLiveData<String>()

val liveDataTest = livedata.test()

livedata.postValue("abc")
livedata.postValue("def")
livedata.postValue("123")
livedata.postValue("ghi")

// Skips the first two values: "abc" and "def"
assertEquals(listOf("123", "ghi"), liveDataTest.skip(2).values)
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