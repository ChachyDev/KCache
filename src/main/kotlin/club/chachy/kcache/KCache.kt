package club.chachy.kcache

import club.chachy.kcache.features.data.holders.CacheObject
import club.chachy.kcache.features.data.holders.DeletedCacheObject
import club.chachy.kcache.utils.launchCoroutine
import club.minnced.jda.reactor.on
import dev.cubxity.libs.kdp.KDP
import dev.cubxity.libs.kdp.feature.KDPFeature
import dev.cubxity.libs.kdp.feature.install
import net.dv8tion.jda.api.events.message.MessageDeleteEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class KCache {
    private val cache = mutableListOf<CacheObject>()

    private val deletedMessagesCache = mutableListOf<DeletedCacheObject>()

    private val caches = listOf(cache, deletedMessagesCache)

    var ignoreBots = false

    fun processEvent(event: MessageReceivedEvent) {
        when {
            ignoreBots && event.author.isBot -> return
            else -> cache.add(CacheObject(event.message, event.author, event.guild))

        }
    }

    fun processEvent(event: MessageDeleteEvent) {
        val `object` = DeletedCacheObject(cache.firstOrNull { it.message.id == event.messageId } ?: return)
        when {
            ignoreBots && `object`.cachedObject.author.isBot -> return

            else -> deletedMessagesCache.add(`object`)
        }
    }

    fun retrieveCache() = cache.toList()

    fun retrieveDeleteCache() = deletedMessagesCache.toList()

    fun clear(cacheType: CacheType = CacheType.All) = when(cacheType) {
        CacheType.Message -> launchCoroutine("Clear Coroutine") { cache.clear() }

        CacheType.Delete -> launchCoroutine("Clear Coroutine") { deletedMessagesCache.clear() }

        CacheType.All -> launchCoroutine("Clear Coroutine") { caches.forEach { it.clear() } }
    }


    companion object : KDPFeature<KDP, KCache, KCache> {
        override val key: String = "kcache.features.cache"

        override fun install(pipeline: KDP, configure: KCache.() -> Unit): KCache {
            val feature = KCache().apply(configure)
            with(pipeline.manager) {
                on<MessageReceivedEvent>().subscribe { launchCoroutine("KCache Cache") { feature.processEvent(it) } }
                on<MessageDeleteEvent>().subscribe { launchCoroutine("KCache Cache") { feature.processEvent(it) } }
            }
            return feature
        }

    }
}

fun KDP.kCache(opt: KCache.() -> Unit = {}): KCache = (features[KCache.key] as KCache?
    ?: install(KCache)).apply(opt)

enum class CacheType {
    Message,
    Delete,
    All
}