package com.darrenai.jarvis.ui.dashboard

import android.os.Bundle
import androidx.navigation.NavDirections
import com.darrenai.jarvis.R

data class AgentInfo(val name: String, val status: String, val colorRes: Int)

object DashboardFragmentDirections {
    fun actionNavDashboardToNavChat(prompt: String): NavDirections {
        return object : NavDirections {
            override val arguments = bundleOf("prompt" to prompt)
            override val actionId = R.id.nav_chat
        }
    }

    private fun bundleOf(vararg pairs: Pair<String, Any?>): Bundle {
        return Bundle().apply {
            pairs.forEach { (key, value) ->
                when (value) {
                    is String -> putString(key, value)
                    is Int -> putInt(key, value)
                    is Boolean -> putBoolean(key, value)
                    is Double -> putDouble(key, value)
                    is Float -> putFloat(key, value)
                    is Long -> putLong(key, value)
                    null -> {}
                }
            }
        }
    }
}
