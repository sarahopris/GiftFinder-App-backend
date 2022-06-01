package com.bachelorwork.backend.algorithm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


public class JaccardAlgorithm {

    private Integer calculateJaccardIndex(final List<String> userTags, final List<String> itemTags) {
        Set<String> intersectionSet = new HashSet<String>();
        Set<String> unionSet = new HashSet<String>();
        if (userTags.isEmpty() || itemTags.isEmpty()) {
            return 0;
        }
        for (String userTag : userTags) {
            unionSet.add(userTag);
            for (String itemTag : itemTags) {
                if (userTag.equals(itemTag)) {
                    intersectionSet.add(userTag);
                } else {
                    unionSet.add(itemTag);
                }
            }
        }
        return (int) Math.round((double) intersectionSet.size() / (double) unionSet.size() * 100);
    }

    public LinkedHashMap<String, Integer> calculateRelevantItemsMap() {

        Map<String, Integer> relevanceItemsMap = new LinkedHashMap<String, Integer>();

        List<String> userTags = getUserTags();

        relevanceItemsMap.put("item1", calculateJaccardIndex(userTags, getItem1Tags()));
        relevanceItemsMap.put("item2", calculateJaccardIndex(userTags, getItem2Tags()));
        relevanceItemsMap.put("item3", calculateJaccardIndex(userTags, getItem3Tags()));
        relevanceItemsMap.put("item4", calculateJaccardIndex(userTags, getItem4Tags()));
        relevanceItemsMap.put("item5", calculateJaccardIndex(userTags, getItem5Tags()));

        System.out.println("---------------------------"
                        + relevanceItemsMap.entrySet().stream().collect(Collectors.toList()));

        LinkedHashMap<String, Integer> sortedRelevanceItemsMap = new LinkedHashMap<>();

        // Use Comparator.reverseOrder() for reverse ordering
        relevanceItemsMap.entrySet().stream().filter(item -> item.getValue() > 0)
                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                        .forEachOrdered(entry -> sortedRelevanceItemsMap.put(entry.getKey(), entry.getValue()));

        return sortedRelevanceItemsMap;

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