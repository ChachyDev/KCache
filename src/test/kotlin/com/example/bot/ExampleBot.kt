package com.example.bot

import club.chachy.kcache.KCache
import club.chachy.kcache.kCache
import com.example.bot.commands.SnipeModule
import dev.cubxity.libs.kdp.kdp
import dev.cubxity.libs.kdp.processing.CommandProcessingPipeline
import dev.cubxity.libs.kdp.processing.processing
import dev.cubxity.libs.kdp.serialization.DefaultSerializationFactory
import dev.cubxity.libs.kdp.utils.embed.embed
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import java.awt.Color

object ExampleBot {

    lateinit var kCache: KCache

    lateinit var jda: JDA

    fun start(token: String) {
        val kdp = kdp {
            +SnipeModule::class

            processing {
                prefix = "."
            }

            kCache = kCache {
                ignoreBots = true
            }

            intercept(CommandProcessingPipeline.ERROR) {
                val embed = embed {
                    setAuthor("Error", null, jda.selfUser.effectiveAvatarUrl)
                    val error = context.exception?.message ?: "An error occured"
                    +error
                    color = Color(239,83,80)
                }
                context.send(embed)
            }

            serializationFactory = DefaultSerializationFactory()

            init()
        }

        jda = JDABuilder.createDefault(token)
            .setEventManager(kdp.manager)
            .build()
    }
}