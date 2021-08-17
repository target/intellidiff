# IntelliDiff
A better test failure message tool - get diffs field by field directly in failing test output.

IntelliDiff will identify the values that have changed and include an inline comparison between the provided and expected pieces of a standard unit test. _(the examples are from one of the larger classes in our real codebase)_

<details><summary>:point_left: Using IntelliDiff :point_left:</summary>
<p>

```
java.lang.AssertionError:

### IntelliDiff:

* com.target.diffjunit4.IntelliDiffAssertFailureTest$whatever$Attendance.date
|--> expected: Mon May 17 10:19:17 CDT 2021
|-->   actual: Sun May 16 10:19:17 CDT 2021

* DifferentType
|-->(type: kotlin.Boolean)--> expected: true
|-->(type: kotlin.String) -->   actual: true

* com.target.diffjunit4.IntelliDiffAssertFailureTest$whatever$Attendance.absent[0].id
|--> expected: d96cb798-3034-4aef-bf20-54724d6c4e92e
|-->   actual: d96cb798-3034-4aef-bf20-54724d6c4e92

* com.target.diffjunit4.IntelliDiffAssertFailureTest$whatever$Attendance.absent[0].firstName
|--> expected: <last-name>
|-->   actual: <first-name>

* com.target.diffjunit4.IntelliDiffAssertFailureTest$whatever$Attendance.absent[0].lastName
|--> expected: <first-name>
|-->   actual: <last-name>

* com.target.diffjunit4.IntelliDiffAssertFailureTest$whatever$Attendance.absent[0].nickname
|-->(content: null)--> expected: null
|                  -->   actual: null

expected:<Attendance(date=Mon May 17 10:19:17 CDT 2021, absent=[Person(id=d96cb798-3034-4aef-bf20-54724d6c4e92e, firstName=<last-name>, lastName=<first-name>, nickname=null)], present=[], complete=true)> but was:<Attendance(date=Sun May 16 10:19:17 CDT 2021, absent=[Person(id=d96cb798-3034-4aef-bf20-54724d6c4e92, firstName=<first-name>, lastName=<last-name>, nickname=null)], present=[], complete=true)>

	at com.target.diffjunit4.IntelliDiffAssertions.fail$intellidiff_junit4(IntelliDiffAssertions.kt:27)
	at com.target.diffjunit4.IntelliDiffAssertions.assertEquals(IntelliDiffAssertions.kt:77)
	at com.target.diffjunit4.IntelliDiffAssertions.assertEquals(IntelliDiffAssertions.kt:60)
	at com.target.diffjunit4.IntelliDiffAssert.assertEquals(IntelliDiffAssert.kt:55)
	at com.target.diffjunit4.IntelliDiffAssertFailureTest.whatever(IntelliDiffAssertFailureTest.kt:112)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:50)
	at org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)
	at org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:47)
	at org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:17)
	at org.junit.runners.ParentRunner.runLeaf(ParentRunner.java:325)
	at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:78)
	at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:57)
	at org.junit.runners.ParentRunner$3.run(ParentRunner.java:290)
	at org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:71)
	at org.junit.runners.ParentRunner.runChildren(ParentRunner.java:288)
	at org.junit.runners.ParentRunner.access$000(ParentRunner.java:58)
	at org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:268)
	at org.junit.runners.ParentRunner.run(ParentRunner.java:363)
	at org.gradle.api.internal.tasks.testing.junit.JUnitTestClassExecutor.runTestClass(JUnitTestClassExecutor.java:110)
	at org.gradle.api.internal.tasks.testing.junit.JUnitTestClassExecutor.execute(JUnitTestClassExecutor.java:58)
	at org.gradle.api.internal.tasks.testing.junit.JUnitTestClassExecutor.execute(JUnitTestClassExecutor.java:38)
	at org.gradle.api.internal.tasks.testing.junit.AbstractJUnitTestClassProcessor.processTestClass(AbstractJUnitTestClassProcessor.java:62)
	at org.gradle.api.internal.tasks.testing.SuiteTestClassProcessor.processTestClass(SuiteTestClassProcessor.java:51)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.gradle.internal.dispatch.ReflectionDispatch.dispatch(ReflectionDispatch.java:36)
	at org.gradle.internal.dispatch.ReflectionDispatch.dispatch(ReflectionDispatch.java:24)
	at org.gradle.internal.dispatch.ContextClassLoaderDispatch.dispatch(ContextClassLoaderDispatch.java:33)
	at org.gradle.internal.dispatch.ProxyDispatchAdapter$DispatchingInvocationHandler.invoke(ProxyDispatchAdapter.java:94)
	at com.sun.proxy.$Proxy2.processTestClass(Unknown Source)
	at org.gradle.api.internal.tasks.testing.worker.TestWorker.processTestClass(TestWorker.java:119)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.gradle.internal.dispatch.ReflectionDispatch.dispatch(ReflectionDispatch.java:36)
	at org.gradle.internal.dispatch.ReflectionDispatch.dispatch(ReflectionDispatch.java:24)
	at org.gradle.internal.remote.internal.hub.MessageHubBackedObjectConnection$DispatchWrapper.dispatch(MessageHubBackedObjectConnection.java:182)
	at org.gradle.internal.remote.internal.hub.MessageHubBackedObjectConnection$DispatchWrapper.dispatch(MessageHubBackedObjectConnection.java:164)
	at org.gradle.internal.remote.internal.hub.MessageHub$Handler.run(MessageHub.java:414)
	at org.gradle.internal.concurrent.ExecutorPolicy$CatchAndRecordFailures.onExecute(ExecutorPolicy.java:64)
	at org.gradle.internal.concurrent.ManagedExecutorImpl$1.run(ManagedExecutorImpl.java:48)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
	at org.gradle.internal.concurrent.ThreadFactoryImpl$ManagedThreadRunnable.run(ThreadFactoryImpl.java:56)
	at java.lang.Thread.run(Thread.java:748)
```

</p>
</details>


<details><summary>:point_left: Without IntelliDiff :point_left:</summary>
<p>

```

expected:<Attendance(date=Mon May 17 10:19:17 CDT 2021, absent=[Person(id=d96cb798-3034-4aef-bf20-54724d6c4e92e, firstName=<last-name>, lastName=<first-name>, nickname=null)], present=[], complete=true)> but was:<Attendance(date=Sun May 16 10:19:17 CDT 2021, absent=[Person(id=d96cb798-3034-4aef-bf20-54724d6c4e92, firstName=<first-name>, lastName=<last-name>, nickname=null)], present=[], complete=true)>

	at com.target.diffjunit4.IntelliDiffAssertions.fail$intellidiff_junit4(IntelliDiffAssertions.kt:27)
	at com.target.diffjunit4.IntelliDiffAssertions.assertEquals(IntelliDiffAssertions.kt:77)
	at com.target.diffjunit4.IntelliDiffAssertions.assertEquals(IntelliDiffAssertions.kt:60)
	at com.target.diffjunit4.IntelliDiffAssert.assertEquals(IntelliDiffAssert.kt:55)
	at com.target.diffjunit4.IntelliDiffAssertFailureTest.whatever(IntelliDiffAssertFailureTest.kt:112)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:50)
	at org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)
	at org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:47)
	at org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:17)
	at org.junit.runners.ParentRunner.runLeaf(ParentRunner.java:325)
	at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:78)
	at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:57)
	at org.junit.runners.ParentRunner$3.run(ParentRunner.java:290)
	at org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:71)
	at org.junit.runners.ParentRunner.runChildren(ParentRunner.java:288)
	at org.junit.runners.ParentRunner.access$000(ParentRunner.java:58)
	at org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:268)
	at org.junit.runners.ParentRunner.run(ParentRunner.java:363)
	at org.gradle.api.internal.tasks.testing.junit.JUnitTestClassExecutor.runTestClass(JUnitTestClassExecutor.java:110)
	at org.gradle.api.internal.tasks.testing.junit.JUnitTestClassExecutor.execute(JUnitTestClassExecutor.java:58)
	at org.gradle.api.internal.tasks.testing.junit.JUnitTestClassExecutor.execute(JUnitTestClassExecutor.java:38)
	at org.gradle.api.internal.tasks.testing.junit.AbstractJUnitTestClassProcessor.processTestClass(AbstractJUnitTestClassProcessor.java:62)
	at org.gradle.api.internal.tasks.testing.SuiteTestClassProcessor.processTestClass(SuiteTestClassProcessor.java:51)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.gradle.internal.dispatch.ReflectionDispatch.dispatch(ReflectionDispatch.java:36)
	at org.gradle.internal.dispatch.ReflectionDispatch.dispatch(ReflectionDispatch.java:24)
	at org.gradle.internal.dispatch.ContextClassLoaderDispatch.dispatch(ContextClassLoaderDispatch.java:33)
	at org.gradle.internal.dispatch.ProxyDispatchAdapter$DispatchingInvocationHandler.invoke(ProxyDispatchAdapter.java:94)
	at com.sun.proxy.$Proxy2.processTestClass(Unknown Source)
	at org.gradle.api.internal.tasks.testing.worker.TestWorker.processTestClass(TestWorker.java:119)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.gradle.internal.dispatch.ReflectionDispatch.dispatch(ReflectionDispatch.java:36)
	at org.gradle.internal.dispatch.ReflectionDispatch.dispatch(ReflectionDispatch.java:24)
	at org.gradle.internal.remote.internal.hub.MessageHubBackedObjectConnection$DispatchWrapper.dispatch(MessageHubBackedObjectConnection.java:182)
	at org.gradle.internal.remote.internal.hub.MessageHubBackedObjectConnection$DispatchWrapper.dispatch(MessageHubBackedObjectConnection.java:164)
	at org.gradle.internal.remote.internal.hub.MessageHub$Handler.run(MessageHub.java:414)
	at org.gradle.internal.concurrent.ExecutorPolicy$CatchAndRecordFailures.onExecute(ExecutorPolicy.java:64)
	at org.gradle.internal.concurrent.ManagedExecutorImpl$1.run(ManagedExecutorImpl.java:48)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
	at org.gradle.internal.concurrent.ThreadFactoryImpl$ManagedThreadRunnable.run(ThreadFactoryImpl.java:56)
	at java.lang.Thread.run(Thread.java:748)
```

</p>
</details>

# Use it
You can create your own IntelliDiff by calling us directly:

```kotlin
val diff: String = IntelliDiff().calculateDiff(
  message = <message>,
  expected = <expected-object>,
  actual = <actual-object>)
```

# Helpful hints
Standard test outputs can be deceptive when only given their String representation. We try and improve those scenarios by including hints (these between parentheses before expected and actual output.)

### String hints
Viewing the contents of a String on a screen full of empty space can be misleading.

Some cases that don't lend themselves well to display are empty strings:
```
### IntelliDiff:

* java.lang.String
|                   --> expected: This
|-->(content: empty)-->   actual: 

expected:<This> but was:<>
```

Non-visible characters such as a sequence of spaces trip us up:
```
### IntelliDiff:

* java.lang.String
|                   --> expected: This
|-->(content: blank)-->   actual:     

expected:<This> but was:<    >
```

Content that appears the same but is padded with non-visible content:
```
### IntelliDiff:

* java.lang.String
|                        --> expected: This
|-->(content: trim-alert)-->   actual: This    

expected:<This> but was:<This    >
```


### Collection hints
Failing test with assertions against objects containing anything more than a trivial amount of items with a trivial amount of fields can be painful. This usually means manually parsing through the output and trying to find commas to get the bounds of an element. When containers differ in size we surface the sizes up front:

```
### IntelliDiff:

* java.util.Arrays$ArrayList
|-->(size: 11)--> expected: [One, Two, Three, Four, Five, Six, Seven, Eight, Nine, Ten, Eleven]
|-->(size: 0) -->   actual: []

expected:<[One, Two, Three, Four, Five, Six, Seven, Eight, Nine, Ten, Eleven]> but was:<[]>
```

### Type hints
Objects of different types can have matching String representation:
```
### IntelliDiff:

* DifferentType
|-->(type: kotlin.String)--> expected: a
|-->(type: kotlin.Char)  -->   actual: a

expected:<a> but was:<a>
```

Nullability gets everybody eventually. Displays of null values compared to null content look exactly the same:
```
* java.lang.String
|-->(content: null)--> expected: null
|                  -->   actual: null

expected:<null> but was:<null>
```

# Testing Artifacts
If you use JUnit4 we also provide `IntelliDiffAssert` which mirrors `org.junit.Assert`. The benefit is that under the hood it utilizes lazy message evaluation - generating IntelliDiffs are reflection heavy operations so we recommend using this to keep your test suite fast for passing cases.

JUnit5 offers a better system for extending functionality than previous versions. Getting IntelliDiffs in your console is as easy as annotating an entire class or single method with:

```kotlin	
@ExtendWith(IntelliDiffExtension::class)	
```