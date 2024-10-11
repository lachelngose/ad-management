package dev.lachelngose.adManagement.domain.campaign.model.converter

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import dev.lachelngose.adManagement.domain.campaign.model.AdTarget

@Converter(autoApply = true)
class AdTargetConverter : AttributeConverter<AdTarget, String> {
    private val objectMapper = jacksonObjectMapper()

    override fun convertToDatabaseColumn(attribute: AdTarget?): String? {
        return attribute?.let { objectMapper.writeValueAsString(it) }
    }

    override fun convertToEntityAttribute(dbData: String?): AdTarget? {
        return dbData?.let { objectMapper.readValue(it, AdTarget::class.java) }
    }
}