# KProfile - A Profiling library made with and for Kotlin

KProfile lets you efficiently create, store, view and compare profiling 
results made with a simple Dsl. This helps to understand the performance
changes caused by different changes to the code.

## Creating Profiling results

For creating profiling results for a method you have to simply define
an object which inherits from `ProfilingSuite`.
```
object Initial: Tag("Initial")

object PrimeTestProfiling: ProfilingSuite("prime test", Initial {
    val x = 23090909
    profile("testing primality of a very big integer") {
        primeTest(x)
    }
})
```

Now you can call the `run` method of `ProfilingSuite` to store the results
in a file in the AppData directory.

You don't have to do anything except
calling the `run` method and KProfile stores all the information for you.

But what about the parameters of the constructor for `ProfilingSuite`?

The first parameter which we have passes "prime test" to is the topic.

It should describe what you profile in this suite.

We will hear more about
the topic in the section Viewing Profiling results.

The second parameter is a tag. It defines which version of the program
is profiled.
Tags will become essential when comparing many versions of a program.

For now we simply pass `Initial` because we are profiling
the first version of out program.
When we apply changes we create new tags and pass them to the `ProfilingSuite`.

## Viewing and comparing profiling results
Now if KProfile would just store the profiling results in some unreadable
format in some unknown file with a crazy name it would be pretty useless.

KProfile provides a Kotlin-API to view and compare profiling results using
plain text or converting the results to markdown.

This API works best with the Kotlin-REPL by Jetbrains.

You can view results with the `view` function of the interface
`ResultFormat`.

There are two implementations of that interface.

* `plaintext`
* `markdown`

The latter is a function and takes a markdown theme or applies the default
theme.