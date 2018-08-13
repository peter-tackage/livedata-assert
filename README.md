# LiveTest

A small Android Kotlin library to provide a flexible set of assertions for Android's LiveData.

## Usage

Generally the API is similar to assertions provided by RxJava's TestSubscriber.


```kotlin
// Create your LiveData object to test
val livedata : LiveData<String> = ...

// Use the test() extension to capture the most recent and subsequent events emitted by the LiveData
val liveDataEvents : LiveDataAssertion<String> = livedata.test()

// Act to trigger events
// ...

// Assert
liveDataEvents
    .assertValueCount(2)
    .assertValues("value1", "value2")
```

You can directly access the values captured via `test().values()` and assert on them with a more sophisticated assertion library, such as AssertJ or Truth.

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