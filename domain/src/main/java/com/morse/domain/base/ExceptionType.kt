package com.morse.domain.base

sealed class ExceptionType {
    data object SocketTimeoutException : ExceptionType()
    data object SSLHandshakeException : ExceptionType()
    data object UnknownHostException : ExceptionType()
    data object ProtocolException : ExceptionType()
    data object SSLException : ExceptionType()
    data object SocketException : ExceptionType()
    data object EOFException : ExceptionType()
    data object UserCancellationException : ExceptionType()
    data class GenericException (val message : String) : ExceptionType()
}




