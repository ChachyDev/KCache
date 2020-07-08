package com.example.bot.commands

import com.example.bot.ExampleBot
import dev.cubxity.libs.kdp.KDP
import dev.cubxity.libs.kdp.dsl.command
import dev.cubxity.libs.kdp.module.Module
import dev.cubxity.libs.kdp.utils.embed.embed
import net.dv8tion.jda.api.entities.Message
import java.awt.Color

class SnipeModule(kdp: KDP) : Module(kdp, "Snipe", "Snipe Module") {
    companion object {
        val snipe by command("snipe [message]", "Snipes a message")
    }

    init {
        snipe {
            handler {
                val message: Message? = args["message"]
                val cache = ExampleBot.kCache.retrieveDeleteCache()
                val cacheObject = if (message != null) {
                    cache.find { it.cachedObject.message.id == message.id }
                } else {
                    if (guild == null) {
                        error("Guild only command!")
                    } else {
                        try {
                            val c = cache.filter {
                                if (it.cachedObject.guild != null) {
                                    it.cachedObject.guild?.id == guild?.id
                                } else {
                                    return@handler
                                }
                            }
                            c[c.size - 1]
                        } catch (e: ArrayIndexOutOfBoundsException) {
                            error("No snipes found! :(")
                        }
                    }
                } ?: error("Failed to retrieve message")

                send(embed {
                    setAuthor("ExampleBot >> Snipe", null, ExampleBot.jda.selfUser.effectiveAvatarUrl)
                    color = Color(129,199,132)

                    +cacheObject.cachedObject.message.contentRaw

                    val sniped = cacheObject.cachedObject.author
                    setFooter("Sniped by ${executor.name}#${executor.discriminator} || User Sniped: ${sniped.name}#${sniped.discriminator}")
                })
            }
        }
    }
}