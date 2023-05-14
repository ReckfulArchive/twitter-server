package org.reckful.archive.twitter.server.importer

import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

/**
 * Imports all available data at application startup
 */
@Component
class DataImporterRunner(
    private val dataImporters: List<DataImporter>
) {
    @EventListener(ApplicationReadyEvent::class)
    fun importAll() {
        dataImporters.forEach { it.import() }
    }
}
