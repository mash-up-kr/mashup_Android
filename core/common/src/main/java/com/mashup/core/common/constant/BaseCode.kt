package com.mashup.core.common.constant

/**
 * 요청에 오류가 있습니다.
 */
const val BAD_REQUEST = "BAD_REQUEST"

/**
 * 잠시 후 다시 시도해주세요.
 */
const val RETRY_REQUEST = "RETRY_REQUEST"

/**
 * 인증이 필요한 요청입니다.
 */
const val UNAUTHORIZED = "UNAUTHORIZED"

const val DISCONNECT_NETWORK = "DISCONNECT_NETWORK"

/**
 * 허용되지 않은 접근입니다.
 */
const val FORBIDDEN = "FORBIDDEN"

/**
 * 대상이 존재하지 않습니다.
 */
const val NOT_FOUND = "NOT_FOUND"

/**
 * 서버에 오류가 발생했습니다. 잠시 후 다시 시도해주세요.
 */
const val INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR"
