package com.bachelorwork.backend.algorithm;

import com.bachelorwork.backend.model.Item;
import com.bachelorwork.backend.model.Tag;
import com.bachelorwork.backend.repository.ITagRepository;
import com.bachelorwork.backend.repository.IUserRepository;
import com.bachelorwork.backend.repository.ItemRepository;
import com.bachelorwork.backend.service.ItemService;
import com.bachelorwork.backend.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JaccardAlgorithm {

    @Autowired
    ITagRepository iTagRepository;

    @Autowired
    IUserRepository iUserRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    TagService tagService;

    @Autowired
    ItemService itemService;

    @Autowired
    FilterMandatoryTags filterMandatoryTags;


//    private Integer calculateJaccardIndex(final List<String> userTags, final List<String> itemTags) {
//        Set<String> intersectionSet = new HashSet<String>();
//        Set<String> unionSet = new HashSet<String>();
//        if (userTags.isEmpty() || itemTags.isEmpty()) {
//            return 0;
//        }
//        for (String userTag : userTags) {
//            unionSet.add(userTag);
//            for (String itemTag : itemTags) {
//                if (userTag.equals(itemTag)) {
//                    intersectionSet.add(userTag);
//                } else {
//                    unionSet.add(itemTag);
//                }
//            }
//        }
//        return (int) Math.round((double) intersectionSet.size() / (double) unionSet.size() * 100);
//    }
    private Integer calculateJaccardIndex(List<Tag> selectedTags, List<Tag> itemTags) {
        Set<Tag> intersectionSet = new HashSet<>();
        Set<Tag> unionSet = new HashSet<>();


//        filteredItemsList = itemService.filteredItemsByMandatoryTags(selectedTagList);

        for (Tag selectedTag : selectedTags) {
            unionSet.add(selectedTag);
            for (Tag itemTag : itemTags) {
                if (selectedTag.equals(itemTag)) {
                    intersectionSet.add(selectedTag);
                } else {
                    unionSet.add(itemTag);
                }
            }
        }
        return (int) Math.round((double) intersectionSet.size() / (double) unionSet.size() * 100);
    }



    public LinkedHashMap<Long, Integer> calculateRelevantItemsMap(List<String> selectedTagLabels) {

        Map<Long, Integer> relevanceItemsMap = new LinkedHashMap<Long, Integer>();
        List<Tag> selectedTags = new ArrayList<>();
        selectedTagLabels.
                forEach(tagName->
                    selectedTags.add(tagService.findByTagName(tagName)));

        List<Item> filteredItemsByMandatory = filterMandatoryTags.filteredItemsByMandatoryTags(selectedTagLabels);

        List<String> userTags = getUserTags();
        filteredItemsByMandatory.forEach(item ->
            relevanceItemsMap.put(item.getIdItem(),calculateJaccardIndex(selectedTags,item.getTagList()))
        );


        LinkedHashMap<Long, Integer> sortedRelevanceItemsMap = new LinkedHashMap<>();

        // Use Comparator.reverseOrder() for reverse ordering
        relevanceItemsMap.entrySet().stream().filter(item -> item.getValue() > 0)
                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                        .forEachOrdered(entry -> sortedRelevanceItemsMap.put(entry.getKey(), entry.getValue()));

        return sortedRelevanceItemsMap;

    }

    public List<Item> resultedItems(List<String> selectedTagLabels){
        LinkedHashMap<Long,Integer> sortedRelevanceItems = calculateRelevantItemsMap(selectedTagLabels);
        List<Item> sortedItems = new ArrayList<>();
        sortedRelevanceItems.keySet()
                .forEach(id-> sortedItems.add(itemRepository.findById(id).stream().findFirst().orElse(null)));
        return sortedItems;
    }

    public List<String> getSortedRelevanceItems(final LinkedHashMap<String, Integer> sortedRelevanceItemsMap) {
        return sortedRelevanceItemsMap.entrySet().stream().filter(item -> item.getValue() > 0)
                        .map(entry -> entry.getKey().toString()).collect(Collectors.toList());
    }

    private List<String> getUserTags() {
        List<String> userTags = new ArrayList<String>();
        userTags.add("sport");
        userTags.add("boy");
        userTags.add("child");
        userTags.add("music");
        userTags.add("travel");
        return userTags;
    }

    private List<String> getItem1Tags() {
        // guitar
        List<String> itemTag = new ArrayList<String>();
        itemTag.add("girl");
        itemTag.add("copil");
        itemTag.add("music");
        return itemTag;
    }

    private List<String> getItem2Tags() {
        // doll
        List<String> itemTag = new ArrayList<String>();
        itemTag.add("girl");
        itemTag.add("copil");
        itemTag.add("music");
        itemTag.add("travel");
        return itemTag;
    }

    private List<String> getItem3Tags() {
        // rucksac
        List<String> itemTag = new ArrayList<String>();
        itemTag.add("sport");
        itemTag.add("travel");
        return itemTag;
    }

    private List<String> getItem4Tags() {
        // rucksac
        List<String> itemTag = new ArrayList<String>();
        itemTag.add("old");
        itemTag.add("reading");
        itemTag.add("fishing");
        return itemTag;
    }

    private List<String> getItem5Tags() {
        List<String> itemTag = new ArrayList<String>();
        itemTag.add("sport");
        itemTag.add("boy");
        itemTag.add("child");
        itemTag.add("music");
        itemTag.add("travel");
        return itemTag;
    }
}