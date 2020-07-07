package club.chachy.kcache.features.data.holders

import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.User

data class CacheObject(val message: Message, val author: User, val guild: Guild?) : Cache