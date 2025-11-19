package com.hub.course_service.service;

import com.hub.common_library.exception.NotFoundException;
import com.hub.course_service.model.Tag;
import com.hub.course_service.model.dto.tag.TagGetDetailDto;
import com.hub.course_service.model.dto.tag.TagNameUpdateDto;
import com.hub.course_service.model.dto.tag.TagPostDto;
import com.hub.course_service.repository.TagRepository;
import com.hub.course_service.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public TagGetDetailDto getDetailTagById(Long tagId) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new NotFoundException(Constants.ErrorCode.TAG_NOT_FOUND, tagId));

        return TagGetDetailDto.fromModel(tag);
    }

    public List<TagGetDetailDto> getAllDetailTag(int pageNo, int pageSize) {

        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Tag> tagPage = tagRepository.findAll(pageable);

        return tagPage.getContent()
                .stream()
                .map(
                        tagModel -> TagGetDetailDto.fromModel(tagModel)
                ).collect(Collectors.toList());
    }

    public TagGetDetailDto createTag(TagPostDto tagPostDto) {
        Tag tag = new Tag();
        tag.setActive((tagPostDto.isActive() != null) ? tagPostDto.isActive() : false);
        tag.setTagName(tagPostDto.tagName());
        tagRepository.save(tag);

        return TagGetDetailDto.fromModel(tag);
    }

    public void renameTag(Long tagId, TagNameUpdateDto tagNameUpdateDto) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new NotFoundException(Constants.ErrorCode.TAG_NOT_FOUND, tagId));

        if (!tagNameUpdateDto.tagName().isEmpty())
            tag.setTagName(tagNameUpdateDto.tagName());

        tagRepository.save(tag);
    }

    public void deleteTagById(Long tagId) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new NotFoundException(Constants.ErrorCode.TAG_NOT_FOUND, tagId));

        tagRepository.delete(tag);
    }

}
