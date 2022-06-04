package com.bachelorwork.backend.algorithm;

import com.bachelorwork.backend.model.Item;
import com.bachelorwork.backend.model.Tag;
import com.bachelorwork.backend.repository.ITagRepository;
import com.bachelorwork.backend.repository.ItemRepository;
import com.bachelorwork.backend.service.ItemService;
import com.bachelorwork.backend.service.ReceiverService;
import com.bachelorwork.backend.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilterMandatoryTags {
    @Autowired
    ItemService itemService;

    @Autowired
    ReceiverService receiverService;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ITagRepository iTagRepository;

    @Autowired
    TagService tagService;

    public List<Item> filteredItemsByMandatoryTags(List<String> selectedTagsNames){
        List<Item> allItems = (List<Item>) itemRepository.findAll();
        List<Tag> selectedTags = new ArrayList<>();
        selectedTagsNames.
                forEach(tagName->
                    selectedTags.add(tagService.findByTagName(tagName)));

        List<Tag> mandatoryTags = selectedTags.stream().filter(tag->tag.getMandatory() == 1).collect(Collectors.toList());

        return allItems.stream().
                filter(item -> itemService.getMandatoryTagsFromItem(item.getIdItem()).containsAll(mandatoryTags)).collect(Collectors.toList());

    }
}
