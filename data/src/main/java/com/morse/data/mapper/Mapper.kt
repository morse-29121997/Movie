package com.morse.data.mapper

interface Mapper<InputType, OutputType> {

    // From InputType to OutputType
    fun from(input: InputType): OutputType

    // From OutputType to InputType
    fun to(outputType: OutputType): InputType

}