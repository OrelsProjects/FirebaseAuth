package com.orels.auth.domain.exception

import java.lang.Exception

class AuthNotInitializedException(message: String = "Did you call initialize?"): Exception(message) {
}