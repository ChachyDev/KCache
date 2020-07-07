package com.example.bot

fun main(args: Array<String>) : Unit = ExampleBot.start(args[args.indexOf("--token") + 1])