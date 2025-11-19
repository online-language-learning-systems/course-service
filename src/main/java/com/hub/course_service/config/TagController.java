package com.hub.course_service.config;

import com.hub.course_service.model.dto.tag.TagGetDetailDto;
import com.hub.course_service.model.dto.tag.TagNameUpdateDto;
import com.hub.course_service.model.dto.tag.TagPostDto;
import com.hub.course_service.service.TagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping(path = "/storefront/tag/all")
    public ResponseEntity<List<TagGetDetailDto>> getAllTag(
            @RequestParam(name = "pageNo") int pageNo,
            @RequestParam(name = "pageSize") int pageSize) {

        return ResponseEntity.ok(tagService.getAllDetailTag(pageNo, pageSize));
    }

    @GetMapping(path = "/storefront/tag/{tagId}")
    public ResponseEntity<TagGetDetailDto> getTagById(@PathVariable(name = "tagId") Long tagId) {
        return ResponseEntity.ok(tagService.getDetailTagById(tagId));
    }

    @PostMapping(path = "/backoffice/tag")
    public ResponseEntity<TagGetDetailDto> createNewTag(@RequestBody TagPostDto tagPostDto) {
        return ResponseEntity.ok(tagService.createTag(tagPostDto));
    }

    @PatchMapping(path = "/backoffice/tag/{tagId}/rename")
    public ResponseEntity<Void> renameTag(@PathVariable(name = "tagId") Long tagId,
                                          @RequestBody TagNameUpdateDto tagNameUpdateDto) {
        tagService.renameTag(tagId, tagNameUpdateDto);
        return ResponseEntity.noContent().build();
        
    }

    @DeleteMapping(path = "/backoffice/tag/{tagId}/delete")
    public ResponseEntity<Void> deleteTagById(@PathVariable(name = "tagId") Long tagId) {
        tagService.deleteTagById(tagId);
        return ResponseEntity.noContent().build();
    }
}
