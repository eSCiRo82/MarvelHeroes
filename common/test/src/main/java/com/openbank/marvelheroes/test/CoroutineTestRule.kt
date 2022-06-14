package com.openbank.marvelheroes.test

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

/**
 * This rule allows to launch a tests in a coroutine running on the test scope.
 */
@ExperimentalCoroutinesApi
class CoroutineTestRule: TestRule
{
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    override fun apply(base: Statement, description: Description): Statement =
        object : Statement() {
            override fun evaluate() {
                Dispatchers.setMain(testDispatcher)
                base.evaluate()
                Dispatchers.resetMain()
            }
        }

    fun runTest(delay: Long = 0, test: suspend TestScope.() -> Unit) = testScope.runTest(delay, test)
}