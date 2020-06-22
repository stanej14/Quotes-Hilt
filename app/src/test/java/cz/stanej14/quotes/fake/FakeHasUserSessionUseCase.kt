package cz.stanej14.quotes.fake

import cz.stanej14.quotes.domain.session.HasUserSessionUseCase

class FakeHasUserSessionUseCase : HasUserSessionUseCase {

    var hasUserSession = false

    override fun hasUserSession() = hasUserSession
}
