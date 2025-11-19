package com.hub.course_service.model.dto.tag;

import com.hub.course_service.model.Tag;

public record TagGetDetailDto(
        Long id,
        String tagName,
        boolean isActive
) {
    public static TagGetDetailDto fromModel(Tag tag) {
        return new TagGetDetailDto(
                tag.getId(),
                tag.getTagName(),
                tag.isActive()
        );
    }
}
