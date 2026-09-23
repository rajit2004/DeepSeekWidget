package com.rajit2004.deepseekwidget

import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class PromptStoreTest {

    private lateinit var store: PromptStore

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        context.getSharedPreferences("deepseek_prompts", android.content.Context.MODE_PRIVATE)
            .edit().clear().commit()
        store = PromptStore(context)
    }

    @Test
    fun `save and read back single prompt`() {
        store.savePrompt("hello")
        assertEquals(listOf("hello"), store.getPrompts())
        assertEquals("hello", store.getLatestPrompt())
    }

    @Test
    fun `blank prompts are ignored`() {
        store.savePrompt("   ")
        assertTrue(store.getPrompts().isEmpty())
        assertNull(store.getLatestPrompt())
    }

    @Test
    fun `consecutive duplicates are ignored`() {
        store.savePrompt("hi")
        store.savePrompt("hi")
        assertEquals(listOf("hi"), store.getPrompts())
    }

    @Test
    fun `keeps newest first and caps at max`() {
        for (i in 1..7) {
            // Bypass the consecutive-duplicate guard by varying text.
            store.savePrompt("prompt $i")
        }
        val prompts = store.getPrompts()
        assertEquals(PromptStore.MAX_PROMPTS, prompts.size)
        assertEquals("prompt 7", prompts.first())
        assertEquals("prompt 3", prompts.last())
    }

    @Test
    fun `corrupt json returns empty list`() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        context.getSharedPreferences("deepseek_prompts", android.content.Context.MODE_PRIVATE)
            .edit().putString("prompts", "not-json{{{").commit()
        assertTrue(store.getPrompts().isEmpty())
    }

    @Test
    fun `clear removes everything`() {
        store.savePrompt("a")
        store.clear()
        assertTrue(store.getPrompts().isEmpty())
    }
}
