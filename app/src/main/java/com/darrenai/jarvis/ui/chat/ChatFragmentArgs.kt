package com.darrenai.jarvis.ui.chat

import android.os.Bundle

data class ChatFragmentArgs(val prompt: String) {
    companion object {
        fun fromBundle(args: Bundle): ChatFragmentArgs {
            return ChatFragmentArgs(args.getString("prompt", ""))
        }
    }
}
