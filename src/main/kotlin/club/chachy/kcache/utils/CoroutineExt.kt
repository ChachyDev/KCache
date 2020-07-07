package club.chachy.kcache.utils

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun launchCoroutine(name: String, block: suspend CoroutineScope.() -> Unit) = CoroutineScope(Dispatchers.IO + CoroutineName(name)).launch(block = block)